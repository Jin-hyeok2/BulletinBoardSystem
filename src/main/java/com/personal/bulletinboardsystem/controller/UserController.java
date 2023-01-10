package com.personal.bulletinboardsystem.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.personal.bulletinboardsystem.exception.ExistsEmailException;
import com.personal.bulletinboardsystem.model.UserLogin;
import com.personal.bulletinboardsystem.model.UserSignup;
import com.personal.bulletinboardsystem.model.UserToken;
import com.personal.bulletinboardsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody @Valid UserSignup userInput) {
        userService.signup(userInput);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid UserLogin userLogin) {
        String token = userService.signin(userLogin);

        return ResponseEntity.ok(UserToken.builder()
                .token(token).build());
    }

    @PatchMapping("/signin")
    public ResponseEntity<?> refreshToken(@RequestHeader("F-TOKEN") String token) {

        String newToken = userService.refreshToken(token);

        return ResponseEntity.ok(UserToken.builder()
                .token(newToken).build());
    }

    @GetMapping("/signout")
    public ResponseEntity<?> singout(@RequestHeader("F-TOKEN") String token) {

        return ResponseEntity.ok(userService.signout(token));
    }

    @ExceptionHandler(ExistsEmailException.class)
    public ResponseEntity<?> ExistEmailExceptionHandler(ExistsEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
