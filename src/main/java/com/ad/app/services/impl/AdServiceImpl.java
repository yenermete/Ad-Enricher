package com.ad.app.services.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ad.app.exceptions.InvalidCountryException;
import com.ad.app.exceptions.PublisherNotFoundException;
import com.ad.app.requests.ad.AdRequest;
import com.ad.app.responses.AdResponse;
import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.publisher.Publisher;
import com.ad.app.services.AdService;
import com.ad.app.services.GeoService;
import com.ad.app.services.SiteService;
import com.ad.app.transformers.ResponseTransformer;
import com.maxmind.geoip2.record.Country;

@Service
public class AdServiceImpl implements AdService {

	// @Autowired
	private SiteService siteService;
	// @Autowired
	private GeoService geoService;

	private final String allowedCountry;
	private final ResponseTransformer transformer;

	private static final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

	@Autowired
	public AdServiceImpl(@Value("${adApp.allowedCountry}") String allowedCountry, SiteService siteService,
			GeoService geoService) {
		this.allowedCountry = allowedCountry;
		this.siteService = siteService;
		this.geoService = geoService;
		transformer = new ResponseTransformer();
	}

	@Override
	public AdResponse enrichRequest(AdRequest request) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(request.getDevice().getIp());
		} catch (UnknownHostException e) {
			throw new InvalidCountryException(request.getDevice().getIp(), e);
		}
		Country country = geoService.getCountry(address);
		if (country == null || !allowedCountry.equalsIgnoreCase(country.getIsoCode())) {
			throw new InvalidCountryException(request.getDevice().getIp(), country.getIsoCode());
		}
		Publisher publisher = siteService.getPublisher(request.getSite().getId());
		if (publisher == null || publisher.getPublisher() == null || publisher.getPublisher().getId() == null) {
			throw new PublisherNotFoundException(
					String.format("Publisher %s does not exist", request.getSite().getId()));
		}
		Demographics demoResponse = null;
		try {
			demoResponse = siteService.getDemographics(request.getSite().getId());
		} catch (Exception e) {
			logger.error(String.format("Demographics for %s could not be received", request.getSite().getId()), e);
		}
		return transformer.transformSiteResponses(publisher, demoResponse, country, request);
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public GeoService getGeoService() {
		return geoService;
	}

	public void setGeoService(GeoService geoService) {
		this.geoService = geoService;
	}

}
