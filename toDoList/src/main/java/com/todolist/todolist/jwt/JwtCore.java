package com.todolist.todolist.jwt;

import com.todolist.todolist.UserDetailsImplementation;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${todolist.app.secret}")
    private String secret;
    @Value("${todolist.app.lifetimeMS}")
    private int lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImplementation userDetailsImplementation = (UserDetailsImplementation) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userDetailsImplementation.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + lifetime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public String getNameFromJwt(String token){

        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
