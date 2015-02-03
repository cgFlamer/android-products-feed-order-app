package com.gc.productorders;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class ProductParser {
	
	public static final String KEY_FEED_PRODUCT = "item";
	
	String content = null;
	
	ProductParser(String url) {
		content = HttpRequest.getContentFromUrl(url);
	}
	
	/**
	 * Get products from feed content.
	 * @return
	 * @throws JSONException 
	 */
	public ArrayList<HashMap<String, String>> getProducts() throws JSONException {
		Log.d("content", content);
		ArrayList<HashMap<String, String>> products = new ArrayList<HashMap<String, String>>();
		JSONObject object = getJsonObject();
		JSONArray data = null;
		try {
			data = object.getJSONArray("products");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		for(int i = 0; i < data.length(); i++) {
			HashMap<String, String> product = new HashMap<String, String>();
			JSONObject productData = data.getJSONObject(i);
			String title = "";
			String sku = "";
			String price = "";
			try {
				title = productData.getString("name");
				sku = productData.getString("sku");
				price = productData.getString("price");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(title != null && title.length() > 0) {
				product.put(CartContainer.KEY_NAME, title);
				product.put(CartContainer.KEY_SKU, sku);
				product.put(CartContainer.KEY_PRICE, price);			
				products.add(product);
			}			
		}
		
		return products;
	}
	
	/**
	 * Get JSON object.
	 * @return
	 */
	private JSONObject getJsonObject() {
		JSONObject object = null;
		try {
			object = new JSONObject(content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
