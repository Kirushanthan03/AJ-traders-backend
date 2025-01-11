package com.ajtraders.security.auth;

import com.ajtraders.security.auth.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    @JsonProperty("access_Token")
    private  String accessToken;
    @JsonProperty("refresh_Token")
    private  String refreshToken;
    private Role role;
}
