package in.vasista.vsales.adapter;


import android.annotation.SuppressLint;
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
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.indent.IndentItem;
import in.vasista.vsales.indent.IndentItemNHDC;


public class IndentItemAdapter extends ArrayAdapter<IndentItemNHDC>{
	public static final String module = IndentItemAdapter.class.getName();
	
	  int resource;
	  boolean isEditable = false;
	Context c;
	  List<IndentItemNHDC> myItems;
	  public IndentItemAdapter(Context context,
	                         int resource,
	                         List<IndentItemNHDC> items) {
	    super(context, resource, items);
	    this.myItems = items;
		  this.c = context;
	    this.resource = resource;
	  }

	  public boolean isEditable() {
		  return isEditable;
	  }

		public void setEditable(boolean isEditable) {
			this.isEditable = isEditable;
		}
		
	  @SuppressLint("NewApi") @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout indentItemView;

		  IndentItemNHDC item = getItem(position);

//	    String productId = item.getProductId();
		  ProductsDataSource productsDataSource = new ProductsDataSource(c);
		  productsDataSource.open();
		  Product p = productsDataSource.getproductDetails(Integer.parseInt(item.getProductId()));
	    String productName = p.getName();
	    String qty = item.getQuantity();
	    String unitPrice = item.getBundleUnitPrice();

		  Log.v("jj",""+unitPrice);
		//double itemTotal = Math.round(item.getQty()*unitPrice * 100.0) / 100.0;
	    //String amount = String.format("%.2f", itemTotal);
//	    if(item.getQty() == -1) {
//	    	qty = "";
//	    	amount = "";
//	    }
		//Log.d( module, "item=" + item); 		  

	    if (convertView == null) {
	      indentItemView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, indentItemView, true);
	    } else {
	    	indentItemView = (LinearLayout) convertView;
	    }

	    TextView nameView = (TextView)indentItemView.findViewById(R.id.indentitemRowProductName);
	    final TextView qtyView = (TextView)indentItemView.findViewById(R.id.indentitemRowQty);
	    final TextView amountView = (TextView)indentItemView.findViewById(R.id.indentitemRowAmount);

	    nameView.setText(productName);
	    qtyView.setText(qty);
		  if(item.getBasicPrice().equalsIgnoreCase(""))
		  		amountView.setText(unitPrice);
		  else
			  amountView.setText(item.getBasicPrice());
	    
	    if (!isEditable)  {
	    	qtyView.setFocusable(false);
	    	int sdk = android.os.Build.VERSION.SDK_INT;
	    	if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	    		qtyView.setBackgroundDrawable(null); 
	    	} else {
	    		qtyView.setBackground(null);
	    	}
	    }
        
	    //amountView.setText(item.getBasicPrice());
	    return indentItemView;
	  }
}

