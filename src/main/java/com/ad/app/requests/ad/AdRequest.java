package com.ad.app.requests.ad;

import javax.validation.constraints.NotNull;

public class AdRequest {

	@NotNull
	private Site site;
	private User user;
	@NotNull
	private Device device;

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
