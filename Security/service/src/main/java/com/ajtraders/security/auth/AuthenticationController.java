package com.ajtraders.security.auth;


import com.ajtraders.security.auth.config.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/security/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;
    private final LogoutService logoutService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info(request.toString());

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {


        return ResponseEntity.ok(service.authenticate(request));
    }



    //    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        service.refreshToken(request, response);
//    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        AuthenticationResponse authResponse = service.refreshToken(request);
        return ResponseEntity.ok(authResponse); // Return the success response with a 200 status code
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<AuthenticationResponse> googleLogin(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(service.processOAuth2Credential(payload.get("credential")));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.ok("Logout successful.");
    }

}
