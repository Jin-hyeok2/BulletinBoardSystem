package com.personal.bulletinboardsystem.user.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.personal.bulletinboardsystem.exception.ExistsEmailException;
import com.personal.bulletinboardsystem.exception.PasswordNotMatchException;
import com.personal.bulletinboardsystem.exception.UserNotFoundException;
import com.personal.bulletinboardsystem.user.entity.User;
import com.personal.bulletinboardsystem.user.entity.UserRepository;
import com.personal.bulletinboardsystem.user.model.UserInput;
import com.personal.bulletinboardsystem.user.model.UserLogin;
import com.personal.bulletinboardsystem.user.model.UserToken;
import com.personal.bulletinboardsystem.util.JWTUtils;
import com.personal.bulletinboardsystem.util.PasswordUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody @Valid UserInput userInput) {
        // List<ResponseError> responseErrors = new ArrayList<>();

        // if(errors.hasErrors()) {
        // errors.getAllErrors().forEach((e) -> {
        // responseErrors.add(ResponseError.of((FiledError)e));
        // });
        // return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        // }
        System.out.println("register");

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

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid UserLogin userLogin) {
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC512("helloWorld"));

        return ResponseEntity.ok(UserToken.builder()
        .token(token).build());
    }

    @PatchMapping("/signin")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("F-TOKEN");

        String email = JWT.require(Algorithm.HMAC512("helloWorld"))
        .build()
        .verify(token)
        .getIssuer();

        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        LocalDateTime expiredDateTime = LocalDateTime.now().plusDays(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String newToken = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getEmail())
                .sign(Algorithm.HMAC512("helloWorld"));

        return ResponseEntity.ok(UserToken.builder()
        .token(newToken).build());
    }

    public ResponseEntity<?> singout(@RequestHeader("F-TOKEN") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            // TODO: handle exception
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<?> ExistEmailExceptionHandler(ExistsEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
