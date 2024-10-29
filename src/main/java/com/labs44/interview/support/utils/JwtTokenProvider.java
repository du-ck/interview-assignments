package com.labs44.interview.support.utils;


import com.labs44.interview.domain.user.Authority;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * JWT Token 관련
 * 발급과 validate 처리하는 클래스
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    /**
     * 이메일과 권한을 이용하여 JWT 토큰 생성
     */
    public String issue(String email, Authority role) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validate(HttpServletRequest request, String token) {
        String exception = "Exception";
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer ", ""));
            return true;
        } catch (ExpiredJwtException e) {
            request.setAttribute(exception, "토큰이 만료되었습니다");
        } catch (UnsupportedJwtException e) {
            request.setAttribute(exception, "지원하지 않는 토큰입니다");
        } catch (IllegalArgumentException e) {
            request.setAttribute(exception, "토큰이 비었습니다");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        // 사용자 이름과 권한을 기반으로 Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString())));
    }
}
