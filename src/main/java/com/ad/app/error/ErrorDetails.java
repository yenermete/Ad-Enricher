package com.ad.app.error;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorDetails {

	private String details;
	private List<String> messages;

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public ErrorDetails(List<String> messages, String details) {
		this(messages);
		this.details = details;
	}

	public ErrorDetails(String message) {
		this.messages = Arrays.asList(message);
	}

	public ErrorDetails(List<String> messages) {
		this.messages = messages;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}
