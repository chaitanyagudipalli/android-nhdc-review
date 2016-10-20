package in.vasista.vsales.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.supplier.SupplierPO;
import in.vasista.vsales.supplier.SupplierPOItem;


public class SupplierPOItemsAdapter extends ArrayAdapter<SupplierPOItem> {
	  int resource;

	  public SupplierPOItemsAdapter(Context context,
									int resource,
									List<SupplierPOItem> items) {
	    super(context, resource, items);
	    this.resource = resource;
	  }  

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout indentView;

		  SupplierPOItem item = getItem(position);

		  Log.v("asd",""+item.getStatus());


//	    String status = item.getStatus();
	    String poID = item.getPoid();

	   // boolean isgeneratedPO = item.isgeneratedPO();
	    String status = item.getStatus();


	    if (convertView == null) {
	    	indentView = new LinearLayout(getContext()); 
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, indentView, true);
	    } else {
	    	indentView = (LinearLayout) convertView;  
	    }     

	    TextView dateView = (TextView)indentView.findViewById(R.id.facilityRowId);
	    TextView subscriptionTypeView = (TextView)indentView.findViewById(R.id.facilityRowName);
	    TextView syncedView = (TextView)indentView.findViewById(R.id.facilityRowCategory);
		  TextView balView = (TextView)indentView.findViewById(R.id.facilityRowBal);

	    dateView.setText(item.getItemname());
	    subscriptionTypeView.setText(""+item.getItemQty());
	    syncedView.setText(""+item.getDispatchQty());
		  balView.setText(""+item.getBalanceQty());
	    return indentView;
	  }
}
