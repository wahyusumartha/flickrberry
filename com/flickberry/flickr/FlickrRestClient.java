package com.flickberry.flickr;

import java.util.Enumeration;
import java.util.Hashtable;

import com.flickberry.json.JSONObject;
import com.flickberry.json.JSONTokener;
import com.flickberry.signature.MD5Signature;
import com.flickberry.util.network.HttpClient;
import com.flickberry.util.network.HttpConnectionFactory;

public class FlickrRestClient {

	/*
	 * Url Request to Flickr REST SERVER
	 */
	private String url = null;

	/*
	 * Your API Key Application
	 */
	private String apiKey = null;

	/*
	 * Your API Secret Application
	 */
	private String apiSecret = null;

	/*
	 * Your Authentication Token
	 */
	private String authToken = null;

	private HttpConnectionFactory factory;

	private HttpClient httpClient;
	
	private FlickrSetting setting;

	/*
	 * Default Constructor
	 */
	public FlickrRestClient(HttpConnectionFactory hFactory) {
		factory = hFactory;
		httpClient = new HttpClient(factory);
	}

	/*
	 * Create FlickrRestClient with url
	 * 
	 * @mUrl is a url Address to the Rest Server
	 */
	public FlickrRestClient(FlickrSetting setting, HttpConnectionFactory hFactory) {
		apiKey = setting.apiKey;
		apiSecret = setting.apiSecret;
		url = setting.restUrl;
		factory = hFactory;
		httpClient = new HttpClient(factory);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	/*
	 * GET Data to Rest Server URL
	 */
	public JSONObject getData(Hashtable parameters)
			throws FlickrRestClientException {
		try {
			if (authToken != null) {
				parameters.put("auth_token", authToken);
			}
			parameters.put("nojsoncallback", "1");
			parameters.put("api_key", apiKey);
			parameters.put("format", "json");
			parameters.put("api_sig", signature(parameters, apiSecret));

			StringBuffer response = httpClient.doGet(url, parameters);
			if (response.length() == 0) {
				throw new FlickrRestClientException("No Respond");
			}
			return new JSONObject(new JSONTokener(response.toString()));

		} catch (FlickrRestClientException e) {
			throw e;
		} catch (Exception e) {
			throw new FlickrRestClientException(e.getMessage());
		}
	}

	/*
	 * Create MD5 Signature for api_sig parameter
	 */
	public String signature(Hashtable parameters, String secret) {
		MD5Signature md5 = new MD5Signature();
		String key = "";
		String param = "";

		Enumeration enumeration = Util.sort(parameters.keys());
		while (enumeration.hasMoreElements()) {
			key = (String) enumeration.nextElement();
			key += parameters.get(key);

			param += key;

			// debug
			System.out.println(param);
		}

		md5.setMessage(secret + param);
		return md5.getSignature();
	}

}
