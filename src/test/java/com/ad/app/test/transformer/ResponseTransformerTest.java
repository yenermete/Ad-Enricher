package com.ad.app.test.transformer;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ad.app.requests.ad.AdRequest;
import com.ad.app.requests.ad.Device;
import com.ad.app.requests.ad.Site;
import com.ad.app.requests.ad.User;
import com.ad.app.responses.AdResponse;
import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.demographics.DemographicsData;
import com.ad.app.responses.publisher.Publisher;
import com.ad.app.responses.publisher.PublisherData;
import com.ad.app.transformers.ResponseTransformer;
import com.maxmind.geoip2.record.Country;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
public class ResponseTransformerTest extends TestCase {

	private ResponseTransformer transformer;

	@Before
	public void setUp() {
		transformer = new ResponseTransformer();
	}

	@Test
	public void testResponseWithNoDemographics() {
		AdResponse response = getAdResponse("testId", "testName", 15.2, "US", "192.234.214.23", "testSiteId",
				"testSitePage", "testUserId");
		assertEquals("192.234.214.23", response.getDevice().getIp());
		assertEquals("US", response.getDevice().getGeo().getCountry());
		assertEquals("testUserId", response.getUser().getId());
		assertEquals("testSiteId", response.getSite().getId());
		assertEquals("testSitePage", response.getSite().getPage());
		assertEquals("testId", response.getSite().getPublisher().getId());
		assertEquals("testName", response.getSite().getPublisher().getName());
		assertEquals(15.2, response.getSite().getDemographics().getFemalePercent());
		assertEquals(84.8, response.getSite().getDemographics().getMalePercent());
	}

	@Test
	public void testResponseWithNoUser() {
		AdResponse response = getAdResponse("testId", "testName", 45.2341, "US", "192.234.214.23", "testSiteId",
				"testSitePage", null);
		assertEquals("192.234.214.23", response.getDevice().getIp());
		assertEquals("US", response.getDevice().getGeo().getCountry());
		assertNull(response.getUser());
		assertEquals("testSiteId", response.getSite().getId());
		assertEquals("testSitePage", response.getSite().getPage());
		assertEquals("testId", response.getSite().getPublisher().getId());
		assertEquals("testName", response.getSite().getPublisher().getName());
		assertEquals(45.2341, response.getSite().getDemographics().getFemalePercent());
		assertEquals(54.7659, response.getSite().getDemographics().getMalePercent());
	}

	@Test
	public void testResponseWithFullData() {
		AdResponse response = getAdResponse("testId", "testName", null, "US", "192.234.214.23", "testSiteId",
				"testSitePage", "testUserId");
		assertEquals("192.234.214.23", response.getDevice().getIp());
		assertEquals("US", response.getDevice().getGeo().getCountry());
		assertEquals("testUserId", response.getUser().getId());
		assertEquals("testSiteId", response.getSite().getId());
		assertEquals("testSitePage", response.getSite().getPage());
		assertEquals("testId", response.getSite().getPublisher().getId());
		assertEquals("testName", response.getSite().getPublisher().getName());
		assertNull(response.getSite().getDemographics());
	}

	private AdResponse getAdResponse(String publisherId, String publisherPage, Double femalePct, String countryIso,
			String ip, String siteId, String sitePage, String userId) {
		Publisher publisher = new Publisher();
		PublisherData publisherData = new PublisherData(publisherId, publisherPage);
		publisher.setPublisher(publisherData);
		Demographics demographics = null;
		DemographicsData demographicsData = null;
		if (femalePct != null) {
			demographics = new Demographics();
			demographicsData = new DemographicsData();
			demographicsData.setPctFemale(femalePct);
			demographics.setDemographics(demographicsData);
		}
		Country country = new Country(Arrays.asList(new Locale(countryIso).getCountry()), 99, 1, countryIso, null);
		AdRequest request = new AdRequest();
		Device device = new Device();
		device.setIp(ip);
		request.setDevice(device);
		Site site = new Site();
		site.setId(siteId);
		site.setPage(sitePage);
		request.setSite(site);
		User user = null;
		if (userId != null) {
			user = new User();
			user.setId(userId);
		}
		request.setUser(user);
		return transformer.transformSiteResponses(publisher, demographics, country, request);
	}

}
