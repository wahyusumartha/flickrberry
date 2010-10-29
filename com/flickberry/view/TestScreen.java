package com.flickberry.view;

import java.util.Hashtable;

import com.flickberry.flickr.FlickrAuthImpl;
import com.flickberry.flickr.FlickrContext;
import com.flickberry.flickr.FlickrRestClient;
import com.flickberry.flickr.FlickrRestClientException;
import com.flickberry.flickr.FlickrSetting;
import com.flickberry.json.JSONException;
import com.flickberry.util.network.HttpConnectionFactory;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class TestScreen extends MainScreen implements FieldChangeListener {
	private LabelField label;
	private HttpConnectionFactory hFactory;
	private ButtonField buttonField;

	public String restUrl = "http://api.flickr.com/services/rest/";
	public String apiKey = "d8a5aa4315e957e3a660bf927b0f1bef";
	public String apiSecret = "b6c6312f6f8cb2a6";

	private FlickrContext flc;

	public TestScreen() {
		init();
		label = new LabelField("TEST", LabelField.USE_ALL_WIDTH);
		buttonField = new ButtonField("GET My Info", ButtonField.CONSUME_CLICK);
		buttonField.setChangeListener(this);
		// FlickrSignature signature = new FlickrSignature(
		// "d8a5aa4315e957e3a660bf927b0f1bef", "b6c6312f6f8cb2a6");
		// signature.addParameter("api_key",
		// "d8a5aa4315e957e3a660bf927b0f1bef");
		// signature.addParameter("method", "flickr.people.getInfo");
		// signature.addParameter("user_id", "40694677@N02");
		// // signature.addParameter("auth_token",
		// // "72157625251516778-e2b26ad696cb6e4e");
		// signature.addParameter("format", "json");
		// String url = signature.convertToUrl(signature.getSignature());
		// try {
		// label.setText(getToken(url));
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// }
		// try {
		// label.setText(getToken());
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (FlickrRestClientException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		add(label);
		add(buttonField);
	}

	private String getToken() throws JSONException, FlickrRestClientException {
		Hashtable hashtable = new Hashtable();
		hashtable.put("auth_token", "72157625266625062-0eb27b1694f2d8c5");
		hashtable.put("method", "flickr.auth.checkToken");
		FlickrAuthImpl flickrAuthImpl = new FlickrAuthImpl(flc, hashtable);
		return flickrAuthImpl.userId();
		// hFactory = new HttpConnectionFactory();
		// FlickrRestClient restClient = new FlickrRestClient(hFactory);
		// FlickrSetting setting = new FlickrSetting();
		// restClient.setUrl(setting.restUrl);
		// restClient.setApiSecret(setting.apiSecret);
		// restClient.setApiKey(setting.apiKey);
		// Hashtable hashtable = new Hashtable();
		// hashtable.put("auth_token", "72157625266625062-0eb27b1694f2d8c5");
		// hashtable.put("method", "flickr.auth.checkToken");
		// // System.out.println(restClient.getData(hashtable).toString());
		// return restClient.getData(hashtable).getJSONObject("auth")
		// .getJSONObject("user").getString("username");
	}

	private void getMyInfo() {

	}

	private void init() {
		hFactory = new HttpConnectionFactory();
		FlickrSetting setting = new FlickrSetting(restUrl, apiKey, apiSecret);
		flc = new FlickrContext(setting, hFactory);
	}

	public void fieldChanged(Field field, int context) {
		if (field == buttonField) {
			try {
				label.setText(getToken());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Dialog.alert(e.getMessage());
			} catch (FlickrRestClientException e) {
				// TODO Auto-generated catch block
				Dialog.alert(e.getMessage());
			}
		}

	}

}
