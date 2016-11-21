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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.db.StocksDataSource;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.stocks.Stock;

public class StockDetailsActivity extends DashboardAppCompatActivity{

	MapView mapView;
	GoogleMap map;
	GoogleApiClient googleApiClient;
	static final int REQUEST_CODE_FINELOCATION = 1;
	//static final int REQUEST_CODE_COARSELOCATION = 2;

	String invId;
	String partyId;
	Object suppDet;
	SharedPreferences prefs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.activity_stockdetails);
		actionBarHomeEnabled();

		Intent stockDetailsIntent = getIntent();
		invId = stockDetailsIntent.getStringExtra("invId");
		final Stock stock;
		StocksDataSource datasource = new StocksDataSource(this);
		datasource.open();
		stock = datasource.getStockDetails(invId);
		datasource.close();
		if (stock == null) {
			return;
		}

		TextView nameView = (TextView) findViewById(R.id.stock_prod_name);
		nameView.setText(stock.getProdname());
		TextView specView = (TextView) findViewById(R.id.stock_prod_spec);
		specView.setText(stock.getSpec());
		TextView depotView = (TextView) findViewById(R.id.stock_prod_depot);
		depotView.setText(stock.getDepot());
		TextView supplView = (TextView) findViewById(R.id.stock_prod_suppl);
		supplView.setText(stock.getSupp());
		TextView qtyView = (TextView) findViewById(R.id.stock_prod_qty);
		String qtyStr = "";
		qtyStr = String.format("%.2f", stock.getQty());
		qtyView.setText(qtyStr);

		TextView priceView = (TextView) findViewById(R.id.stock_prod_price);
		String priceStr = "";
		priceStr = String.format("%.2f", stock.getPrice());
		priceView.setText(priceStr);


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
