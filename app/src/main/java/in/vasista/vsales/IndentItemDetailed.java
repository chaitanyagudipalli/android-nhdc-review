package in.vasista.vsales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import in.vasista.nhdc.R;
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

        //setPageTitle("Indent Item Details");

        getSupportActionBar().setHomeButtonEnabled(false);

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
        R.id.b_unit_price,R.id.total_mnt,R.id.remarks,};

        String[] values = {product.getName(),indentItem.getQuantity(),""+indentItem.getDiscountAmount(),""+indentItem.getOtherCharges(),
        indentItem.getBaleQuantity(), indentItem.getBundleWeight(),indentItem.getBundleUnitPrice(),indentItem.getTotalAmt(),
                indentItem.getRemarks(), ""+indentItem.getShippedQty()};

        for (int ij=0;ij<idlist.length;ij++){
            textView = (TextView) findViewById(idlist[ij]);
            textView.setText(values[ij]);
        }


    }
}
