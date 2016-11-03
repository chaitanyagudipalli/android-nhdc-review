package in.vasista.vsales.supplier;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.SupplierDetailsActivity;
import in.vasista.vsales.SupplierPODeliveryActivity;
import in.vasista.vsales.adapter.SupplierAdapter;
import in.vasista.vsales.adapter.SupplierPOAdapter;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.sync.ServerSync;


public class SupplierPOListFragment extends ListFragment {
	public static final String module = SupplierPOListFragment.class.getName();

	SupplierPOAdapter adapter;
	PODataSource datasource;
	List<SupplierPO> supplierItems;

	final SupplierPOListFragment listFragment = this;

	SharedPreferences prefs;


	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
		
		final ListView listView = getListView();
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


		if (listView.getHeaderViewsCount() == 0) {           
						
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.supplierpo_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {  
			/*
    		// Create the array list of to do items
    		ArrayList<Product> catalogItems = new ArrayList<Product>();
    	    catalogItems.add(new Product("B01", "BUTTER-C 500G",130));	     
    	    catalogItems.add(new Product("G05", "GHEE 1LTR (TIN)",290));	
    	    */
			
    	    datasource = new PODataSource(getActivity());
    	    datasource.open();
			supplierItems = datasource.getAllSuppPOs();

			Log.d(module, "supplierItems.size() = " + supplierItems.size());
    	    
		    adapter = new SupplierPOAdapter(getActivity(),
                    R.layout.supplierpi_list,
					supplierItems);
		}
		setListAdapter(adapter);
		
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

		    SupplierPO facility = (SupplierPO)listView.getItemAtPosition(position);
            if (facility != null) {
            	Intent facilityDetailsIntent = new Intent(getActivity(), SupplierPODeliveryActivity.class);
				SharedPreferences.Editor prefEditor = prefs.edit();

				prefEditor.putString("SUPP_POID",""+facility.getPoid());
				prefEditor.putString("SUPP_ORDERID",""+facility.getOrderId());

				prefEditor.apply();

            	facilityDetailsIntent.putExtra("supp_poId", facility.getPoid());
				facilityDetailsIntent.putExtra("orderId",facility.getOrderId());
            	startActivity(facilityDetailsIntent);
            }
		  }
		});	

	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
		supplierItems = datasource.getAllSuppPOs();
    	Log.d(module, "supplierItems.size() = " + supplierItems.size());
	    try {
			adapter = new SupplierPOAdapter(getActivity(),
					R.layout.supplierpi_list,
					supplierItems);
			setListAdapter(adapter);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}
	public void syncSupplierPOs(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.updateSupplierPOs(menuItem, p, listFragment);
	}
}
