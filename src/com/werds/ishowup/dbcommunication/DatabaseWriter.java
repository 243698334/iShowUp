package com.werds.ishowup.dbcommunication;

import java.util.Map;

public class DatabaseWriter extends DatabaseComm {
	
	public DatabaseWriter(String phpScriptURL) {
		this.phpScriptURL = new String(phpScriptURL);
	}
	
	/**
	 * The execute method for the writer. 
	 * 
	 * @param params
	 * @return true if successfully done. 
	 */
	public boolean performWrite(Map<String, String> params) {
		String result = performComm(params);
		return result == "Success";
	}
	
}
