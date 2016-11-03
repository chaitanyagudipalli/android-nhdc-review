package in.vasista.vsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.transporter.Transporter;


public class TransporterAdapter extends ArrayAdapter<Transporter> {
	  int resource;
	  public ArrayList<Transporter> items = new ArrayList<Transporter>();
	public ArrayList<Transporter> filtered = new ArrayList<Transporter>();
	//private Context context;
	    private Filter filter;

	    public TransporterAdapter(Context context, int resource, List<Transporter> items)
	    {
	        super(context, resource, items);
		    this.resource = resource;	        
	        this.filtered.addAll(items);
	        this.items.addAll(items);
	        //this.context = context;
	        this.filter = new TransporterAutoFilter();
	    }
	    
	    @Override
	    public int getCount() {
	        return filtered.size();
	    }
	 
	    @Override
	    public Transporter getItem(int position) {
	        return filtered.get(position);
	    }	    

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent)
	    {
		    LinearLayout TransporterView;

		    Transporter item = getItem(position);
		    String id = item.getId();
		    String name = item.getName();
		    String category = item.getPhone();
//		    String phoneNum = item.getOwnerPhone();
//		    String amRouteId = item.getAmRouteId();
//		    String pmRouteId = item.getPmRouteId();
		    

		    if (convertView == null) { 
		    	TransporterView = new LinearLayout(getContext());
		      String inflater = Context.LAYOUT_INFLATER_SERVICE;
		      LayoutInflater li;
		      li = (LayoutInflater)getContext().getSystemService(inflater);
		      li.inflate(resource, TransporterView, true);
		    } else {
		    	TransporterView = (LinearLayout) convertView;
		    }

		    TextView idView = (TextView)TransporterView.findViewById(R.id.transporterRowId);
		    TextView nameView = (TextView)TransporterView.findViewById(R.id.transporterRowName);
		    TextView categoryView = (TextView)TransporterView.findViewById(R.id.transporterRowCategory);
		    //TextView routeView = (TextView)TransporterView.findViewById(R.id.TransporterRowRoute);
		    //TextView phoneNumView = (TextView)TransporterView.findViewById(R.id.TransporterRowPhone);
		    

		    idView.setText(id);
		    nameView.setText(name);
		    categoryView.setText(category);
		    //routeView.setText(amRouteId + " " + pmRouteId);
		    //phoneNumView.setText(phoneNum);
		    return TransporterView;
	    }


	    @Override
	    public Filter getFilter()
	    {
	        if(filter == null)
	            filter = new TransporterAutoFilter();
	        return filter;
	    }

	    private class TransporterAutoFilter extends Filter
	    {

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            // NOTE: this function is *always* called from a background thread, and
	            // not the UI thread.
	            FilterResults result = new FilterResults();
	            if(constraint != null && constraint.toString().length() > 0)
	            {
	                ArrayList<Transporter> filt = new ArrayList<Transporter>();
	                ArrayList<Transporter> lItems = new ArrayList<Transporter>();
	                //synchronized (this)
	                {
	                    lItems.addAll(items);
	                }
	                for(int i = 0, l = lItems.size(); i < l; i++)
	                {
	                	Transporter Transporter = lItems.get(i);
    	                if (Transporter.getId().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) ||
    	                		Transporter.getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))	) {
    	                	filt.add(Transporter); 
    	                }
	                }  
	                result.count = filt.size();
	                result.values = filt;
	            }
	            else
	            {
	                //synchronized(this)
	                {
	                    result.values = items;
	                    result.count = items.size();
	                }
	            }
	            return result;
	        }

	        @SuppressWarnings("unchecked")
	        @Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	            // NOTE: this function is *always* called from the UI thread.
                if (results != null && results.count > 0) {
                	filtered = (ArrayList<Transporter>)results.values;
                	clear();
                	addAll(filtered);
                }
	        }
	    }
}
