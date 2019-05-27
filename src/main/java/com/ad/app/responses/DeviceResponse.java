package com.ad.app.responses;

import com.ad.app.requests.ad.Device;

public class DeviceResponse extends Device {

	private Geo geo;

	public DeviceResponse() {
		super();
	}

	public DeviceResponse(Device device, String country) {
		this.setIp(device.getIp());
		this.setGeo(new Geo(country));
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

}
