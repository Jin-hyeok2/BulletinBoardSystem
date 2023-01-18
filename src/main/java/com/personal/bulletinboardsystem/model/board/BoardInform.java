package com.personal.bulletinboardsystem.model.board;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BoardInform {
    private String userEmail;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
