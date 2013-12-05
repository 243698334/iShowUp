package com.werds.ishowup.dbcommunication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public abstract class DatabaseComm {

	protected String phpScriptURL;

	protected String performComm(Map<String, String> params) {
		
		String url = assembleURL(params);
		
		// Result String to return
		String result = null;

		// HTTP connection process
		InputStream isr = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse HttpResponse = httpClient.execute(httpPost);
			HttpEntity HttpEntity = HttpResponse.getEntity();
			isr = HttpEntity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
			StringBuilder resultBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				resultBuilder.append(line + "\n");
			}
			isr.close();
			result = new String(resultBuilder.toString());
		} catch (Exception e) {
			Log.e("log_tag", "Error  converting result " + e.toString());
		}
		Log.d("Return result", result);
		return result;
	}

	/**
	 * Get the full accessible URL of the communication
	 * 
	 * @param params
	 * @return URL String
	 */
	protected String assembleURL(Map<String, String> params) {
		return phpScriptURL + "?" + canonicalize(params);
	}

	/**
	 * Canonicalize the parameters to fit the PHP URL style
	 * 
	 * @param sortedParamMap
	 *            Parameter name-value pairs in lexicographical order.
	 * @return Canonical form of query string.
	 */
	protected String canonicalize(Map<String, String> params) {
		if (params.isEmpty()) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();

		while (iter.hasNext()) {
			Map.Entry<String, String> kvpair = iter.next();
			buffer.append(kvpair.getKey());
			buffer.append("=");
			buffer.append(kvpair.getValue());
			if (iter.hasNext()) {
				buffer.append("&");
			}
		}
		String cannoical = buffer.toString();
		return cannoical;
	}
}
