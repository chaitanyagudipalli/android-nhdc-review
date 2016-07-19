package in.vasista.location;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import in.vasista.nhdc.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.db.LocationsDataSource;

public class LocationActivity extends DashboardAppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	public static String TAG = "LocationActivity";


	//private PendingIntent mPendingIntent;

	GoogleApiClient googleApiClient;
	LocationRequest locationRequest;
	CoordinatorLayout coordinatorLayout;
	static final int REQUEST_CODE_FINELOCATION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_location);

		// Inflate your view
		setContentChildView(R.layout.activity_location);
		actionBarHomeEnabled();

		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					REQUEST_CODE_FINELOCATION);
			return;
		}

		getLocationviaGPS();

		//Intent intentService = new Intent(this, LocationService.class);
		//mPendingIntent = PendingIntent.getService(this, 1, intentService, 0);

		final LocationActivity thisActivity = this;

		final Button recordNoteBtn;
		recordNoteBtn = (Button) findViewById(R.id.recordNote);

		recordNoteBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (googleApiClient != null && googleApiClient.isConnected()) {
					try {
						getLocationviaGPS();
						// Get the current location's latitude & longitude
						EditText noteName = (EditText) recordNoteBtn.getRootView().findViewById(R.id.noteName);
						String noteNameStr = noteName.getText().toString();
						EditText noteInfo = (EditText) recordNoteBtn.getRootView().findViewById(R.id.noteInfo);
						String noteInfoStr = noteInfo.getText().toString();

						/**
                         * Validation
						 */
						// Reset errors.
						noteName.setError(null);
						noteInfo.setError(null);

						// Store values at the time of the login attempt.
						String name = noteName.getText().toString();
						String detail = noteInfo.getText().toString();

						boolean cancel = false;
						View focusView = null;

						// Check for a valid email address.
						if (TextUtils.isEmpty(name)) {
							noteName.setError(getString(R.string.error_field_required));
							focusView = noteName;
							cancel = true;
						} else if (TextUtils.isEmpty(detail)) {
							noteInfo.setError(getString(R.string.error_field_required));
							focusView = noteInfo;
							cancel = true;
						}

						if (cancel) {
							// There was an error; don't attempt login and focus the first
							// form field with an error.
							focusView.requestFocus();
							return;
						}
						/**
						 * Validation ended
						 */

						if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
							return;
						}
						Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//						String msg = "Current Location: " +
//								Double.toString(location.getLatitude()) + "," +
//								Double.toString(location.getLongitude());
						LocationsDataSource datasource = new LocationsDataSource(thisActivity);
						datasource.open();
						long locationId = datasource.insertLocation(location.getLatitude(), location.getLongitude(),
								location.getTime(), noteNameStr, noteInfoStr);
						datasource.close();
//					Toast.makeText(thisActivity, msg + locationId, Toast.LENGTH_SHORT).show();

						// Hide the Keyboard
						hideKeyboard();

						// Show info at the bottom
						showSnackBar(getString(R.string.saved_location));


					}catch (Exception e){
						// Hide the Keyboard
						hideKeyboard();

						// Show info at the bottom
						showSnackBar(getString(R.string.exception_location));
					}

				}
			}
		});

		Button goMap = (Button) findViewById(R.id .goMap);
		goMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LocationActivity.this,MapsActivity.class));
			}
		});

		Button getLocationBtn;
		getLocationBtn = (Button) findViewById(R.id.getLocation);

		getLocationBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
//				if(mLocationClient != null && mLocationClient.isConnected()){
//					displayCurrentLocation();
//				}
			}
		});

		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (googleApiClient != null && googleApiClient.isConnected()) {
//					Log.i(TAG, "start button handler ");
//					mLocationClient.requestLocationUpdates(mLocationRequest, mPendingIntent);
					if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
						return;
					}
					LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,LocationActivity.this);
				}
			}
		});

		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
//				if(mLocationClient != null && mLocationClient.isConnected()){
//					Log.i(TAG, "stop button handler ");
//					mLocationClient.removeLocationUpdates(mPendingIntent);
//				}
				if (googleApiClient != null && googleApiClient.isConnected()) {
					if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
						return;
					}
					LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, LocationActivity.this);
				}
			}
		});
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		// Display the error code on failure
		Toast.makeText(this, "Connection Failure : " +
						connectionResult.getErrorCode(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		// Display the connection status
//		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_CODE_FINELOCATION);
				return;
			}
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	public void displayCurrentLocation() {
		// Get the current location's latitude & longitude
//		Location currentLocation = mLocationClient.getLastLocation();
//		String msg = "Current Location: " +
//				Double.toString(currentLocation.getLatitude()) + "," +
//				Double.toString(currentLocation.getLongitude());
//
//		// Display the current location in the UI
//		TextView locationLabel;
//		locationLabel = (TextView) findViewById(R.id.locationLabel);
//		locationLabel.setText(msg);
	}

	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
//		String msg = "Updated Location: " +
//				Double.toString(location.getLatitude()) + "," +
//				Double.toString(location.getLongitude());
		LocationsDataSource datasource = new LocationsDataSource(this);
		datasource.open();
		//long locationId = datasource.insertLocation(location.getLatitude(), location.getLongitude(), location.getTime());
		datasource.close();
//		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(googleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
			googleApiClient.disconnect();
		}
	}

	private void getLocationviaGPS() {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
			googleApiClient.connect();

			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(30 * 1000);
			locationRequest.setFastestInterval(5 * 1000);
			locationRequest.setSmallestDisplacement(10); //::TODO::


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
										LocationActivity.this, 1000);
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
				getLocationviaGPS();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.refresh, menu);
		menu.getItem(0).setTitle(R.string.title_menu_synclocaltion);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.action_refresh:
				item.setActionView(R.layout.progressbar);
				//ProgressBar progressBar=(ProgressBar)item.getActionView().findViewById(R.id.menuitem_progress);
				//ServerSync serverSync = new ServerSync(this);
				//serverSync.syncLocations(menuItem,progressBar,this);

				// hide keyboard
				hideKeyboard();

				return true;
		}
		return false;
	}


}

//public class LocationActivity extends Activity {
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_location);
//	}
//}