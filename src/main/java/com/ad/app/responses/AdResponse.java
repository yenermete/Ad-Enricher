package com.ad.app.responses;

import com.ad.app.requests.ad.AdRequest;
import com.ad.app.requests.ad.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AdResponse extends AdRequest {

	private DeviceResponse device;
	private SiteResponse site;
	private User user;

	public AdResponse() {
	}

	public AdResponse(DeviceResponse device, SiteResponse site, User user) {
		this.device = device;
		this.site = site;
		this.user = user;
	}

	public DeviceResponse getDevice() {
		return device;
	}

	public void setDevice(DeviceResponse device) {
		this.device = device;
	}

	public SiteResponse getSite() {
		return site;
	}

	public void setSite(SiteResponse site) {
		this.site = site;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
