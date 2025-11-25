package com.example.demo.Mycoffe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
//signing key is a cryptographic key used to create the digital signature of a JWT.
// This signature is a crucial component of a JWT, as it ensures the token's integrity and authenticity
// minimum signin key require for JWT token is 256bit
//pritvate The variable or method is only accessible inside the same class
//static You can access it without creating an object:
//final Means the value cannot be changed after being assigned.

@Service //This class is meant to hold logic related to JWTs
public class jwtService {
    private static final String SECRET_KEY = "8ea14567316a098f58359a29024afd4257414d7c948fda80130f352075c3b16b";
    public String extractUsername(String token) {

        return extractClaimByToken(token, Claims::getSubject); //subject is email, username
    }
    public <T> T extractClaimByToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) { // make this global
        return generateToken(new HashMap<>(), userDetails);
    }



    // pass information you want within the token
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder(). //generate token out of extra claims and userDetails
                setClaims(extraClaims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + 1000 *60 *24)).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).
                compact();
    }
    // Validate if token belong to userdetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //make sure token expire in the current time
    }

    private Date extractExpiration(String token) {
        return extractClaimByToken(token, Claims::getExpiration);

    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey()).build(). //when decode, generate token, we need Signninkey
                parseClaimsJws(token).
                getBody(); //get all the claims in the class
    }
   private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // return algorithm with keyBytes parameter

   }


}
