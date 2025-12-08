package com.moviq.movie_reservation_service.exception;

import com.moviq.movie_reservation_service.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){
        String errorMessage = ex.getBindingResult().getFieldErrors().
                stream().map(error->error.getField()+ " : "+ error.getDefaultMessage())
                .findFirst().orElse("Validation error");
        return new ResponseEntity<>(new ErrorResponse(errorMessage,HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintValidationErrors(ConstraintViolationException ex){
        String errorMessage = ex.getConstraintViolations().
                stream().map(error->error.getPropertyPath() + " : " + error.getMessage())
                .findFirst().orElse("Validation error");
        return new ResponseEntity<>(new ErrorResponse(errorMessage,HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value()),HttpStatus.CONFLICT);

    }
}
