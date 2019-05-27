package com.ad.app.requests.ad;

import javax.validation.constraints.NotBlank;

public class User {

	@NotBlank
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
