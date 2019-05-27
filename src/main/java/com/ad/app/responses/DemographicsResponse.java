package com.ad.app.responses;

import com.ad.app.responses.demographics.DemographicsData;

public class DemographicsResponse extends DemographicsData {

	private Double pctMale;

	public Double getPctMale() {
		return pctMale;
	}

	public void setPctMale(Double pctMale) {
		this.pctMale = pctMale;
	}

}
