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

import in.vasista.nhdcapp.R;
import in.vasista.vsales.payment.Payment;



public class PaymentAdapter extends ArrayAdapter<Payment> {
	  int resource;

	  public PaymentAdapter(Context context,
	                         int resource,
	                         List<Payment> items) {
	    super(context, resource, items);
	    this.resource = resource;                   
	  } 

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout paymentView;

	   Payment item = getItem(position);

	    String id = item.getId();
	    Date date = item.getDate();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
	    String dateStr = dateFormat.format(date);
	    String paymentMethod = item.getType();
	        
	    String amount_paid = String.format("%.2f", item.getAmount_paid());
		  String amount_balance = String.format("%.2f", item.getAmount_balance());


	    if (convertView == null) {
	    	paymentView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, paymentView, true);
	    } else {
	    	paymentView = (LinearLayout) convertView;
	    }     

	    TextView idView = (TextView)paymentView.findViewById(R.id.paymentRowId);
	    TextView dateView = (TextView)paymentView.findViewById(R.id.paymentRowDate);
	    TextView pymentMethodView = (TextView)paymentView.findViewById(R.id.paymentRowPaymentMethod);	    
	    TextView amountView = (TextView)paymentView.findViewById(R.id.paymentRowAmount);
	    
	    idView.setText(id);
	    dateView.setText(dateStr);
	    pymentMethodView.setText(paymentMethod);
	    amountView.setText(amount_paid);
	    return paymentView;
	  }
}
