package com.example.product.core.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.product.user.StringArrayConverter;
import com.example.product.user.User;

import java.util.Date;

public class JwtTokenProvider {

    // ** JWT 토큰의 만료 시간을 1시간으로 설정.
    private static final Long EXP = 1000L * 60 * 60;

    // ** 인증 헤더에 사용될 토큰의 접두어 ("Bearer ")
    public static final String TOKEN_PREFIX = "Bearer ";
    
    // ** 인증 헤더의 이름을 "Authorization"으로 설정.
    public static final String HEADER = "Authorization";
    
    // ** 토큰의 서명을 생성하고 검증할 때 사용하는 비밀 키
    private static final String SECRET = "SECRET_KEY";

    // ** User 객체의 정보를 사용해 JWT 토큰을 생성하고 반환.
    public static String create(User user){
        StringArrayConverter stringArrayConverter = new StringArrayConverter();

        String roles = stringArrayConverter.convertToDatabaseColumn(
                user.getRoles()
        );

        String jwt = JWT.create()
                .withSubject(user.getEmail())// ** 토큰의 대상정보 셋팅
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .withClaim("id", user.getId())
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC512(SECRET));// ** JWT 생성 알고리즘 설정
        return TOKEN_PREFIX + jwt;
    }
    public static String createRefresh(User user){
        String jwt = JWT.create()
                .withSubject(user.getEmail())// ** 토큰의 대상정보 셋팅
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP))
                .sign(Algorithm.HMAC512(SECRET));// ** JWT 생성 알고리즘 설정
        return jwt;
    }

    // **  JWT 토큰 문자열을 검증하고, 유효하다면 디코딩된 DecodedJWT 객체를 반환.
    public static DecodedJWT verify(String jwt) throws SignatureVerificationException, TokenExpiredException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET)) // 검증 방법
                .build()
                .verify(jwt); // 검증 할 것

        return decodedJWT;
    }
}
