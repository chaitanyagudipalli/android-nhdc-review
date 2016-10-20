package in.vasista.vsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.supplier.SupplierPO;


public class SupplierPOAdapter extends ArrayAdapter<SupplierPO> {
	  int resource;

	  public SupplierPOAdapter(Context context,
							   int resource,
							   List<SupplierPO> items) {
	    super(context, resource, items);
	    this.resource = resource;
	  }  

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout indentView;

		  SupplierPO item = getItem(position);


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

	    TextView dateView = (TextView)indentView.findViewById(R.id.indentRowDate);
	    TextView subscriptionTypeView = (TextView)indentView.findViewById(R.id.indentRowSupply);	    
	    TextView syncedView = (TextView)indentView.findViewById(R.id.indentRowSynced);

	    dateView.setText(item.getPoid());
	    subscriptionTypeView.setText(poID);
	    syncedView.setText(status);
	    return indentView;
	  }
}
