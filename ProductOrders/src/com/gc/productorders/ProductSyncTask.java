package com.gc.productorders;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class ProductSyncTask extends AsyncTask<Context, String, String> {

	private Context context = null;
	
	ProgressDialog dialog = null;
	
	SQLiteDatabase dbWrite = null;
	
	public ProductSyncTask(Context paramContext) {
		context = paramContext;
		dbWrite = new SqlLite(context).getWritableDatabase();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		// Create dialog
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getResources().getString(R.string.refresh_waiting));
		dialog.setIndeterminate(false);
		dialog.setMax(100);
		dialog.setCancelable(false);
		dialog.show();
	}
	
	@Override
	protected String doInBackground(Context... params) {
		String feedUrl = context.getResources().getString(R.string.product_feed_url);
		Log.d("feed_url", feedUrl);
		ProductParser parser = new ProductParser(feedUrl);
		ArrayList<HashMap<String, String>> products;
		try {
			products = parser.getProducts();
			if(products != null && products.size() > 0) {
				for(HashMap<String, String> product : products) {
					writeProduct(product);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Write product in database.
	 * @param product
	 */
	private void writeProduct(HashMap<String, String> product) {
		ContentValues content = new ContentValues();
		content.put("name", product.get("name"));
		content.put("price", product.get("price"));
		content.put("sku", product.get("sku"));
		try {
			dbWrite.insert("products", null, content);
		} catch(Exception ex) {
			
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		dbWrite.close();
		dialog.dismiss();
	}

}
