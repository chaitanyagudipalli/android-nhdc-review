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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.vasista.nhdc.R;
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
    int indentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_detailed);
        //setSalesDashboardTitle(R.string.title_feature1_plurer);
        setPageTitle(getString(R.string.indent_details));
        list = new ArrayList<>();
        int[] ids = { R.id.order_date, R.id.order_number,R.id.order_total,
        R.id.supplier_name,R.id.generated_po,R.id.po_sequence_no,R.id.status_id,R.id.balance_ammount,
                R.id.paid_ammount,R.id.disc_amnt,R.id.t_name};
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
    indent.gettId()};

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
    } else if (indent.getStatusId().equalsIgnoreCase("CREATED")) {
        deletemode = true;
        invalidateOptionsMenu();
    }
}catch (NullPointerException e){

}

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
        }
        return false;
    }

    public void cancelIndentAction(final MenuItem menuItem){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                IndentDetailed.this);
        alert.setTitle("Cancel Indent?");
        alert.setPositiveButton("Ok",
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

        alert.setNegativeButton("Cancel",
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
        alert.setTitle("Upload Indent?");
        alert.setMessage("Sample terms:\n" +
                "\n" +
                "These terms and conditions create a contract between you and NHDC (the “Agreement”). Please read the Agreement carefully. To confirm your understanding and acceptance of the Agreement, click “Agree.”\n\n" +
                "A. INTRODUCTION TO OUR SERVICES\n" +
                "This Agreement governs your use of NHDC’s services (“Services”), through which you can place indents.\n" +
                "All Transactions are considered final from your end based on which NHDC shall process indents based on feasibility subject to availability and other risks. Prices indicated for the indent may change at any time. If technical problems prevent or unreasonably delay delivery your exclusive and sole remedy is either replacement of the indent or refund of the amount paid, as determined by NHDC. From time to time, NHDC may refuse a refund request if we find evidence of fraud, refund abuse, or other manipulative behavior that entitles NHDC to a corresponding counterclaim.\n" +
                "You are a registered user of NHDC mobile application and you are of age 18 or above to create an NHDC indent and use our Services.\n\n" +
                "CONTENT AND SERVICE AVAILABILITY\n" +
                "Terms found in this Agreement that relate to Services are subject to other applicable laws governing NHDC operations.\n\n" +
                "TERMINATION AND SUSPENSION OF SERVICES\n" +
                "If you fail, or NHDC suspects that you have failed, to comply with any of the provisions of this Agreement, NHDC may, without notice to you: (i) terminate services offered, and you will remain liable for all amounts due under your NHDC up to and including the date of termination; and/or (ii) preclude your access to the Services.\n" +
                "NHDC further reserves the right to modify, suspend, or discontinue the Services (or any part or Content thereof) at any time with or without notice to you, and NHDC will not be liable to you or to any third party should it exercise such rights\n\n" +
                "GOVERNING LAW\n" +
                "Except to the extent expressly provided in the following paragraph, this Agreement and the relationship between you and NHDC, and all Transactions on the Services shall be governed by the laws");
        alert.setPositiveButton("I Agree",
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

        alert.setNegativeButton("No",
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
