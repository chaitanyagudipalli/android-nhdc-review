package in.vasista.vsales.supplier;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
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

import in.vasista.nhdc.R;
import in.vasista.vsales.FacilityDetailsActivity;
import in.vasista.vsales.SupplierDetailsActivity;
import in.vasista.vsales.adapter.FacilityAdapter;
import in.vasista.vsales.adapter.SupplierAdapter;
import in.vasista.vsales.db.FacilityDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.facility.Facility;
import in.vasista.vsales.sync.ServerSync;


public class SupplierListFragment extends ListFragment {
	public static final String module = SupplierListFragment.class.getName();
	
	SupplierAdapter adapter;
	SupplierDataSource datasource;
	List<Supplier> supplierItems;

	final SupplierListFragment listFragment = this;

	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
		
		final ListView listView = getListView();

		if (listView.getHeaderViewsCount() == 0) {           
						
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.facility_header, null);
			((TextView)headerView2.findViewById(R.id.column_category_header)).setText(R.string.supplier_roletypeid);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {  
			/*
    		// Create the array list of to do items
    		ArrayList<Product> catalogItems = new ArrayList<Product>();
    	    catalogItems.add(new Product("B01", "BUTTER-C 500G",130));	     
    	    catalogItems.add(new Product("G05", "GHEE 1LTR (TIN)",290));	
    	    */
			
    	    datasource = new SupplierDataSource(getActivity());
    	    datasource.open();
			supplierItems = datasource.getAllSuppliers();
    	    
		    adapter = new SupplierAdapter(getActivity(),
                    R.layout.facilitylist_item,
					supplierItems);
		}
		setListAdapter(adapter);
		final EditText inputSearch = (EditText) getActivity().findViewById(R.id.inputSearch);
		final FrameLayout inputSearchFrame = (FrameLayout) getActivity().findViewById(R.id.inputSearchFrame);
		
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

		    Supplier facility = (Supplier)listView.getItemAtPosition(position);
            if (facility != null) { 
            	Intent facilityDetailsIntent = new Intent(getActivity(), SupplierDetailsActivity.class);
            	facilityDetailsIntent.putExtra("partyId", facility.getId());
            	startActivity(facilityDetailsIntent);
            } 
		  }
		});	
		
		inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	//Toast.makeText( getActivity(), "inputSearch: cs=" + cs, Toast.LENGTH_SHORT ).show();
				listFragment.adapter.getFilter().filter(cs);
            }
              
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
        });
		ImageButton searchClearButton = (ImageButton)getActivity().findViewById(R.id.inputSearchClear);
		searchClearButton.setOnClickListener(new OnClickListener() { 
			public void onClick(View view) {  
				inputSearch.setText("");
			}
		});
		
		inputSearchFrame.setVisibility(View.GONE);
		  
	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
		supplierItems = datasource.getAllSuppliers();
    	Log.d(module, "supplierItems.size() = " + supplierItems.size());
	    try {
			adapter = new SupplierAdapter(getActivity(),
					R.layout.facilitylist_item,
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
	public void syncSuppliers(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.updateSuppliers(menuItem, p, listFragment);
	}
}
