package com.werds.ishowup.dbcommunication;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

/**
 * @author KevinC
 * 
 *         This is the documentation of my DatabaseReader/Writer classes. The
 *         code below is just an example. Do not call the methods.
 * 
 */

public abstract class DocsForDBClasses {

	/* Example for DatabaseReader */
	private void ReaderExample() {

		/* Constructor */
		/**
		 * Constructor
		 * 
		 * @param String The URL of the PHP script.
		 * 
		 *        The instance of the created DatabaseReader stores the URL
		 *        of the script as a String.
		 */
		DatabaseReader readerExample = new DatabaseReader(
				"http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/example.php");

		/* Prepare the parameters for the next step */
		/**
		 * The parameters are two ArrayList<String> which contains the "keys"
		 * and the "values" to be queried. Their indices have to be mapped.
		 * 
		 * e.g. For Key-Value pairs below: KEY | VALUE "crn" | "12345" "date" |
		 * "11132013"
		 */
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("crn");
		keys.add("date");
		ArrayList<String> values = new ArrayList<String>();
		values.add("12345");
		values.add("11132013");

		/* Use of the parameters */
		/**
		 * fetchDataByString()
		 * 
		 * @param  ArrayList<String>, ArrayList<String>
		 * @return The String retrieved from the database.
		 * 
		 *         This is the execute method of the DatabaseReader class. Call
		 *         it when the parameters are ready.
		 */
		String fetchedData = readerExample.fetchDataByString(keys, values);

		/* Customize the AsyncTask class */
		/**
		 * Notice that all the network operations should be performed on a
		 * separate thread from the UI. So the DatabaseReader/Writer classes
		 * always work with AsyncTask. (See:
		 * http://developer.android.com/reference/android/os/AsyncTask.html)
		 */
		/* public */class FetchDataTask extends
				AsyncTask<ArrayList<String>, Void, String> {

			@Override
			protected String doInBackground(ArrayList<String>... arrLists) {
				ArrayList<String> keys = arrLists[0];
				ArrayList<String> values = arrLists[1];

				DatabaseReader readerExample = new DatabaseReader(
						"http://web.engr.illinois.edu/~ishowup4cs411/cgi-bin/example.php");
				return readerExample.fetchDataByString(keys, values);
			}
		}

		/* Use AsyncTask to fetch data */
		FetchDataTask mFetch = new FetchDataTask();
		try {
			String myData = new String(mFetch.execute(keys, values).get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Notice that in this case, the URL is not a parameter passed into the AsyncTask constructor.
	}

	/* Example for DatabaseWriter */
	private void writerExample() {
		// to be finished...
	}
}
