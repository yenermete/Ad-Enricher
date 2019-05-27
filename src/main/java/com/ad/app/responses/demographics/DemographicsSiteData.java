package com.ad.app.responses.demographics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DemographicsSiteData {

	@JsonProperty("male_percent")
	private Double malePercent;
	@JsonProperty("female_percent")
	private Double femalePercent;

	public DemographicsSiteData() {
	}

	public DemographicsSiteData(Double femalePercent) {
		this.femalePercent = femalePercent;
		this.malePercent = 100 - femalePercent;
	}

	public Double getMalePercent() {
		return malePercent;
	}

	public void setMalePercent(Double malePercent) {
		this.malePercent = malePercent;
	}

	public Double getFemalePercent() {
		return femalePercent;
	}

	public void setFemalePercent(Double femalePercent) {
		this.femalePercent = femalePercent;
	}
}
