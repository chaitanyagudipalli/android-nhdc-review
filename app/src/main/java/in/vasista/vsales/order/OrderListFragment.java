package in.vasista.vsales.order;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.OrderItemsListActivity;
import in.vasista.vsales.adapter.OrderAdapter;
import in.vasista.vsales.db.OrdersDataSource;
import in.vasista.vsales.sync.ServerSync;


public class OrderListFragment extends ListFragment{
	public static final String module = OrderListFragment.class.getName();	
	List<Order> orderItems; 
	OrderAdapter adapter;
	OrdersDataSource datasource;
	final OrderListFragment orderListFragment = this;

	public void onActivityCreated(Bundle savedInstanceState) {  
		
		super.onActivityCreated(savedInstanceState);
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	final String retailerId = prefs.getString("storeId", "");
//		TextView retailerIdView = (TextView)getActivity().findViewById(R.id.retailerId);
//		retailerIdView.setText(retailerId + " : Orders");
		
		if (adapter == null) {			
    	    datasource = new OrdersDataSource(getActivity());
    	    datasource.open();
    	    orderItems = datasource.getAllOrders();
		}
		final ListView listView = getListView();


		if (listView.getHeaderViewsCount() == 0) {	

			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.order_header, null);
			listView.addHeaderView(headerView2);
		}
		if (adapter == null) {			   	    
		    adapter = new OrderAdapter(getActivity(),
                    R.layout.orderlist_item, 
                    orderItems);  
		}
		setListAdapter(adapter);				
		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		  @Override
		  public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

		    Order order = (Order)listView.getItemAtPosition(position);
//Log.d(module, "Order: [" + order.getId() + ";" + order.getSupplyDate() + ";" + order.getSubscriptionType() + ";" + order.getTotal() + "]");				    	  		    
//Toast.makeText( getActivity(), "Clicked item [" +order.getId() + "]", Toast.LENGTH_SHORT ).show();	
            if (order != null) { 
            	Intent orderItemsIntent = new Intent(getActivity(), OrderItemsListActivity.class);
            	orderItemsIntent.putExtra("orderId", order.getId());
            	orderItemsIntent.putExtra("retailerId", retailerId);            	
            	startActivity(orderItemsIntent);
            }
		  }
		});		 
	}
	public void syncOrder(MenuItem menuItem){

		ProgressBar p=(ProgressBar)menuItem.getActionView().findViewById(R.id.menuitem_progress);
		menuItem.expandActionView();
		ServerSync serverSync = new ServerSync(getActivity());
		serverSync.fetchOrders(menuItem, p, orderListFragment);
	}
	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
	    orderItems = datasource.getAllOrders();
	    
	    adapter = new OrderAdapter(getActivity(),
                R.layout.orderlist_item,
                orderItems);	
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
	    orderItems = datasource.getAllOrders();
		//Toast.makeText( getActivity(), "onResume [" +indentItems.size() + "]", Toast.LENGTH_SHORT ).show();	    		    
	    adapter.clear();
	    adapter.addAll(orderItems);
    }	
}