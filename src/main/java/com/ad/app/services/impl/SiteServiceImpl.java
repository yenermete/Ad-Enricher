package com.ad.app.services.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ad.app.requests.publisher.PublisherRequest;
import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.publisher.Publisher;
import com.ad.app.services.SiteService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class SiteServiceImpl implements SiteService {

	private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate;
	private final String siteServerHost;
	private final String siteServerPort;
	private final String publisherPath;
	private final String demographicsPath;
	private final String resourceUrl;
	private final HttpHeaders jsonHeaders;
	private final LoadingCache<String, Publisher> publisherCache = CacheBuilder.newBuilder().initialCapacity(50)
			.maximumSize(1000).recordStats().build(new CacheLoader<String, Publisher>() {

				@Override
				public Publisher load(String key) throws Exception {
					return getPublisherForCache(key);
				}

			});
	private final LoadingCache<String, Demographics> demographicsCache = CacheBuilder.newBuilder().initialCapacity(50)
			.maximumSize(1000).recordStats().expireAfterWrite(12, TimeUnit.HOURS)
			.build(new CacheLoader<String, Demographics>() {

				@Override
				public Demographics load(String key) throws Exception {
					return getDemographicsForCache(key);
				}

			});

	@Autowired
	public SiteServiceImpl(@Value("${adApp.siteServer.host}") String siteServerHost,
			@Value("${adApp.siteServer.port}") String siteServerPort,
			@Value("${adApp.siteServer.publisherPath}") String publisherPath,
			@Value("${adApp.siteServer.demographicsPath}") String demographicsPath,
			@Value("${adApp.resourceWithPort}") String resourceUrl) {
		this.siteServerHost = siteServerHost;
		this.siteServerPort = siteServerPort;
		this.publisherPath = publisherPath;
		this.demographicsPath = demographicsPath;
		this.resourceUrl = resourceUrl;
		jsonHeaders = new HttpHeaders();
		jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		jsonHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	private Publisher getPublisherForCache(String siteId) {
		ResponseEntity<Publisher> response = restTemplate.postForEntity(
				String.format(resourceUrl, siteServerHost, siteServerPort, publisherPath),
				new HttpEntity<PublisherRequest>(new PublisherRequest(siteId), jsonHeaders), Publisher.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		throw new RuntimeException(String.format("Invalid response for getting publisher of site %s with code %s",
				siteId, response.getStatusCodeValue()));

	}

	private Demographics getDemographicsForCache(String siteId) {
		ResponseEntity<Demographics> response = restTemplate.getForEntity(
				String.format(resourceUrl, siteServerHost, siteServerPort, String.format(demographicsPath, siteId)),
				Demographics.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}

		throw new RuntimeException(String.format("Invalid response for getting publisher of site %s with code %s",
				siteId, response.getStatusCodeValue()));
	}

	@Override
	public Publisher getPublisher(String siteId) {
		try {
			return publisherCache.get(siteId);
		} catch (ExecutionException e) {
			logger.error(String.format("Publisher cache for %s could not be loaded.", siteId), e);
			throw new RuntimeException("Publisher could not be loaded!");
		}
	}

	@Override
	public Demographics getDemographics(String siteId) { // weekly
		try {
			return demographicsCache.get(siteId);
		} catch (ExecutionException e) {
			logger.error(String.format("Demographics cache for %s could not be loaded.", siteId), e);
			return null;
		}
	}

	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}

	@Override
	public void clearPublisherCacheBySiteId(String siteId) {
		publisherCache.invalidate(siteId);

	}

}
