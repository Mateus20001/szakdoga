package com.szakdoga.backend.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // Generate a token using the identifier (e.g., user ID)
    public String generateToken(String identifier) {
        return generateToken(Map.of(), identifier);
    }

    // Generate a token with additional claims
    public String generateToken(Map<String, Object> additionalClaims, String identifier) {
        return Jwts.builder()
                .setClaims(additionalClaims)
                .setSubject(identifier) // Use identifier as the subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate the token by comparing its identifier and ensuring it has not expired
    public boolean isTokenValid(String token, String identifier) {
        final String tokenIdentifier = extractIdentifier(token);
        return (tokenIdentifier.equals(identifier)) && !isTokenExpired(token);
    }

    // Extract the identifier (subject) from the token
    public String extractIdentifier(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim using a resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get the signing key based on the secret
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public long getTokenValidityInSeconds() {
        return jwtExpiration / 1000; // Convert milliseconds to seconds
    }
}
