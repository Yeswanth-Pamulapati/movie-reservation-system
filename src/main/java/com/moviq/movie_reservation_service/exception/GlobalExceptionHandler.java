package com.moviq.movie_reservation_service.exception;

import com.moviq.movie_reservation_service.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){
        String errorMessages = ex.getBindingResult().getFieldErrors().
                stream().map(error->error.getField()+ " : "+ error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(new ErrorResponse(errorMessages,HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value()),HttpStatus.CONFLICT);

    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectCredentialsException(BadCredentialsException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value()),HttpStatus.CONFLICT);
    }
}
