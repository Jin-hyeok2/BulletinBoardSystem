package com.personal.bulletinboardsystem.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.bulletinboardsystem.model.board.BoardInform;
import com.personal.bulletinboardsystem.model.board.BoardWriter;
import com.personal.bulletinboardsystem.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board/write")
    public ResponseEntity<?> wirteBoard(@RequestBody @Valid BoardWriter BoardWriter,
            @RequestHeader("F-TOKEN") String token) {
        boardService.wirteBoard(BoardWriter, token);

        return ResponseEntity.ok().build();
    }

    // @DateTimeFormat(pattern = "yyyy-MM-dd") 
    @GetMapping("/board")
    public ResponseEntity<Page<BoardInform>> getBoardList(@RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyyMMdd") LocalDate end) {
        if (start == null) {
            return ResponseEntity.ok(boardService.getList(page, size));
        } else {
            return ResponseEntity.ok(boardService.getList(page, size, start, end));
        }
    }

}
