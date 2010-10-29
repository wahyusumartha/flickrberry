package com.flickberry.flickr;

import java.util.Hashtable;

import com.flickberry.json.JSONException;
import com.flickberry.json.JSONObject;
import com.flickberry.log.Logger;

public class UserImpl implements User {

	private JSONObject jsonObject = null;
	private Logger log = Logger.getLogger(getClass());
	private FlickrContext flc;

	public UserImpl(FlickrContext flc, Hashtable parameters)
			throws FlickrRestClientException {
		this.flc = flc;
		jsonObject = flc.getFlickrRestClient().getData(parameters);
	}

	public String getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMobileUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNsId() {
		try {
			return jsonObject.getString("nsid");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getPhotosUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProfileUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRealName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

}
