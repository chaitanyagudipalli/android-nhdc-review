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

import in.vasista.nhdc.R;
import in.vasista.vsales.supplier.Supplier;


public class SupplierAdapter extends ArrayAdapter<Supplier> {
	  int resource;
	  public ArrayList<Supplier> items = new ArrayList<Supplier>();
	public ArrayList<Supplier> filtered = new ArrayList<Supplier>();
	//private Context context;
	    private Filter filter;

	    public SupplierAdapter(Context context, int resource, List<Supplier> items)
	    {
	        super(context, resource, items);
		    this.resource = resource;	        
	        this.filtered.addAll(items);
	        this.items.addAll(items);
	        //this.context = context;
	        this.filter = new FacilityAutoFilter();
	    }
	    
	    @Override
	    public int getCount() {
	        return filtered.size();
	    }
	 
	    @Override
	    public Supplier getItem(int position) {
	        return filtered.get(position);
	    }	    

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent)
	    {
		    LinearLayout facilityView;

			Supplier item = getItem(position);
		    String id = item.getId();
		    String name = item.getName();
		    String roletypeid = item.getRoletypeid();
			String partytypeid = item.getPartytypeid();
		    

		    if (convertView == null) { 
		    	facilityView = new LinearLayout(getContext());
		      String inflater = Context.LAYOUT_INFLATER_SERVICE;
		      LayoutInflater li;
		      li = (LayoutInflater)getContext().getSystemService(inflater);
		      li.inflate(resource, facilityView, true);
		    } else {
		    	facilityView = (LinearLayout) convertView;
		    }

		    TextView idView = (TextView)facilityView.findViewById(R.id.facilityRowId);
		    TextView nameView = (TextView)facilityView.findViewById(R.id.facilityRowName);	    
		    TextView categoryView = (TextView)facilityView.findViewById(R.id.facilityRowCategory);

		    

		    idView.setText(id);
		    nameView.setText(name);
		    categoryView.setText(roletypeid);

		    return facilityView;
	    }


	    @Override
	    public Filter getFilter()
	    {
	        if(filter == null)
	            filter = new FacilityAutoFilter();
	        return filter;
	    }

	    private class FacilityAutoFilter extends Filter
	    {

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            // NOTE: this function is *always* called from a background thread, and
	            // not the UI thread.
	            FilterResults result = new FilterResults();
	            if(constraint != null && constraint.toString().length() > 0)
	            {
	                ArrayList<Supplier> filt = new ArrayList<Supplier>();
	                ArrayList<Supplier> lItems = new ArrayList<Supplier>();
	                //synchronized (this)
	                {
	                    lItems.addAll(items);
	                }
	                for(int i = 0, l = lItems.size(); i < l; i++)
	                {
						Supplier facility = lItems.get(i);
    	                if (facility.getId().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) || (facility.getName() != null && facility.getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())))) {
    	                	filt.add(facility); 
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
                	filtered = (ArrayList<Supplier>)results.values;
                	clear();
                	addAll(filtered);
                }
	        }
	    }
}
