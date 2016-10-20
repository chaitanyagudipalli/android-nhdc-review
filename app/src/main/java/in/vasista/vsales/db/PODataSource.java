package in.vasista.vsales.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentItemNHDC;
import in.vasista.vsales.supplier.SupplierPO;
import in.vasista.vsales.supplier.SupplierPOItem;

public class PODataSource {
	public static final String module = PODataSource.class.getName();
	private Context context;
	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_SUPP_POID,
	      MySQLiteHelper.COLUMN_SUPP_ORDERDATE,
	      MySQLiteHelper.COLUMN_SUPP_ORDERID,
	      MySQLiteHelper.COLUMN_SUPP_ORDERNUM,
	      MySQLiteHelper.COLUMN_SUPP_STATUS,
	  };

	  private String[] allItemColumns = {
			  MySQLiteHelper.COLUMN_SUPP_POID,
		      MySQLiteHelper.COLUMN_SUPP_POPID,
		      MySQLiteHelper.COLUMN_SUPP_PO_ITEMNAME,
		      MySQLiteHelper.COLUMN_SUPP_SPEC,
		      MySQLiteHelper.COLUMN_SUPP_UNITPRICE,
			  MySQLiteHelper.COLUMN_SUPP_ITEMQ,
			  MySQLiteHelper.COLUMN_SUPP_DISPATCHQ,
			  MySQLiteHelper.COLUMN_SUPP_BALANCEQ,
	  };

	private String[] allShipColumns = { MySQLiteHelper.COLUMN_SHIP_ID,
			MySQLiteHelper.COLUMN_SUPP_POID,
			MySQLiteHelper.COLUMN_SUPP_POPID,
			MySQLiteHelper.COLUMN_SUPP_PO_ITEMNAME,
			MySQLiteHelper.COLUMN_SHIP_ITEMSEQID,
			MySQLiteHelper.COLUMN_SUPP_POPID,
			MySQLiteHelper.COLUMN_SHIP_QTY,
			MySQLiteHelper.COLUMN_SHIP_UNITPRICE,
			MySQLiteHelper.COLUMN_SHIP_ITEMAMT,
	};

	  public PODataSource(Context context) {
		  this.context = context;		  
	    dbHelper = new MySQLiteHelper(context); 
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public void deleteAllPOs() {
		  database.delete(MySQLiteHelper.TABLE_SUPP_PO, null, null);
	  }
		public void deleteAllPOItems() {
			//database.delete(MySQLiteHelper.TABLE_SUPP_PO_ITEMS, null, null);
		}
	public void deleteIndent(String po_id){
		database.delete(MySQLiteHelper.TABLE_SUPP_PO, MySQLiteHelper.COLUMN_INDENT_ID+"="+po_id, null);
		database.delete(MySQLiteHelper.TABLE_SUPP_PO_ITEMS, MySQLiteHelper.COLUMN_INDENT_ID+"="+po_id, null);

	}
	  

	public void insertSuppPO(List<SupplierPO> supplierPOs){
		ContentValues values = new ContentValues();
		for (int i=0; i < supplierPOs.size(); ++i) {
			SupplierPO supplierPO = supplierPOs.get(i);


			values.put(MySQLiteHelper.COLUMN_SUPP_POID, supplierPO.getPoid());
			values.put(MySQLiteHelper.COLUMN_SUPP_ORDERDATE, supplierPO.getOrderdate());
			values.put(MySQLiteHelper.COLUMN_SUPP_ORDERID, supplierPO.getOrderId());
			values.put(MySQLiteHelper.COLUMN_SUPP_ORDERNUM, supplierPO.getOrderNum());
			values.put(MySQLiteHelper.COLUMN_SUPP_STATUS, supplierPO.getStatus());

			database.insert(MySQLiteHelper.TABLE_SUPP_PO, null, values);
 		}

	}
	         

	  public List<SupplierPO> getAllSuppPOs() {
	    List<SupplierPO> supplierPOs = new ArrayList<>();
	    String orderBy =  MySQLiteHelper.COLUMN_SUPP_ORDERDATE + " DESC";
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPP_PO,
	        allColumns, null, null, null, null, orderBy);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
			SupplierPO supplierPO = cursorToIndent(cursor);
			supplierPOs.add(supplierPO);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return supplierPOs;
	  }

	  public SupplierPO getIndentDetails(String supplierPOid) {
		  try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPP_PO,
		        allColumns, MySQLiteHelper.COLUMN_SUPP_POID + " = " + supplierPOid, null, null, null, null);

		    cursor.moveToFirst();
			  SupplierPO indent = cursorToIndent(cursor);
		    // Make sure to close the cursor
		    cursor.close();
		    return indent;

		  }catch (Exception e){
			  return null;
		  }
	  }
	  
	  private SupplierPO cursorToIndent(Cursor cursor) {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			  return new SupplierPO(cursor.getString(0),
                      cursor.getString(1),
                      cursor.getString(2),
                      cursor.getString(3),cursor.getString(4));
	  }
	  
	  public void updateIndentStatus(String supplierPOid, String indentStatus) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_SUPP_STATUS, indentStatus);
		    final String[] whereArgs = { supplierPOid};
		    database.update(MySQLiteHelper.TABLE_SUPP_PO, values, MySQLiteHelper.COLUMN_SUPP_POID + " = ?", whereArgs);
	  }


	  
	  public long insertSuppPOItem(SupplierPOItem supplierPOItem) {


		  ContentValues values = new ContentValues();
		  values.put(MySQLiteHelper.COLUMN_SUPP_POID, supplierPOItem.getPoid());
		  values.put(MySQLiteHelper.COLUMN_SUPP_POPID, supplierPOItem.getProdid());
		  values.put(MySQLiteHelper.COLUMN_SUPP_PO_ITEMNAME, supplierPOItem.getItemname());
		  values.put(MySQLiteHelper.COLUMN_SUPP_SPEC, supplierPOItem.getSpec());
		  values.put(MySQLiteHelper.COLUMN_SUPP_UNITPRICE, supplierPOItem.getUnitPrice());
		  values.put(MySQLiteHelper.COLUMN_SUPP_ITEMQ, supplierPOItem.getItemQty());
		  values.put(MySQLiteHelper.COLUMN_SUPP_DISPATCHQ, supplierPOItem.getDispatchQty());
		  values.put(MySQLiteHelper.COLUMN_SUPP_BALANCEQ, supplierPOItem.getBalanceQty());

		  return database.insert(MySQLiteHelper.TABLE_SUPP_PO_ITEMS, null, values);
	  }
	  
	  public List<SupplierPOItem> getSuppPOItems(String supplierPOid) {
		  List<SupplierPOItem> indentItems = new ArrayList<>();
		  Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPP_PO_ITEMS,
				  allItemColumns, MySQLiteHelper.COLUMN_SUPP_POID + " = \"" + supplierPOid+"\"", null, null, null, null);
		  ProductsDataSource datasource = new ProductsDataSource(context);
		  datasource.open();
		  Map productMap = datasource.getSaleProductMap();
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  SupplierPOItem indentItem = cursorToSuppPOItem(cursor);
			  indentItems.add(indentItem);
		      cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  datasource.close();
		  return indentItems;
	  }

	public SupplierPOItem getIndentItemDetails(int indentItemId) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SUPP_PO_ITEMS,
				allItemColumns, MySQLiteHelper.COLUMN_SUPP_POITEM_ID + " = " + indentItemId, null, null, null, null);

		cursor.moveToFirst();
		SupplierPOItem indentItem = cursorToSuppPOItem(cursor);
		// Make sure to close the cursor
		cursor.close();
		return indentItem;
	}

	  private SupplierPOItem cursorToSuppPOItem(Cursor cursor) {
		  return new SupplierPOItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),
				  cursor.getString(3),cursor.getFloat(4),cursor.getFloat(5),
				  cursor.getFloat(6),cursor.getFloat(7));
	  }

}
