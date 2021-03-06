package in.vasista.vsales.indent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.IndentItemDetailed;
import in.vasista.vsales.adapter.IndentItemAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.util.DateUtil;


public class IndentItemsListFragment extends ListFragment{
	public static final String module = IndentItemsListFragment.class.getName();	
	
	IndentItemAdapter adapter;
    List<IndentItemNHDC> indentItems;
    Indent indent;   
	IndentsDataSource datasource;
	boolean isEditableList;
	ListView listView;
	int indentId = -1;
	final IndentItemsListFragment indentItemsListFragment = this;
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		indentItems = new ArrayList<IndentItemNHDC>();
		Intent indentItemsIntent= getActivity().getIntent();

		indentId = indentItemsIntent.getIntExtra("indentId", indentId);	
		final String retailerId = indentItemsIntent.getStringExtra("retailerId");	 			
		if (adapter == null) {		  
			datasource = new IndentsDataSource(getActivity());
			if (indentId == -1) {  
				indentItems = new ArrayList<IndentItemNHDC>();
				initializeIndentItems();
			}
			else {  
				datasource.open();
				indent = datasource.getIndentDetails(indentId);
				indentItems = datasource.getIndentItems(indentId);
			}
		}

		if (indentId != -1) {
			datasource.open();
			indent = datasource.getIndentDetails(indentId);
		} 		
		listView = getListView();
		

		if (listView.getHeaderViewsCount() == 0) {

			
			if (indent != null) {           
					
				
			}
			else { 
				isEditableList = true;		
									
			} 
			
			final View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.indentitems_header, null);
			listView.addHeaderView(headerView2);
		}

		if (adapter == null) {		
		    adapter = new IndentItemAdapter(getActivity(),
                    R.layout.indentitems_item,
                    indentItems);
		    updateIndentHeaderView();
		}
		adapter.setEditable(isEditableList);		
		setListAdapter(adapter);

		// Handle Complete Order Button
		

					 
		if (indent != null) { 
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
//			boolean isActiveIndent =  fmt.format(new Date()).compareTo(fmt.format(indent.getSupplyDate())) <= 0;
	    	//Log.d(module, "now = " + fmt.format(new Date()));
	    	//Log.d(module, "supplyDate = " + fmt.format(indent.getSupplyDate()));
//			if (!isActiveIndent) {
//				editButton.setVisibility(View.GONE);
//				uploadButton.setVisibility(View.GONE);
//			}
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg) {
Log.v("jgjg",""+position);

				try {
					if ((datasource.getIndentDetails((int) (indentId)).getStatusId().equalsIgnoreCase("Not Uploaded"))) {
						return;
					}
				}catch (Exception e){

				}
				try {
					final IndentItemNHDC item = indentItems.get(position-1);
					Log.v("Upendra", "item:" + item.getProductId());

					//Intent intent = new Intent(getActivity(),IndentCreateProduct.class);
					Intent intent = new Intent(getActivity(), IndentItemDetailed.class);
					intent.putExtra("indentId", indentId);
					intent.putExtra("indentitem_id", item.getId());

					startActivity(intent);
				}catch (IndexOutOfBoundsException e){

				}
			}

		});

	}
	
	public void onDestroyView() { 
		super.onDestroyView();
		setListAdapter(null);     
	}
	
//	public double getTotal() {
//		double retVal = 0;
//		for (int i = 0; i < indentItems.size(); ++i) {
//			IndentItemNHDC item = indentItems.get(i);
////			if (item.getQty() == -1) {
////				continue;
////			}
//			retVal += item.getUnitPrice() * item.getQty();
//		}
//		retVal = Math.round(retVal * 100.0) / 100.0;
//		return retVal;
//	}
	
	public void updateIndentHeaderView() {
		updateIndentHeaderViewInternal(indent);	
	}
	
	void updateIndentHeaderViewInternal(Indent indent) {
		//double total = getTotal();
		String totalStr = "Total: Rs ";
		Date date = DateUtil.addDays(new Date(), 1);
		String indentSupply = "";
		String synced = ""; 
//		if (indent != null) {
//			indent.setTotal(total);
//			date = indent.getSupplyDate();
//			indentSupply = "Supply: " + indent.getSubscriptionType();
//			synced = "Synced: " + (indent.isSynced() ? "Y":"N");
//		}
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");   
	    String indentDateStr = dateFormat.format(date);
		TextView totalView = (TextView)listView.getRootView().findViewById(R.id.indentitemsTotal);
		if (totalView != null) {   
			totalView.setText(totalStr);        
		}
	
		TextView indentSupplyView = (TextView)listView.getRootView().findViewById(R.id.indentSupply);	
		if (indentSupplyView != null) {
			indentSupplyView.setText(indentSupply);  
		}	
		//TextView indentSyncedView = (TextView)listView.getRootView().findViewById(R.id.indentSynced);	
		//if (indentSyncedView != null) {
		//	indentSyncedView.setText(synced); 
		//}			
		TextView indentDateView = (TextView)listView.getRootView().findViewById(R.id.indentDate);	
		if (indentDateView != null) {
			indentDateView.setText(indentDateStr);
		}	
	}		
	
	void initializeIndentItems() {
//		ProductsDataSource prodsDatasource = new ProductsDataSource(getActivity());
//		prodsDatasource.open();
//		List<Product> products = prodsDatasource.getAllProducts();
//		for (int i = 0; i < products.size(); ++i) {
//			Product product = products.get(i);
//			//IndentItem item = new IndentItem(product.getId(), product.getName(), -1, product.getPrice());
//			IndentItemNHDC item = new IndentItem(product.getId(), product.getName(), -1, product.getPrice());
//			indentItems.add(item);
//		}
		IndentsDataSource indentsDataSource = new IndentsDataSource(this.getActivity());
		indentsDataSource.open();
		indentItems = indentsDataSource.getIndentItems(indentId);
		indentsDataSource.close();
	}

	void makeIndentItemsEditable() {  
		if (indent == null) {
			return;
		}
		List <IndentItemNHDC> newItemsList = new ArrayList<IndentItemNHDC>();
		ProductsDataSource prodsDatasource = new ProductsDataSource(getActivity());
		prodsDatasource.open();
		List<Product> products = prodsDatasource.getAllProducts();
		for (int i = 0; i < products.size(); ++i) {
			Product product = products.get(i);
			boolean productExists = false;
			for (int j = 0; j < indentItems.size(); ++j) {
				if (product.getId().equals(indentItems.get(j).getProductId())) {
					productExists = true;
					newItemsList.add(indentItems.get(j));              
				}         
			}                
			if (!productExists) {
				//IndentItem item = new IndentItem(product.getId(), product.getName(), -1, product.getPrice());
//				IndentItem item = new IndentItem(product.getId(), product.getName(), -1, product.getPrice());
//				newItemsList.add(item);
			}
		}
		//setListAdapter(null);
	    indentItems = newItemsList;
	    
	    adapter = new IndentItemAdapter(getActivity(),
                R.layout.indentitems_item,
                indentItems);	
	    adapter.setEditable(isEditableList);
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		notifyChange();
		Intent indentItemsIntent= getActivity().getIntent();
		Log.v("Upendra","Change"+indentItemsIntent.getIntExtra("indentId", indentId)	);
	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
//		if (indent == null) {
//			// nothing to do
//			return;
//		}
		if (indentItems.size() > 0)
			IndentCreationActivity.editMode = true;
		getActivity().invalidateOptionsMenu();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		;
		setListAdapter(null);
	    datasource.open();
	    indentItems = datasource.getIndentItems(prefs.getInt("IndentId",0));
	    
	    adapter = new IndentItemAdapter(getActivity(),
                R.layout.indentitems_item,
                indentItems);	
	    //adapter.setEditable(isEditableList);
		setListAdapter(adapter);
	}
	public void editIndentAction(){
		isEditableList = true;
		datasource.open();
		makeIndentItemsEditable();
		updateIndentHeaderViewInternal(indent);

	}
	public void doneIndentAction(){
		datasource.open();
//		if (indent == null) {
//			long indentId = datasource.insertIndent("Created", getTotal(), "AM");
//			datasource.insertIndentItems(indentId, indentItems);
//			indent = datasource.getIndentDetails(indentId);
//		}
//		else {
//			indent.setSynced(false);
//			indent.setTotal(getTotal());
//			datasource.updateIndentAndIndentItems(indent, indentItems);
//		}

		notifyChange();
		updateIndentHeaderViewInternal(indent);
		datasource.close();
		isEditableList = false;
	}
	public void uploadIndentAction(final MenuItem menuItem){
		AlertDialog.Builder alert = new AlertDialog.Builder(
				getActivity());
		alert.setTitle(R.string.upload_indent);
		alert.setMessage(getString(R.string.term_n_cond));
		alert.setPositiveButton(getString(R.string.i_agree),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int whichButton) {
						ProgressBar progressBar = null;
						if(menuItem != null) {
							menuItem.setActionView(R.layout.progressbar);
//						ProgressBar progressBar = (ProgressBar) listView.getRootView().findViewById(R.id.indentUploadProgress);
//						progressBar.setVisibility(View.VISIBLE);
							progressBar = (ProgressBar) menuItem.getActionView().findViewById(R.id.menuitem_progress);
						}
						ServerSync serverSync = new ServerSync(getActivity());
						serverSync.uploadIndent(menuItem,indent, progressBar, indentItemsListFragment);
					}
				});

		alert.setNegativeButton(getString(R.string.no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int whichButton) {
						// Canceled.
					}
				});
		alert.show();
	}
}
