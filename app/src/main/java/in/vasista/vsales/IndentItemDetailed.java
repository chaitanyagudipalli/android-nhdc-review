package in.vasista.vsales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.indent.IndentItemNHDC;

public class IndentItemDetailed extends DashboardAppCompatActivity {

    IndentItemNHDC indentItem;
    Product product;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_item_detailed);

        setPageTitle(getString(R.string.indent_item_details));

        //getSupportActionBar().setHomeButtonEnabled(false);

        final Intent i = getIntent();
        int indentitem_id = i.getIntExtra("indentitem_id",0);


        IndentsDataSource ids = new IndentsDataSource(this);
        ids.open();
        indentItem = ids.getIndentItemDetails(indentitem_id);
        ids.close();

        ProductsDataSource pds = new ProductsDataSource(this);
        pds.open();
        product = pds.getproductDetails(Integer.parseInt(indentItem.getProductId()));
        pds.close();

        int[] idlist = { R.id.product_name, R.id.quantity,R.id.disc_amnt, R.id.other_charges, R.id.quantitynos, R.id.b_wt,
        R.id.b_unit_price,R.id.total_mnt,R.id.remarks,R.id.shipped_qty,R.id.specification};
        String uom = "";
        if(indentItem.getYarnUOM().equalsIgnoreCase("")){
            uom = "";
        }else{
            uom = " ("+indentItem.getYarnUOM()+")";
        }

        String[] values = {product.getName(),indentItem.getQuantity(),
                this.getResources().getString(R.string.Rs)+" "+new BigDecimal(indentItem.getDiscountAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                this.getResources().getString(R.string.Rs)+" "+new BigDecimal(indentItem.getOtherCharges()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                indentItem.getBaleQuantity() + uom, indentItem.getBundleWeight(),
                this.getResources().getString(R.string.Rs)+" "+String.valueOf(new BigDecimal(indentItem.getBundleUnitPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue()),
                this.getResources().getString(R.string.Rs)+" "+indentItem.getTotalAmt(),
                indentItem.getRemarks(),
                ""+indentItem.getShippedQty(), indentItem.getRemarks()};

        for (int ij=0;ij<idlist.length;ij++){
            textView = (TextView) findViewById(idlist[ij]);
            textView.setText(values[ij]);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }
}
