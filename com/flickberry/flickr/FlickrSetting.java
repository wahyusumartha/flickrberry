package com.flickberry.flickr;

public class FlickrSetting {

	public String restUrl = "http://api.flickr.com/services/rest/";
	public String apiKey = "d8a5aa4315e957e3a660bf927b0f1bef";
	public String apiSecret = "b6c6312f6f8cb2a6";

	public String authUrl = "http://www.flickr.com/auth-72157625251141460";

	public FlickrSetting(String restUrl, String apiKey, String apiSecret) {
		this.restUrl = restUrl;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public FlickrSetting() {
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

}
