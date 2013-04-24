package com.waffles.justinthyme;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class ApiRequest {

	private static final String base_url = "http://boyshouse.asuscomm.com:8080/api/";
	
	private static boolean threadRunning = false;
	private static List<ApiRequest> queue = new ArrayList<ApiRequest>();

	
	private final String tag = getClass().getSimpleName();
	private final Object lock = new Object();
	
	private String requestUrl;
	private String params;
	
	public ApiRequest(String requestUrl, String params) {
		this.requestUrl = requestUrl;
		this.params = params;
	}
	
	public ApiRequest(String requestUrl) {
		this.requestUrl = requestUrl;
		this.params = "";
	}

	private String getRequestString() {
		return ApiRequest.base_url + this.requestUrl + this.params;
	}

	public void sendThreaded(final JITResponse listener) {
		// get the request string before we thread in case other calls modify it
		final String requestString = getRequestString();


		if (!threadRunning) {
			Log.d(tag, requestString);
			threadRunning = true;
			new Thread() {
				public void run() {
					try {
						JSONObject response = send(requestString);
						listener.onResponse(response);
					} catch (Exception e) {
						listener.onException(e.getMessage(), e);
					} finally {
						threadRunning = false;
						synchronized (lock) {
							if (queue.size() != 0){
								Log.i("ApiRequest", "Sending queued request. Queue size: " + queue.size());
								ApiRequest item = queue.get(0);
								item.sendThreaded(listener);
								queue.remove(0);
							}
						}
					}
				}
			}.start();
			
		} else {
			synchronized (lock) {
				queue.add(this);
			}
		}
	}

	private JSONObject send(String URL) throws JITException {
		try {
			// create an HTTP request to a protected resource
			HttpRequestBase httpRequest = new HttpGet(URL);
			HttpClient httpClient = new DefaultHttpClient();

			HttpResponse response = httpClient.execute(httpRequest);
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();

			if (status < 200 || status > 299) {
				throw new JITException("Error communicating with server. "
						+ statusLine.getReasonPhrase() + " " + status);
			} else {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				return new JSONObject(result);
			}
		} catch (Exception e) {
			throw new JITException("Error handling results from server!\n" + e.getMessage(), e);
		}

	}

}
