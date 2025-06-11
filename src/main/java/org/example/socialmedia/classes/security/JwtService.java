package org.example.socialmedia.classes.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {
    @Value("${secretKey}")
    public String secret;

    public String generateToken(String username){
        return Jwts.builder().signWith(getKey()).subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+1000*60*60*24*7)).compact();
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
