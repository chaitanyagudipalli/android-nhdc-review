package in.vasista.vsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.transporter.Transporter;


public class TransporterAutoAdapter extends ArrayAdapter<Transporter> {
	  public ArrayList<Transporter> items = new ArrayList<Transporter>();
	public ArrayList<Transporter> filtered = new ArrayList<Transporter>();
	private Context context;
	    private Filter filter;

	    public TransporterAutoAdapter(Context context, int textViewResourceId, List<Transporter> items)
	    {
	        super(context, textViewResourceId, items);
	        this.filtered.addAll(items);
	        this.items.addAll(items);
	        this.context = context;
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
	        View facilityView = convertView;
	        if(facilityView == null)
	        {
	            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            facilityView = vi.inflate(R.layout.autocomplete_item, null);
	        }

			Transporter facility = getItem(position);
	        if(facility != null)
	        {
	    	    TextView tview = (TextView)facilityView.findViewById(R.id.facilityAutocompleteLabel);
	    	    tview.setText(facility.getId() + " [" + facility.getName() + "]");
				tview.setMaxLines(4);
	        }
	        return facilityView;
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
						Transporter facility = lItems.get(i);
						if(facility.getName() == null || facility.getName() == "null")
							continue;

    	                if (facility.getId().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) ||
    	                		facility.getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))	) {
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
                	filtered = (ArrayList<Transporter>)results.values;
                	clear();
                	addAll(filtered);
                }
	        }
	    }
}
