package com.jung9407.bookreviewapp.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreateDTO {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

}
