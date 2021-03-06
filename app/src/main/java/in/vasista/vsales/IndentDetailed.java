package in.vasista.vsales;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.vasista.atom.AtomActivity;
import in.vasista.nhdcapp.R;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentItemNHDC;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.transporter.Transporter;

public class IndentDetailed extends DashboardAppCompatActivity {

    IndentsDataSource datasource;TransporterDataSource tds;
    TextView textView;
    Indent indent;
    List<HashMap> list;
    static int indentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_detailed);
        //setSalesDashboardTitle(R.string.title_feature1_plurer);
        setPageTitle(getString(R.string.indent_details));
        list = new ArrayList<>();
        int[] ids = { R.id.order_date, R.id.order_number,R.id.order_total,
        R.id.supplier_name,R.id.generated_po,R.id.po_sequence_no,R.id.status_id,R.id.balance_ammount,
                R.id.paid_ammount,R.id.disc_amnt,R.id.t_name,R.id.scheme};
        Intent facilityDetailsIntent = getIntent();
        indentId = facilityDetailsIntent.getIntExtra("indentId",0);
        datasource = new IndentsDataSource(this);
        datasource.open();
        indent = datasource.getIndentDetails(indentId);
        datasource.close();
//        Transporter t = null;
//        if(indent.gettId().equalsIgnoreCase("")) {
//            tds = new TransporterDataSource(this);
//            tds.open();
//            t = tds.getTransporterDetails(indent.gettId());
//            tds.close();
//        }
//        String t_name = "";
//        if (t != null){
//            t_name = t.getName();
//        }


try {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
    String[] values = {dateFormat.format(indent.getOrderDate()), indent.getOrderNo(),
            this.getResources().getString(R.string.Rs) + " " + new BigDecimal(indent.getOrderTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
            indent.getSupplierPartyName(), (indent.isgeneratedPO()) ? "YES" : "NO", indent.getPoSquenceNo(), indent.getStatusId()
            , this.getResources().getString(R.string.Rs) + " " + new BigDecimal(indent.getBalance()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
            this.getResources().getString(R.string.Rs) + " " + new BigDecimal(indent.getPaidAmt()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
            this.getResources().getString(R.string.Rs) + " " + new BigDecimal(indent.getTotDiscountAmt()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
    indent.gettId(),indent.getSchemeType()};


    if (!indent.isgeneratedPO()){
        findViewById(R.id.po_seq_row).setVisibility(View.GONE);
    }
    for (int i = 0; i < ids.length; i++) {
        textView = (TextView) findViewById(ids[i]);
        textView.setText(values[i]);
    }

    if (indent.getStatusId().equalsIgnoreCase("Not Uploaded")) {
        editMode = true;
        invalidateOptionsMenu();
    } else if (indent.getStatusId().equalsIgnoreCase("Indent Received") || indent.getStatusId().equalsIgnoreCase("Created")) {
        deletemode = true;
        invalidateOptionsMenu();
    }
}catch (NullPointerException e){

}

        ((Button)findViewById(R.id.gotoShipments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("orderid",""+indent.getOrderId());
                Intent intent = new Intent(getBaseContext(),IndentShipmentActivity.class);
                intent.putExtra("orderId",indent.getOrderId());
                startActivity(intent);
            }
        });

    }


    boolean editMode = false,deletemode = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.indent_menu, menu);
        menu.findItem(R.id.action_indent_delete).setVisible(false);
        menu.findItem(R.id.action_indent_upload).setVisible(false);
        menu.findItem(R.id.action_indent_cancel).setVisible(false);
        if (editMode || deletemode){
            if(editMode) {
                menu.findItem(R.id.action_indent_upload).setVisible(true);
                menu.findItem(R.id.action_indent_delete).setVisible(true);
            }else{
                menu.findItem(R.id.action_indent_cancel).setVisible(true);
            }
        }else{
            //menu.findItem(R.id.action_indent_done).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.action_indent_upload:
                editMode = false;
                invalidateOptionsMenu();
                //indentItemsListFragment.uploadIndentAction(item);
                datasource.open();
                List<IndentItemNHDC> indentItems = datasource.getIndentItems(indentId);
                datasource.close();
                for (int i=0;i<indentItems.size();i++) {
                    IndentItemNHDC indentItemNHDC=indentItems.get(i);
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("productId", indentItemNHDC.getProductId());
                    hm.put("quantity", indentItemNHDC.getQuantity());
                    hm.put("remarks", indentItemNHDC.getRemarks());
                    hm.put("baleQuantity", indentItemNHDC.getBaleQuantity());
                    hm.put("bundleWeight", indentItemNHDC.getBundleWeight());
                    hm.put("bundleUnitPrice", indentItemNHDC.getBundleUnitPrice());
                    hm.put("yarnUOM", indentItemNHDC.getYarnUOM());
                    hm.put("basicPrice", indentItemNHDC.getBasicPrice());
                    hm.put("serviceCharge", indentItemNHDC.getServiceCharge());
                    hm.put("serviceChargeAmt", indentItemNHDC.getServiceChargeAmt());


                    list.add(hm);
                }
                uploadIndentAction(item);


                return true;
            case R.id.action_indent_delete:
                datasource.open();
                datasource.deleteIndent(indentId);
                datasource.close();
                finish();
                return true;
            case R.id.action_indent_cancel:
                cancelIndentAction(item);
                return true;
            case R.id.action_indent_payment:
                //startActivity(new Intent(getApplicationContext(), PayumoneyActivity.class));
                Intent i = new Intent(new Intent(getApplicationContext(),AtomActivity.class));
                i.putExtra("orderId",indent.getOrderId());
                i.putExtra("amount",((float) indent.getBalance()));
                if(Double.compare(indent.getBalance(), Double.valueOf(0.0)) > 0 ){
                    startActivity(i);
                }

                return true;
        }
        return false;
    }

    public void cancelIndentAction(final MenuItem menuItem){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                IndentDetailed.this);
        alert.setTitle(R.string.cancel_indent);
        alert.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        ProgressBar progressBar = null;
                        if(menuItem != null) {
                            menuItem.setActionView(R.layout.progressbar);
//						ProgressBar progressBar = (ProgressBar) listView.getRootView().findViewById(R.id.indentUploadProgress);
//						progressBar.setVisibility(View.VISIBLE);
                            progressBar = (ProgressBar) menuItem.getActionView().findViewById(R.id.menuitem_progress);
                        }
                        ServerSync serverSync = new ServerSync(IndentDetailed.this);
                        serverSync.cancelIndent(menuItem,null,indent.getOrderId(),IndentDetailed.this);

                    }
                });

        alert.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // Canceled.

                    }
                });
        alert.show();
    }

    public void navtolistOfIndents(){
        Intent i = new Intent(this,IndentActivity.class);
        i.putExtra("indent_refresh",true);
        startActivity(i);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void uploadIndentAction(final MenuItem menuItem){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                IndentDetailed.this);
        alert.setTitle(R.string.upload_indent);
        alert.setMessage(getString(R.string.term_n_cond));
        alert.setPositiveButton(getString(R.string.i_agree),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        ProgressBar progressBar = null;
                        if(menuItem != null) {
                            menuItem.setActionView(R.layout.progressbar);
//						ProgressBar progressBar = (ProgressBar) listView.getRootView().findViewById(R.id.indentUploadProgress);
//						progressBar.setVisibility(View.VISIBLE);
                            progressBar = (ProgressBar) menuItem.getActionView().findViewById(R.id.menuitem_progress);
                        }
                        ServerSync serverSync = new ServerSync(IndentDetailed.this);
                        serverSync.uploadNHDCIndent(menuItem, null, list,indent.getSupplierPartyId(),indent.gettId(),indent.getSchemeType(), indentId, indent.getProdstoreid());
                    }
                });

        alert.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // Canceled.
                        editMode=true;
                        invalidateOptionsMenu();
                    }
                });
        alert.show();
    }
}
