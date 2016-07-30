package in.vasista.vsales.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLE_PRODUCT = "PRODUCT";
	  public static final String COLUMN_PRODUCT_ID = "PRODUCT_ID";
	  public static final String COLUMN_PRODUCT_NAME = "PRODUCT_NAME";
	  public static final String COLUMN_PRODUCT_BRAND_NAME = "PRODUCT_BRAND_NAME";
	  public static final String COLUMN_PRODUCT_INRENAL_NAME = "PRODUCT_INTERNAL_NAME";
	  public static final String COLUMN_PRODUCT_DESC = "PRODUCT_DESC";	  
	  public static final String COLUMN_PRODUCT_PRICE = "PRODUCT_PRICE";
	  public static final String COLUMN_PRODUCT_CATEGORY_ID = "PRODUCT_CATEGORY_ID";
	public static final String COLUMN_PRODUCT_PARENT_CATEGORY_ID = "PRODUCT_PARENT_CATEGORY_ID";
	public static final String COLUMN_PRODUCT_TYPE_ID = "PRODUCT_TYPE_ID";
	public static final String COLUMN_PRODUCT_QUANTITY_UOM_ID = "PRODUCT_QUANTITY_UOM_ID";
	public static final String COLUMN_PRODUCT_QUANTITY_INCLUDED = "PRODUCT_QUANTITY_INCLUDED";


  
	  
	public static final String TABLE_INDENT = "INDENT";
	public static final String COLUMN_INDENT_ID = "INDENT_ID";
	public static final String COLUMN_INDENT_TALLY_REFNO = "INDENT_TALLY_REFNO";
	public static final String COLUMN_INDENT_PO_ORDER = "INDENT_PO_ORDER";
	public static final String COLUMN_INDENT_PO_SEQ_NO = "INDENT_PO_SEQ_NO";
	public static final String COLUMN_INDENT_IS_GEN_PO = "INDENT_IS_GEN_PO";
	public static final String COLUMN_INDENT_SUPP_PARTY_ID = "INDENT_SUPP_PARTY_ID";
	public static final String COLUMN_INDENT_STORENAME = "INDENT_STORENAME";
	public static final String COLUMN_INDENT_SUPP_PARTY_NAME = "INDENT_SUPP_PARTY_NAME";
	public static final String COLUMN_INDENT_ORDER_NO = "INDENT_ORDER_NO";
	public static final String COLUMN_INDENT_ORDER_ID = "INDENT_ORDER_ID";
	public static final String COLUMN_INDENT_ORDER_DATE = "INDENT_ORDER_DATE";
	public static final String COLUMN_INDENT_STATUS_ID = "INDENT_STATUS_ID";
	public static final String COLUMN_INDENT_ORDER_TOTAL = "INDENT_ORDER_TOTAL";
	public static final String COLUMN_INDENT_PAID = "INDENT_PAID";
	public static final String COLUMN_INDENT_BALANCE = "INDENT_BALANCE";
	public static final String COLUMN_INDENT_SCHEMECAT = "INDENT_SCHEMECAT";


	public static final String TABLE_INDENT_ITEM = "INDENT_ITEM";
	  public static final String COLUMN_INDENT_ITEM_ID = "_ID";	  
	  public static final String COLUMN_INDENT_ITEM_QTY = "QTY";
	public static final String COLUMN_INDENT_ITEM_REMARKS = "REMARKS";
	public static final String COLUMN_INDENT_ITEM_BALE_QTY = "BALE_QTY";
	public static final String COLUMN_INDENT_ITEM_BUNDLE_WEIGHT= "BUNDLE_WEIGHT";
	public static final String COLUMN_INDENT_ITEM_BUNDLE_UNITPRICE = "BUNDLE_UNITPRICE";
	public static final String COLUMN_INDENT_ITEM_UOM = "UOM";
	public static final String COLUMN_INDENT_ITEM_BASICPRICE= "BASICPRICE";
	public static final String COLUMN_INDENT_ITEM_TAXRATELIST = "TAXRATELIST";
	public static final String COLUMN_INDENT_ITEM_SERVICECHARGE = "SERVICECHARGE";
	public static final String COLUMN_INDENT_ITEM_SERVICECHARGE_AMT = "SERVICECHARGE_AMT";

	  public static final String TABLE_ORDER = "ORDER_HEADER";
	  public static final String COLUMN_ORDER_ID = "ORDER_ID";
	  public static final String COLUMN_ORDER_SUPPLYDATE = "ORDER_SUPPLYDATE";	
	  public static final String COLUMN_ORDER_SUBSCRIPTIONTYPE = "ORDER_SUBTYPE";	  	  
	  public static final String COLUMN_ORDER_TOTAL = "ORDER_TOTAL";	  
	  
	  public static final String TABLE_ORDER_ITEM = "ORDER_ITEM";
	  public static final String COLUMN_ORDER_ITEM_ID = "_ID";	  
	  public static final String COLUMN_ORDER_ITEM_QTY = "QTY";
	  public static final String COLUMN_ORDER_ITEM_UNIT_PRICE = "UNIT_PRICE";	
	  
	  public static final String TABLE_PAYMENT = "PAYMENT";
	  public static final String COLUMN_PAYMENT_ID = "PAYMENT_ID";
	  public static final String COLUMN_PAYMENT_DATE = "PAYMENT_DATE";
	  public static final String COLUMN_PAYMENT_METHOD = "PAYMENT_METHOD";		  	  
	  public static final String COLUMN_PAYMENT_AMOUNT_PAID = "PAYMENT_AMOUNT_PAID";
	public static final String COLUMN_PAYMENT_AMOUNT_BALANCE = "PAYMENT_AMOUNT_BALANCE";
	public static final String COLUMN_PAYMENT_FROM = "PAYMENT_FROM";
	public static final String COLUMN_PAYMENT_TO = "PAYMENT_TO";
	public static final String COLUMN_PAYMENT_STATUS = "PAYMENT_STATUS";
	  
	  public static final String TABLE_FACILITY = "FACILITY";
	  public static final String COLUMN_FACILITY_ID = "FACILITY_ID";
	  public static final String COLUMN_FACILITY_NAME = "FACILITY_NAME";
	  public static final String COLUMN_FACILITY_CAT = "CATEGORY";		  	  
	  public static final String COLUMN_FACILITY_PHONE_NUM = "PHONE_NUM";	
	  public static final String COLUMN_FACILITY_SALESREP = "SALESREP";		  
	  public static final String COLUMN_FACILITY_AM_ROUTE = "AM_ROUTE_ID";		  	  
	  public static final String COLUMN_FACILITY_PM_ROUTE = "PM_ROUTE_ID";	
	  public static final String COLUMN_FACILITY_LATITUDE = "LATITUDE";		  	  
	  public static final String COLUMN_FACILITY_LONGITUDE = "LONGITUDE";

	public static final String TABLE_SUPPLIER = "SUPPLIER";
	public static final String COLUMN_SUPPLIER_ID = "SUPPLIER_ID";
	public static final String COLUMN_SUPPLIER_GROUPNAME = "SUPPLIER_GROUPNAME";
	public static final String COLUMN_SUPPLIER_ROLETYPEID = "ROLETYPEID";
	public static final String COLUMN_SUPPLIER_PARTYTYPEID = "PARTYTYPEID";
	  
	  public static final String TABLE_EMPLOYEE = "EMPLOYEE";
	  public static final String COLUMN_EMPLOYEE_ID = "EMPLOYEE_ID";
	  public static final String COLUMN_EMPLOYEE_NAME = "EMPLOYEE_NAME";
	  public static final String COLUMN_EMPLOYEE_DEPT = "DEPT";	
	  public static final String COLUMN_EMPLOYEE_POSITION = "POSITION";			  
	  public static final String COLUMN_EMPLOYEE_PHONE_NUM = "PHONE_NUM";	
	  public static final String COLUMN_EMPLOYEE_JOIN_DATE = "JOIN_DATE";		  
	  public static final String COLUMN_EMPLOYEE_LEAVE_BALANCE_DATE = "LEAVE_BALANCE_DATE";		  
	  public static final String COLUMN_EMPLOYEE_EARNED_LEAVE = "EARNED_LEAVE";		  
	  public static final String COLUMN_EMPLOYEE_CASUAL_LEAVE = "CASUAL_LEAVE";		  
	  public static final String COLUMN_EMPLOYEE_HALF_PAY_LEAVE = "HALF_PAY_LEAVE";		  
	  public static final String COLUMN_EMPLOYEE_WEEKLY_OFF = "WEEKLY_OFF";		  
	
	  public static final String TABLE_PAYROLL_HEADER = "PAYROLL_HEADER";
	  public static final String COLUMN_PAYROLL_HEADER_ID = "PAYROLL_HEADER_ID";
	  public static final String COLUMN_PAYROLL_DATE = "PAYROLL_DATE";	
	  public static final String COLUMN_PAYROLL_PERIOD = "PAYROLL_PERIOD";	  	  
	  public static final String COLUMN_PAYROLL_EMPLOYEE_ID = "EMPLOYEE_ID";
	  public static final String COLUMN_PAYROLL_NET_AMOUNT = "NET_AMOUNT";	  
	  
	  
	  public static final String TABLE_PAYROLL_HEADER_ITEM = "PAYROLL_HEADER_ITEM";
	  public static final String COLUMN_PAYROLL_HEADER_ITEM_ID = "_ID";	  
	  public static final String COLUMN_PAYROLL_ITEM_NAME = "NAME";
	  public static final String COLUMN_PAYROLL_ITEM_AMOUNT = "AMOUNT";	
	  
	  public static final String TABLE_EMPLOYEE_LEAVE = "EMPLOYEE_LEAVE";
	  public static final String COLUMN_EMPLOYEE_LEAVE_ID = "_ID";	  
	  public static final String COLUMN_EMPLOYEE_LEAVE_TYPE_ID = "LEAVE_TYPE_ID";	  
	  public static final String COLUMN_EMPLOYEE_LEAVE_STATUS = "LEAVE_STATUS";	  
	  public static final String COLUMN_EMPLOYEE_LEAVE_FROM_DATE = "FROM_DATE";	  
	  public static final String COLUMN_EMPLOYEE_LEAVE_THRU_DATE = "THRU_DATE";	  
	  
	  public static final String TABLE_EMPLOYEE_ATTENDANCE = "EMPLOYEE_ATTENDANCE";
	  public static final String COLUMN_EMPLOYEE_ATTENDANCE_ID = "_ID";	  
	  public static final String COLUMN_EMPLOYEE_ATTENDANCE_IN_TIME = "IN_TIME";	  
	  public static final String COLUMN_EMPLOYEE_ATTENDANCE_OUT_TIME = "OUT_TIME";	  
	  public static final String COLUMN_EMPLOYEE_ATTENDANCE_DURATION = "DURATION";	  
	  
	  public static final String TABLE_LOCATION = "LOCATION";
	  public static final String COLUMN_LOCATION_ID = "LOCATION_ID";
	  public static final String COLUMN_LOCATION_CRDATE = "LOCATION_CRDATE";
	  public static final String COLUMN_LOCATION_LAT = "LOCATION_LAT";	  
	  public static final String COLUMN_LOCATION_LONG = "LOCATION_LONG";	
	  public static final String COLUMN_LOCATION_NOTE_NAME = "LOCATION_NOTE_NAME";	  
	  public static final String COLUMN_LOCATION_NOTE_INFO = "LOCATION_NOTE_INFO";	  	  
	  public static final String COLUMN_LOCATION_IS_SYNCED = "LOCATION_IS_SYNCED";	
	  
	  public static final String TABLE_TICKET = "TICKET";
	  public static final String COLUMN_TICKET_ID_INTERNAL = "_ID";	  	  
	  public static final String COLUMN_TICKET_ID = "TICKET_ID";
	  public static final String COLUMN_TICKET_CRDATE = "TICKET_CRDATE";
	  public static final String COLUMN_TICKET_LAT = "TICKET_LAT";	  
	  public static final String COLUMN_TICKET_LONG = "TICKET_LONG";	
	  public static final String COLUMN_TICKET_TYPE_ID = "TICKET_TYPE_ID";	  
	  public static final String COLUMN_TICKET_DESC = "TICKET_DESC";	  	
	  public static final String COLUMN_TICKET_STATUS = "TICKET_STATUS";	  	  
	  public static final String COLUMN_TICKET_IS_SYNCED = "TICKET_IS_SYNCED";	  

	  // Database creation sql statement
	  private static final String DATABASE_CREATE_PRODUCT = "create table "
	      + TABLE_PRODUCT + " (" + COLUMN_PRODUCT_ID
	      + " text primary key, " + COLUMN_PRODUCT_NAME
		  + " text not null, "  + COLUMN_PRODUCT_INRENAL_NAME
	      + " text, " + COLUMN_PRODUCT_BRAND_NAME
	      + " text, "+ COLUMN_PRODUCT_DESC
			  + " text, " +COLUMN_PRODUCT_TYPE_ID +" text," + COLUMN_PRODUCT_QUANTITY_UOM_ID +" text,"+ COLUMN_PRODUCT_PRICE
			  + " real, "+ COLUMN_PRODUCT_QUANTITY_INCLUDED+" real," + COLUMN_PRODUCT_CATEGORY_ID
			  + " text, "+ COLUMN_PRODUCT_PARENT_CATEGORY_ID+" text"
			  +");";


	  
	  private static final String DATABASE_CREATE_INDENT = "create table "
		      + TABLE_INDENT + " (" + COLUMN_INDENT_ID
		      + " integer primary key autoincrement, " + COLUMN_INDENT_TALLY_REFNO
		      + " text, " + COLUMN_INDENT_PO_ORDER
		      + " text, " + COLUMN_INDENT_PO_SEQ_NO
		      + " text, " + COLUMN_INDENT_IS_GEN_PO
		      + " text, " + COLUMN_INDENT_SUPP_PARTY_ID
			  + " text, " + COLUMN_INDENT_STORENAME
			  + " text, " + COLUMN_INDENT_SUPP_PARTY_NAME
			  + " text, " + COLUMN_INDENT_ORDER_NO
			  + " text, " + COLUMN_INDENT_ORDER_ID
			  + " text, " + COLUMN_INDENT_ORDER_DATE
			  + " text, " + COLUMN_INDENT_STATUS_ID
			  + " text, " + COLUMN_INDENT_ORDER_TOTAL
			  + " real not null, " + COLUMN_INDENT_PAID
		      + " real, " + COLUMN_INDENT_BALANCE
		      + " real, " + COLUMN_INDENT_SCHEMECAT + " text" +
			  ");";


	  private static final String DATABASE_CREATE_INDENT_ITEM = "create table "
		      + TABLE_INDENT_ITEM + " (" + COLUMN_INDENT_ITEM_ID
		      + " integer primary key autoincrement, " + COLUMN_INDENT_ID
		      + " integer not null, " + COLUMN_PRODUCT_ID
		      + " text not null, " + COLUMN_INDENT_ITEM_QTY
		      + " integer not null, "
			  + COLUMN_INDENT_ITEM_REMARKS +" text, "
			  + COLUMN_INDENT_ITEM_BALE_QTY + " real, "
			  + COLUMN_INDENT_ITEM_BUNDLE_WEIGHT + " real, "
			  + COLUMN_INDENT_ITEM_BUNDLE_UNITPRICE + " real, "
			  + COLUMN_INDENT_ITEM_UOM + " text,"
			  + COLUMN_INDENT_ITEM_BASICPRICE + " real,"
			  + COLUMN_INDENT_ITEM_TAXRATELIST + " text,"
			  + COLUMN_INDENT_ITEM_SERVICECHARGE + " real,"
			  + COLUMN_INDENT_ITEM_SERVICECHARGE_AMT + " real "
			  + ");";
	  
	  private static final String DATABASE_CREATE_ORDER = "create table "
		      + TABLE_ORDER + " (" + COLUMN_ORDER_ID
		      + " integer primary key autoincrement, " + COLUMN_ORDER_SUPPLYDATE
		      + " text not null, " + COLUMN_ORDER_SUBSCRIPTIONTYPE		      
		      + " integer not null, " + COLUMN_ORDER_TOTAL	      
		      + " real not null);";
	  
	  private static final String DATABASE_CREATE_ORDER_ITEM = "create table "
		      + TABLE_ORDER_ITEM + " (" + COLUMN_ORDER_ITEM_ID
		      + " integer primary key autoincrement, " + COLUMN_ORDER_ID
		      + " integer not null, " + COLUMN_PRODUCT_ID
		      + " text not null, " + COLUMN_ORDER_ITEM_QTY			      
		      + " integer not null, " + COLUMN_ORDER_ITEM_UNIT_PRICE	      
		      + " real not null);";		  
	  
	  // Database creation sql statement
	  private static final String DATABASE_CREATE_PAYMENT = "create table "
	      + TABLE_PAYMENT + " (" + COLUMN_PAYMENT_ID
	      + " text primary key, " + COLUMN_PAYMENT_DATE
	      + " integer not null, " + COLUMN_PAYMENT_METHOD	
	      + " text, " + COLUMN_PAYMENT_AMOUNT_PAID
			  + " real not null, " + COLUMN_PAYMENT_AMOUNT_BALANCE
			  + " real, " + COLUMN_PAYMENT_FROM
			  + " text, " + COLUMN_PAYMENT_TO
			  + " text, " + COLUMN_PAYMENT_STATUS
	      + " text);";

	private static final String DATABASE_CREATE_SUPPLIER = "create table "
			+ TABLE_SUPPLIER + " (" + COLUMN_SUPPLIER_ID
			+ " text primary key, " + COLUMN_SUPPLIER_GROUPNAME
			+ " text, " + COLUMN_SUPPLIER_ROLETYPEID
			+ " text, " + COLUMN_SUPPLIER_PARTYTYPEID
			+ " text);";

	  private static final String DATABASE_CREATE_FACILITY = "create table "
		      + TABLE_FACILITY + " (" + COLUMN_FACILITY_ID
		      + " text primary key, " + COLUMN_FACILITY_NAME
		      + " text, " + COLUMN_FACILITY_CAT	
		      + " text, " + COLUMN_FACILITY_PHONE_NUM
		      + " text, " + COLUMN_FACILITY_SALESREP		      
		      + " text, " + COLUMN_FACILITY_AM_ROUTE	
		      + " text, " + COLUMN_FACILITY_PM_ROUTE	
		      + " text, " + COLUMN_FACILITY_LATITUDE	
		      + " text, " + COLUMN_FACILITY_LONGITUDE			      
		      + " text);";	  
	  
	  private static final String DATABASE_CREATE_EMPLOYEE = "create table "
		      + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID
		      + " text primary key, " + COLUMN_EMPLOYEE_NAME
		      + " text, " + COLUMN_EMPLOYEE_DEPT	
		      + " text, " + COLUMN_EMPLOYEE_POSITION
		      + " text, " + COLUMN_EMPLOYEE_PHONE_NUM		      
		      + " text, " + COLUMN_EMPLOYEE_JOIN_DATE	
		      + " text, " + COLUMN_EMPLOYEE_LEAVE_BALANCE_DATE	
		      + " text, " + COLUMN_EMPLOYEE_EARNED_LEAVE
		      + " real, " + COLUMN_EMPLOYEE_CASUAL_LEAVE	
		      + " real, " + COLUMN_EMPLOYEE_HALF_PAY_LEAVE			      		      		      		      
		      + " real, " + COLUMN_EMPLOYEE_WEEKLY_OFF		      
		      + " text);";		
	  
	  private static final String DATABASE_CREATE_PAYROLL_HEADER = "create table "
		      + TABLE_PAYROLL_HEADER + " (" + COLUMN_PAYROLL_HEADER_ID
		      + " text primary key, " + COLUMN_PAYROLL_DATE
		      + " text not null, " + COLUMN_PAYROLL_PERIOD		      
		      + " text, " + COLUMN_PAYROLL_EMPLOYEE_ID	      
		      + " text, " + COLUMN_PAYROLL_NET_AMOUNT
		      + " real);";
	  
	  private static final String DATABASE_CREATE_PAYROLL_HEADER_ITEM = "create table "
		      + TABLE_PAYROLL_HEADER_ITEM + " (" + COLUMN_PAYROLL_HEADER_ITEM_ID
		      + " integer primary key autoincrement, " + COLUMN_PAYROLL_HEADER_ID
		      + " text not null, " + COLUMN_PAYROLL_ITEM_NAME
		      + " text not null, " + COLUMN_PAYROLL_ITEM_AMOUNT			            
		      + " real not null);";	
	  
	  private static final String DATABASE_CREATE_EMPLOYEE_LEAVE = "create table "
		      + TABLE_EMPLOYEE_LEAVE + " (" + COLUMN_EMPLOYEE_LEAVE_ID
		      + " text primary key, " + COLUMN_EMPLOYEE_ID
		      + " text, " + COLUMN_EMPLOYEE_LEAVE_TYPE_ID
		      + " text, " + COLUMN_EMPLOYEE_LEAVE_STATUS	
		      + " text, " + COLUMN_EMPLOYEE_LEAVE_FROM_DATE
		      + " text, " + COLUMN_EMPLOYEE_LEAVE_THRU_DATE		      	      
		      + " text);";	
	  
	  private static final String DATABASE_CREATE_EMPLOYEE_ATTENDANCE = "create table "
		      + TABLE_EMPLOYEE_ATTENDANCE + " (" + COLUMN_EMPLOYEE_ATTENDANCE_ID
		      + " text primary key, " + COLUMN_EMPLOYEE_ID
		      + " text, " + COLUMN_EMPLOYEE_ATTENDANCE_IN_TIME
		      + " text, " + COLUMN_EMPLOYEE_ATTENDANCE_OUT_TIME	
		      + " text, " + COLUMN_EMPLOYEE_ATTENDANCE_DURATION		      	      
		      + " text);";	
	  
	  private static final String DATABASE_CREATE_LOCATION = "create table "
		      + TABLE_LOCATION + " (" + COLUMN_LOCATION_ID
		      + " integer primary key autoincrement, " + COLUMN_LOCATION_CRDATE
		      + " integer not null, " + COLUMN_LOCATION_LAT	      
		      + " real not null, " + COLUMN_LOCATION_LONG
		      + " real not null, " + COLUMN_LOCATION_NOTE_NAME
		      + " text, " + COLUMN_LOCATION_NOTE_INFO	
		      + " text, " + COLUMN_LOCATION_IS_SYNCED	      
		      + " integer not null);";	
	  
	  private static final String DATABASE_CREATE_TICKET = "create table "
		      + TABLE_TICKET + " (" + COLUMN_TICKET_ID_INTERNAL
		      + " integer primary key autoincrement, " + COLUMN_TICKET_CRDATE
		      + " integer not null, " + COLUMN_TICKET_LAT	      
		      + " real not null, " + COLUMN_TICKET_LONG
		      + " real not null, " + COLUMN_TICKET_DESC
		      + " text, " + COLUMN_TICKET_ID	
		      + " text, " + COLUMN_TICKET_TYPE_ID	
		      + " text, " + COLUMN_TICKET_STATUS			      
		      + " text, " + COLUMN_TICKET_IS_SYNCED	      
		      + " integer not null);";	
	  

	  private static final String DATABASE_NAME = "vsalesagent.db";
//	  private static final int DATABASE_VERSION = 24;

		private static final int DATABASE_VERSION = 25; // For Indent Crates

	public MySQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	  } 

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE_PRODUCT);    
	    database.execSQL(DATABASE_CREATE_INDENT);	
	    database.execSQL(DATABASE_CREATE_INDENT_ITEM);	 
	    database.execSQL(DATABASE_CREATE_ORDER);	
	    database.execSQL(DATABASE_CREATE_ORDER_ITEM);		    
	    database.execSQL(DATABASE_CREATE_PAYMENT);
		  database.execSQL(DATABASE_CREATE_SUPPLIER);
	    database.execSQL(DATABASE_CREATE_FACILITY);		 
	    database.execSQL(DATABASE_CREATE_EMPLOYEE);		
	    database.execSQL(DATABASE_CREATE_PAYROLL_HEADER);		    	    	    
	    database.execSQL(DATABASE_CREATE_PAYROLL_HEADER_ITEM);	
	    database.execSQL(DATABASE_CREATE_EMPLOYEE_LEAVE);
	    database.execSQL(DATABASE_CREATE_EMPLOYEE_ATTENDANCE);
	    database.execSQL(DATABASE_CREATE_LOCATION);		    
	    database.execSQL(DATABASE_CREATE_TICKET);		    	    
	  } 

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion);
	    if (oldVersion < 24 ) { 

	    db.execSQL("drop table PRODUCT");	
	    db.execSQL("drop table INDENT_ITEM");		    	    	    
	    db.execSQL("drop table INDENT");		    	    
	    db.execSQL("drop table ORDER_ITEM");		    	    
	    db.execSQL("drop table ORDER_HEADER");		    	    
	    db.execSQL("drop table PAYMENT");		    	    
	    db.execSQL("drop table FACILITY");		    	    
	    db.execSQL("drop table EMPLOYEE");		    	    
	    db.execSQL("drop table PAYROLL_HEADER_ITEM");		    	    
	    db.execSQL("drop table PAYROLL_HEADER");		    	    
	    db.execSQL("drop table EMPLOYEE_LEAVE");		    	    
	    db.execSQL("drop table EMPLOYEE_ATTENDANCE");		    	    
	    db.execSQL("drop table LOCATION");		    	    
	    db.execSQL("drop table TICKET");	
	    
	    onCreate(db);
	    }


	    //if (oldVersion > 20) {
	    //	db.execSQL("drop table LOCATION");		    	    	    	    
	    //	db.execSQL(DATABASE_CREATE_LOCATION);	 
	    //}

//	    if (oldVersion < 2 && newVersion == 2) {
//	    	db.execSQL(DATABASE_CREATE_LOCATION);
//		    Log.w(MySQLiteHelper.class.getName(),
//			        "created LOCATION table!");	    	
//	    }
//	    db.execSQL(DATABASE_CREATE_TICKET);

	  }

}
