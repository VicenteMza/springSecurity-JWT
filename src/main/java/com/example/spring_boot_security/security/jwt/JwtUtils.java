package com.example.spring_boot_security.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration; 

    //generar token de acceso
    public String generateAccessToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Validar el token de acceso
    public boolean isTokenValid(String token) {
        try {
            JwtParser parser = Jwts.parser().setSigningKey(getSigningKey()).build();
            parser.parseClaimsJws(token)
            .getBody();
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT Token: ".concat(e.getMessage()));
            return false;
        }
    }

    //obtener username del token
    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    //obtener un solo claim del token
    public <T> T getClaim(String token, Function<Claims, T> claimsFunction){
        Claims claims = extractAlClaims(token);
        return claimsFunction.apply(claims);
    }

    //obtener todos los claims del token
    public Claims extractAlClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Obtener firma del token
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
