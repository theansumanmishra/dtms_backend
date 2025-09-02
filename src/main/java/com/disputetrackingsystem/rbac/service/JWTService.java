package com.disputetrackingsystem.rbac.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    // Secret key for signing the JWT
    private String secretKey;

    // Constructor to generate a secret key
    public JWTService(){

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate JWT token
    public String generateToken(String username) {
        //here claims can be any key-value pair you want to add to the token
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()       //it's a builder class to build the token
                .claims()           //here we can add any key-value pair to the token
                .add(claims)        //add all claims
                .subject(username)  //here the username is added as subject
                .issuedAt(new Date(System.currentTimeMillis()))                     //when the token is issued
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // Token validation for 10 mins
                .and()
                .signWith(getKey()) // sign the token with the secret key
                .compact();         //builds the token
    }

    //to get the secret key
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract specific claim from JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Extract all claims from JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate the JWT token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
