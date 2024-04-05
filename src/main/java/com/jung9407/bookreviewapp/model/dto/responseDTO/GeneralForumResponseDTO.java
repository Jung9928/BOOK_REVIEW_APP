package com.jung9407.bookreviewapp.model.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralForumResponseDTO {

    private long post_id;

    private String member_id;

    private String title;

    private String content;

    private int vw_cnt;

    private int rcmnd_cnt;

    private Timestamp registeredAt;

    private Timestamp modifiedAt;
}
