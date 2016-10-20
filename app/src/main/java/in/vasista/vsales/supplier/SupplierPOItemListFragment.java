package in.vasista.vsales.supplier;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.SupplierDetailsActivity;
import in.vasista.vsales.adapter.SupplierPOAdapter;
import in.vasista.vsales.adapter.SupplierPOItemsAdapter;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.sync.ServerSync;


public class SupplierPOItemListFragment extends ListFragment {
	public static final String module = SupplierPOItemListFragment.class.getName();

	SupplierPOItemsAdapter adapter;
	PODataSource datasource;
	List<SupplierPOItem> supplierItems;
	String partyId;

	final SupplierPOItemListFragment listFragment = this;

	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
		
		final ListView listView = getListView();

		if (listView.getHeaderViewsCount() == 0) {           
						
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.supplierpoitem_header, null);
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

			partyId = getActivity().getIntent().getStringExtra("supp_poId");
			supplierItems  = datasource.getSuppPOItems(partyId);

			Log.d(module, "supplierItems.size() = " + supplierItems.size());
    	    
		    adapter = new SupplierPOItemsAdapter(getActivity(),
                    R.layout.supplierpoitem_list,
					supplierItems);
		}
		setListAdapter(adapter);
		
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

//		    SupplierPO facility = (SupplierPO)listView.getItemAtPosition(position);
//            if (facility != null) {
//            	Intent facilityDetailsIntent = new Intent(getActivity(), SupplierDetailsActivity.class);
//            	facilityDetailsIntent.putExtra("supp_poId", facility.getPoid());
//            	startActivity(facilityDetailsIntent);
//            }
		  }
		});	

	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
		supplierItems  = datasource.getSuppPOItems(partyId);

		Log.d(module, "supplierItems.size() = " + supplierItems.size());

		adapter = new SupplierPOItemsAdapter(getActivity(),
				R.layout.supplierpoitem_list,
				supplierItems);

		setListAdapter(adapter);
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

}
