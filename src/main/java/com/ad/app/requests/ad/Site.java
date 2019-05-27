package com.ad.app.requests.ad;

import javax.validation.constraints.NotBlank;

public class Site {

	@NotBlank
	private String page;
	@NotBlank
	private String id;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
