package in.vasista.vsales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.indent.Indent;

public class IndentDetailed extends DashboardAppCompatActivity {

    IndentsDataSource datasource;
    TextView textView;
    Indent indent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_detailed);
        //setSalesDashboardTitle(R.string.title_feature1_plurer);
        setPageTitle(getString(R.string.indent_details));

        int[] ids = { R.id.order_date, R.id.order_number,R.id.order_total,
        R.id.supplier_name,R.id.generated_po,R.id.po_sequence_no,R.id.status_id,R.id.balance_ammount,R.id.paid_ammount};
        Intent facilityDetailsIntent = getIntent();
        int indentId = facilityDetailsIntent.getIntExtra("indentId",0);
        datasource = new IndentsDataSource(this);
        datasource.open();
        indent = datasource.getIndentDetails(indentId);
        datasource.close();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String[] values = {dateFormat.format(indent.getOrderDate()),indent.getOrderNo(), ""+ indent.getOrderTotal(),
        indent.getSupplierPartyName(),(indent.isgeneratedPO())?"YES":"NO",indent.getPoSquenceNo(),indent.getStatusId(),""+indent.getBalance(),""+indent.getPaidAmt()};

        for (int i=0;i<ids.length;i++){
            textView = (TextView) findViewById(ids[i]);
            textView.setText(values[i]);
        }

    }
}
