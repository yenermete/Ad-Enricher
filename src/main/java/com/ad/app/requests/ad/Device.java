package com.ad.app.requests.ad;

import javax.validation.constraints.NotBlank;

public class Device {

	@NotBlank
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
