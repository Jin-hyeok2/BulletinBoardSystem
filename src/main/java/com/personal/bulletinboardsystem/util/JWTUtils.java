package com.personal.bulletinboardsystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JWTUtils {
    public String getSubject(String token, String jwtSecret) {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .build()
                .verify(token)
                .getSubject();
    }

    public long getUserId(String token, String jwtSecret) {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .build()
                .verify(token)
                .getClaim("user_id").asLong();
    }
}
