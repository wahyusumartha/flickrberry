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
		try {
			return jsonObject.getJSONObject("person").getJSONObject("location")
					.getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getMobileUrl() {
		try {
			return jsonObject.getJSONObject("person")
					.getJSONObject("mobileurl").getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getNsId() {
		try {
			return jsonObject.getJSONObject("person").getString("nsid");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getPhotosUrl() {
		try {
			return jsonObject.getJSONObject("person")
					.getJSONObject("photosurl").getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getProfileUrl() {
		try {
			return jsonObject.getJSONObject("person").getJSONObject(
					"profileurl").getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getRealName() {
		try {
			return jsonObject.getJSONObject("person").getJSONObject("realname")
					.getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String getUsername() {
		try {
			return jsonObject.getJSONObject("person").getJSONObject("username")
					.getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

}
