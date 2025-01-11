package com.ajtraders.gatewayservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator validator;

	@Autowired
	private JwtUtil jwtUtil;

	private final ObjectMapper objectMapper;

	public AuthenticationFilter(ObjectMapper objectMapper) {
		super(Config.class);
		this.objectMapper = objectMapper;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// Check if the route should be secured
			if (validator.isSecured.test(request)) {
				log.debug("Securing request to: {}", request.getPath());

				// Check for authorization header
				if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					log.warn("No authorization header found for secured path: {}", request.getPath());
					return handleUnauthorized(request, exchange.getResponse(), "Missing authorization header");
				}

				String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
				if (authHeader == null || !authHeader.startsWith("Bearer ")) {
					log.warn("Invalid authorization header format for path: {}", request.getPath());
					return handleUnauthorized(request, exchange.getResponse(), "Invalid authorization header format");
				}

				String token = authHeader.substring(7);
				try {
					// Validate token
					if (!jwtUtil.validateToken(token)) {
						log.warn("Token validation failed for path: {}", request.getPath());
						return handleUnauthorized(request, exchange.getResponse(), "Invalid token");
					}

					// If token is valid, proceed with the request
					return chain.filter(exchange);

				} catch (Exception e) {
					log.error("Error validating token: {}", e.getMessage());
					return handleUnauthorized(request, exchange.getResponse(), "Token validation failed");
				}
			}

			// If route is not secured, proceed with the request
			return chain.filter(exchange);
		});
	}

	private Mono<Void> handleUnauthorized(ServerHttpRequest request, ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("timestamp", LocalDateTime.now());
		responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
		responseBody.put("error", "Unauthorized");
		responseBody.put("message", message);
		responseBody.put("path", request.getPath().toString());

		try {
			byte[] responseBytes = objectMapper.writeValueAsBytes(responseBody);
			return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes)));
		} catch (JsonProcessingException e) {
			log.error("Error writing response: {}", e.getMessage());
			return Mono.error(e);
		}
	}

	public static class Config {
		// Configuration properties can be added here if needed
	}
}