package com.gc.productorders;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpRequest {

	/**
	 * Get XML document from provided URL.
	 * @param url
	 * @return
	 */
	public static String getContentFromUrl(String url) {
		String content = null;
		
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = client.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity);
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch(ClientProtocolException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	
	
}
