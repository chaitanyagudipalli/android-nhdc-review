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
import java.util.Map;

import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentItemNHDC;

public class IndentsDataSource {
	public static final String module = IndentsDataSource.class.getName();	
	private Context context;	
	  // Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_INDENT_ID,
	      MySQLiteHelper.COLUMN_INDENT_TALLY_REFNO,
	      MySQLiteHelper.COLUMN_INDENT_PO_ORDER,
	      MySQLiteHelper.COLUMN_INDENT_PO_SEQ_NO,
	      MySQLiteHelper.COLUMN_INDENT_IS_GEN_PO,
	      MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_ID,
			  MySQLiteHelper.COLUMN_INDENT_T_PARTY_ID,
	      MySQLiteHelper.COLUMN_INDENT_STORENAME,
			  MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_NAME,
			  MySQLiteHelper.COLUMN_INDENT_ORDER_NO,
			  MySQLiteHelper.COLUMN_INDENT_ORDER_ID,
			  MySQLiteHelper.COLUMN_INDENT_ORDER_DATE,
			  MySQLiteHelper.COLUMN_INDENT_STATUS_ID,
			  MySQLiteHelper.COLUMN_INDENT_ORDER_TOTAL,
			  MySQLiteHelper.COLUMN_INDENT_PAID,
			  MySQLiteHelper.COLUMN_INDENT_BALANCE,
			  MySQLiteHelper.COLUMN_INDENT_SCHEMECAT,
			  MySQLiteHelper.COLUMN_INDENT_PRODSTORE_ID,
			  MySQLiteHelper.COLUMN_INDENT_DISC_AMNT,

	  };

	  private String[] allIndentItemColumns = { MySQLiteHelper.COLUMN_INDENT_ITEM_ID,
		      MySQLiteHelper.COLUMN_INDENT_ID,
		      MySQLiteHelper.COLUMN_PRODUCT_ID,
		      MySQLiteHelper.COLUMN_INDENT_ITEM_QTY,
		      MySQLiteHelper.COLUMN_INDENT_ITEM_REMARKS,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_BALE_QTY,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_BUNDLE_WEIGHT,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_BUNDLE_UNITPRICE,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_UOM,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_BASICPRICE,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_TAXRATELIST,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_SERVICECHARGE,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_SERVICECHARGE_AMT,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_TOTAL_AMT,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_VAT_PER,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_VAT_AMT,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_CST_PER ,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_CST_AMT ,
				MySQLiteHelper.COLUMN_INDENT_ITEM_DISC_AMT,
				MySQLiteHelper.COLUMN_INDENT_ITEM_SHIPPED_QTY,
				MySQLiteHelper.COLUMN_INDENT_ITEM_OTHER_CHARGES,
			  MySQLiteHelper.COLUMN_INDENT_ITEM_SPEC

	  };
	  
	  public IndentsDataSource(Context context) {
		  this.context = context;		  
	    dbHelper = new MySQLiteHelper(context); 
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public void deleteAllIndents() {
		  database.delete(MySQLiteHelper.TABLE_INDENT, MySQLiteHelper.COLUMN_INDENT_STATUS_ID+"!=\"Not Uploaded\"", null);
	  }
		public void deleteAllIndentItems() {
			//database.delete(MySQLiteHelper.TABLE_INDENT_ITEM, null, null);
		}
	public void deleteIndent(long indent_id){
		database.delete(MySQLiteHelper.TABLE_INDENT, MySQLiteHelper.COLUMN_INDENT_ID+"="+Long.toString(indent_id), null);
		database.delete(MySQLiteHelper.TABLE_INDENT_ITEM, MySQLiteHelper.COLUMN_INDENT_ID+"="+Long.toString(indent_id), null);

	}
	  
//	  static public void insertIndent(SQLiteDatabase database, String indentStatus, double indentTotal) {
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_INDENT_CRDATE, System.currentTimeMillis());
//		    values.put(MySQLiteHelper.COLUMN_INDENT_STATUS, indentStatus);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_IS_SYNCED, 0);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_TOTAL, indentTotal);
//		    database.insert(MySQLiteHelper.TABLE_INDENT, null, values);
//	  }

//	  public long insertIndent(String indentStatus, double indentTotal, String subscriptionType) {
//		  	Date now = new Date();
//			Date supplyDate = DateUtil.addDays(now, 1);
//			return insertIndent(indentStatus, indentTotal, subscriptionType, supplyDate);
//	  }
	  
//	  public long insertIndent(String indentStatus, double indentTotal, String subscriptionType, Date supplyDate) {
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_INDENT_CRDATE, System.currentTimeMillis());
//		    values.put(MySQLiteHelper.COLUMN_INDENT_SUPPLYDATE, supplyDate.getTime());
//		    values.put(MySQLiteHelper.COLUMN_INDENT_SUBSCRIPTIONTYPE, subscriptionType);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_STATUS, indentStatus);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_IS_SYNCED, 0);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_TOTAL, indentTotal);
//		    return database.insert(MySQLiteHelper.TABLE_INDENT, null, values);
//	  }

	public long insertIndent(Indent indent){
		ContentValues values = new ContentValues();

			values.put(MySQLiteHelper.COLUMN_INDENT_TALLY_REFNO, indent.getTallyRefNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_PO_ORDER, indent.getPOorder());
			values.put(MySQLiteHelper.COLUMN_INDENT_PO_SEQ_NO, indent.getPoSquenceNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_IS_GEN_PO, indent.isgeneratedPO());
			values.put(MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_ID, indent.getSupplierPartyId());
		values.put(MySQLiteHelper.COLUMN_INDENT_T_PARTY_ID, indent.gettId());
			values.put(MySQLiteHelper.COLUMN_INDENT_STORENAME, indent.getStoreName());
			values.put(MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_NAME, indent.getSupplierPartyName());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_NO, indent.getOrderNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_ID, indent.getOrderId());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_DATE, indent.getOrderDate().getTime());
			values.put(MySQLiteHelper.COLUMN_INDENT_STATUS_ID, indent.getStatusId());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_TOTAL, indent.getOrderTotal());
			values.put(MySQLiteHelper.COLUMN_INDENT_PAID, indent.getPaidAmt());
			values.put(MySQLiteHelper.COLUMN_INDENT_BALANCE, indent.getBalance());
		values.put(MySQLiteHelper.COLUMN_INDENT_SCHEMECAT, indent.getSchemeType());
		values.put(MySQLiteHelper.COLUMN_INDENT_PRODSTORE_ID,indent.getProdstoreid());
		values.put(MySQLiteHelper.COLUMN_INDENT_DISC_AMNT,indent.getTotDiscountAmt());

			return database.insert(MySQLiteHelper.TABLE_INDENT, null, values);


	}

	public void insertIndents(List<Indent> indents){
		ContentValues values = new ContentValues();
		for (int i=0; i < indents.size(); ++i) {
			Indent indent = indents.get(i);

			values.put(MySQLiteHelper.COLUMN_INDENT_TALLY_REFNO, indent.getTallyRefNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_PO_ORDER, indent.getPOorder());
			values.put(MySQLiteHelper.COLUMN_INDENT_PO_SEQ_NO, indent.getPoSquenceNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_IS_GEN_PO, indent.isgeneratedPO());
			values.put(MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_ID, indent.getSupplierPartyId());
			values.put(MySQLiteHelper.COLUMN_INDENT_T_PARTY_ID, indent.gettId());
			values.put(MySQLiteHelper.COLUMN_INDENT_STORENAME, indent.getStoreName());
			values.put(MySQLiteHelper.COLUMN_INDENT_SUPP_PARTY_NAME, indent.getSupplierPartyName());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_NO, indent.getOrderNo());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_ID, indent.getOrderId());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_DATE, indent.getOrderDate().getTime());
			values.put(MySQLiteHelper.COLUMN_INDENT_STATUS_ID, indent.getStatusId());
			values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_TOTAL, indent.getOrderTotal());
			values.put(MySQLiteHelper.COLUMN_INDENT_PAID, indent.getPaidAmt());
			values.put(MySQLiteHelper.COLUMN_INDENT_BALANCE, indent.getBalance());
			values.put(MySQLiteHelper.COLUMN_INDENT_SCHEMECAT, indent.getSchemeType());
			values.put(MySQLiteHelper.COLUMN_INDENT_PRODSTORE_ID,indent.getProdstoreid());
			values.put(MySQLiteHelper.COLUMN_INDENT_DISC_AMNT,indent.getTotDiscountAmt());

			database.insert(MySQLiteHelper.TABLE_INDENT, null, values);
 		}

	}
	         
	  /*
	   * Returns the indent if it exists for the supplydate and shift, else
	   * null
	   */
//	  public Indent fetchIndent(Date supplyDate, String subscriptionType) {
//		  Indent indent = null;
//		  Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteHelper.TABLE_INDENT
//				  + " WHERE "
//				  + MySQLiteHelper.COLUMN_INDENT_SUBSCRIPTIONTYPE + "= '" + subscriptionType + "'"
//				  + " AND date(datetime("
//				  + MySQLiteHelper.COLUMN_INDENT_SUPPLYDATE + " / 1000 , 'unixepoch','localtime')) = date(datetime("
//				  + supplyDate.getTime()/1000 + ", 'unixepoch','localtime'))", null);
//		  if ( cursor.moveToFirst()) {
//			  indent = cursorToIndent(cursor);
//		  }
//		  cursor.close();
//		  Log.d( module, "supplyDate=" + supplyDate);
//		  Log.d( module, "subscriptionType=" + subscriptionType);
//		  Log.d( module, "indent=" + indent);
//		  return indent;
//	  }
	  
	  public List<Indent> getAllIndents() {
	    List<Indent> indents = new ArrayList<Indent>();
	    String orderBy =  MySQLiteHelper.COLUMN_INDENT_ORDER_DATE + " DESC";
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_INDENT,
	        allColumns, null, null, null, null, orderBy);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Indent indent = cursorToIndent(cursor);
	      indents.add(indent);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return indents;
	  }

	  public Indent getIndentDetails(int indentId) {
		  try {

			Cursor cursor = database.query(MySQLiteHelper.TABLE_INDENT,
		        allColumns, MySQLiteHelper.COLUMN_INDENT_ID + " = " + indentId, null, null, null, null);

		    cursor.moveToFirst();
		    Indent indent = cursorToIndent(cursor);
		    // Make sure to close the cursor
		    cursor.close();
		    return indent;

		  }catch (Exception e){
			  return null;
		  }
	  }
	  
	  private Indent cursorToIndent(Cursor cursor) {
	    return new Indent(cursor.getInt(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3),
				(cursor.getInt(4) == 1),
				cursor.getString(5),
				cursor.getString(6),
				cursor.getString(7),
				cursor.getString(8),
				cursor.getString(9),
				cursor.getString(10),
				new Date(cursor.getLong(11)),
				cursor.getString(12),
				cursor.getDouble(13),
				cursor.getDouble(14),
				cursor.getDouble(15),
				cursor.getString(16),
				cursor.getString(17),
				cursor.getFloat(18));
	  }
	  
	  public void updateIndentStatus(long indentId, String indentStatus) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_INDENT_STATUS_ID, indentStatus);
		    final String[] whereArgs = { Long.toString(indentId)};
		    database.update(MySQLiteHelper.TABLE_INDENT, values, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
	  }

	public void updateTotalAmount(int indentId, double amnt) {
		Indent i =getIndentDetails(indentId);
		if (i.getOrderTotal() > 0 ){
			amnt = amnt + i.getOrderTotal();
		}
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_INDENT_ORDER_TOTAL, amnt);
		values.put(MySQLiteHelper.COLUMN_INDENT_BALANCE, amnt);
		final String[] whereArgs = { Integer.toString(indentId)};
		database.update(MySQLiteHelper.TABLE_INDENT, values, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
	}
	  
//	  public void updateIndentTotal(long indentId, double indentTotal) {
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_INDENT_TOTAL, indentTotal);
//		    final String[] whereArgs = { Long.toString(indentId)};
//		    database.update(MySQLiteHelper.TABLE_INDENT, values, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
//	  }
//
//	  public void setIndentSyncStatus(long indentId, boolean isSynced) {
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_INDENT_IS_SYNCED, isSynced?1:0);
//		    final String[] whereArgs = { Long.toString(indentId)};
//		    database.update(MySQLiteHelper.TABLE_INDENT, values, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
//	  }
//
//	  public void updateIndentAndIndentItems(Indent indent, List<IndentItem> indentItems) {
//			Log.d( module, "indent=" + indent);
//		    ContentValues values = new ContentValues();
//		    values.put(MySQLiteHelper.COLUMN_INDENT_IS_SYNCED, indent.isSynced()?1:0);
//		    values.put(MySQLiteHelper.COLUMN_INDENT_STATUS, indent.getStatus());
//		    values.put(MySQLiteHelper.COLUMN_INDENT_TOTAL, indent.getTotal());
//		    final String[] whereArgs = { Long.toString(indent.getId())};
//		    database.update(MySQLiteHelper.TABLE_INDENT, values, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
//
//			database.delete(MySQLiteHelper.TABLE_INDENT_ITEM, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
//		  	for (int i=0; i < indentItems.size(); ++i) {
//		  		IndentItem indentItem = indentItems.get(i);
//		  		if (indentItem.getQty() == -1) {
//		  			continue;
//		  		}
//		  		ContentValues itemValues = new ContentValues();
//		  		itemValues.put(MySQLiteHelper.COLUMN_INDENT_ID, indent.getId());
//		  		itemValues.put(MySQLiteHelper.COLUMN_PRODUCT_ID, indentItem.getProductId());
//		  		itemValues.put(MySQLiteHelper.COLUMN_INDENT_ITEM_QTY, indentItem.getQty());
//		  		itemValues.put(MySQLiteHelper.COLUMN_INDENT_ITEM_UNIT_PRICE, indentItem.getUnitPrice());
//		  		database.insert(MySQLiteHelper.TABLE_INDENT_ITEM, null, itemValues);
//		  	}
//	  }
	  
	  public long insertIndentItem(long indentId, IndentItemNHDC indentItem) {

		  		ContentValues values = new ContentValues();
		  		values.put(MySQLiteHelper.COLUMN_INDENT_ID, indentId);
		  		values.put(MySQLiteHelper.COLUMN_PRODUCT_ID, indentItem.getProductId());
		  		values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_QTY, indentItem.getQuantity());
		  		values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_REMARKS, indentItem.getRemarks());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_BALE_QTY, indentItem.getBaleQuantity());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_BUNDLE_WEIGHT, indentItem.getBundleWeight());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_BUNDLE_UNITPRICE, indentItem.getBundleUnitPrice());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_UOM, indentItem.getYarnUOM());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_BASICPRICE, indentItem.getBasicPrice());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_TAXRATELIST, indentItem.getTaxRateList());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_SERVICECHARGE, indentItem.getServiceCharge());
				values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_SERVICECHARGE_AMT, indentItem.getServiceChargeAmt());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_TOTAL_AMT,indentItem.getTotalAmt());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_VAT_PER,indentItem.getVatPercent());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_VAT_AMT,indentItem.getVatAmount());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_CST_PER,indentItem.getCstPercent());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_CST_AMT,indentItem.getCstAmount());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_DISC_AMT,indentItem.getDiscountAmount());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_SHIPPED_QTY,indentItem.getShippedQty());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_OTHER_CHARGES,indentItem.getOtherCharges());
		  values.put(MySQLiteHelper.COLUMN_INDENT_ITEM_SPEC,indentItem.getSpec());


		  return database.insert(MySQLiteHelper.TABLE_INDENT_ITEM, null, values);
	  }
	  
	  public List<IndentItemNHDC> getIndentItems(int indentId) {
		  List<IndentItemNHDC> indentItems = new ArrayList<IndentItemNHDC>();
		  Cursor cursor = database.query(MySQLiteHelper.TABLE_INDENT_ITEM,
		        allIndentItemColumns, MySQLiteHelper.COLUMN_INDENT_ID + " = " + indentId, null, null, null, null);
		  ProductsDataSource datasource = new ProductsDataSource(context);
		  datasource.open();
		  Map productMap = datasource.getSaleProductMap();
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  IndentItemNHDC indentItem = cursorToIndentItem(cursor);
			  indentItems.add(indentItem);
		      cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  datasource.close();
		  return indentItems;
	  }

	public IndentItemNHDC getIndentItemDetails(int indentItemId) {
		Cursor cursor = database.query(MySQLiteHelper.TABLE_INDENT_ITEM,
				allIndentItemColumns, MySQLiteHelper.COLUMN_INDENT_ITEM_ID + " = " + indentItemId, null, null, null, null);

		cursor.moveToFirst();
		IndentItemNHDC indentItem = cursorToIndentItem(cursor);
		// Make sure to close the cursor
		cursor.close();
		return indentItem;
	}
	  
//	  public Map[] getXMLRPCSerializedIndentItems(int indentId) {
//	  public Map[] getXMLRPCSerializedIndentItems(int indentId) {
//		  List<IndentItemNHDC> indentItems = getIndentItems(indentId);
//		  if (indentItems.isEmpty()) {
//			  return null;
//		  }
//		  // let's consolidate duplicate product ids
//		  Map <String, IndentItemNHDC> consolidatedItems = new HashMap<String, IndentItemNHDC>();
//		  for (int i = 0; i < indentItems.size(); ++i) {
//			  IndentItemNHDC item = consolidatedItems.get(indentItems.get(i).getProductId());
//			if (item == null) {
//				consolidatedItems.put(indentItems.get(i).getProductId(), indentItems.get(i));
//			}
//			else {
//				item.setQty(item.getQty() + indentItems.get(i).getQty());
//			}
//		  }
//		Log.d( module, "consolidatedItems=" + consolidatedItems);
//		  Map [] result = new TreeMap[consolidatedItems.size()];
//		  int i = 0;
//		  for (IndentItemNHDC ii : consolidatedItems.values()) {
//			  Map item = new TreeMap();
//			  item.put("indentId", indentId);
//			  item.put("indentDate", System.currentTimeMillis());
//			  item.put("productId", ii.getProductId());
//			  item.put("qty", ii.getQty());
//			  item.put("unitPrice", ii.getUnitPrice());
//			  result[i] = item;
//			  ++i;
//		  }
//		  return result;
//	  }
	  
	  private IndentItemNHDC cursorToIndentItem(Cursor cursor) {
		  return new IndentItemNHDC(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),
				  cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),
				  cursor.getString(8),cursor.getString(9),cursor.getString(11),cursor.getString(12),
				  cursor.getString(13), cursor.getFloat(14),cursor.getFloat(15),cursor.getFloat(16),cursor.getFloat(17),cursor.getFloat(19),
				  cursor.getFloat(18), cursor.getFloat(20), cursor.getString(21));
	  }
	  
	  /*
	   * This method will insert a new indent and items for the given day and shift.
	   * Note: Any existing indent for the given day and shift will be deleted
	   */
//	  public long insertIndentAndItems(Date supplyDate, String subscriptionType, List<IndentItem> indentItems) {
//
//		double indentTotal = 0;
//		for (int i = 0; i < indentItems.size(); ++i) {
//			IndentItem indentItem = indentItems.get(i);
//			indentTotal += indentItem.getUnitPrice()*indentItem.getQty();
//		}
//		indentTotal = Math.round(indentTotal * 100.0) / 100.0;
//		long indentId = -1;
//		Indent indent = fetchIndent(supplyDate, subscriptionType);
//		if (indent != null) {
//			indentId = indent.getId();
//		    final String[] whereArgs = { Long.toString(indent.getId())};
//			database.delete(MySQLiteHelper.TABLE_INDENT_ITEM, MySQLiteHelper.COLUMN_INDENT_ID + " = ?", whereArgs);
//			updateIndentTotal(indentId, indentTotal);
//		}
//		else {
//			indentId = insertIndent("Created", indentTotal, subscriptionType, supplyDate);
//		}
//		insertIndentItems(indentId, indentItems);
//		return indentId;
//	}
}
