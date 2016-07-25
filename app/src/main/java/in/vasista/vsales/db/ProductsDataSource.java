package in.vasista.vsales.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.vsales.catalog.Product;
public class ProductsDataSource {
	public static final String module = ProductsDataSource.class.getName();	  
	  // Database fields 
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_PRODUCT_ID,
	      MySQLiteHelper.COLUMN_PRODUCT_NAME,

	      MySQLiteHelper.COLUMN_PRODUCT_INRENAL_NAME,
		  MySQLiteHelper.COLUMN_PRODUCT_BRAND_NAME,
			  MySQLiteHelper.COLUMN_PRODUCT_DESC,
			  MySQLiteHelper.COLUMN_PRODUCT_TYPE_ID,
			  MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_UOM_ID,
	      MySQLiteHelper.COLUMN_PRODUCT_PRICE,
			  MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_INCLUDED,
	      MySQLiteHelper.COLUMN_PRODUCT_CATEGORY_ID,
			  MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID,



	      };
	  
	  public ProductsDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context); 
	  } 

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  static public void insertProduct(SQLiteDatabase database, Product product) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, product.getId());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, product.getName());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_DESC, product.getDescription());	
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_INRENAL_NAME, product.getInternalname());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_BRAND_NAME, product.getBrandname());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_TYPE_ID, product.getTypeid());
		    values.put(MySQLiteHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getProductCategoryId());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID, product.getProductParentCategoryId());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_UOM_ID, product.getUOMid());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_INCLUDED, product.getIncludedquantity());

		    database.insert(MySQLiteHelper.TABLE_PRODUCT, null, values);
	  }
	  
	  public void insertProduct(Product product) {    
		    ContentValues values = new ContentValues();
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, product.getId());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, product.getName());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_DESC, product.getDescription());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_INRENAL_NAME, product.getInternalname());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_BRAND_NAME, product.getBrandname());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_TYPE_ID, product.getTypeid());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getProductCategoryId());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID, product.getProductParentCategoryId());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_UOM_ID, product.getUOMid());
		  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_INCLUDED, product.getIncludedquantity());
		    database.insert(MySQLiteHelper.TABLE_PRODUCT, null, values);    
	  }
	  
	  public void insertProducts(List<Product> products) {    
		  database.beginTransaction();
		  try{
			  for (int i = 0; i < products.size(); ++i) {
				  Product product = products.get(i);
				  ContentValues values = new ContentValues();
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, product.getId());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_NAME, product.getName());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_DESC, product.getDescription());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_INRENAL_NAME, product.getInternalname());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_BRAND_NAME, product.getBrandname());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_TYPE_ID, product.getTypeid());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getProductCategoryId());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID, product.getProductParentCategoryId());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_UOM_ID, product.getUOMid());
				  values.put(MySQLiteHelper.COLUMN_PRODUCT_QUANTITY_INCLUDED, product.getIncludedquantity());
				  database.insert(MySQLiteHelper.TABLE_PRODUCT, null, values);   
			  }
			  database.setTransactionSuccessful();
	  	  } catch(Exception e){
	  			Log.d(module, "products insert into db failed: " + e);	
	  	  } finally{
	  		 database.endTransaction();
	  	  }
	  }	  
	  
	  public void deleteAllProducts() {
		  database.delete(MySQLiteHelper.TABLE_PRODUCT, null, null);
	  }

	  
	  public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();
	    String orderBy =  MySQLiteHelper.COLUMN_PRODUCT_ID + " ASC";

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCT,
	        allColumns, null, null, null, null, orderBy);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) { 
	      Product product = cursorToProduct(cursor);
	      products.add(product);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return products;
	  }

	public List<Product> getProducts(String category) {
		List<Product> products = new ArrayList<Product>();
		String orderBy =  MySQLiteHelper.COLUMN_PRODUCT_ID + " ASC";

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCT,
				allColumns, MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID + " = '" + category + "'", null, null, null, orderBy);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return products;
	}

	public List<Product> getOtherProducts() {
		List<Product> products = new ArrayList<Product>();
		String orderBy =  MySQLiteHelper.COLUMN_PRODUCT_ID + " ASC";

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PRODUCT,
				allColumns, MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID + " !='SILK' and "+MySQLiteHelper.COLUMN_PRODUCT_PARENT_CATEGORY_ID + " !='COTTON'", null, null, null, orderBy);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return products;
	}


	  
	  private Product cursorToProduct(Cursor cursor) {
		  return new Product(cursor.getString(0),
	    		cursor.getString(1),
	    		cursor.getString(2),
	    		cursor.getString(3),
	    		cursor.getString(4),
				  cursor.getString(5),
	    		cursor.getString(6),
				  (float)cursor.getDouble(7), (float)cursor.getDouble(8),cursor.getString(9),cursor.getString(10));
	  }
	   
	  public Map getSaleProductMap() {
		  List<Product> products = getAllProducts();
		  Map result = new HashMap<String, Product> ();
		  for (int i = 0; i < products.size(); ++i) {
			  Product product = products.get(i);
			  result.put(product.getId(), product);
		  }
		  return result;
	  }
}
