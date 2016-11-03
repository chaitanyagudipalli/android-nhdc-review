package in.vasista.vsales;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.adapter.ShipHistoryBaseAdapter;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.supplier.SupplierPOShip;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class ShipmentHistoryActivity extends DashboardAppCompatActivity {

    Object weaverDet;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_supp_shipment_history);

        setPageTitle("Shipment History");

        String poid = getIntent().getStringExtra("poid");


        PODataSource poDataSource = new PODataSource(getApplicationContext());
        poDataSource.open();
        List<SupplierPOShip> supplierPOShips=poDataSource.getAllSuppShips(poid);
        poDataSource.close();

        Log.v("Shipment History",""+supplierPOShips.size());

        ListView lv = (ListView) findViewById(R.id.ship_history_list);
        lv.setAdapter(new ShipHistoryBaseAdapter(this,supplierPOShips));

    }


}
