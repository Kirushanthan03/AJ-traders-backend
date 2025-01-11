package com.ajtraders.gatewayservice;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Predicate;

@Component
@Slf4j
public class RouteValidator {

    // Define open endpoints that don't require authentication
    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/api/v1/security/**",
            "/api/v1/product/featured",
            "/api/v1/product/getAll",
            "/api/public/**",
            "/actuator/health"
            // Add other public endpoints as needed
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();

        // Check if the current path matches any of the open endpoints
        boolean isOpenEndpoint = openApiEndpoints.stream()
                .anyMatch(endpoint -> {
                    if (endpoint.endsWith("/**")) {
                        // Handle wildcard patterns
                        String basePattern = endpoint.substring(0, endpoint.length() - 3);
                        boolean matches = path.startsWith(basePattern);
                        if (matches) {
                            log.debug("Path {} matched open endpoint pattern {}", path, endpoint);
                        }
                        return matches;
                    }
                    // Handle exact matches
                    boolean matches = path.equals(endpoint);
                    if (matches) {
                        log.debug("Path {} matched exact open endpoint {}", path, endpoint);
                    }
                    return matches;
                });

        // Log the security decision
        if (!isOpenEndpoint) {
            log.debug("Path {} requires authentication", path);
        }

        // Return true if the path should be secured (i.e., is NOT an open endpoint)
        return !isOpenEndpoint;
    };
}