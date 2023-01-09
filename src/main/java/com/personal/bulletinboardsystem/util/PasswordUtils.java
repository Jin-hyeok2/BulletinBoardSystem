package com.personal.bulletinboardsystem.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordUtils {
    public boolean equalPassword(String password, String encryptedPassword) {
        return  BCrypt.checkpw(password, encryptedPassword);
    }
}
