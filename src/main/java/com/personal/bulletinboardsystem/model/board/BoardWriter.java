package com.personal.bulletinboardsystem.model.board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardWriter {
    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(max = 50, message = "게시글의 제목은 최대 50까지 입력해야 합니다.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;
}
