package in.vasista.vsales.transporter;

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

import in.vasista.nhdcapp.R;
import in.vasista.vsales.SupplierDetailsActivity;
import in.vasista.vsales.TransporterDetailsActivity;
import in.vasista.vsales.adapter.SupplierAdapter;
import in.vasista.vsales.adapter.TransporterAdapter;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.sync.ServerSync;


public class TransporterListFragment extends ListFragment {
	public static final String module = TransporterListFragment.class.getName();
	
	TransporterAdapter adapter;
	TransporterDataSource datasource;
	List<Transporter> supplierItems;

	final TransporterListFragment listFragment = this;

	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
		
		final ListView listView = getListView();

		if (listView.getHeaderViewsCount() == 0) {           
						
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.transporter_header, null);
//			((TextView)headerView2.findViewById(R.id.column_category_header)).setText(R.string.supplier_roletypeid);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {  
			/*
    		// Create the array list of to do items
    		ArrayList<Product> catalogItems = new ArrayList<Product>();
    	    catalogItems.add(new Product("B01", "BUTTER-C 500G",130));	     
    	    catalogItems.add(new Product("G05", "GHEE 1LTR (TIN)",290));	
    	    */
			
    	    datasource = new TransporterDataSource(getActivity());
    	    datasource.open();
			supplierItems = datasource.getAllTransporters();
    	    
		    adapter = new TransporterAdapter(getActivity(),
                    R.layout.transporterlist_item,
					supplierItems);
		}
		setListAdapter(adapter);
		final EditText inputSearch = (EditText) getActivity().findViewById(R.id.inputSearch);
		final FrameLayout inputSearchFrame = (FrameLayout) getActivity().findViewById(R.id.inputSearchFrame);
		
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

		    Transporter facility = (Transporter)listView.getItemAtPosition(position);
            if (facility != null) { 
            	Intent facilityDetailsIntent = new Intent(getActivity(), TransporterDetailsActivity.class);
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
		supplierItems = datasource.getAllTransporters();
    	Log.d(module, "supplierItems.size() = " + supplierItems.size());
	    try {
			adapter = new TransporterAdapter(getActivity(),
					R.layout.transporterlist_item,
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
	public void syncTransporters(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.updateTransporters(menuItem, p, listFragment);
	}
}
