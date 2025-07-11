package org.example.socialmedia.classes.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {
    @Value("${secret-key}")
    public String secret;
    @Value("${token-max-age}")
    long TOKEN_MAX_AGE; // in days

    public String generateToken(String username){
        return Jwts.builder().signWith(getKey()).subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+ TOKEN_MAX_AGE * 1000 * 60 * 60 * 24)).compact();
    }
    public String extractUsername(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean isExpired(String token){
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public boolean verifyToken(String token, String username){
        if(isExpired(token)) return false;
        if(!username.equals(extractUsername(token))) return false;
        return true;
    }
    public SecretKey getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
