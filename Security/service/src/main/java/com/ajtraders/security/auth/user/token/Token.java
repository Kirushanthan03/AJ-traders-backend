package com.ajtraders.security.auth.user.token;



import com.ajtraders.security.auth.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TokenType  tokenType;


    private  String token;

    private boolean isRevoked;

    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
