package com.moviq.movie_reservation_service.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.metamodel.internal.StandardEmbeddableInstantiator;
import org.hibernate.sql.Insert;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class JwtTokenService {

  private final SecretKey key;// to store the key
  private final long expirationMinutes;

    public JwtTokenService(@Value("${security.jwt.secret}") String secret,
                           @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authority){
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .claim("roles",authority.stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
//compact() Produces the final JWT string: header.payload.signature in Base64URL format
    }

    public Jws<Claims> parseToken(String token){
        log.info("entering parser....");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

    }
    public Boolean isValid(String token){
        try{

            Jws<Claims> claims = parseToken(token);
            log.info("Validated the token: JWT, subject={}", claims.getBody().getSubject());
            return true;
        }catch (JwtException | IllegalArgumentException e){
            log.error("Invalid JWT token",e);
            return false;
        }

    }
    public String getUsername(String token){
        return parseToken(token).getBody().getSubject();
    }

    public List<String> getRoles(String token){
       Object roles = parseToken(token).getBody().get("roles");
       if(roles instanceof List<?> list){
           return list.stream().map(Object::toString).toList();
       }
       return List.of();

    }

}
