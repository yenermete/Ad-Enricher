package com.ad.app.test.it;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ad.app.AdApplication;
import com.ad.app.test.common.TestConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdControllerIntegrationTest {

	@LocalServerPort
	private int port;
	@Autowired
	TestRestTemplate restTemplate;
	private HttpHeaders headers;
	private String adEnrichPostUrl;

	@Before
	public void setUp() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		adEnrichPostUrl = String.format(TestConstants.RESOURCE_URL, port, "/ads/enrich/v1/");
	}

	@Test
	public void testEnrichAdSuccess() throws JSONException, IOException {
		testData("site_full_request.json", "site_success_response.json", HttpStatus.OK);
	}

	@Test
	public void testEnrichAdMinRequestSuccess() throws JSONException, IOException {
		testData("site_minimal_request.json", "site_minimal_success_response.json", HttpStatus.OK);
	}

	@Test
	public void testEnrichAdInvalidRequest() throws JSONException, IOException {
		testData("site_invalid_request.json", "site_error_response.json", HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testEnrichAdInvalidRequestWithWrongIp() throws JSONException, IOException {
		testData("site_invalid_request_wrong_ip.json", null, HttpStatus.FORBIDDEN);
	}

	private void testData(String resource, String expectedResource, HttpStatus expectedCode)
			throws IOException, JSONException {
		String payload = getResource(resource);
		HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
		ResponseEntity<String> response = restTemplate.exchange(adEnrichPostUrl, HttpMethod.POST, entity, String.class);
		assertEquals(expectedCode, response.getStatusCode());
		if (expectedCode == HttpStatus.OK) {
			String expected = getResource(expectedResource);
			JSONAssert.assertEquals(expected, response.getBody(), false);
		}
	}

	private String getResource(String fileName) throws IOException {
		return new String(Files.readAllBytes(new ClassPathResource(fileName).getFile().toPath()));
	}
}
