package in.vasista.vsales.catalog;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.adapter.ProductAdapter;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.sync.ServerSync;


public class CatalogListFragment extends ListFragment {
	public static final String module = CatalogListFragment.class.getName();	
	
	ProductAdapter adapter;
	ProductsDataSource datasource;
	List<Product> catalogItems;
	final CatalogListFragment catalogListFragment = this;
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	//String retailerId = prefs.getString("storeId", "");

		final ListView listView = getListView();
		final EditText inputSearch = (EditText) getActivity().findViewById(R.id.inputSearch);

		/**
		 * Search functionality - Filter
		 */
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				catalogListFragment.adapter.getFilter().filter(cs);
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
		 
		if (listView.getHeaderViewsCount() == 0) {
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.catalog_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) { 
    	    datasource = new ProductsDataSource(getActivity());
    	    datasource.open();
    	    catalogItems = datasource.getAllProducts();
    	    
		    adapter = new ProductAdapter(getActivity(), 
                    R.layout.cataloglist_item,
                    catalogItems);
		}
		setListAdapter(adapter);
	}
	public void syncCatalog(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.updateProducts(menuItem, p, catalogListFragment);
	}
	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
	    catalogItems = datasource.getAllProducts();
    	Log.d(module, "catalogItems.size() = " + catalogItems.size());
	    
	    adapter = new ProductAdapter(getActivity(),
                R.layout.cataloglist_item,
                catalogItems);	
		setListAdapter(adapter);
	}
	
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

}
