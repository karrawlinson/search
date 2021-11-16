package com.carina.search.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class used to set custom error message
 *  
 * @author Karen Rawlinson
 *
 */
@Data
@AllArgsConstructor
public class SearchApiError {

	private HttpStatus status;
	private String message;
	
}
