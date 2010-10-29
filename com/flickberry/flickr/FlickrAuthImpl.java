package com.flickberry.flickr;

import java.util.Hashtable;

import com.flickberry.json.JSONException;
import com.flickberry.json.JSONObject;
import com.flickberry.log.Logger;

public class FlickrAuthImpl implements FlickrAuth {

	private JSONObject jsonObject = null;
	protected Logger log = Logger.getLogger(getClass());
	private FlickrContext flc = null;

	public FlickrAuthImpl(FlickrContext context, Hashtable parameters)
			throws FlickrRestClientException {
		this.flc = context;
		jsonObject = flc.getFlickrRestClient().getData(parameters);

	}

	public String getPerms() {
		try {
			return jsonObject.getJSONObject("auth").getJSONObject("perms")
					.getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;

	}

	public String getToken() {
		try {
			return jsonObject.getJSONObject("auth").getJSONObject("token")
					.getString("_content");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public String userId() {
		try {
			return jsonObject.getJSONObject("auth").getJSONObject("user")
					.getString("nsid");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return null;
	}

}
