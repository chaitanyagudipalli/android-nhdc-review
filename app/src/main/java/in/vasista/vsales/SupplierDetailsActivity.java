package in.vasista.vsales;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import in.vasista.nhdc.R;
import in.vasista.vsales.db.FacilityDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.facility.Facility;
import in.vasista.vsales.supplier.Supplier;

public class SupplierDetailsActivity extends DashboardAppCompatActivity{

	MapView mapView;
	GoogleMap map;
	GoogleApiClient googleApiClient;
	static final int REQUEST_CODE_FINELOCATION = 1;
	//static final int REQUEST_CODE_COARSELOCATION = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.activity_supplierdetails);
		actionBarHomeEnabled();

		Intent facilityDetailsIntent = getIntent();
		String partyId = facilityDetailsIntent.getStringExtra("partyId");
		final Supplier facility;
		SupplierDataSource datasource = new SupplierDataSource(this);
		datasource.open();
		facility = datasource.getSupplierDetails(partyId);
		datasource.close();
		if (facility == null) {
			return;
		}

		TextView idView = (TextView) findViewById(R.id.facilityId);
		idView.setText(partyId);
		TextView nameView = (TextView) findViewById(R.id.facilityName);
		nameView.setText(facility.getName());
		TextView categoryView = (TextView) findViewById(R.id.facilityCategory);
		categoryView.setText(facility.getRoletypeid());
		TextView partyType = (TextView) findViewById(R.id.partyType);
		partyType.setText(facility.getPartytypeid());


	}


}
