package ua.zotin.java.testtask.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ua.zotin.java.testtask.exception.exceptions.*;

import java.time.LocalDateTime;
import java.util.*;


@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler  {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>>  handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {
        Map<String, String> map =  new TreeMap<>();
        map.put("error", exception.getMessage());
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> map =  new TreeMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return map;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                                     HttpServletRequest request, WebRequest webRequest) {
        Map<String, String> map =  new TreeMap<>();
        if(request.getMethod().equals("POST")){
            String errorMessage = "Invalid date format. Please provide the date in the format 'yyyy-MM-dd'.";
            map.put("error", errorMessage);
            map.put("path", webRequest.getDescription(false));
            map.put("status", "400");
            map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        } else if(request.getMethod().equals("PATCH")){
            String errorMessage = "Invalid data format";
            map.put("error", errorMessage);
            map.put("path", webRequest.getDescription(false));
            map.put("status", "400");
            map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        } else {
            String errorMessage = "Bad request";
            map.put("error", errorMessage);
            map.put("path", webRequest.getDescription(false));
            map.put("status", "400");
            map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        }

        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataReadFromFilePropertiesException.class)
    public ResponseEntity<Map<String, String>>  handleDataReadFromFilePropertiesException(DataReadFromFilePropertiesException exception, WebRequest request) {
        Map<String, String> map =  new TreeMap<>();
        map.put("error", exception.getMessage());
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonIsNullException.class)
    public ResponseEntity<Map<String, String>>  handleJsonIsNullException(JsonIsNullException exception, WebRequest request) {
        Map<String, String> map =  new TreeMap<>();
        map.put("error", exception.getMessage());
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, WebRequest request) {
        Map<String, String> map =  new TreeMap<>();
        String errorMessage = "Invalid type of parameter try to change it in url";
        map.put("error", errorMessage);
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @Value("${minimum.age}")
    public int minAge;
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Map<String, String> map =  new TreeMap<>();

        for (ConstraintViolation<?> violation : violations) {
            if ("birthDate".equals(violation.getPropertyPath().toString())) {
                String message = "Now minimal birthDate is " + minAge + " ,change your birthDate";
                map.put("error", message);
                map.put("path", request.getDescription(false));
                map.put("status", "400");
                map.put( "timestamp", String.valueOf(LocalDateTime.now()));
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
        }
        map.put("error", "Validation failed");
        log.info("Exception is " + ex.getMessage());
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler({MissingServletRequestParameterException.class, FromAndToException.class,
            UserWithIdWasNotFoundException.class, UserAlreadyExistsException.class, IdAreNotEqualsException.class})
    public ResponseEntity<Map<String, String>> handleException(Exception ex, WebRequest request) {
        Map<String, String> map = new TreeMap<>();
        map.put("error", ex.getMessage());
        map.put("path", request.getDescription(false));
        map.put("status", "400");
        map.put( "timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


}