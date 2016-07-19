package in.vasista.vsales.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.vasista.vsales.supplier.Supplier;

public class SupplierDataSource {
	public static final String module = SupplierDataSource.class.getName();

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_SUPPLIER_ID,
	      MySQLiteHelper.COLUMN_SUPPLIER_GROUPNAME,
	      MySQLiteHelper.COLUMN_SUPPLIER_ROLETYPEID,
	      MySQLiteHelper.COLUMN_SUPPLIER_PARTYTYPEID};

	  public SupplierDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context); 
	  } 

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void insertSupplier(Supplier supplier) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_SUPPLIER_ID, supplier.getId());
		    values.put(MySQLiteHelper.COLUMN_SUPPLIER_GROUPNAME, supplier.getName());
		    values.put(MySQLiteHelper.COLUMN_SUPPLIER_ROLETYPEID, supplier.getRoletypeid());
		    values.put(MySQLiteHelper.COLUMN_SUPPLIER_PARTYTYPEID, supplier.getPartytypeid());
		    
		    database.insert(MySQLiteHelper.TABLE_SUPPLIER, null, values);
	  }

	  public void insertSuppliers(List<Supplier> suppliers) {
		  database.beginTransaction();
		  try{
			  for (int i = 0; i < suppliers.size(); ++i) {
				  Supplier supplier = suppliers.get(i);
				    ContentValues values = new ContentValues();
				  values.put(MySQLiteHelper.COLUMN_SUPPLIER_ID, supplier.getId());
				  values.put(MySQLiteHelper.COLUMN_SUPPLIER_GROUPNAME, supplier.getName());
				  values.put(MySQLiteHelper.COLUMN_SUPPLIER_ROLETYPEID, supplier.getRoletypeid());
				  values.put(MySQLiteHelper.COLUMN_SUPPLIER_PARTYTYPEID, supplier.getPartytypeid());

				    database.insert(MySQLiteHelper.TABLE_SUPPLIER, null, values);
			  }
			  database.setTransactionSuccessful();
	  	  } catch(Exception e){
	  			Log.d(module, "supplier insert into db failed: " + e);
	  	  } finally{
	  		 database.endTransaction();
	  	  }
	  }	
	  
	  public void deleteAllSuppliers() {
		  database.delete(MySQLiteHelper.TABLE_SUPPLIER, null, null);
	  }

	  public Supplier getSupplierDetails(String supplierId) {
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
		        allColumns, MySQLiteHelper.COLUMN_SUPPLIER_ID + " = '" + supplierId + "'", null, null, null, null);

		    cursor.moveToFirst();
		    Supplier supplier = cursorToSupplier(cursor);
		    // Make sure to close the cursor
		    cursor.close();
		    return supplier;
	  }
	  
	  public List<Supplier> getAllSuppliers() {
		List<Supplier> suppliers = new ArrayList<Supplier>();
	    String orderBy =  MySQLiteHelper.COLUMN_SUPPLIER_ID + " ASC";

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPPLIER,
	        allColumns, null, null, null, null, orderBy);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
			Supplier supplier = cursorToSupplier(cursor);
			suppliers.add(supplier);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return suppliers;
	  }

	  private Supplier cursorToSupplier(Cursor cursor) {
	    return new Supplier(cursor.getString(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3));
	  }
}
