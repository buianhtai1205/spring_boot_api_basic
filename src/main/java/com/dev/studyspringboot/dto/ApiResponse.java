package com.dev.studyspringboot.dto;

import com.dev.studyspringboot.response.brand.BrandResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ApiResponse(T data) {
        this.statusCode = 200;
        this.message = "Requested successfully";
        this.data = data;
    }
}
