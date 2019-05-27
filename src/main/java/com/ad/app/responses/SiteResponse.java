package com.ad.app.responses;

import com.ad.app.requests.ad.Site;
import com.ad.app.responses.demographics.DemographicsSiteData;
import com.ad.app.responses.publisher.PublisherData;

public class SiteResponse extends Site {

	private PublisherData publisher;
	private DemographicsSiteData demographics;

	public SiteResponse() {
	}

	public SiteResponse(Site site, String id, String name, DemographicsSiteData demographics) {
		this.publisher = new PublisherData(id, name);
		this.setPage(site.getPage());
		this.setId(site.getId());
		this.demographics = demographics;
	}

	public PublisherData getPublisher() {
		return publisher;
	}

	public void setPublisher(PublisherData publisher) {
		this.publisher = publisher;
	}

	public DemographicsSiteData getDemographics() {
		return demographics;
	}

	public void setDemographics(DemographicsSiteData demographics) {
		this.demographics = demographics;
	}
}
