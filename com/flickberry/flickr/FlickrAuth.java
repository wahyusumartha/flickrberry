package com.flickberry.flickr;

import com.flickberry.json.JSONObject;

public interface FlickrAuth {
	public String getToken();

	public String getPerms();
	
	public String userId();

}
