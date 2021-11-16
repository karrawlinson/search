package com.carina.search.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
/**
 * Handle any exceptions thrown
 * 
 * @author Karen Rawlinson
 *
 */
import lombok.extern.slf4j.Slf4j;
@Slf4j 
@ControllerAdvice("com.carina.search.controllers")
public class SearchExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SearchApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Missing path variable"));
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SearchApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Missing request parameters"));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("Error processing request", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SearchApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request"));
	}

}
