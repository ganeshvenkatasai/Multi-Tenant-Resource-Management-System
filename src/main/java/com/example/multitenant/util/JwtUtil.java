package com.example.multitenant.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationMs = 1000L * 60 * 60 * 8;

    public JwtUtil() {
        // Demo secret; replace in production
        String secret = "bXktdmVyeS1sb25nLXN1cGVyLXNlY3JldC1rZXktbWl4ZWQtd2l0aC1zYWx0";
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String username, String role, String tenantId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role, "tenantId", tenantId))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String getTenant(String token) {
        return parse(token).getBody().get("tenantId", String.class);
    }

    public String getRole(String token) {
        return parse(token).getBody().get("role", String.class);
    }

    public String getUsername(String token) {
        return parse(token).getBody().getSubject();
    }
}
