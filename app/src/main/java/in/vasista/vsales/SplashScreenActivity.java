package in.vasista.vsales;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.preference.FragmentPreferences;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.sync.xmlrpc.XMLRPCMethodCallback;

public class SplashScreenActivity extends Activity  
{  
	public static final String module = SplashScreenActivity.class.getName();		
	private ProgressBar progressBar;

	private static final int SHOW_PREFERENCES = 1;

	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState)     
	{   
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_splash);
		//Initialize a LoadViewTask object and call the execute() method  
		new LoadViewTask().execute();         

	}  

	//To use the AsyncTask, it must be subclassed  
	private class LoadViewTask extends AsyncTask<Void, Integer, Void>  
	{  
		//Before running code in separate thread   
		@Override  
		protected void onPreExecute()  
		{  
			progressBar = (ProgressBar)findViewById(R.id.splashProgress);
			progressBar.setVisibility(View.VISIBLE);   
		}  

		//The code to be executed in a background thread.  
		@Override  
		protected Void doInBackground(Void... params)  
		{  
			/* This is just a code that delays the thread execution 4 times, 
			 * during 850 milliseconds and updates the current progress. This 
			 * is where the code that is going to be executed on a background 
			 * thread must be placed. 
			 */  
			try  
			{  
				//Get the current thread's token  
				synchronized (this)  
				{
					Map paramMap = new HashMap();
					final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					SharedPreferences.Editor prefEditor = prefs.edit();
					XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(getBaseContext());
					Object result = adapter.callSync("getMobilePermissions", paramMap);	
					if (result != null) { 
						Map  permissions = (Map)((Map)result).get("permissionResults");
						if (permissions != null) {
							Log.d(module, "permissions.size() = " + permissions.size());
							Object[]  permissionList = (Object[] ) permissions.get("permissionList");
							if (permissionList != null)  {
								Log.d(module, "permissionList.length = " + permissionList.length);	
						    	String retailerPerm = "N";
						    	String salesRepPerm = "N";
						    	String hrPerm = "N";
						    	String inventoryPerm = "N", locationPerm = "N";
								String suppPerm = "N";
						    	
								for (int i = 0; i < permissionList.length; ++i) {
									prefEditor.putBoolean(MainActivity.IS_SUP_PORTAL,false);
									String dashboardPermission = (String)permissionList[i];
									Log.d(module, "dashboardPermission = " + dashboardPermission);
									if (MainActivity.RETAILER_DB_PERM.equals(dashboardPermission)) {
										retailerPerm = "Y";
									}
									else if (MainActivity.SALESREP_DB_PERM.equals(dashboardPermission)) {
										salesRepPerm = "Y";
									}
									else if (MainActivity.HR_DB_PERM.equals(dashboardPermission)) {
										hrPerm = "Y";
									}
									else if (MainActivity.INVENTORY_DB_PERM.equals(dashboardPermission)) {
										inventoryPerm = "Y";
									}
									else if (MainActivity.LOCATION_DB_PERM.equals(dashboardPermission)) {
										locationPerm = "Y";
									}
									else if (MainActivity.SUP_DB_PERM.equals(dashboardPermission)) {
										suppPerm = "Y";
										prefEditor.putBoolean(MainActivity.IS_SUP_PORTAL,true);
									}
								}
								// refresh permissions prefs

					    		prefEditor.putString(MainActivity.RETAILER_DB_PERM, retailerPerm);
					    		prefEditor.putString(MainActivity.SALESREP_DB_PERM, salesRepPerm);
					    		prefEditor.putString(MainActivity.HR_DB_PERM, hrPerm);		
					    		prefEditor.putString(MainActivity.INVENTORY_DB_PERM, inventoryPerm);
								prefEditor.putString(MainActivity.LOCATION_DB_PERM, locationPerm);
								prefEditor.putString(MainActivity.SUP_DB_PERM, suppPerm);

							}

							prefEditor.putString("storeId",(String) permissions.get("userLoginParty"));
						}

						String name = (String)((Map)result).get("name");

						if(name != null)
							prefEditor.putString(MainActivity.USER_FULLNAME, name);

						String contactNumber = (String)((Map)result).get("contactNumber");
						if (contactNumber != null)
							prefEditor.putString(MainActivity.USER_PASSBOOK, contactNumber);

						String productStoreId = (String) permissions.get("productStoreId");
						if (productStoreId != null)
							prefEditor.putString("productStoreId", productStoreId);
						prefEditor.apply();

					}
					
					/*
					//Initialize an integer (that will act as a counter) to zero  
					int counter = 0;  
					//While the counter is smaller than four  
					while(counter <= 4)  
					{  
						this.wait(500);  
						//Increment the counter  
						counter++;  

					}  
					*/

					paramMap = new HashMap();
					String partyId = prefs.getString("storeId", "");
					paramMap.put("partyId", partyId);

					//kk10635
					//		nhdc123
					paramMap.put("effectiveDate", (new Date()).getTime());

					adapter = new XMLRPCApacheAdapter(getBaseContext());
					if(prefs.getBoolean(MainActivity.IS_SUP_PORTAL,false)){
						// Supplier
						Object suppDet = adapter.callSync("getSupplierDetails", paramMap);

						if (suppDet != null){
							Map supplierDetails = (Map) ((Map) suppDet).get("supplierDetails");
							prefEditor.putString("suppDetailsMap", "" + supplierDetails);
							prefEditor.putString(MainActivity.USER_FULLNAME, "" + supplierDetails.get("partyName"));
							prefEditor.putString(MainActivity.USER_PASSBOOK, "" + supplierDetails.get("contactNumber"));
							prefEditor.apply();
						}

					}else {
						// Weaver and sales rep
						Object weaverDet = adapter.callSync("getWeaverDetails", paramMap);

						if (weaverDet != null) {
							Map weaverDetails = (Map) ((Map) weaverDet).get("weaverDetails");
							prefEditor.putString("weaverDetailsMap", "" + weaverDetails);
							prefEditor.putString(MainActivity.USER_FULLNAME, "" + weaverDetails.get("partyName"));
							prefEditor.putString(MainActivity.USER_PASSBOOK, "" + weaverDetails.get("passBookNo"));
							prefEditor.putString("customerName",(String)weaverDetails.get("partyName"));
							prefEditor.apply();

							Map loomDetails = (Map) ((Map) weaverDetails).get("loomDetails");

							Map silk = (Map) ((Map) loomDetails).get("SILK_YARN");
							Map cotton_40above = (Map) ((Map) loomDetails).get("COTTON_40ABOVE");
							Map cotton_40upto = (Map) ((Map) loomDetails).get("COTTON_UPTO40");
							Map wool_10to39 = (Map) ((Map) loomDetails).get("WOOLYARN_10STO39NM");
							Map wool_40above = (Map) ((Map) loomDetails).get("WOOLYARN_40SNMABOVE");
							Map wool_10below = (Map) ((Map) loomDetails).get("WOOLYARN_BELOW10NM");

							prefEditor.putInt("SILK_YARN", (int) silk.get("avlQuota"));
							prefEditor.putInt("COTTON_40ABOVE", (int) cotton_40above.get("avlQuota"));
							prefEditor.putInt("COTTON_UPTO40", (int) cotton_40upto.get("avlQuota"));
							prefEditor.putInt("WOOLYARN_10STO39NM", (int) wool_10to39.get("avlQuota"));
							prefEditor.putInt("WOOLYARN_40SNMABOVE", (int) wool_40above.get("avlQuota"));
							prefEditor.putInt("WOOLYARN_BELOW10NM", (int) wool_10below.get("avlQuota"));
							prefEditor.apply();

						}
					}

				}
			}  
/*			catch (InterruptedException e)  
			{  
				e.printStackTrace();  
			} */ 
			catch (Exception e) {
				e.printStackTrace();  				
			}
			return null;  
		}  

		//Update the progress  
		@Override  
		protected void onProgressUpdate(Integer... values)  
		{  

		}  

		//after executing the code in the thread  
		@Override  
		protected void onPostExecute(Void result)  
		{
			progressBar.setVisibility(View.GONE);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
			String serverURL = prefs.getString("serverURL", "nhdc-test.vasista.in");
			String userName = prefs.getString("userName", "");
			String password = prefs.getString("password", "");
			String tenantId = prefs.getString("tenantId", "nhdc-test");
			String language = prefs.getString("lang_preference", "en");
			SharedPreferences.Editor prefEditor = prefs.edit();
			prefEditor.putString("serverURL",serverURL);
			prefEditor.putString("tenantId",tenantId);
			prefEditor.putString("lang_preference",language);
			prefEditor.apply();


			Locale myLocale = new Locale(language);
			Locale.setDefault(myLocale);
			Resources res = getResources();
			DisplayMetrics dm = res.getDisplayMetrics();
			Configuration conf = res.getConfiguration();
			conf.locale = myLocale;
			res.updateConfiguration(conf, dm);

			/*if (serverURL.isEmpty() || userName.isEmpty() || password.isEmpty() || tenantId.isEmpty()){
				//show settings
				SharedPreferences.Editor prefEditor = prefs.edit();
				prefEditor.putString("serverURL", serverURL).apply();
				startActivityForResult( new Intent(getApplicationContext(), FragmentPreferences.class), SHOW_PREFERENCES);
			}else {*/
				Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
				startActivity(i);
				if (serverURL.isEmpty() || userName.isEmpty() || password.isEmpty() || tenantId.isEmpty()){
					//show settings
					prefEditor = prefs.edit();
					prefEditor.putString("serverURL", serverURL).apply();
					startActivityForResult( new Intent(getApplicationContext(), FragmentPreferences.class), SHOW_PREFERENCES);
				}
			//}
            
         // close this activity
            finish();
		}
	}  
}  

