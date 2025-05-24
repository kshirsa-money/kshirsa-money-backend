package com.kshirsa.userservice.security;

import com.kshirsa.userservice.entity.UserDetails;
import com.kshirsa.userservice.repository.UserDetailsRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    @Value("#{new Integer(${JWT.VALIDITY})}")
    public int JWT_VALIDITY;
    @Value("${JWT.SECRET}")
    public String JWT_SECRET;

    private final UserDetailsRepository userDetailsRepository;
    private Key signKey;

    @PostConstruct
    public void init() {
        this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    protected Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return (!isTokenExpired(token));
    }

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        UserDetails user = userDetailsRepository.findById(userId).orElseThrow();
        claims.put("Name", user.getName());
        claims.put("User Email", user.getUserEmail());
        return createToken(claims, userId);
    }

    private String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setClaims(claims)
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_VALIDITY * 1000L + 10*1000L))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

//    private String getTemporaryToken(String email, int validityInMin) {
//        return Jwts.builder()
//                .setSubject(email.toLowerCase())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + validityInMin * 1000L + 10*1000L))
//                .signWith(signKey, SignatureAlgorithm.HS256).compact();
//    }
}
