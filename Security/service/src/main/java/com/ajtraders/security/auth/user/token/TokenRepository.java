package com.ajtraders.security.auth.user.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

   @Query("SELECT t from Token t join User  u on t.user.id=u.id where u.id=:id and (t.isExpired=false or t.isRevoked=false )")
    List<Token> findAllByValidTokensByUser(Integer id);


    Optional<Token> findByToken(String token);
}
