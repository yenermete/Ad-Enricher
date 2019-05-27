package com.ad.app.services;

import java.net.InetAddress;

import com.maxmind.geoip2.record.Country;

public interface GeoService {

	public Country getCountry(InetAddress address);
}
