package in.vasista.vsales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.HashMap;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.transporter.Transporter;

public class StockDetailsActivity extends DashboardAppCompatActivity{

	MapView mapView;
	GoogleMap map;
	GoogleApiClient googleApiClient;
	static final int REQUEST_CODE_FINELOCATION = 1;
	//static final int REQUEST_CODE_COARSELOCATION = 2;

	String partyId;

	Object suppDet;
	SharedPreferences prefs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.activity_transporterdetails);
		actionBarHomeEnabled();

		Intent facilityDetailsIntent = getIntent();
		partyId = facilityDetailsIntent.getStringExtra("partyId");
		final Transporter facility;
		TransporterDataSource datasource = new TransporterDataSource(this);
		datasource.open();
		facility = datasource.getTransporterDetails(partyId);
		datasource.close();
		if (facility == null) {
			return;
		}

		TextView idView = (TextView) findViewById(R.id.facilityId);
		idView.setText(partyId);
		TextView nameView = (TextView) findViewById(R.id.facilityName);
		nameView.setText(facility.getName());

		TextView textView = (TextView) findViewById(R.id.supplierContact);
		textView.setText(""+facility.getPhone());

		textView = (TextView) findViewById(R.id.supplierAddress);

		textView.setText(""+facility.getAddress());

		Button callBtn = (Button) findViewById(R.id.callButton);
		if (facility.getPhone() == null || (facility.getPhone()).equalsIgnoreCase("")) {
			callBtn.setVisibility(View.GONE);
			return;
		}
		callBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// set the data
					String uri = "tel:" + "+91" + facility.getPhone();
					Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

					startActivity(callIntent);
				}catch(Exception e) {
					Toast.makeText(getApplicationContext(),"Your call has failed...",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});

		//new LoadViewTask().execute();

	}

	//To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void>
	{
		//Before running code in separate thread
		@Override
		protected void onPreExecute()
		{

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
					prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


					paramMap.put("partyId", partyId);

					XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(getBaseContext());
					suppDet = adapter.callSync("getTransporters", paramMap);



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

			TextView textView;
			if (suppDet != null) {
				Map supplierDetails = (Map)((Map)suppDet).get("transportersMap");
				for ( Object key : supplierDetails.keySet() ) {
					final Map suppMap = (Map) supplierDetails.get(key);
					textView = (TextView) findViewById(R.id.supplierContact);
					textView.setText(""+suppMap.get("contactNumber"));

					textView = (TextView) findViewById(R.id.supplierAddress);

					Map address = (Map) suppMap.get("addressMap");
					textView.setText(""+address.get("address1")+", "+""+address.get("city")+", "+""+address.get("stateProvinceGeoId")+". "+"\n\nPostal Code: "+""+address.get("postalCode"));

					Button callBtn = (Button) findViewById(R.id.callButton);
					if (suppMap.get("contactNumber") == null || ((String)suppMap.get("contactNumber")).equalsIgnoreCase("")) {
						callBtn.setVisibility(View.GONE);
						return;
					}
					callBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								// set the data
								String uri = "tel:" + "+91" + suppMap.get("contactNumber");
								Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

								startActivity(callIntent);
							}catch(Exception e) {
								Toast.makeText(getApplicationContext(),"Your call has failed...",
										Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
						}
					});

				}


			}
		}
	}

}
