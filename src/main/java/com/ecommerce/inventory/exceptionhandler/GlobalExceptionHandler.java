package com.ecommerce.inventory.exceptionhandler;

import com.ecommerce.inventory.dto.ApiErrorResponse;
import com.ecommerce.inventory.exceptions.BadRequestException;
import com.ecommerce.inventory.exceptions.ResourceAlreadyExists;
import com.ecommerce.inventory.exceptions.ResourceNotFoundException;
import com.ecommerce.inventory.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------- 400 : Validation Errors ----------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildError(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );
    }

    // ---------- 404 ----------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ApiErrorResponse> handleResourceExist(
            ResourceAlreadyExists ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.ALREADY_REPORTED,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ---------- 401 ----------
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ---------- 400 ----------
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ---------- 500 (Fallback) ----------
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handleGeneric(
//            Exception ex,
//            HttpServletRequest request) {
//
//        return buildError(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "Something went wrong",
//                request.getRequestURI()
//        );
//    }

    // ---------- Helper ----------
    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            String path) {

        return ResponseEntity.status(status).body(
                new ApiErrorResponse(
                        Instant.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        path
                )
        );
    }
}
