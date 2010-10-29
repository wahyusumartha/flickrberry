package com.flickberry.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;

public class HttpClient {

	private HttpConnectionFactory factory;

	public HttpClient(HttpConnectionFactory factory) {
		this.factory = factory;
	}

	public StringBuffer doGet(String url) throws Exception {
		return doGet(url, null, factory);
	}

	public StringBuffer doGet(String url, Hashtable hashtable) throws Exception {
		return doGet(url, hashtable, factory);
	}

	public StringBuffer doGet(String url, Hashtable hashtable,
			HttpConnectionFactory hFactory) throws Exception {
		StringBuffer buffer = new StringBuffer(url);

		if (hashtable != null && hashtable.size() != 0) {
			buffer.append('?');
			Enumeration keysEnum = hashtable.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				String value = (String) hashtable.get(key);
				buffer.append(key).append('=').append(value);

				if (keysEnum.hasMoreElements()) {
					buffer.append('&');
				}
			}
			// Debug
			// System.out.println("Url Destination : " + buffer.toString());
		}
		return doGet(buffer.toString(), hFactory);
	}

	/*
	 * GET Operation Method
	 * 
	 * @param url : GET Url
	 * 
	 * @return : Response
	 * 
	 * @throws Exception when any error
	 */
	public StringBuffer doGet(String url, HttpConnectionFactory hFactory)
			throws Exception {
		HttpConnection connection = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if (url == null || url.equals("") || hFactory == null) {
				return null;
			}

			connection = hFactory.getHttpConnection(url);

			switch (connection.getResponseCode()) {
			case HttpConnection.HTTP_OK: {
				InputStream inputStream = connection.openInputStream();
				int c;
				while ((c = inputStream.read()) != -1) {
					buffer.append((char) c);
				}
				inputStream.close();
				break;
			}

			case HttpConnection.HTTP_TEMP_REDIRECT:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_MOVED_PERM: {
				url = connection.getHeaderField("Location");
				buffer = doGet(url, hFactory);
				break;
			}

			default:
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;

	}

	public StringBuffer doPost(String url, Hashtable hashtable)
			throws IOException {
		return doPost(url, hashtable, factory);
	}

	public StringBuffer doPost(String url, Hashtable hashtable,
			HttpConnectionFactory hFactory) throws IOException {
		URLEncodedPostData encoder = new URLEncodedPostData("UTF-8", false);

		if (hashtable != null && hashtable.size() != 0) {
			Enumeration keysEnum = hashtable.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				String value = (String) hashtable.get(key);
				encoder.append(key, value);
			}
		}
		return doPost(url, encoder.getBytes(), hFactory);

	}

	/*
	 * POST Operation Method
	 * 
	 * @param url : POST Url
	 * 
	 * @return : Response
	 * 
	 * @throws : Exception when any errors
	 */
	public StringBuffer doPost(String url, byte[] data,
			HttpConnectionFactory hFactory) throws IOException {
		HttpConnection connection = null;
		StringBuffer buffer = new StringBuffer();

		try {
			if (url == null || url.equals("") || hFactory == null) {
				return null;
			}

			connection = hFactory.getHttpConnection(url, data);

			switch (connection.getResponseCode()) {
			case HttpConnection.HTTP_OK: {
				InputStream inputStream = connection.openInputStream();
				int c;
				while ((c = inputStream.read()) != -1) {
					buffer.append((char) c);
				}
				inputStream.close();
				break;
			}
			case HttpConnection.HTTP_TEMP_REDIRECT:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_MOVED_PERM: {
				url = connection.getHeaderField("Location");
				buffer = doPost(url, data, hFactory);
				break;
			}
			default:
				break;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}
}
