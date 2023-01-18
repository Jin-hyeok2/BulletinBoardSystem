package com.personal.bulletinboardsystem.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.personal.bulletinboardsystem.entity.Board;
import com.personal.bulletinboardsystem.model.board.BoardInform;
import com.personal.bulletinboardsystem.model.board.BoardWriter;
import com.personal.bulletinboardsystem.repository.BoardRepository;
import com.personal.bulletinboardsystem.repository.UserRepository;
import com.personal.bulletinboardsystem.util.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

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
                .userId(userId)
                .title(boardWriter.getTitle())
                .content(boardWriter.getContent())
                .build());
    }

    public Page<BoardInform> getList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Board> boardPage = boardRepository.findAll(pageRequest);

        for (Board board : boardPage.getContent()) {
            System.out.println(board.getTitle());
        }

        return boardPage.map(board -> BoardInform.builder()
                .title(board.getTitle())
                .userEmail(getUserEmail(board.getUserId()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build());
    }

    public Page<BoardInform> getList(int page, int size, LocalDate start, LocalDate end) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Board> boardPage = boardRepository.findAllByCreatedAtBetween(pageRequest,
                LocalDateTime.of(start, LocalTime.of(0, 0, 0)), LocalDateTime.of(end, LocalTime.of(23, 59, 59)));

        return boardPage.map(board -> BoardInform.builder()
                .title(board.getTitle())
                .userEmail(getUserEmail(board.getUserId()))
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build());
    }

    public String getUserEmail(Long userId) {
        return userRepository.findById(userId).orElseThrow().getEmail();
    }

}
