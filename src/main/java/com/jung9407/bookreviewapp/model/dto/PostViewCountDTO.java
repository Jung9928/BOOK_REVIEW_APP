package com.jung9407.bookreviewapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostViewCountDTO {

    private long todayViewCount;

    private long totalViewCount;
}
