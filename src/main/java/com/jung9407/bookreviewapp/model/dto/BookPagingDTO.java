package com.jung9407.bookreviewapp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookPagingDTO {

    private int page;
    private int dataCount;
    private int pageSize;
    private String keyword;
    private String searchType;


}
