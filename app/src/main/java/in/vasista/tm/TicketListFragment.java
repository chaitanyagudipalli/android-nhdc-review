package in.vasista.tm;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.db.TicketsDataSource;


public class TicketListFragment extends ListFragment {
	public static final String module = TicketListFragment.class.getName();	
	
	TicketAdapter adapter;
	TicketsDataSource datasource;
	List<Ticket> ticketItems;   
 
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
    	//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		final ListView listView = getListView();
		final TicketListFragment catalogListFragment = this; 
		if (listView.getHeaderViewsCount() == 0) {           
			ImageButton button = (ImageButton)catalogListFragment.getActivity().findViewById(R.id.refreshTicketsButton);
			button.setOnClickListener(new OnClickListener() { 
				public void onClick(View view) {   
/*					ProgressBar progressBar = (ProgressBar) catalogListFragment.getActivity().findViewById(R.id.productsRefreshProgress);
					progressBar.setVisibility(View.VISIBLE);                       
					ServerSync serverSync = new ServerSync(getActivity());
					serverSync.updateProducts(progressBar, catalogListFragment);	*/				
				}
			});			
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.catalog_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) { 
    	    datasource = new TicketsDataSource(getActivity());
    	    datasource.open();
    	    ticketItems = datasource.getAllTickets();
    	    
		    adapter = null; //new TicketAdapter(getActivity(), 
                   // R.layout.ticketlist_item,
                   // ticketItems);
		}
		//setListAdapter(adapter);
	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
	    ticketItems = datasource.getAllTickets();
    	Log.d(module, "ticketItems.size() = " + ticketItems.size());
	    
	    adapter = null; //new TicketAdapter(getActivity(),
                //R.layout.ticketlist_item,
                //ticketItems);	
		//setListAdapter(adapter);
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

}
