package it.unical.progettoweb.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "progettoweb2025-chiave-segreta-molto-lunga-e-sicura-1234567890";
    private static final long EXPIRATION_MS = 86400000L;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // genera il token con email e ruolo ("USER", "SELLER", "ADMIN")
    public String generateToken(String email, String ruolo,int id) {
        return Jwts.builder()
                .setSubject(email)
                .claim("ruolo", ruolo)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // estrae il ruolo dal token
    public String extractRuolo(String token) {
        return parseClaims(token).get("ruolo", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public int extractUserId(String token) {
        return parseClaims(token).get("id", Integer.class);
    }
}