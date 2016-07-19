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
import in.vasista.vsales.facility.Facility;

public class FacilityDetailsActivity extends DashboardAppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	MapView mapView;
	GoogleMap map;
	GoogleApiClient googleApiClient;
	static final int REQUEST_CODE_FINELOCATION = 1;
	//static final int REQUEST_CODE_COARSELOCATION = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.facilitydetails_layout);
		actionBarHomeEnabled();

		Intent facilityDetailsIntent = getIntent();
		String facilityId = facilityDetailsIntent.getStringExtra("facilityId");
		final Facility facility;
		FacilityDataSource datasource = new FacilityDataSource(this);
		datasource.open();
		facility = datasource.getFacilityDetails(facilityId);
		datasource.close();
		if (facility == null) {
			return;
		}
		setTitle(facilityId+" Details");
		TextView idView = (TextView) findViewById(R.id.facilityId);
		idView.setText(facilityId);
		TextView nameView = (TextView) findViewById(R.id.facilityName);
		nameView.setText(facility.getName());
		TextView categoryView = (TextView) findViewById(R.id.facilityCategory);
		categoryView.setText(facility.getCategory());
		TextView salesRepView = (TextView) findViewById(R.id.facilitySalesRep);
		salesRepView.setText(facility.getSalesRep());
		TextView amRouteView = (TextView) findViewById(R.id.facilityAmRoute);
		amRouteView.setText(facility.getAmRouteId());
		TextView pmRouteView = (TextView) findViewById(R.id.facilityPmRoute);
		pmRouteView.setText(facility.getPmRouteId());
		TextView phoneView = (TextView) findViewById(R.id.facilityPhone);
		phoneView.setText(facility.getOwnerPhone());

		map = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
		if (map != null) {

			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_CODE_FINELOCATION);
				return;
			}

			String latStr = facility.getLatitude();
			String longStr = facility.getLongitude();
			double latitude = 13.095042; // default MD coordinates
			double longitude = 77.573120;
			if ( latStr != null && !latStr.isEmpty() && longStr != null && !longStr.isEmpty())  {
				latitude = Double.valueOf(latStr);
				longitude = Double.valueOf(longStr);

			}
			//map.getUiSettings().setMyLocationButtonEnabled(false);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			map.setMyLocationEnabled(true);
			MapsInitializer.initialize(this);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12);
			map.animateCamera(cameraUpdate);
			map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
					.title(facilityId)).showInfoWindow();
			getLoactionviaGPS();
		}
		
		Button callBtn = (Button) findViewById(R.id.callButton);
   
		if (facility.getOwnerPhone() == null) {
			callBtn.setVisibility(View.GONE);
			return; 
		} 
		// add PhoneStateListener for monitoring
//		MyPhoneListener phoneListener = new MyPhoneListener();
//		TelephonyManager telephonyManager = 
//			(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		// receive notifications of telephony state changes 
//		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
				
		callBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					// set the data
					String uri = "tel:" + "+91" + facility.getOwnerPhone();
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

	@Override
	public void onConnected(Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	private class MyPhoneListener extends PhoneStateListener {
		 
		private boolean onCall = false;
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// phone ringing...
				break;
			
			case TelephonyManager.CALL_STATE_OFFHOOK:
				// one call exists that is dialing, active, or on hold
				//because user answers the incoming call
				onCall = true;
				break;

			case TelephonyManager.CALL_STATE_IDLE: 
				// in initialization of the class and at the end of phone call 
				  
				// detect flag from CALL_STATE_OFFHOOK
				if (onCall) {
 
					// restart our application
					Intent restart = getBaseContext().getPackageManager().
						getLaunchIntentForPackage(getBaseContext().getPackageName());
					restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(restart);
 
					onCall = false;
				}
				break;
			default:
				break;   
			}
			
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_CODE_FINELOCATION ) {
			if (grantResults.length == 1
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// success!
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					return;
				}
				map.setMyLocationEnabled(true);
				map.getUiSettings().setAllGesturesEnabled(true);
				getLoactionviaGPS();
			}

		}
	}

	private void getLoactionviaGPS() {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
			googleApiClient.connect();

			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(30 * 1000);
			locationRequest.setFastestInterval(5 * 1000);
			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
					.addLocationRequest(locationRequest);

			//**************************
			builder.setAlwaysShow(true); //this is the key ingredient
			//**************************

			PendingResult<LocationSettingsResult> result =
					LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

			result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
				@Override
				public void onResult(LocationSettingsResult result) {

					final Status status = result.getStatus();
//					final LocationSettingsStates state = result.getLocationSettingsStates();
					switch (status.getStatusCode()) {
						case LocationSettingsStatusCodes.SUCCESS:
							// All location settings are satisfied. The client can initialize location
							// requests here.
							break;
						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
							// Location settings are not satisfied. But could be fixed by showing the user
							// a dialog.
							try {
								// Show the dialog by calling startResolutionForResult(),
								// and check the result in onActivityResult().
								status.startResolutionForResult(
										FacilityDetailsActivity.this, 1000);
							} catch (IntentSender.SendIntentException e) {
								// Ignore the error.
							}
							break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
							// Location settings are not satisfied. However, we have no way to fix the
							// settings so we won't show the dialog.
							break;
					}
				}
			});
		}
	}
}
