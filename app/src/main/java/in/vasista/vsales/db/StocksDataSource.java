package in.vasista.vsales.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vasista.vsales.payment.Payment;
import in.vasista.vsales.stocks.Stock;

public class StocksDataSource {
	public static final String module = IndentsDataSource.class.getName();
	//private Context context;
	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_STOCK_ID,
	      MySQLiteHelper.COLUMN_STOCK_PRODID,
	      MySQLiteHelper.COLUMN_STOCK_PRODNAME,MySQLiteHelper.COLUMN_STOCK_DEPOT,
			  MySQLiteHelper.COLUMN_STOCK_SUPPLIER,
			  MySQLiteHelper.COLUMN_STOCK_SPEC,
	      MySQLiteHelper.COLUMN_STOCK_QTY,
	  		MySQLiteHelper.COLUMN_STOCK_PRICE,

};

	  public StocksDataSource(Context context) {
		  //this.context = context;
	    dbHelper = new MySQLiteHelper(context); 
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }	  
	  


	public void insertStocks(List<Stock> stocks) {
		database.beginTransaction();
		try{
			for (int i = 0; i < stocks.size(); ++i) {
				Stock stock = stocks.get(i);
				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.COLUMN_STOCK_PRODID, stock.getProdid());
				values.put(MySQLiteHelper.COLUMN_STOCK_PRODNAME, stock.getProdname());
				values.put(MySQLiteHelper.COLUMN_STOCK_DEPOT, stock.getDepot());
				values.put(MySQLiteHelper.COLUMN_STOCK_SUPPLIER, stock.getSupp());
				values.put(MySQLiteHelper.COLUMN_STOCK_SPEC, stock.getSpec());
				values.put(MySQLiteHelper.COLUMN_STOCK_QTY, stock.getQty());
				values.put(MySQLiteHelper.COLUMN_STOCK_PRICE, stock.getPrice());

				database.insert(MySQLiteHelper.TABLE_PAYMENT, null, values);
			}
			database.setTransactionSuccessful();
		} catch(Exception e){
			Log.d(module, "products insert into db failed: " + e);
		} finally{
			database.endTransaction();
		}
	}
	  
	  public List<Stock> getAllStocks() {
		    List<Stock> stocks = new ArrayList<Stock>();
		    String orderBy =  MySQLiteHelper.COLUMN_STOCK_PRODNAME + " DESC";
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPP_STOCKS,
		        allColumns, null, null, null, null, orderBy);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
				Stock payment = cursorToStock(cursor);
				stocks.add(payment);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return stocks;
	  }
	  
	  private Stock cursorToStock(Cursor cursor) {
		    return new Stock(cursor.getString(0),
					cursor.getString(1),
					cursor.getString(2),cursor.getString(3),cursor.getString(4),
					cursor.getDouble(6),cursor.getDouble(7));
	  }	  
	  
	  public void deleteAllStocks() {
		  database.delete(MySQLiteHelper.TABLE_SUPP_STOCKS, null, null);
	  }
}
