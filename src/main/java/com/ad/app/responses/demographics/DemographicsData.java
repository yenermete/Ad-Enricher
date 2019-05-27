package com.ad.app.responses.demographics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DemographicsData {

	@JsonProperty(value = "pct_female")
	private Double pctFemale;

	public Double getPctFemale() {
		return pctFemale;
	}

	public void setPctFemale(Double pctFemale) {
		this.pctFemale = pctFemale;
	}
}
