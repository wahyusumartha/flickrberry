package com.flickberry.flickr;

public class FlickrException extends Exception {

	/*
	 * Default Constructor for FlickrException Class
	 */
	public FlickrException() {
		super();
	}

	/*
	 * Generic Excetion for Flickr Exception given message
	 * 
	 * @param message is the exception message
	 */
	public FlickrException(String message) {
		super(message);
	}

}
