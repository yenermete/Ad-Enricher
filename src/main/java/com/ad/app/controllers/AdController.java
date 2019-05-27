package com.ad.app.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.app.requests.ad.AdRequest;
import com.ad.app.responses.AdResponse;
import com.ad.app.services.AdService;

@RestController
@RequestMapping("/ads/")
public class AdController {

	private static final Logger logger = LoggerFactory.getLogger(AdController.class);

	@Autowired
	private AdService adService;

	@PostMapping(path = "enrich/v1/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AdResponse enrichAd(@Valid @RequestBody AdRequest request) {
		return adService.enrichRequest(request);
	}

	public AdService getAdService() {
		return adService;
	}

	public void setAdService(AdService adService) {
		this.adService = adService;
	}
}
