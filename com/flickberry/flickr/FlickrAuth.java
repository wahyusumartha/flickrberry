package com.flickberry.flickr;

/*
 * This is using to handle method flickr.auth.getFullToken 
 */
public interface FlickrAuth {
	/*
	 * Get Authentication Token a.k.a auth_token
	 */
	public String getToken();

	/*
	 * Get Permission When using your application a.k.a perms
	 */
	public String getPerms();

	/*
	 * Get Your User Id
	 */
	public String userId();

}
