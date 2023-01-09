package com.personal.bulletinboardsystem.service;

import java.util.Date;

import javax.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.personal.bulletinboardsystem.entity.User;
import com.personal.bulletinboardsystem.exception.ExistsEmailException;
import com.personal.bulletinboardsystem.exception.PasswordNotMatchException;
import com.personal.bulletinboardsystem.exception.UserNotFoundException;
import com.personal.bulletinboardsystem.model.UserLogin;
import com.personal.bulletinboardsystem.model.UserSignup;
import com.personal.bulletinboardsystem.repository.UserRepository;
import com.personal.bulletinboardsystem.util.JWTUtils;
import com.personal.bulletinboardsystem.util.PasswordUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    public boolean signup(@Valid UserSignup userInput) {
        if (userRepository.findByEmail(userInput.getEmail()).isPresent()) {
            throw new ExistsEmailException("가입된 이메일이 이미 존재합니다.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode(userInput.getPassword());
        userRepository.save(User.builder()
                .email(userInput.getEmail())
                .name(userInput.getName())
                .password(encryptPassword)
                .phone(userInput.getPhone())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build());

        return true;
    }

    public String signin(@Valid UserLogin userLogin) {
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        return getToken(user);
    }

    public String getToken(User user) {
        LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        return JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String refreshToken(String token) {
        System.out.println("======================= token 정보 =======================");
        System.out.println(token);
        String email = JWTUtils.getSubject(token, jwtSecret);

        System.out.println("======================= email 정보 =======================");
        System.out.println(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        return JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public String signout(String token) {
        String email = "";
        try {
            email = JWTUtils.getSubject(token, jwtSecret);
        } catch (SignatureVerificationException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

        return email;
    }
}
