package com.proj.ems.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class EmployeeManagementExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request)
	{
		return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
		WebRequest request)
	{
		return handleExceptionInternal(ex, ex.getLocalizedMessage(), headers, HttpStatus.BAD_REQUEST, request);
	}
}
