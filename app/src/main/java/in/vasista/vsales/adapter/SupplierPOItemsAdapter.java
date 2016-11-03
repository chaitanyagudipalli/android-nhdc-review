package in.vasista.vsales.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.supplier.SupplierPO;
import in.vasista.vsales.supplier.SupplierPOItem;


public class SupplierPOItemsAdapter extends ArrayAdapter<SupplierPOItem> {
	  int resource;
	boolean isEditable = false;

	  public SupplierPOItemsAdapter(Context context,
									int resource,
									List<SupplierPOItem> items) {
	    super(context, resource, items);
	    this.resource = resource;
	  }
	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	  @SuppressLint("NewApi")
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
	    TextView dispView = (TextView)indentView.findViewById(R.id.facilityRowCategory);
		  TextView balView = (TextView)indentView.findViewById(R.id.facilityRowBal);
		  TextView qtyView = (TextView)indentView.findViewById(R.id.facilityRowQty);

	    dateView.setText(item.getItemname());
	    subscriptionTypeView.setText(""+item.getItemQty());
	    dispView.setText(""+item.getDispatchQty());
		  balView.setText(""+item.getBalanceQty());
		  qtyView.setText(""+item.getQty());

		  if (!isEditable) {
			  dispView.setFocusable(false);
			  int sdk = android.os.Build.VERSION.SDK_INT;
			  if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				  dispView.setBackgroundDrawable(null);
			  } else {
				  dispView.setBackground(null);
			  }
		  }
	    return indentView;
	  }
}
