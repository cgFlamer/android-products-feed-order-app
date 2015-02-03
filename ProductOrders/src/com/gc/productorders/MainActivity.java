package com.gc.productorders;

import java.util.HashMap;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	CartContainer cart = null;
	
	public static Integer ADD_PRODUCT_ACTIVITY = 1;
	
	/**
	 * Initialize cart container.
	 */
	public MainActivity() {
		super();
		cart = new CartContainer();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshStatus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Refresh status:
     * - change status text
     * - activate / deactivate place order button
     */
    private void refreshStatus() {
    	TextView statusText = (TextView) findViewById(R.id.cartStatusText);
    	Button placeOrderBtn = (Button) findViewById(R.id.placeOrderBtn);
    	if(cart.getProducts().size() == 0) {
    		statusText.setText(getResources().getString(R.string.main_cart_no_products));
    		placeOrderBtn.setEnabled(false);
    	} else {
    		statusText.setText(
				String.format(getResources().getString(R.string.main_cart_products), cart.getProducts().size(), cart.getProductsTotal())
			);
    		placeOrderBtn.setEnabled(true);
    	}
    }
    
    public void onRefreshBtnClick(View v) {
    	ProductSyncTask task = new ProductSyncTask(this);
    	task.execute();
    }
    
    public void onAddProductsBtnClick(View v) {
    	Intent intent = new Intent(this, ProductsActivity.class);
    	intent.putExtra("cart", cart);
    	startActivityForResult(intent, ADD_PRODUCT_ACTIVITY);
    }
    
    /**
     * Save current cart and convert it into an order.
     * @param v
     */
    public void onPlaceOrderBtnClick(View v) {
    	SQLiteDatabase db = new SqlLite(this).getWritableDatabase();    	
    	ContentValues orderContent = new ContentValues();
    	orderContent.put("date", "datetime()");
    	orderContent.put("increment_id", generateOrderIncrementId());
    	Integer orderId = (int) db.insert("orders", null, orderContent);
    	if(orderId > 0) {
    		for(HashMap<String, String> product : cart.getProducts()) {
    			String sku = product.get("sku");
    			ContentValues productContent = new ContentValues();
    			productContent.put("order_id", orderId.toString());
    			productContent.put("product_id", getProductIdBySku(sku));
    			productContent.put("qty", product.get("qty"));
    			db.insert("order_items", null, productContent);
    			cart.removeProduct(product);
    		}
    	}
    	db.close();
    	Toast.makeText(this, getResources().getString(R.string.main_order_placed), Toast.LENGTH_LONG);
    	refreshStatus();
	}
    
    /**
     * Get product id by sku.
     * @param sku
     * @return
     */
    private String getProductIdBySku(String sku) {
    	SQLiteDatabase db = new SqlLite(this).getReadableDatabase();
    	Cursor res = db.rawQuery("SELECT id FROM products WHERE sku = '" + sku + "'", null);
    	res.moveToFirst();
    	String id = res.getString(res.getColumnIndex("id"));
    	db.close();
    	return id;
    }
    
    /**
     * Generate order increment id.
     * @return
     */
    private String generateOrderIncrementId() {
    	return String.format("GC00" + Math.random());
    }
    
    public void onShowOrdersGraphClick(MenuItem m) {
    	Intent intent = new Intent(this, OrdersGraphActivity.class);
    	startActivity(intent);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == ADD_PRODUCT_ACTIVITY) {
    		if(resultCode == RESULT_OK) {
        		Toast.makeText(this, getResources().getString(R.string.main_product_added), Toast.LENGTH_SHORT).show();
        		/**
        		 * Get cart from 2ndary activity
        		 */
        		CartContainer cartContainer = (CartContainer) data.getSerializableExtra("cart");
        		cart = cartContainer;
        	}
    	}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	refreshStatus();
    }
}
