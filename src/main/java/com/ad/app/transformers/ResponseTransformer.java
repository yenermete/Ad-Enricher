package com.ad.app.transformers;

import com.ad.app.requests.ad.AdRequest;
import com.ad.app.responses.AdResponse;
import com.ad.app.responses.DeviceResponse;
import com.ad.app.responses.SiteResponse;
import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.demographics.DemographicsSiteData;
import com.ad.app.responses.publisher.Publisher;
import com.maxmind.geoip2.record.Country;

public class ResponseTransformer {

	public AdResponse transformSiteResponses(Publisher publisher, Demographics demographics, Country country,
			AdRequest request) {
		DeviceResponse device = new DeviceResponse(request.getDevice(), country.getIsoCode());
		DemographicsSiteData demographicsData = null;
		if (demographics != null) {
			demographicsData = new DemographicsSiteData(demographics.getDemographics().getPctFemale());
		}
		SiteResponse site = new SiteResponse(request.getSite(), publisher.getPublisher().getId(),
				publisher.getPublisher().getName(), demographicsData);
		return new AdResponse(device, site, request.getUser());
	}
}
