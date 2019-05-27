package com.ad.app.requests.publisher;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublisherRequestData {

	@JsonProperty(value = "siteID")
	private String siteId;

	public PublisherRequestData() {
	}

	public PublisherRequestData(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

}
