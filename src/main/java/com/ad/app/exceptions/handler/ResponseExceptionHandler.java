package com.ad.app.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ad.app.error.ErrorDetails;
import com.ad.app.exceptions.InvalidCountryException;
import com.ad.app.exceptions.PublisherNotFoundException;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	private final String description = "Country %s not allowed.";
	private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);

	@ExceptionHandler(InvalidCountryException.class)
	public final ResponseEntity<ErrorDetails> handleCountryNotFoundException(InvalidCountryException e,
			WebRequest request) {
		logger.error("Invalid country", e);
		ErrorDetails errorDetails = new ErrorDetails(
				StringUtils.isEmpty(e.getIso()) ? null : String.format(description, e.getIso()));
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(PublisherNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handlePublisherNotFoundException(PublisherNotFoundException e,
			WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception e, WebRequest request) {
		logger.error(e.getMessage(), e);
		ErrorDetails errorDetails = new ErrorDetails(e.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : e.getBindingResult().getAllErrors()) {
			String field = null;
			for (Object o : error.getArguments()) {
				if (o instanceof DefaultMessageSourceResolvable) {
					field = ((DefaultMessageSourceResolvable) o).getDefaultMessage();
				}
			}
			details.add(field + " " + error.getDefaultMessage());
		}
		ErrorDetails error = new ErrorDetails(details, "Validation Failed");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
