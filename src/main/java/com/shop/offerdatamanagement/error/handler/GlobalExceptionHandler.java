package com.shop.offerdatamanagement.error.handler;



import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.shop.offerdatamanagement.error.ExceptionResponse;
import com.shop.offerdatamanagement.error.exception.BadRequestException;
import com.shop.offerdatamanagement.error.exception.DuplicateOfferException;
import com.shop.offerdatamanagement.error.exception.ResourceNotFoundException;
import com.shop.offerdatamanagement.error.message.ErrorCode;

@ControllerAdvice
public class GlobalExceptionHandler extends AbstractGlobalExceptionHandler {

	@ExceptionHandler(DuplicateOfferException.class)
	public ResponseEntity<ExceptionResponse> offerCodeDuplicated(DuplicateOfferException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_DUPLICATE_OFFER, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ExceptionResponse> offerCodeMalformed(BadRequestException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}
	
	// HANDLING CUSTOM EXCEPTIONS
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex) {
        return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

	

	// HANDLING SOME NON-CUSTOM EXCEPTIONS
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponse> constraintViolation(ConstraintViolationException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERRROR_CODE_CONSTRAINT_VIOLATION, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnexpectedTypeException.class)
	public ResponseEntity<ExceptionResponse> requestWithUnexpectedType(UnexpectedTypeException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> requestWithInvalidArgument(MethodArgumentNotValidException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> requestNotReadable(HttpMessageNotReadableException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<ExceptionResponse> requestNotParsable(JsonParseException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<ExceptionResponse> invalidRequest(TransactionSystemException ex) {
		return buildAndSendErrorResponse(ex, ErrorCode.ERROR_CODE_MALFORMED_REQUEST, HttpStatus.BAD_REQUEST);
	}
}
