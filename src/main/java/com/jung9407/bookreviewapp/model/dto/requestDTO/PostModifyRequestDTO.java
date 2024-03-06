package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequestDTO {

    private String title;
    private String content;
}
