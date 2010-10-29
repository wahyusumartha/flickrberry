package com.flickberry.flickr;

import com.flickberry.util.network.HttpClient;
import com.flickberry.util.network.HttpConnectionFactory;

public class FlickrContext implements Flickr {

	private FlickrSetting flickrSetting = null;
	private FlickrRestClient flickrRestClient = null;
	private HttpConnectionFactory factory = null;
	private HttpClient httpClient = null;

	public FlickrContext(FlickrSetting setting, HttpConnectionFactory hFactory) {
		this.flickrSetting = setting;
		this.factory = hFactory;
		this.httpClient = new HttpClient(factory);
		flickrRestClient = new FlickrRestClient(setting, factory);
	}

	public String getToken(String miniToken) throws FlickrException {
		return null;

	}

	public boolean isAuth() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getToken() throws FlickrException {
		// TODO Auto-generated method stub
		return null;
	}

	public FlickrSetting getFlickrSetting() {
		return flickrSetting;
	}

	public void setFlickrSetting(FlickrSetting flickrSetting) {
		this.flickrSetting = flickrSetting;
	}

	public FlickrRestClient getFlickrRestClient() {
		return flickrRestClient;
	}

	public void setFlickrRestClient(FlickrRestClient flickrRestClient) {
		this.flickrRestClient = flickrRestClient;
	}

	public HttpConnectionFactory getFactory() {
		return factory;
	}

	public void setFactory(HttpConnectionFactory factory) {
		this.factory = factory;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
