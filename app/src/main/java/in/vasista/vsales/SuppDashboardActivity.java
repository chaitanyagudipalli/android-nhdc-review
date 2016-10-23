package in.vasista.vsales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import in.vasista.global.GlobalApplication;
import in.vasista.nhdc.R;
import in.vasista.vsales.adapter.FacilityAutoAdapter;
import in.vasista.vsales.adapter.SupplierAutoAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.FacilityDataSource;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.OrdersDataSource;
import in.vasista.vsales.db.PaymentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.facility.Facility;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.transporter.Transporter;

public class SuppDashboardActivity extends DrawerCompatActivity  {
	public static final String module = SuppDashboardActivity.class.getName();

    static final private int MENU_PREFERENCES = Menu.FIRST+1;
    private static final int SHOW_PREFERENCES = 1;

	SharedPreferences prefs;
    
    public static final String RETAILER_DB_PERM = "MOB_RTLR_DB_VIEW";    
    public static final String SALESREP_DB_PERM = "MOB_SREP_DB_VIEW";
	public static final String LOCATION_DB_PERM = "MOB_LOCATION_VIEW";
	public static final String SUP_DB_PERM = "MOB_SUP_DB_VIEW";
    
	private Map facilityMap = new HashMap<String, Facility> ();
	AutoCompleteTextView actv;
    

	/**
	 * onCreate - called when the activity is first created.
	 * Called when the activity is first created. 
	 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
	 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
	 * 
	 * Always followed by onStart().   
	 *
	 */

	protected void onCreate(Bundle savedInstanceState) 
	{   
	    super.onCreate(savedInstanceState);   	    
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	String onlySUPPDashboard = prefs.getString("onlySUPPDashboard", "N");
Log.d(module, "onlySUPPDashboard equals " + onlySUPPDashboard);
    	if (onlySUPPDashboard.equals("Y")) {
    	    setContentChildView(R.layout.activity_supp_home);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    	}
    	else {
			setContentChildView(R.layout.activity_sales_home_alt);
			actionBarHomeEnabled();

			// back button functionality
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
    	}


		SharedPreferences.Editor prefEditor = prefs.edit();
    	String storeId;
    	storeId = prefs.getString("storeId", "");
    	if (storeId.isEmpty()) {
    		storeId = "V01608";
    		//prefEditor.putString("storeId", storeId);
    		prefEditor.apply();
    	}
	}
	    
	/**
	 * onDestroy
	 * The final call you receive before your activity is destroyed. 
	 * This can happen either because the activity is finishing (someone called finish() on it, 
	 * or because the system is temporarily destroying this instance of the activity to save space. 
	 * You can distinguish between these two scenarios with the isFinishing() method.
	 *
	 */

	protected void onDestroy ()
	{
	   super.onDestroy();
	}

	/**
	 * onPause
	 * Called when the system is about to start resuming a previous activity. 
	 * This is typically used to commit unsaved changes to persistent data, stop animations 
	 * and other things that may be consuming CPU, etc. 
	 * Implementations of this method must be very quick because the next activity will not be resumed 
	 * until this method returns.
	 * Followed by either onResume() if the activity returns back to the front, 
	 * or onStop() if it becomes invisible to the user.
	 *
	 */

	protected void onPause ()
	{
	   super.onPause();
	}

	/**
	 * onRestart
	 * Called after your activity has been stopped, prior to it being started again.
	 * Always followed by onStart().
	 *
	 */

	protected void onRestart ()
	{
	   super.onRestart();
	}

	/**
	 * onResume
	 * Called when the activity will start interacting with the user. 
	 * At this point your activity is at the top of the activity stack, with user input going to it.
	 * Always followed by onPause().
	 *
	 */

	protected void onResume ()
	{
	   super.onResume();
		if(((GlobalApplication)getApplication()).isPrefChange()){
			((GlobalApplication)getApplication()).setPrefChange(false);
			Intent i = new Intent(this.getBaseContext(), SplashScreenActivity.class);
			startActivity(i);
			finish();
		}
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		String locationPerm = prefs.getString(LOCATION_DB_PERM, "N");
		if (locationPerm.equals("Y")) {
			navigationView.getMenu().setGroupVisible(R.id.group_location, true);
		}

	}

	/**
	 * onStart
	 * Called when the activity is becoming visible to the user.
	 * Followed by onResume() if the activity comes to the foreground, or onStop() if it becomes hidden.
	 *
	 */

	protected void onStart ()
	{
	   super.onStart ();
	}

	/**
	 * onStop
	 * Called when the activity is no longer visible to the user
	 * because another activity has been resumed and is covering this one. 
	 * This may happen either because a new activity is being started, an existing one 
	 * is being brought in front of this one, or this one is being destroyed.
	 *
	 * Followed by either onRestart() if this activity is coming back to interact with the user, 
	 * or onDestroy() if this activity is going away.
	 */
 
	protected void onStop () 
	{
	   super.onStop ();
	}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);

	menu.removeItem(R.id.action_refresh);
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	String salesrepPerm = prefs.getString(SALESREP_DB_PERM, "N");
	if (salesrepPerm.equals("N")) {
		menu.removeItem(R.id.homeSearch);
	}
	return super.onCreateOptionsMenu(menu);
}
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.homeSearch:
				if (actv.isShown()) {
					actv.setVisibility(View.GONE);
				}
				else {
					actv.setVisibility(View.VISIBLE);
				}
				return true;
		}
		return false;
	}
    
    
    public void onClickFeature (View v)
    {
        int id = v.getId ();
        switch (id) {
          case R.id.home_btn_supppo :
              startActivity (new Intent(getApplicationContext(), SuppPOActivity.class));
               break;
          case R.id.home_btn_orders :
              startActivity (new Intent(getApplicationContext(), OrderActivity.class));
               break; 
          case R.id.home_btn_payments :
               startActivity (new Intent(getApplicationContext(), PaymentActivity.class));
               break;
          case R.id.home_btn_catalog :
               startActivity (new Intent(getApplicationContext(), CatalogActivity.class));
               break;
          case R.id.home_btn_outlets :
               startActivity (new Intent(getApplicationContext(), SupplierActivity.class));
               break;
			case R.id.home_btn_transporters :
				startActivity (new Intent(getApplicationContext(), TranporterActivity.class));
				break;
			case R.id.home_btn_profile :
				startActivity (new Intent(getApplicationContext(), SupplierDetailedActivity.class));
				break;
          default:    
        	   break;    
        }
    }    


}
