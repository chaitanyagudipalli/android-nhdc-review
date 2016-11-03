package in.vasista.vsales.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.catalog.Product;


public class ProductAdapter extends ArrayAdapter<Product> implements Filterable{
	  int resource;

	private Filter filter;
	public ArrayList<Product> items = new ArrayList<>();
	public ArrayList<Product> filtered = new ArrayList<>();
	  public ProductAdapter(Context context,
	                         int resource,
	                         List<Product> items) {
	    super(context, resource, items);
	    this.resource = resource;
		  this.filtered.addAll(items);
		  this.items.addAll(items);
		  this.filter = new ProductAutoFilter();
	  }
	@Override
	public int getCount() {
		return filtered.size();
	}

	@Override
	public Product getItem(int position) {
		return filtered.get(position);
	}
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout productView;

	    Product item = getItem(position);

	    String name = item.getName();
	    String description = item.getDescription();	    
	    //String price = String.format("%.2f", item.getPrice()) ;
		  String category = item.getProductCategoryId();


	    if (convertView == null) { 
	      productView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, productView, true);
	    } else {
	    	productView = (LinearLayout) convertView;
	    }

	    TextView nameView = (TextView)productView.findViewById(R.id.rowName);
	    TextView descriptionView = (TextView)productView.findViewById(R.id.rowDescription);	    
	    TextView priceView = (TextView)productView.findViewById(R.id.category);
//	    TextView mrpPriceView = (TextView)productView.findViewById(R.id.rowMrpPrice);
//	    TextView cratePriceView = (TextView)productView.findViewById(R.id.rowCratePrice);

	    nameView.setText(name);
	    descriptionView.setText(description);
	    priceView.setText(category);
//		  cratePriceView.setText(typeid);
	    return productView;
	  }

	@Override
	public Filter getFilter()
	{
		if(filter == null)
			filter = new ProductAutoFilter();
		return filter;
	}

	/**
	 * Filter - Search
	 */
	private class ProductAutoFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread, and
			// not the UI thread.
			FilterResults result = new FilterResults();
			if(constraint != null && constraint.toString().length() > 0)
			{
				ArrayList<Product> filt = new ArrayList<>();
				ArrayList<Product> lItems = new ArrayList<>();
				//synchronized (this)
				{
					lItems.addAll(items);
				}
				for(int i = 0, l = lItems.size(); i < l; i++)
				{
					Product product = lItems.get(i);
					if (product.getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) ||
							product.getDescription().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))	) {
						filt.add(product);
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
				filtered = (ArrayList<Product>)results.values;
				clear();
				addAll(filtered);
			}
		}
	}
}
