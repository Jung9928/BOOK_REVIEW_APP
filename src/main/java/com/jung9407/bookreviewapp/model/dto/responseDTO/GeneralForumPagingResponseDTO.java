package com.jung9407.bookreviewapp.model.dto.responseDTO;

import com.jung9407.bookreviewapp.model.dto.GeneralForumPaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralForumPagingResponseDTO<T> {

    private LocalDateTime  transactionTime;
    private String resultCode;
    private String description;
    private T data;
    private GeneralForumPaginationDTO generalForumPaginationDTO;

    public static <T> GeneralForumPagingResponseDTO<T> OK() {
        return (GeneralForumPagingResponseDTO<T>) GeneralForumPagingResponseDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .build();
    }

    // DATA OK
    public static <T> GeneralForumPagingResponseDTO<T> OK(T data) {
        return (GeneralForumPagingResponseDTO<T>) GeneralForumPagingResponseDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .build();
    }

    public static <T> GeneralForumPagingResponseDTO<T> OK(T data, GeneralForumPaginationDTO generalForumPaginationDTO) {
        return (GeneralForumPagingResponseDTO<T>) GeneralForumPagingResponseDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .generalForumPaginationDTO(generalForumPaginationDTO)
                .build();
    }

    public static <T> GeneralForumPagingResponseDTO<T> ERROR(String description) {
        return (GeneralForumPagingResponseDTO<T>) GeneralForumPagingResponseDTO.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .description(description)
                .build();
    }
}
