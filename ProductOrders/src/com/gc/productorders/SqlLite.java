package com.gc.productorders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLite extends SQLiteOpenHelper {

	public static String database_name = "product_shopper.db";
	
	/**
	 * Create sqlite connection.
	 * @param context
	 */
	public SqlLite(Context context) {
		super(context, database_name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE products ( "
				+ "id integer primary key,"
				+ "name text not null,"
				+ "sku text not null unique,"
				+ "price FLOAT(10,4) not null"
				+ ");");
		
		db.execSQL("CREATE TABLE orders ("
				+ "id integer primary key,"
				+ "date datetime not null"
				+ ")");
		
		db.execSQL("CREATE TABLE order_items ("
				+ "id integer primary key,"
				+ "order_id integer NOT NULL ,"
				+ "product_id integer NOT NULL ,"
				+ "qty integer not null"
				+ ");");
		
		// add dummy data
		db.execSQL("INSERT INTO products VALUES (null, 'SAMSUNG I9195 Galaxy S4 mini LTE', 'SMTI9195BK', 899.99),"
				+ "(null, 'SAMSUNG G355 Galaxy Core 2', 'SMTG355HWH', 499.99),"
				+ "(null, 'ALLVIEW V1 VIPER', 'SMTV1VIPER16GB', 699.99);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF exists products");
		db.execSQL("DROP TABLE IF exists orders");
		db.execSQL("DROP TABLE IF exists order_items");
		
		onCreate(db);
	}

}
