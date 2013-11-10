package com.werds.ishowup.dbcommunication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class DatabaseReader {
	
	private String phpScriptURL;
	
	public DatabaseReader(String phpScriptURL) {
		this.phpScriptURL = new String(phpScriptURL);
	}
	
	/**
	 * @param Two ArrayList<String> for parameters and values
	 * @return the result of the execution of php script as a String 
	 */
	public String fetchDataByString(ArrayList<String> parameters, ArrayList<String> values) {
		if (parameters.size() != values.size()) {
			return null;
		}
		
		// Parse the parameters and the values
		StringBuilder phpCommandBuilder = new StringBuilder();
		phpCommandBuilder.append("?");
		Iterator<String> p = parameters.iterator();
		Iterator<String> v = values.iterator();
		while (p.hasNext() && v.hasNext()) {
			phpCommandBuilder.append(p.next() + "=" + v.next() + "&");
		}
		String phpCommand = phpCommandBuilder.toString();
		
		// Result ArrayList to return
		String result = null;
		
		// HTTP connection process
		InputStream isr = null; 
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(phpScriptURL + phpCommand);
			HttpResponse HttpResponse = httpClient.execute(httpPost);
			HttpEntity HttpEntity = HttpResponse.getEntity();
			isr = HttpEntity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection "+e.toString());
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
			Log.e("log_tag", "Error  converting result "+e.toString());
		}
		Log.d("Return result", result);
		return result;
	}
	
	/**
	 * @param Two ArrayList<String> for parameters and values
	 * @return the result of the execution of php script as an ArrayList, one line per entry 
	 */
	public ArrayList<String> fetchDataByArrayList(ArrayList<String> parameters, ArrayList<String> values) {
		if (parameters.size() != values.size()) {
			return null;
		}
		
		// Parse the parameters and the values
		StringBuilder phpCommandBuilder = new StringBuilder();
		phpCommandBuilder.append("?");
		Iterator<String> p = parameters.iterator();
		Iterator<String> v = values.iterator();
		while (p.hasNext() && v.hasNext()) {
			phpCommandBuilder.append(p.next() + "=" + v.next() + "&");
		}
		String phpCommand = phpCommandBuilder.toString();
		
		// Result ArrayList to return
		ArrayList<String> result = new ArrayList<String>();
		
		// HTTP connection process
		InputStream isr = null; 
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(phpScriptURL + phpCommand);
			HttpResponse HttpResponse = httpClient.execute(httpPost);
			HttpEntity HttpEntity = HttpResponse.getEntity();
			isr = HttpEntity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
			isr.close();
		} catch (Exception e) {
			Log.e("log_tag", "Error  converting result "+e.toString());
		}
		return null;
	}
}
