package com.ad.app.services;

import com.ad.app.requests.ad.AdRequest;
import com.ad.app.responses.AdResponse;

public interface AdService {

	public AdResponse enrichRequest(AdRequest request);

}
