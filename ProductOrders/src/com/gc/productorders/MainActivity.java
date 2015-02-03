package com.gc.productorders;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
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
    
    public void onPlaceOrderBtnClick(View v) {
    	
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
