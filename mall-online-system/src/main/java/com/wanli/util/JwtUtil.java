package com.wanli.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "token.jwt")
public class JwtUtil {
    private long expire;
    private String secret;
    private String header;

    public String createToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime()+expire*1000);

        return Jwts.builder().setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public Claims getClaimsToken(String jwt){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
