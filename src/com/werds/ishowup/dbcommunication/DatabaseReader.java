package com.werds.ishowup.dbcommunication;

import java.util.Map;

public class DatabaseReader extends DatabaseComm {
		
	public DatabaseReader(String phpScriptURL) {
		this.phpScriptURL = new String(phpScriptURL);
	}
	
	/**
	 * The execute method for the reader. 
	 * 
	 * @param params
	 * @return the String returned by the server
	 */
	public String performRead(Map<String, String> params) {
		return performComm(params);
	}
	
}
