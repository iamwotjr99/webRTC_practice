package com.practice.webRTC.global.exception;

import com.practice.webRTC.global.common.ApiResponse;
import com.practice.webRTC.global.common.ResponseFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handlerCustomException(CustomException ex) {
        return ResponseFactory.fail(ex);
    }
}
