package com.example.scheduling_service.infrastructure.adapter.inbound.web;

import com.example.scheduling_service.application.dto.response.GenericErrorResponse;
import com.example.scheduling_service.domain.exception.SessionConflictException;
import com.example.scheduling_service.domain.exception.SessionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionException.class)
    public ResponseEntity<GenericErrorResponse> handleSessionException(
            SessionException ex, HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(SessionConflictException.class)
    public ResponseEntity<GenericErrorResponse> handleSessionException(
            SessionConflictException ex, HttpServletRequest request
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GenericErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

}
