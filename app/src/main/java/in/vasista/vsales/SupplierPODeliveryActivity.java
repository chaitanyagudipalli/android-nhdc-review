package in.vasista.vsales;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
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
import in.vasista.vsales.indent.IndentItemsListFragment;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.supplier.SupplierPOItem;
import in.vasista.vsales.supplier.SupplierPOItemListFragment;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class SupplierPODeliveryActivity extends DashboardAppCompatActivity{


	String partyId;

	boolean editMode = false;

	SharedPreferences prefs;

	SupplierPOItemListFragment supplierPOItemListFragment = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate your view 
		setContentChildView(R.layout.activity_supppoitem);
		actionBarHomeEnabled();

		setSalesDashboardTitle(R.string.supplier_delivery);

		FragmentManager fm = getFragmentManager();
		supplierPOItemListFragment= (SupplierPOItemListFragment) fm.findFragmentById(R.id.indent_list_fragment);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.po_menu, menu);
		if (editMode){
			menu.findItem(R.id.action_shipment_upload).setVisible(true);
			menu.findItem(R.id.action_shipment_edit).setVisible(false);
		}else{
			menu.findItem(R.id.action_shipment_edit).setVisible(true);
			menu.findItem(R.id.action_shipment_upload).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
			case R.id.action_shipment_edit:
				editMode = true;
				invalidateOptionsMenu();
				supplierPOItemListFragment.editShipmentAction();
				return true;
			case R.id.action_shipment_upload:
				editMode = false;
				invalidateOptionsMenu();
				supplierPOItemListFragment.uploadShipmentAction(item);
				return true;
		}
		return false;
	}


}
