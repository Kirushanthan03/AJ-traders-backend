package com.ajtraders.security.auth;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    @Email(message = "Enter a valid email")
    private String email;
    private String password;
}
