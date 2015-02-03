package com.gc.productorders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> data = null;
	Context context = null;
	LayoutInflater inflater = null;
	
	/**
	 * Append data in adapter.
	 * @param context
	 * @param products
	 */
	ProductListAdapter(Context context, ArrayList<HashMap<String, String>> products) {
		this.data = products;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View productView = convertView;
		if(productView == null) {
			productView = inflater.inflate(R.layout.product_row_item, null);
		}
		HashMap<String, String> product = this.getItem(position);
		
		TextView productTitleTxt = (TextView) productView.findViewById(R.id.productTitleTxt);
		productTitleTxt.setText(product.get(CartContainer.KEY_NAME));
		
		TextView productSkuTxt = (TextView) productView.findViewById(R.id.productSkuTxt);
		productSkuTxt.setText(product.get(CartContainer.KEY_SKU));
		
		TextView productPriceTxt = (TextView) productView.findViewById(R.id.productPriceTxt);
		productPriceTxt.setText(product.get(CartContainer.KEY_PRICE) + " RON");
		
		return productView;
	}
}
