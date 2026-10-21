package com.example.spring_rag.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public static ApiException badRequest(String message){
        return new ApiException(message, HttpStatus.BAD_REQUEST);
    }

    public static ApiException notFound(String message){
        return new ApiException(message, HttpStatus.NOT_FOUND);
    }

    public static ApiException unAuthorized(String message){
        return new ApiException(message, HttpStatus.UNAUTHORIZED);
    }

}
