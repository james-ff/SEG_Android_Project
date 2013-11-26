package com.worldly.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

/**
 * This class is used to obtain data from the internet.
 * @author Jeroen Keppens
 *
 */
class JSONCore {
	/**
	 * Static method to download a data feed from a given URL.
	 * @param url : A String object containing the URL of the data feed to be downloaded
	 * @return A String object containing the data feed.
	 */
	static String readData(String url) {
	    // Create download objects
	    HttpClient client = new DefaultHttpClient();
	    HttpGet get = new HttpGet(url);
	    StringBuilder content = new StringBuilder();
	    
	    try {
	        // Execute response and create input stream
	        HttpResponse response = client.execute(get);
	        int responseCode = response.getStatusLine().getStatusCode();
	        if (responseCode == 200) {
	            InputStream in = response.getEntity().getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            
	            // Build string from input stream
	            String readLine = reader.readLine();
	            while (readLine != null) {
	                content.append(readLine);
	                readLine = reader.readLine();
	            }
	        } else {
	            Log.w("DATA RETRIEVAL","Unable to read data.  HTTP response code = " + responseCode);
	            content = null;
	        }
	    } catch (ClientProtocolException e) {
	        Log.e("readData","ClientProtocolException:\n" + e.getMessage());
	    } catch (IOException e) {
	        Log.e("readData","IOException:\n" + e.getMessage());
	    }
		
	    // Returns data as String object or null if unable to retrieve it
	    return (content==null) ? null : content.toString();
	}
}