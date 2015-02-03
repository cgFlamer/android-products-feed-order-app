package com.gc.productorders;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsActivity extends ActionBarActivity {

	private CartContainer cart = null;
	private Integer response = RESULT_CANCELED;
	private SqlLite databaseConnection = null;
	private SQLiteDatabase db = null;
	private ArrayList<HashMap<String, String>> products = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		
		ListView listViewProducts = (ListView) findViewById(R.id.productsListView);
		listViewProducts.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            	String sku = ((TextView) view.findViewById(R.id.productSkuTxt)).getText().toString();
	            	TextView skuText = (TextView) findViewById(R.id.productTxt);
	            	skuText.setText(sku);
	            }
			}
        );
		
		cart = (CartContainer) getIntent().getSerializableExtra("cart");
		setDatabaseProperties();
		getProductsFromDatabase();
		populateListView();
	}
	
	private void setDatabaseProperties() {
		databaseConnection = new SqlLite(this);
		db = databaseConnection.getReadableDatabase();		
	}
	
	private void getProductsFromDatabase() {
		Cursor res = db.rawQuery("SELECT * FROM products ORDER BY id DESC", null);
		res.moveToFirst();
		
		while(res.isAfterLast() == false) {
			HashMap<String, String> product = new HashMap<String, String>();
			Log.d("product_name", res.getString(res.getColumnIndex(CartContainer.KEY_NAME)));
			product.put(CartContainer.KEY_NAME, res.getString(res.getColumnIndex(CartContainer.KEY_NAME)));
			product.put(CartContainer.KEY_SKU, res.getString(res.getColumnIndex(CartContainer.KEY_SKU)));
			product.put(CartContainer.KEY_PRICE, res.getString(res.getColumnIndex(CartContainer.KEY_PRICE)));
			products.add(product);
			res.moveToNext();
		}
		
	}
	
	private void populateListView() {
		ListView productsListView = (ListView) findViewById(R.id.productsListView);
		ProductListAdapter adapter = new ProductListAdapter(this, products);
		productsListView.setVisibility(1);
		productsListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	public void onAddToCartBtnClick(View v) {
		String sku = ((TextView) findViewById(R.id.productTxt)).getText().toString();
		String qty = ((Spinner) findViewById(R.id.qtySelector)).getSelectedItem().toString();
		
		if(sku.isEmpty() || qty.isEmpty()) {
			Toast.makeText(this, getResources().getString(R.string.product_add_error), Toast.LENGTH_SHORT).show();
		} else {
			Log.d("chosen_sku", sku);
			for(HashMap<String, String> product : products) {
				Log.d("product_sku", product.get(CartContainer.KEY_SKU).toString());
				if(product.get(CartContainer.KEY_SKU).toString().equals(sku)) {
					product.put(CartContainer.KEY_QTY, String.valueOf(qty));
					cart.addProduct(product);
					response = MainActivity.RESULT_OK;
					break;
				}
			}
			Intent returnIntent = new Intent();
			returnIntent.putExtra("cart", cart);
			Log.d("cart_size", String.valueOf(cart.getProducts().size()));
			setResult(response, returnIntent);
			finish();
		}
	}
}
