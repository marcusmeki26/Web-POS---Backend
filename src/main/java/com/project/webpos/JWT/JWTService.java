package com.project.webpos.JWT;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.webpos.Entity.DTO.TokenResponse;
import com.project.webpos.Exception.ExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Service 
@Getter
public class JWTService {
    private final String accessSecretKey;
    private final String refreshSecretKey;

    public JWTService(){
        // This generates a key to be used by secretKey.
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            accessSecretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

            KeyGenerator refreshKeyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey refreshSk = refreshKeyGen.generateKey();
            refreshSecretKey = Base64.getEncoder().encodeToString(refreshSk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public TokenResponse generateToken(String username, String userId, Collection<? extends GrantedAuthority> role) {
        // List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN"); // Create a roles. We will do a helper method to get the role from the database.
        // claims.put("roles", roles); // inserting the roles inside the claims object.

        // Map<String, String> jwtTokens = new HashMap<>(); // used to make access token and refresh token in JSON object.
        Map<String, Object> claims = new HashMap<>(); // used to store the JWT into JSON object

        TokenResponse tokensResponse = new TokenResponse();
        tokensResponse.setAccessToken(createAccessToken(username, role));
        tokensResponse.setRefreshToken(createToken(claims, userId, issuedAt(), expRefreshToken(), refreshSecretKey));
        return tokensResponse;
    }

    // Method for creating access token.
    public String createAccessToken(String subject, Collection<? extends GrantedAuthority> role){
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = role.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
        claims.put("role", roles);
        return createToken(claims, subject, issuedAt(), expAccessToken(), accessSecretKey);
    }

    // This is a helper method for signWith() method for generating a token.
    private SecretKey getAccessKey(String secretKey) {
    //        System.out.println(secretKey);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // returns a secret key based on the passed bytes. This uses HMAC-SHA algorithm
    }

    // This is a helper method for creating a token.
    private String createToken(Map<String, Object> claims, String subject, Date iat, Date exp, String secretKey){
        String accessTokenId = UUID.randomUUID().toString();
        return Jwts.builder()
                .claims()// starts the nested claims
                .id(accessTokenId)
                .add(claims) // uses the map to set the key-value pairs below.
                .subject(subject) // sets sub - username = "sub": "username"
                .issuedAt(iat) // sets iat - date = "iat": "1752848877"
                .expiration(exp) // sets exp - date = "exp": "1752848985"
                .and() // exits the nested claims
                .signWith(getAccessKey(secretKey)) // signs the JWT using a secret key
                .compact(); // finalizes JWT and returns it as a String
    }

    // returning the current date and time
    private Date issuedAt(){
        return new Date(System.currentTimeMillis());
    }

    // returning expiration for access token
    private Date expAccessToken(){
        return new Date(System.currentTimeMillis() + 60 * 60 * 1000); // an hour long 
    }

    // returning expiration for refresh token
    private Date expRefreshToken(){
        return new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30); // a month long
    }

    public String extractJti(String token){
        return extractClaim(token, Claims::getId);
    }

    // This method is used to extract the username from the token and return it to validateToken method.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // This calls the extractAllClaims to parse the token and applies it to the given function, the claimResolver.
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims); // This uses the Claims:getSubject and returns the subject. Same with Claims::getExpiration
    }

    // This will parse and verifies the JWT. This verifies the token using the verifyWith method and parseSignedClaims method. Then, it returns the claims part of the token using getPayload method.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getAccessKey(accessSecretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload(); // return Claims
    }

    // This checks if the username from the token and username from the UserDetails is equal
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    // This checks if the date from the token is expired
    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    // This method is used to extract the date from the token and return it to isTokenExpired method.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Method for checking/validating refresh token
    public String validateRefreshToken(String token){
        if(isTokenRefreshExpired(token))
            return extractRefreshClaim(token, Claims::getSubject);

        throw new ExpiredTokenException("EXPIRED_REFRESH_TOKEN", "Refresh token is expired");
    }

    public String extractRefreskTokenJti(String token){
        return extractRefreshClaim(token, Claims::getId);
    }

    private boolean isTokenRefreshExpired(String token) {
        return !extractRefreshExpiration(token).before(new Date());
    }

    public Date extractRefreshExpiration(String token) {
        return extractRefreshClaim(token, Claims::getExpiration);
    }

    private <T> T extractRefreshClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractUserIdAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractUserIdAllClaims(String token) {
        System.out.println("Refresh key from valiation: " + refreshSecretKey);
        return Jwts.parser()
                .verifyWith(getAccessKey(refreshSecretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
