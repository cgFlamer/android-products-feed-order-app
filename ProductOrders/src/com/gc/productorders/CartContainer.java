package com.gc.productorders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CartContainer implements Serializable {
	/**
	 * Used when serializing object.
	 */
	private static final long serialVersionUID = -9007415108260168726L;
	
	/**
	 * Products list.
	 */
	private ArrayList<HashMap<String, String>> products = null;
	
	/**
	 * Static keys
	 */
	public static final String KEY_NAME = "name";
	
	public static final String KEY_SKU = "sku";
	
	public static final String KEY_PRICE = "price";
	
	public static final String KEY_QTY = "qty";	
	
	/**
	 * Init cart.
	 */
	public CartContainer() {
		products = new ArrayList<HashMap<String, String>>();
	}
	
	/**
	 * Add product.
	 * @param product
	 */
	public void addProduct(HashMap<String, String> product) {
		/**
		 * If product exists, update qty.
		 */
		HashMap<String, String> inCartProduct = this.getProductBySku(product.get(KEY_SKU));
		if(inCartProduct != null) {
			inCartProduct.put(KEY_QTY, product.get(KEY_QTY));
		} else {
			products.add(product);
		}		
	}
	
	/**
	 * Search for product by sku code.
	 * @param sku
	 * @return
	 */
	public HashMap<String, String> getProductBySku(String sku) {
		HashMap<String, String> result = null;
		for(HashMap<String, String> product : products) {
			if(product.get(KEY_SKU).equals(sku)) {
				result = product;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Get product.
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getProducts() {
		return products;
	}
	
	/**
	 * Remove product by object.
	 * @param product
	 */
	public void removeProduct(HashMap<String, String> product) {
		products.remove(product);
	}
	
	/**
	 * Remove product by idx.
	 * @param index
	 */
	public void removeProduct(Integer index) {
		products.remove(index);
	}
	
	/**
	 * Get products total.
	 * @return
	 */
	public String getProductsTotal() {
		Float value = (float) 0.0;
		for(HashMap<String, String> map : products) {
			Float productValue = Float.parseFloat(map.get(KEY_PRICE));
			Integer productQty = Integer.parseInt(map.get(KEY_QTY));
			value += productValue * productQty;
		}
		return value.toString();
	}
	
	/**
	 * Create product.
	 * @param name
	 * @param sku
	 * @param price
	 * @param qty
	 * @return
	 */
	public HashMap<String, String> createProduct(String name, String sku, Float price, Integer qty) {
		HashMap<String, String> product = new HashMap<String, String>();
		
		product.put(KEY_PRICE, price.toString());
		product.put(KEY_SKU, sku);
		product.put(KEY_NAME, name);
		product.put(KEY_QTY, qty.toString());
		
		return product;
		
	}
}
