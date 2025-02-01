package com.liro.zuulservice.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String code;

    public ApiError(HttpStatus status, String message, String code ) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.code = code;
    }
}