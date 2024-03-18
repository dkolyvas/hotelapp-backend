package com.gmail.kolyvas.hotelapp.util.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {


    private final SecretKey secret;

    public JwtUtil(){
        secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("nargkn4295i20mlaskmgaoir951169816816161651651616518"));
    }

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + 7*24*60*60*1000);
        String userType = authentication.getAuthorities().toArray()[0].toString();

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(secret)
                .claim("usertype", userType)
                .compact();
        return  token;

    }

    public String getUsername(String token){
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secret).build();
        Claims claims = parser.parseClaimsJwt(token).getBody();
        return claims.getSubject();
    }

    public String getUsertType(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build()
                .parseClaimsJwt(token).getBody();
        return claims.get("usertype").toString();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJwt(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
