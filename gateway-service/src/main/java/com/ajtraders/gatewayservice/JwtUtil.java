package com.ajtraders.gatewayservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	private static final String TOKEN_PREFIX = "Bearer ";

	public boolean validateToken(final String token) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody();

			// Additional validation can be added here
			// For example, checking if the user exists in the database
			// or if the user has required permissions

			String username = claims.getSubject();
			if (username == null || username.isEmpty()) {
				log.warn("Token validation failed: empty subject");
				return false;
			}

			// Check token expiration
			if (claims.getExpiration() != null && claims.getExpiration().before(new java.util.Date())) {
				log.warn("Token validation failed: token expired");
				return false;
			}

			return true;

		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
			return false;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			return false;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			return false;
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
			return false;
		}
	}
}