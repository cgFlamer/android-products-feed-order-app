package com.gc.productorders;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class OrdersGraphActivity extends ActionBarActivity {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders_graph);
		SQLiteDatabase db = new SqlLite(this).getReadableDatabase();
		Cursor res = db.rawQuery("SELECT"
				+ " o.id as order_id,"
				+ " (SELECT COUNT(*) FROM order_items WHERE order_id = o.id) AS product_count"
				+ " FROM orders o"
				+ " WHERE product_count > 0"
				+ " LIMIT 10",
			null);
		res.moveToFirst();
		ViewGroup parent = (ViewGroup) findViewById(R.id.order_graph_container);
		Integer margin = 0;
		while(res.isAfterLast() == false) {
			Log.d("order_id", String.valueOf(res.getInt(res.getColumnIndex("order_id"))));
			Log.d("product_count", String.valueOf(res.getInt(res.getColumnIndex("product_count"))));
			View view = LayoutInflater.from(this).inflate(R.layout.bar_red, null);
			view.setTranslationX(margin);
			view.setTranslationY(100);
			parent.addView(view,
				res.getInt(res.getColumnIndex("product_count")) * 150,
				res.getInt(res.getColumnIndex("order_id")) * 110
			);
			margin += 50;
			res.moveToNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}
