package com.ad.app.test.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.ad.app.exceptions.InvalidCountryException;
import com.ad.app.exceptions.PublisherNotFoundException;
import com.ad.app.requests.ad.AdRequest;
import com.ad.app.requests.ad.Device;
import com.ad.app.requests.ad.Site;
import com.ad.app.requests.ad.User;
import com.ad.app.responses.AdResponse;
import com.ad.app.responses.demographics.Demographics;
import com.ad.app.responses.demographics.DemographicsData;
import com.ad.app.responses.publisher.Publisher;
import com.ad.app.responses.publisher.PublisherData;
import com.ad.app.services.GeoService;
import com.ad.app.services.SiteService;
import com.ad.app.services.impl.AdServiceImpl;
import com.maxmind.geoip2.record.Country;

import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class AdServiceTest extends TestCase {

	@Mock
	private SiteService siteService;
	@Mock
	private GeoService geoService;
	@Mock
	private RestTemplate rest;
	@InjectMocks
	private AdServiceImpl adService;
	private final String validUSIp = "142.213.28.173";
	private final String validEUIp = "142.213.28.141";
	private final String invalidIp = "145.256.24.221";
	private final String userId = "userId";
	private final String siteId = "siteId";
	private final String notExistingSiteId = "noSiteId";
	private final String sitePage = "sitePage";
	private final String demographicsErrorSiteId = "errorSiteId";
	private final String demographicEmptySiteId = "emptySiteId";

	@Before
	public void setUp() throws UnknownHostException {
		ReflectionTestUtils.setField(adService, "allowedCountry", "US");
		Mockito.when(geoService.getCountry(InetAddress.getByName(validEUIp)))
				.thenReturn(new Country(Arrays.asList("en_US"), 99, 2, "UK", null));
		Mockito.when(geoService.getCountry(InetAddress.getByName(validUSIp)))
				.thenReturn(new Country(Arrays.asList("en_US"), 99, 1, "US", null));
		Publisher publisher = new Publisher();
		publisher.setPublisher(new PublisherData());
		publisher.getPublisher().setId(siteId);
		publisher.getPublisher().setName("somename");
		Mockito.when(siteService.getPublisher(siteId)).thenReturn(publisher);
		Demographics demographics = new Demographics();
		demographics.setDemographics(new DemographicsData());
		demographics.getDemographics().setPctFemale(50.422);
		Mockito.when(siteService.getPublisher(notExistingSiteId)).thenReturn(null);
		Mockito.when(siteService.getPublisher(demographicsErrorSiteId)).thenReturn(publisher);
		Mockito.when(siteService.getPublisher(demographicEmptySiteId)).thenReturn(publisher);
		Mockito.when(siteService.getDemographics(siteId)).thenReturn(demographics);
		Mockito.when(siteService.getDemographics(demographicsErrorSiteId))
				.thenThrow(new RuntimeException("Some error"));
		Mockito.when(siteService.getDemographics(demographicEmptySiteId)).thenReturn(null);
	}

	@Test
	public void testIpInvalid() {
		assertThatThrownBy(() -> adService.enrichRequest(getAdRequest(invalidIp, userId, siteId, sitePage)))
				.isInstanceOf(InvalidCountryException.class).hasCauseInstanceOf(UnknownHostException.class);
	}

	@Test
	public void testCountryNotUSA() throws UnknownHostException {
		assertThatThrownBy(() -> adService.enrichRequest(getAdRequest(validEUIp, userId, siteId, sitePage)))
				.isInstanceOf(InvalidCountryException.class).hasNoCause();
	}

	@Test
	public void testPublisherDoesNotExist() {
		assertThatThrownBy(() -> adService.enrichRequest(getAdRequest(validUSIp, userId, notExistingSiteId, sitePage)))
				.isInstanceOf(PublisherNotFoundException.class).hasNoCause()
				.hasMessage(String.format("Publisher %s does not exist", notExistingSiteId));
	}

	@Test
	public void testDemographicsError() {
		checkSuccessResponse(
				adService.enrichRequest(getAdRequest(validUSIp, userId, demographicsErrorSiteId, sitePage)),
				demographicsErrorSiteId, true);
	}

	@Test
	public void testDemographicsEmpty() {
		checkSuccessResponse(adService.enrichRequest(getAdRequest(validUSIp, userId, demographicEmptySiteId, sitePage)),
				demographicEmptySiteId, true);
	}

	@Test
	public void testSuccess() {
		checkSuccessResponse(adService.enrichRequest(getAdRequest(validUSIp, userId, siteId, sitePage)),
				demographicEmptySiteId, false);
	}

	private void checkSuccessResponse(AdResponse response, String responseSiteId, boolean demographicsNull) {
		assertNotNull(response);
		assertNotNull(response.getDevice());
		assertNotNull(response.getDevice().getGeo());
		assertEquals(validUSIp, response.getDevice().getIp());
		assertEquals("US", response.getDevice().getGeo().getCountry());
		assertNotNull(response.getUser());
		assertNotNull(response.getSite());
		assertNotNull(response.getSite().getPublisher());
		if (demographicsNull) {
			assertNull(response.getSite().getDemographics());
		} else {
			assertNotNull(response.getSite().getDemographics());
			assertEquals(50.422, response.getSite().getDemographics().getFemalePercent());
			assertEquals(49.578, response.getSite().getDemographics().getMalePercent());
		}
	}

	private AdRequest getAdRequest(String ip, String userId, String siteId, String sitePage) {
		AdRequest request = new AdRequest();
		Device device = new Device();
		device.setIp(ip);
		request.setDevice(device);
		Site site = new Site();
		site.setId(siteId);
		site.setPage(sitePage);
		request.setSite(site);
		if (userId != null) {
			User user = new User();
			user.setId(userId);
			request.setUser(user);
		}
		return request;
	}
}
