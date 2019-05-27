package com.ad.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidCountryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String iso;

	public InvalidCountryException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidCountryException(String message) {
		super(message);
	}

	public InvalidCountryException(String message, String iso) {
		this(message);
		this.iso = iso;
	}

	public String getIso() {
		return iso;
	}
}
