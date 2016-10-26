package in.vasista.vsales.supplier;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.HRDashboardActivity;
import in.vasista.vsales.ShipmentHistoryActivity;
import in.vasista.vsales.SupplierDetailsActivity;
import in.vasista.vsales.adapter.SupplierPOAdapter;
import in.vasista.vsales.adapter.SupplierPOItemsAdapter;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentItem;
import in.vasista.vsales.indent.IndentItemNHDC;
import in.vasista.vsales.sync.ServerSync;


public class SupplierPOItemListFragment extends ListFragment implements View.OnClickListener {
	public static final String module = SupplierPOItemListFragment.class.getName();

	SupplierPOItemsAdapter adapter;
	PODataSource datasource;
	List<SupplierPOItem> supplierItems;ListView listView;
	String partyId,orderId,supp_poid;
	boolean isEditableList =false;


	Map<String,String> po;

	final SupplierPOItemListFragment listFragment = this;

	SharedPreferences prefs;

	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);  

		listView = getListView();
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		if (listView.getHeaderViewsCount() == 0) {           
						
			View headerView2 = getActivity().getLayoutInflater().inflate(R.layout.supplierpoitem_header, null);
			headerView2.findViewById(R.id.shp_history).setOnClickListener(this);
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

//			supp_poid = getActivity().getIntent().getStringExtra("supp_poId");
//			orderId = getActivity().getIntent().getStringExtra("orderId");

			supp_poid = prefs.getString("SUPP_POID","");
			orderId =  prefs.getString("SUPP_ORDERID","");



			supplierItems  = datasource.getSuppPOItems(supp_poid);

			Log.d(module, "supplierItems.size() = " + supplierItems.size());
    	    
		    adapter = new SupplierPOItemsAdapter(getActivity(),
                    R.layout.supplierpoitem_list,
					supplierItems);
		}
		adapter.setEditable(isEditableList);
		setListAdapter(adapter);
		
		listView.setClickable(false);




	}

	public void uploadShipmentAction(final MenuItem menuItem){
		AlertDialog.Builder alert = new AlertDialog.Builder(
				getActivity());

		po = new HashMap<>();
		po.put("suppInvoiceDate",((EditText)getActivity().findViewById(R.id.suppInvDate)).getEditableText().toString());
		po.put("suppInvoiceId",((EditText)getActivity().findViewById(R.id.suppInvId)).getEditableText().toString());
		po.put("lrNumber",((EditText)getActivity().findViewById(R.id.lrNumber)).getEditableText().toString());
		po.put("lrDate",((EditText)getActivity().findViewById(R.id.lrDate)).getEditableText().toString());
		po.put("carrierName",((AutoCompleteTextView)getActivity().findViewById(R.id.courierName)).getText().toString());
		po.put("vehicleId",((EditText)getActivity().findViewById(R.id.vehicleNum)).getEditableText().toString());
		po.put("freightCharges",((EditText)getActivity().findViewById(R.id.flightCharges)).getEditableText().toString());
		po.put("remarks",((EditText)getActivity().findViewById(R.id.remarks)).getEditableText().toString());
		po.put("orderId",prefs.getString("SUPP_ORDERID",""));


		alert.setTitle("Upload goods?");
		alert.setPositiveButton("Ok",
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
						serverSync.uploadShipments(menuItem,supplierItems, po, progressBar, listFragment);
					}
				});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int whichButton) {
						// Canceled.
					}
				});
		alert.show();
	}

	/**
	 * Brute force update of list
	 */
	public void notifyChange() {
		setListAdapter(null);
	    datasource.open();
		supplierItems  = datasource.getSuppPOItems(prefs.getString("SUPP_POID",""));

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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.shp_history){
			Log.v("poid",""+prefs.getString("SUPP_POID",""));
			Intent i = new Intent(getActivity(), ShipmentHistoryActivity.class);
			i.putExtra("poid",""+prefs.getString("SUPP_POID",""));
			startActivity (i);

		}
	}

	void makeItemsEditable() {
		isEditableList = true;
		setListAdapter(null);
		listView.setClickable(true);
		adapter.setEditable(isEditableList);
		setListAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				final SupplierPOItem item = (SupplierPOItem) listView
						.getItemAtPosition(position);

				alert.setTitle("Enter dispatch quantity for " + item.getItemname());

				// Set an EditText view to get user input
				final EditText input = new EditText(getActivity());
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				//input.setFilters(new InputFilter[]{new CustomRangeInputFilter((int) item.getMinQ(),1000000)});
				String qtyStr = Integer.toString((int)item.getDispatchQty());
				if (item.getDispatchQty() == -1) {
					qtyStr = "";
				}
				input.setText(qtyStr);
				input.requestFocus();
				input.selectAll();
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {

							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
												int whichButton) {
								// Canceled.
							}
						});

				//alert.show();
				final AlertDialog a= alert.create();
				a.show();
				a.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Log.v("bbb","clcikc");
						String value = input.getText().toString();
						int newQty = -1;
						try {
							newQty = Integer.parseInt(value);
							if (newQty > item.getItemQty()-item.getDispatchQty())
								return;


						} catch (NumberFormatException e) {
							//
						}
						item.setQty(newQty);
						item.setBalanceQty(item.getItemQty()-item.getQty()-item.getDispatchQty());
						//item.setDispatchQty(newQty);
						adapter.notifyDataSetChanged();

						//Do stuff, possibly set wantToCloseDialog to true then...
						a.dismiss();
						//else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
					}
				});
			}
		});
		Toast.makeText(this.getActivity(),"Now you enabled edit mode, click on any item to edit.",Toast.LENGTH_LONG).show();
	}

	public void editShipmentAction(){
		isEditableList = true;
		makeItemsEditable();

	}
}
