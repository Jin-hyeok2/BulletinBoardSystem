package com.personal.bulletinboardsystem.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.personal.bulletinboardsystem.entity.Board;
import com.personal.bulletinboardsystem.model.board.BoardWriter;
import com.personal.bulletinboardsystem.repository.BoardRepository;
import com.personal.bulletinboardsystem.util.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    public void wirteBoard(@Valid BoardWriter boardWriter, String token) {
        long userId = 0;
        try {
            userId = JWTUtils.getUserId(token, jwtSecret);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
        
        boardRepository.save(Board.builder()
                .user_id(userId)
                .title(boardWriter.getTitle())
                .content(boardWriter.getContent())
                .build());
    }

}
