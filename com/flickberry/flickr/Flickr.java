package com.flickberry.flickr;

public interface Flickr {

	public String getToken(String miniToken) throws FlickrException;

	public String getToken() throws FlickrException;

	public boolean isAuth();

}
