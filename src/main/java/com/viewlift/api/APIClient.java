package com.viewlift.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIClient {

	private static final String BASE_URL = "https://apisnagfilms-dev.viewlift.com";

	private static final Logger LOGGER = Logger.getLogger(APIClient.class);

	private void testCreateSite(String apikey) throws JsonProcessingException {
		HashMap<String, String> map = new HashMap<>();
		map.put("name", "Viewlift");
		ObjectMapper mapper = new ObjectMapper();
		String jsonRequest = mapper.writeValueAsString(map);
		Map<String, String> headerParams = new HashMap<>();
		headerParams.put("x-api-key", apikey);
		headerParams.put("Content-Type", "application/json");
		String response = processHttpPost(BASE_URL + "/content/site", jsonRequest, headerParams);
		// Logging at highest level :)
		LOGGER.error(response);
	}

	public static String processHttpPost(String url, String bodyString, Map<String, String> headerParams) {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpclient = null;
		HttpPost httpPost = null;
		String responseString = "";

		StringEntity input = null;
		try {

			httpPost = new HttpPost(url);
			if (headerParams != null) {
				for (String key : headerParams.keySet()) {
					httpPost.addHeader(key, headerParams.get(key));
				}
			}

			if (bodyString != null) {
				input = new StringEntity(bodyString);
				httpPost.setEntity(input);
			}
			httpclient = getApacheHttpConnection();
			response = httpclient.execute(httpPost);
			if (response.getEntity() != null && response.getEntity().getContent() != null) {
				responseString = IOUtils.toString(response.getEntity().getContent());
			}

		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}

		return responseString;
	}

	public static CloseableHttpClient getApacheHttpConnection() {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		int timeOut = 3 * 1000; // 3 seconds
		RequestConfig config = null;
		config = RequestConfig.custom().setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut)
				.setSocketTimeout(timeOut).build();

		CloseableHttpClient httpclient = clientBuilder.setDefaultRequestConfig(config).build();

		return httpclient;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Please pass the api key as the argument.");
		}
		String apikey = args[0];
		new APIClient().testCreateSite(apikey);
	}

}
