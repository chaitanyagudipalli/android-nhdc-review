package in.vasista.vsales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.supplier.SupplierPOShip;

/**
 * Created by upendra on 23/10/16.
 */
public class ShipHistoryBaseAdapter extends BaseAdapter {

    List<SupplierPOShip> myList = new ArrayList<SupplierPOShip>();
    LayoutInflater inflater;
    Context context;


    public ShipHistoryBaseAdapter(Context context, List<SupplierPOShip> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public SupplierPOShip getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ship_history_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        SupplierPOShip supplierPOShip = getItem(position);
        mViewHolder.dispatch.setText("Dispatch "+supplierPOShip.getItemSeqId());
        mViewHolder.itemname.setText(supplierPOShip.getItemname());
        mViewHolder.itemqty.setText(String.format("%.2f", supplierPOShip.getQty()));
        mViewHolder.amnt.setText(String.format("%.2f", supplierPOShip.getItemAmnt()));
        mViewHolder.price.setText(String.format("%.2f", supplierPOShip.getUnitPrice()));


        return convertView;
    }

    private class MyViewHolder {
        TextView dispatch, itemname, itemqty, price, amnt;


        public MyViewHolder(View item) {
            dispatch = (TextView) item.findViewById(R.id.dispatch);
            itemname = (TextView) item.findViewById(R.id.itemname);
            itemqty = (TextView) item.findViewById(R.id.itemqty);
            price = (TextView) item.findViewById(R.id.price);
            amnt = (TextView) item.findViewById(R.id.amnt);
        }
    }
}
