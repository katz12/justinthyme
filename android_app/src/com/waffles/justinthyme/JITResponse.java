package com.waffles.justinthyme;

import org.json.JSONObject;

public interface JITResponse {

	void onResponse(JSONObject response);

	void onException(String message, Exception e);

}
