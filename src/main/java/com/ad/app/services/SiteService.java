package com.ad.app.services;

import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.publisher.Publisher;

public interface SiteService {

	public Publisher getPublisher(String siteId);
	
	public Demographics getDemographics(String siteId);
	
	public void clearPublisherCacheBySiteId(String siteId);
}
