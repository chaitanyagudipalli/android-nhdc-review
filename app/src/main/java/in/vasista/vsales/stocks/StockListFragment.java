package in.vasista.vsales.stocks;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.StockDetailsActivity;
import in.vasista.vsales.TransporterDetailsActivity;
import in.vasista.vsales.adapter.PaymentAdapter;
import in.vasista.vsales.adapter.StockAdapter;
import in.vasista.vsales.db.PaymentsDataSource;
import in.vasista.vsales.db.StocksDataSource;
import in.vasista.vsales.payment.Payment;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.transporter.Transporter;

public class StockListFragment extends ListFragment {
	public static final String module = StockListFragment.class.getName();
	List<Stock> stockItems;
	StockAdapter adapter;
	StocksDataSource datasource;
	final StockListFragment stockListFragment = this;
	
	public void onActivityCreated(Bundle savedInstanceState) { 
		
		super.onActivityCreated(savedInstanceState);
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	//String retailerId = prefs.getString("storeId", "");
		
		if (adapter == null) {			
    	    datasource = new StocksDataSource(getActivity());
    	    datasource.open();
			stockItems = datasource.getAllStocks();
		}
		final ListView listView = getListView();


		if (listView.getHeaderViewsCount() == 0) {	
	
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.payment_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {			   	    
		    adapter = new StockAdapter(getActivity(),
                    R.layout.paymentlist_item,
					stockItems);
		}
		setListAdapter(adapter);

		final EditText inputSearch = (EditText) getActivity().findViewById(R.id.inputSearch);
		final FrameLayout inputSearchFrame = (FrameLayout) getActivity().findViewById(R.id.inputSearchFrame);

		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

				Stock facility = (Stock)listView.getItemAtPosition(position);
				if (facility != null) {
					Intent facilityDetailsIntent = new Intent(getActivity(), StockDetailsActivity.class);
					facilityDetailsIntent.putExtra("prodId", facility.getProdid());
					startActivity(facilityDetailsIntent);
				}
			}
		});

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				//Toast.makeText( getActivity(), "inputSearch: cs=" + cs, Toast.LENGTH_SHORT ).show();
				stockListFragment.adapter.getFilter().filter(cs);
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
		searchClearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				inputSearch.setText("");
			}
		});

		inputSearchFrame.setVisibility(View.GONE);
	}
	public void syncStocks(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.fetchStocks(menuItem, p, stockListFragment);
	}
	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
		stockItems = datasource.getAllStocks();
	    
	    adapter = new StockAdapter(getActivity(),
                R.layout.paymentlist_item,
				stockItems);
		setListAdapter(adapter);
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}
	
    @SuppressLint("NewApi") @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
	    datasource.open();
		stockItems = datasource.getAllStocks();
	    adapter.clear();
	    adapter.addAll(stockItems);
    }	
}
