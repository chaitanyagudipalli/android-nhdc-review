package in.vasista.vsales.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.vasista.vsales.transporter.Transporter;


public class TransporterDataSource {
	public static final String module = TransporterDataSource.class.getName();

	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_TRANSPORTER_ID,
	      MySQLiteHelper.COLUMN_TRANSPORTER_NAME,
	      MySQLiteHelper.COLUMN_TRANSPORTER_PHONE,
	      MySQLiteHelper.COLUMN_TRANSPORTER_ADDRESS};

	  public TransporterDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context); 
	  } 

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public void insertTransporter(Transporter transporter) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_TRANSPORTER_ID, transporter.getId());
		    values.put(MySQLiteHelper.COLUMN_TRANSPORTER_NAME, transporter.getName());
		    values.put(MySQLiteHelper.COLUMN_TRANSPORTER_PHONE, transporter.getPhone());
		    values.put(MySQLiteHelper.COLUMN_TRANSPORTER_ADDRESS, transporter.getAddress());
		    
		    database.insert(MySQLiteHelper.TABLE_TRANSPORTER, null, values);
	  }

	  public void insertTransporters(List<Transporter> transporters) {
		  database.beginTransaction();
		  try{
			  for (int i = 0; i < transporters.size(); ++i) {
				  Transporter transporter = transporters.get(i);
				    ContentValues values = new ContentValues();
				  values.put(MySQLiteHelper.COLUMN_TRANSPORTER_ID, transporter.getId());
				  values.put(MySQLiteHelper.COLUMN_TRANSPORTER_NAME, transporter.getName());
				  values.put(MySQLiteHelper.COLUMN_TRANSPORTER_PHONE, transporter.getPhone());
				  values.put(MySQLiteHelper.COLUMN_TRANSPORTER_ADDRESS, transporter.getAddress());

				    database.insert(MySQLiteHelper.TABLE_TRANSPORTER, null, values);
			  }
			  database.setTransactionSuccessful();
	  	  } catch(Exception e){
	  			Log.d(module, "transporter insert into db failed: " + e);
	  	  } finally{
	  		 database.endTransaction();
	  	  }
	  }	
	  
	  public void deleteAllTransporters() {
		  database.delete(MySQLiteHelper.TABLE_TRANSPORTER, null, null);
	  }

	  public Transporter getTransporterDetails(String transporterId) {
		  Log.v("trans",""+transporterId);
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSPORTER,
		        allColumns, MySQLiteHelper.COLUMN_TRANSPORTER_ID + " = '" + transporterId + "'", null, null, null, null);

		    cursor.moveToFirst();
		    Transporter transporter = cursorToTransporter(cursor);
		    // Make sure to close the cursor
		    cursor.close();
		    return transporter;
	  }
	  
	  public List<Transporter> getAllTransporters() {
		List<Transporter> transporters = new ArrayList<Transporter>();
	    String orderBy =  MySQLiteHelper.COLUMN_TRANSPORTER_ID + " ASC";

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSPORTER,
	        allColumns, null, null, null, null, orderBy);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
			Transporter transporter = cursorToTransporter(cursor);
			transporters.add(transporter);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return transporters;
	  }

	public int getCountTransporters(){
		return (int) DatabaseUtils.queryNumEntries(database, MySQLiteHelper.TABLE_TRANSPORTER);
	}

	  private Transporter cursorToTransporter(Cursor cursor) {
	    return new Transporter(cursor.getString(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3));
	  }
}
