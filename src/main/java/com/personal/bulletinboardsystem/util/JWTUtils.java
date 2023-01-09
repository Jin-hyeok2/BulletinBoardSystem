package com.personal.bulletinboardsystem.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JWTUtils {
    private static final String KEY = "helloWorld";

    public static String getIssuer(String token) {
        return JWT.require(Algorithm.HMAC512(KEY))
        .build()
        .verify(token)
        .getIssuer();
    }
}
