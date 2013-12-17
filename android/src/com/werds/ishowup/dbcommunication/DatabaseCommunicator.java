package com.werds.ishowup.dbcommunication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class DatabaseCommunicator {

	protected String phpScriptURL;
	
	public DatabaseCommunicator(String url) {
		this.phpScriptURL = new String(url);
	}
	
	@SuppressWarnings("unchecked")
	public String execute(Map<String, String> parameters) {
		CommunicateTask communicator = new CommunicateTask();
		try {
			return communicator.execute(parameters).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private class CommunicateTask extends AsyncTask<Map<String, String>, Void, String> {

		@Override
		protected String doInBackground(Map<String, String>... params) {
			Map<String, String> parameters = params[0];
			String queryURL = assembleURL(parameters);
			Log.d("DatabaseCommunicator", "Requesting from URL: "+queryURL);
			String result = null;
			InputStream isr = null;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(queryURL);
				HttpResponse HttpResponse = httpClient.execute(httpPost);
				HttpEntity HttpEntity = HttpResponse.getEntity();
				isr = HttpEntity.getContent();
			} catch (Exception e) {
				Log.e("DatabaseCommunicator", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
				StringBuilder resultBuilder = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					resultBuilder.append(line + "\n");
				}
				isr.close();
				result = new String(resultBuilder.toString()).trim();
			} catch (Exception e) {
				Log.e("DatabaseCommunicator", "Error converting result " + e.toString());
			}
			Log.d("DatabaseCommunicator", "Result: " + result);
			return result;
		}
		
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
