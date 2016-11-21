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
import in.vasista.vsales.stocks.Stock;


public class StockAdapter extends ArrayAdapter<Stock> {
	int resource;
	public ArrayList<Stock> items = new ArrayList<Stock>();
	public ArrayList<Stock> filtered = new ArrayList<Stock>();
	//private Context context;
	private Filter filter;

	public StockAdapter(Context context, int resource, List<Stock> items)
	{
		super(context, resource, items);
		this.resource = resource;
		this.filtered.addAll(items);
		this.items.addAll(items);
		//this.context = context;
		this.filter = new StockAutoFilter();
	}

	@Override
	public int getCount() {
		return filtered.size();
	}

	@Override
	public Stock getItem(int position) {
		return filtered.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout StockView;

		Stock item = getItem(position);
		String shipid = item.getShipid();
		String prodid = item.getProdid();
		String prodName = item.getProdname();
		String prodQty = String.format("%.2f", item.getQty());
		String prodPrice = String.format("%.2f", item.getPrice());
		String shipId = item.getShipid();

		if (convertView == null) {
			StockView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(resource, StockView, true);
		} else {
			StockView = (LinearLayout) convertView;
		}

		TextView nameView = (TextView)StockView.findViewById(R.id.stock_product_name);
		TextView qtyView = (TextView)StockView.findViewById(R.id.stock_product_qty);
		TextView priceView = (TextView)StockView.findViewById(R.id.stock_product_price);


		nameView.setText(prodName);
		qtyView.setText(prodQty);
		priceView.setText(prodPrice);
		//routeView.setText(amRouteId + " " + pmRouteId);
		//phoneNumView.setText(phoneNum);
		return StockView;
	}


	@Override
	public Filter getFilter()
	{
		if(filter == null)
			filter = new StockAutoFilter();
		return filter;
	}

	private class StockAutoFilter extends Filter
	{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread, and
			// not the UI thread.
			FilterResults result = new FilterResults();
			if(constraint != null && constraint.toString().length() > 0)
			{
				ArrayList<Stock> filt = new ArrayList<Stock>();
				ArrayList<Stock> lItems = new ArrayList<Stock>();
				//synchronized (this)
				{
					lItems.addAll(items);
				}
				for(int i = 0, l = lItems.size(); i < l; i++)
				{
					Stock Stock = lItems.get(i);
					if (Stock.getProdname().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())) ||
							Stock.getDepot().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault()))	) {
						filt.add(Stock);
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
				filtered = (ArrayList<Stock>)results.values;
				clear();
				addAll(filtered);
			}
		}
	}
}
