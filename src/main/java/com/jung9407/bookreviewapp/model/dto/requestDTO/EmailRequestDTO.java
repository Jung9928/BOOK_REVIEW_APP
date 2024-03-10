package com.jung9407.bookreviewapp.model.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EmailRequestDTO {

    private String verificationCode;
}
