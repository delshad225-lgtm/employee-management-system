package com.devportfolio.empmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// Handles specific 404 Not Found scenarios
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		model.addAttribute("statusCode", 404);
		return "error";
		
	}
	
	// Ignore favicon 404s so they don't clutter your logs or trigger error pages
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
	    return ResponseEntity.notFound().build();
	}
	
	// Handles any other unexpected generic runtime exceptions (500)
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGlobalException(Exception ex, Model model) {
		ex.printStackTrace();
		model.addAttribute("errorMessage", "An unexpected error occured. Please Try again later.");
		model.addAttribute("statusCode",500);
		
		return "error";
	}

}
