package com.ajtraders.security.auth;


import com.ajtraders.security.auth.config.JwtService;
import com.ajtraders.security.auth.user.Role;
import com.ajtraders.security.auth.user.User;
import com.ajtraders.security.auth.user.UserRepository;
import com.ajtraders.security.auth.user.token.Token;
import com.ajtraders.security.auth.user.token.TokenRepository;
import com.ajtraders.security.auth.user.token.TokenType;
import com.ajtraders.security.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new ServiceException("Email " + request.getEmail() + " is already registered.", "Bad Request", HttpStatus.BAD_REQUEST);
                });
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var saveduser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(saveduser);
        var token = Token.builder()
                .user(saveduser)
                .token(jwtToken)
                .isExpired(false)
                .isRevoked(false)
                .tokenType(TokenType.BEARER)
                .build();
        saveUserToken(saveduser, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ServiceException("Email " + request.getEmail() + " is not registered.", "Bad Request",  HttpStatus.UNAUTHORIZED));
        boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordValid) {
            throw new ServiceException("Invalid password for the given email.", "Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).role(user.getRole()).build();
    }

    public AuthenticationResponse processOAuth2Credential(String credential) {
        if (credential == null || credential.isEmpty()) {
           throw new ServiceException("Missing Google credential","Bad Request",HttpStatus.BAD_REQUEST);
        }
        RestTemplate restTemplate = new RestTemplate();
        String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + credential;

        Map<String, Object> tokenInfo = restTemplate.getForObject(tokenInfoUrl, Map.class);

        log.info("token info" + tokenInfo);

        if (tokenInfo == null || tokenInfo.get("email") == null) {
            throw new ServiceException("Invalid Google token","Bad Request",HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(tokenInfo.get("email").toString()).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(tokenInfo.get("email").toString());
            newUser.setFirstName(tokenInfo.get("given_name").toString());
            newUser.setLastName(tokenInfo.get("family_name").toString());
            newUser.setPassword("oauth2");
            newUser.setRole(Role.USER);
            return userRepository.save(newUser);
        });

        revokeAllUserTokens(user);


        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        var token = Token.builder()
                .user(user)
                .token(accessToken)
                .isExpired(false)
                .isRevoked(false)
                .tokenType(TokenType.BEARER)
                .build();
        saveUserToken(user, accessToken);
       return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public void revokeAllUserTokens(User user) {
        var validTokens = tokenRepository.findAllByValidTokensByUser(user.getId());
        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
    }

//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.userRepository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
public AuthenticationResponse refreshToken(HttpServletRequest request) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new ServiceException("Invalid Authorization header", "Bad Request", HttpStatus.BAD_REQUEST);
    }

    final String refreshToken = authHeader.substring(7);
    final String userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail == null) {
        throw new ServiceException("Invalid refresh token", "Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    var user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ServiceException("User not found", "Unauthorized", HttpStatus.UNAUTHORIZED));

    if (!jwtService.isTokenValid(refreshToken, user)) {
        throw new ServiceException("Refresh token is invalid or expired", "Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    var accessToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, accessToken);

    return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build(); // Directly return the success response
}


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }


}
