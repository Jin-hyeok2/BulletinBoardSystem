package com.personal.bulletinboardsystem.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.personal.bulletinboardsystem.model.board.BoardWriter;
import com.personal.bulletinboardsystem.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board/write")
    public ResponseEntity<?> wirteBoard(@RequestBody @Valid BoardWriter BoardWriter, @RequestHeader("F-TOKEN") String token) {
        boardService.wirteBoard(BoardWriter, token);

        return ResponseEntity.ok().build();
    }
    
}
