package com.ad.app.requests.publisher;

public class PublisherRequest {

	private PublisherRequestData q;

	public PublisherRequest() {
		q = new PublisherRequestData();
	}

	public PublisherRequest(String siteId) {
		q = new PublisherRequestData(siteId);
	}

	public PublisherRequestData getQ() {
		return q;
	}

	public void setQ(PublisherRequestData q) {
		this.q = q;
	}
}
