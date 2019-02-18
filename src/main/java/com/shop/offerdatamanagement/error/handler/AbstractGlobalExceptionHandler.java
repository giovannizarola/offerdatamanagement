package com.shop.offerdatamanagement.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.shop.offerdatamanagement.error.ExceptionResponse;

public class AbstractGlobalExceptionHandler {

    public ResponseEntity<ExceptionResponse> buildAndSendErrorResponse(Exception ex, String errorCode, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(errorCode);
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(response, httpStatus);
    }
}
