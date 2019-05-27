package com.ad.app.services.impl;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ad.app.services.GeoService;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

@Service
public class GeoServiceImpl implements GeoService {

	private static final Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);
	private final WebServiceClient client;

	@Autowired
	public GeoServiceImpl(@Value("${adApp.maxMind.userId}") Integer userId,
			@Value("${adApp.maxMind.key}") String maxMindKey) {
		client = new WebServiceClient.Builder(userId, maxMindKey).connectTimeout(1000).readTimeout(1000).build();
	}

	@Override
	public Country getCountry(InetAddress address) {
		CountryResponse country = null;
		try {
			country = client.country(address);
		} catch (IOException | GeoIp2Exception e) {
			throw new RuntimeException(
					String.format("Geo ip error for ip %s. Detail: %s", address.getHostAddress(), e.getMessage()), e);
		}
		if (country == null) {
			return null;
		}
		return country.getCountry();
	}

}
