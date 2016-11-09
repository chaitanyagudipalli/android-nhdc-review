package in.vasista.vsales;

import android.os.Bundle;
import android.widget.TextView;

import in.vasista.nhdcapp.R;

/**
 * Created by upendra on 4/11/16.
 */
public class SupplierPOItemDetailedActivity extends DashboardAppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate your view
        setContentChildView(R.layout.activity_supp_poitem_detailed);
        actionBarHomeEnabled();

        //String po_id = getIntent().getStringExtra("po_id");
        //String prod_id = getIntent().getStringExtra("prod_id");

        int[] ids = {R.id.product_name,R.id.specification,R.id.unitprice,R.id.indentqty,R.id.dispatchedqty,R.id.balanaceqty};
        String[] values = {getIntent().getStringExtra("itemname"),
                getIntent().getStringExtra("spec"),
                getIntent().getStringExtra("unitprice"),
                getIntent().getStringExtra("itemq"),
                getIntent().getStringExtra("disp"),
                getIntent().getStringExtra("balance")};

        setPageTitle(getIntent().getStringExtra("prod_id") +" : Product details");

        TextView textView;
        for (int i=0 ; i < ids.length; i++){
            textView = (TextView) findViewById(ids[i]);
            textView.setText(values[i]);
        }


    }
}
