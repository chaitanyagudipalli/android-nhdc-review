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

import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.payment.Payment;

public class PaymentsDataSource {
	public static final String module = IndentsDataSource.class.getName();	
	//private Context context;
	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_PAYMENT_ID,
	      MySQLiteHelper.COLUMN_PAYMENT_DATE,
	      MySQLiteHelper.COLUMN_PAYMENT_METHOD,MySQLiteHelper.COLUMN_PAYMENT_STATUS,
			  MySQLiteHelper.COLUMN_PAYMENT_FROM,
			  MySQLiteHelper.COLUMN_PAYMENT_TO,
	      MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_PAID,
	  		MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_BALANCE,

};
	  
	  public PaymentsDataSource(Context context) {
		  //this.context = context;
	    dbHelper = new MySQLiteHelper(context); 
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }	  
	  
	  public long insertPayment(String id, Date date, String type, String status, String from, String to, double amount_paid, double amount_balance) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_PAYMENT_ID, id);
		    values.put(MySQLiteHelper.COLUMN_PAYMENT_DATE, date.getTime());
		    values.put(MySQLiteHelper.COLUMN_PAYMENT_METHOD, type);
		  values.put(MySQLiteHelper.COLUMN_PAYMENT_STATUS, status);
		  values.put(MySQLiteHelper.COLUMN_PAYMENT_FROM, from);
		  values.put(MySQLiteHelper.COLUMN_PAYMENT_TO, to);
		    values.put(MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_PAID, amount_paid);
		  values.put(MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_BALANCE, amount_balance);
		    return database.insert(MySQLiteHelper.TABLE_PAYMENT, null, values);
	  }

	public void insertPayments(List<Payment> payments) {
		database.beginTransaction();
		try{
			for (int i = 0; i < payments.size(); ++i) {
				Payment payment = payments.get(i);
				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.COLUMN_PAYMENT_ID, payment.getId());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_DATE, payment.getDate().getTime());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_METHOD, payment.getType());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_PAID, payment.getAmount_paid());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_AMOUNT_BALANCE, payment.getAmount_balance());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_FROM, payment.getFrom());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_TO, payment.getTo());
				values.put(MySQLiteHelper.COLUMN_PAYMENT_STATUS, payment.getStatus());

				database.insert(MySQLiteHelper.TABLE_PAYMENT, null, values);
			}
			database.setTransactionSuccessful();
		} catch(Exception e){
			Log.d(module, "products insert into db failed: " + e);
		} finally{
			database.endTransaction();
		}
	}
	  
	  public List<Payment> getAllPayments() {
		    List<Payment> payments = new ArrayList<Payment>();
		    String orderBy =  MySQLiteHelper.COLUMN_PAYMENT_DATE + " DESC";
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_PAYMENT,
		        allColumns, null, null, null, null, orderBy);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Payment payment = cursorToPayment(cursor);
		      payments.add(payment);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return payments;
	  }
	  
	  private Payment cursorToPayment(Cursor cursor) {
		    return new Payment(cursor.getString(0),
					new Date(cursor.getLong(1)),
					cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),
					cursor.getDouble(6),cursor.getDouble(7));
	  }	  
	  
	  public void deleteAllPayments() {
		  database.delete(MySQLiteHelper.TABLE_PAYMENT, null, null);
	  }
}
