package com.personal.bulletinboardsystem.user.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.personal.bulletinboardsystem.user.ExistsEmailException;
import com.personal.bulletinboardsystem.user.entity.User;
import com.personal.bulletinboardsystem.user.entity.UserRepository;
import com.personal.bulletinboardsystem.user.model.UserInput;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping("/api/user")
    public void register(@RequestBody @Valid UserInput userInput) {
        // List<ResponseError> responseErrors = new ArrayList<>();

        // if(errors.hasErrors()) {
        // errors.getAllErrors().forEach((e) -> {
        // responseErrors.add(ResponseError.of((FiledError)e));
        // });
        // return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        // }
        System.out.println("register");

        if(userRepository.findByEmail(userInput.getEmail()).isPresent()) {
            throw new ExistsEmailException("가입된 이메일이 이미 존재합니다.");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode(userInput.getPassword());
        System.out.println("------------" + userInput.getName() + "--------------");
        userRepository.save(User.builder()
                .email(userInput.getEmail())
                .name(userInput.getName())
                .password(encryptPassword)
                .phone(userInput.getPhone())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<?> ExistEmailExceptionHandler(ExistsEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
