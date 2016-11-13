package in.vasista.vsales;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.adapter.IndentShipHistoryBaseAdapter;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.supplier.SupplierPOShip;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

/**
 * Created by upendra on 14/11/16.
 */
public class IndentShipmentActivity extends DashboardAppCompatActivity {

    Object weaverDet;
    SharedPreferences prefs;
    XMLRPCApacheAdapter adapter = null;
    Map paramMap;String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_shipments);

        setPageTitle("Shipment History");

        orderId = getIntent().getStringExtra("orderId");
        orderId = "WS129270";


        paramMap = new HashMap();
        paramMap.put("orderId",orderId);
        try {
            adapter = new XMLRPCApacheAdapter(getBaseContext());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new LoadViewTask().execute();





    }

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        List<SupplierPOShip> supplierPOShips;
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {

        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
                supplierPOShips = new ArrayList<SupplierPOShip>();
                Object weaverDet = adapter.callSync("getIndentShipments", paramMap);

                Map shipmentHistory = (Map) ((Map) weaverDet).get("shipments");
                if (shipmentHistory != null) {
                    for (Object ship_key : shipmentHistory.keySet()) {

                        Map shipment_details = (Map) shipmentHistory.get(ship_key);

                        Object[] shipment = (Object[]) shipment_details.get("shipmentItems");
                        if (shipment.length > 0) {

                            for (int i = 0; i < shipment.length; i++) {
                                Map shipitem = (Map) shipment[i];
                                Log.v("sh", "" + ship_key.toString() + " for " + i);

                                SupplierPOShip supplierPOShip = new SupplierPOShip(ship_key.toString(), orderId,
                                        (String) shipitem.get("productId"),
                                        (String) shipitem.get("itemName"), (String) shipitem.get("orderItemSeqId"),
                                        ((BigDecimal) shipitem.get("quantity")).floatValue(), ((BigDecimal) shipitem.get("unitPrice")).floatValue(),
                                        ((BigDecimal) shipitem.get("itemAmount")).floatValue(),
                                        (String) shipment_details.get("customer"), (String) shipment_details.get("destination"));
                                supplierPOShips.add(supplierPOShip);
                            }
//
                        }
                    }
                }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ListView lv = (ListView) findViewById(R.id.ship_history_list);
            lv.setAdapter(new IndentShipHistoryBaseAdapter(IndentShipmentActivity.this,supplierPOShips));
        }

    }


}