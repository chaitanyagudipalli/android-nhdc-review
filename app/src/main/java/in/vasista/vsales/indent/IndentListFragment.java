package in.vasista.vsales.indent;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.IndentDetailed;
import in.vasista.vsales.IndentItemsListActivity;
import in.vasista.vsales.adapter.IndentAdapter;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.sync.ServerSync;


public class IndentListFragment extends ListFragment{
	public static final String module = IndentListFragment.class.getName();	
	List<Indent> indentItems; 
	IndentAdapter adapter;
	IndentsDataSource datasource;
	final IndentListFragment indentListFragment = this;

	SharedPreferences.Editor prefEditor;

	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
    	final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	final String retailerId = prefs.getString("storeId", "");
		//TextView retailerIdView = (TextView)getActivity().findViewById(R.id.retailerId);
		//retailerIdView.setText(retailerId + " : Indents");    
 
		if (adapter == null) {			
    	    datasource = new IndentsDataSource(getActivity());    
    	    datasource.open();
    	    indentItems = datasource.getAllIndents();
		}
		final ListView listView = getListView();
		

		if (listView.getHeaderViewsCount() == 0) {
			 
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.indent_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {			   	    
		    adapter = new IndentAdapter(getActivity(),
                    R.layout.indentlist_item, 
                    indentItems);  
		}
		setListAdapter(adapter);				
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

		    Indent indent = (Indent)listView.getItemAtPosition(position);
			//Toast.makeText( getActivity(), "Clicked item [" +order.getId() + "]", Toast.LENGTH_SHORT ).show();	
            if (indent != null) {
            	Intent indentItemsIntent = new Intent(getActivity(), IndentDetailed.class);
            	indentItemsIntent.putExtra("indentId", indent.getId());
				prefEditor = prefs.edit();

				prefEditor.putInt("IndentId",indent.getId());
				prefEditor.apply();
            	indentItemsIntent.putExtra("retailerId", retailerId);
            	startActivity(indentItemsIntent);
            }
		  }
		});		
	}

		public void syncIndent(MenuItem menuItem){

			ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
			menuItem.expandActionView();
			ServerSync serverSync = new ServerSync(getActivity());
			serverSync.fetchActiveIndents(menuItem, p, indentListFragment);
		}
	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		Log.v("asdad","chnage");
		setListAdapter(null);
	    datasource.open();
	    indentItems = datasource.getAllIndents();
		// Added try-catch block handling exception  -  Upendra
	    try {
			adapter = new IndentAdapter(getActivity(),
					R.layout.indentlist_item,
					indentItems);
			setListAdapter(adapter);
		}catch (Exception e){
			e.printStackTrace();
		}
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
	    indentItems = datasource.getAllIndents();
		//Toast.makeText( getActivity(), "onResume [" +indentItems.size() + "]", Toast.LENGTH_SHORT ).show();	    		    
	    adapter.clear();
	    adapter.addAll(indentItems);
		notifyChange();
    }	
}