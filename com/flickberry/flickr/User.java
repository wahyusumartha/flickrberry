package com.flickberry.flickr;

/*
 * This is using to retrieve data from method flickr.people.getInfo 
 */
public interface User {

	/*
	 * Retrieve User Id From User Information
	 */
	public String getNsId();

	/*
	 * Retrieve Username From User Information
	 */
	public String getUsername();

	/*
	 * Retrieve Realname From User Information
	 */
	public String getRealName();

	/*
	 * Retrieve Location From User Information
	 */
	public String getLocation();

	/*
	 * Retrieve Photos Url From User Information
	 */
	public String getPhotosUrl();

	/*
	 * Retrieve Profile Url From User Information
	 */
	public String getProfileUrl();

	/*
	 * Retrieve Mobile Url From User Information
	 */
	public String getMobileUrl();

}
