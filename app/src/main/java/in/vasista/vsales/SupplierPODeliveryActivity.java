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
import java.util.List;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.supplier.SupplierPOItem;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class SupplierPODeliveryActivity extends DashboardAppCompatActivity{


	String partyId;

	SharedPreferences prefs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.activity_supppoitem);
		actionBarHomeEnabled();

		setSalesDashboardTitle(R.string.supplier_delivery);

//		findViewById(R.id.supp_ship_history).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity (new Intent(getApplicationContext(), SalesDashboardActivity.class));
//			}
//		});

	}



}
