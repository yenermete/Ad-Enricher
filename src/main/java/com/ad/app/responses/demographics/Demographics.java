package com.ad.app.responses.demographics;

import org.springframework.core.style.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Demographics {

	private DemographicsData demographics;

	public DemographicsData getDemographics() {
		return demographics;
	}

	public void setDemographics(DemographicsData demographics) {
		this.demographics = demographics;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("female_data", demographics == null ? null : demographics.getPctFemale()).toString();
	}

}
