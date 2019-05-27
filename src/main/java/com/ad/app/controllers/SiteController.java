package com.ad.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ad.app.requests.ad.CacheClearRequest;
import com.ad.app.services.SiteService;

@RestController
@RequestMapping("/sites/")
public class SiteController {

	@Autowired
	private SiteService siteService;

	@DeleteMapping(path = "publisher/cache/remove/v1/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void clearCacheEntry(@Valid @RequestBody CacheClearRequest request) {
		siteService.clearPublisherCacheBySiteId(request.getKey());
	}
}
