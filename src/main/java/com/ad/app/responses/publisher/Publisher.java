package com.ad.app.responses.publisher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Publisher {

	private PublisherData publisher;

	public PublisherData getPublisher() {
		return publisher;
	}

	public void setPublisher(PublisherData publisher) {
		this.publisher = publisher;
	}

}
