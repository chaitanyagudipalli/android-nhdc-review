package in.vasista.vsales.indent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.adapter.SupplierAutoAdapter;
import in.vasista.vsales.adapter.TransporterAutoAdapter;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.transporter.Transporter;

public class IndentCreationActivity extends DashboardAppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner category,scheme,uom;

    private static final String[]paths = {"Silk", "Cotton", "Other"};
    private static final String[]schemes = {"MGPS + 10%", "MGPS", "General"};
    List<HashMap> list;
    Button addIndent,submitindent;

    String supplierPartyId = "", tId = "",schemeType = "", category_type = "",billingType = "Direct", supplierName = "", prodStoreId = "";
    long indent_id =0;

    FloatingActionButton fab;

    Spinner branches;
    ArrayList<String> branchids;
    String[] branch_ids;

    IndentsDataSource datasource;
    SharedPreferences.Editor prefEditor;

    TextView indentTotal,selectSupplier;
    Object weaverDet;
    SharedPreferences prefs;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_creation);
        actionBarHomeEnabled();
        //setPageTitle("Create Indent");
        submitindent= (Button)findViewById(R.id.submitindent);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        //fab.setImageResource(R.drawable.title_upload);
        new LoadViewTask().execute();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefEditor = prefs.edit();

        branchids = new ArrayList<>();
        prefEditor.putInt("IndentId",0);
        prefEditor.apply();

        branches = (Spinner)findViewById(R.id.branchid);
        selectSupplier = (TextView)findViewById(R.id.selectSupplier);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getString(R.string.select_supplier));
        int start = builder.length();
        builder.append(" *");
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        selectSupplier.setText(builder);

        indentTotal = (TextView) findViewById(R.id.indentTotal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(IndentCreationActivity.this,IndentCreateProduct.class);
                intent.putExtra("supplierPartyId",""+supplierPartyId);
                intent.putExtra("schemeType",""+schemeType);
                intent.putExtra("category_type",""+category_type);
                intent.putExtra("indent_id",indent_id);
                // editMode = true;
                invalidateOptionsMenu();
                //fab.hide();
                startActivity(intent);
            }
        });

        submitindent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitindent.setVisibility(View.GONE);

                Date supplyDate = new Date();

                Indent indent =new Indent(0,"","","",false,supplierPartyId,tId,"",supplierName,"","",supplyDate,"Not Uploaded",0.0,0.0,0.0,schemeType,prodStoreId, 0);
                datasource = new IndentsDataSource(IndentCreationActivity.this);
                datasource.open();
                indent_id = datasource.insertIndent(indent);
                datasource.close();
                //showSnackBar("Indent created. Please add products.");

                Intent intent = new Intent(IndentCreationActivity.this,IndentCreateProduct.class);
                intent.putExtra("supplierPartyId",""+supplierPartyId);
                intent.putExtra("schemeType",""+schemeType);
                intent.putExtra("category_type",""+category_type);
                intent.putExtra("indent_id",indent_id);

                fab.show();
                editMode = true;
                invalidateOptionsMenu();
                startActivity(intent);
            }
        });
        list = new ArrayList<>();


        addIndent= (Button)findViewById(R.id.addIndent);


        category = (Spinner)findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(this);

        scheme = (Spinner)findViewById(R.id.scheme);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,schemes);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scheme.setAdapter(adapter2);
        scheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        schemeType = "MGPS_10Pecent";
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        schemeType = "MGPS";
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        schemeType = "General";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        setupSuppliers();
        setupTransporters();

    }

    private Map productMap = new HashMap<String, Supplier>();
    AutoCompleteTextView actv,actvT;

    private void setupSuppliers() {
        SupplierDataSource supplierDataSource = new SupplierDataSource(this);
        supplierDataSource.open();
        List<Supplier> supplierList = supplierDataSource.getAllSuppliers();
        Log.v("Upendra","count "+supplierList.size());
        supplierDataSource.close();
        for (int i = 0; i < supplierList.size(); ++i) {
            Supplier p = supplierList.get(i);
            productMap.put(p.getId(), p);
        }

        final String[] suppliers = new String[supplierList.size()];
        int index = 0;
        for (Supplier supplier : supplierList) {
            suppliers[index] = supplier.getId();
            index++;
        }
        final IndentCreationActivity mainActivity = this;
        final SupplierAutoAdapter adapter = new SupplierAutoAdapter(this, R.layout.autocomplete_item, supplierList);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteRetailer);
        actv.setAdapter(adapter);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                actv.clearFocus();
                Supplier supplier =  (Supplier)parent.getItemAtPosition(position);
                actv.setText(supplier.getName());
                supplierPartyId =supplier.getId();
                supplierName = supplier.getName();
                submitindent.setEnabled(true);
                submitindent.setClickable(true);
            }

        });



    }


    private Map tMap = new HashMap<String, Transporter>();

    private void setupTransporters() {
        TransporterDataSource transporterDataSource = new TransporterDataSource(this);
        transporterDataSource.open();
        List<Transporter> supplierList = transporterDataSource.getAllTransporters();
        Log.v("Upendra","count "+supplierList.size());
        transporterDataSource.close();
        for (int i = 0; i < supplierList.size(); ++i) {
            Transporter p = supplierList.get(i);
            tMap.put(p.getId(), p);
        }

        final String[] suppliers = new String[supplierList.size()];
        int index = 0;
        for (Transporter supplier : supplierList) {
            suppliers[index] = supplier.getId();
            index++;
        }
        //final IndentCreationActivity mainActivity = this;
        final TransporterAutoAdapter adapter = new TransporterAutoAdapter(this, R.layout.autocomplete_item, supplierList);
        actvT = (AutoCompleteTextView) findViewById(R.id.autoCompleteTransporter);
        actvT.setAdapter(adapter);

        actvT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(actvT.getWindowToken(), 0);
                actvT.clearFocus();
                Transporter supplier =  (Transporter)parent.getItemAtPosition(position);
                actvT.setText(supplier.getName());
                tId =supplier.getId();
            }

        });



    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout cottonLayout = (LinearLayout) findViewById(R.id.cottonLayout);
        list = new ArrayList<>();
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                category_type = "SILK";
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                category_type = "COTTON";
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                category_type ="OTHER";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static boolean editMode = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.indent_menu, menu);
        menu.findItem(R.id.action_indent_delete).setVisible(false);
        menu.findItem(R.id.action_indent_upload).setVisible(false);
        if (editMode){
            menu.findItem(R.id.action_indent_upload).setVisible(true);
            //menu.findItem(R.id.action_indent_delete).setVisible(true);
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
                datasource = new IndentsDataSource(IndentCreationActivity.this);
                datasource.open();
                List<IndentItemNHDC> indentItems = datasource.getIndentItems(prefs.getInt("IndentId",0));
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
                Log.v("action_indent_delete","ok");
                datasource.open();
                datasource.deleteIndent(prefs.getInt("IndentId",0));
                datasource.close();
                finish();
                return true;
        }
        return false;
    }

    public void uploadIndentAction(final MenuItem menuItem){
        AlertDialog.Builder alert = new AlertDialog.Builder(
                IndentCreationActivity.this);
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
                        ServerSync serverSync = new ServerSync(IndentCreationActivity.this);
                        serverSync.uploadNHDCIndent(menuItem, null, list,supplierPartyId,tId,schemeType,indent_id,prodStoreId);
                        fab.hide();
                        editMode = false;
                        invalidateOptionsMenu();
                    }
                });

        alert.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        // Canceled.
                        editMode = true;

                        invalidateOptionsMenu();
                    }
                });
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(indent_id != 0){
            datasource = new IndentsDataSource(IndentCreationActivity.this);
            datasource.open();
            Indent i = datasource.getIndentDetails((int)indent_id);
            datasource.close();
            indentTotal.setText(""+i.getOrderTotal());
        }

    }

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {

        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
			/* This is just a code that delays the thread execution 4 times,
			 * during 850 milliseconds and updates the current progress. This
			 * is where the code that is going to be executed on a background
			 * thread must be placed.
			 */


            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    Map paramMap = new HashMap();
                    prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


                    String partyId = prefs.getString("storeId", "");
                    paramMap.put("partyId", partyId);
                    paramMap.put("effectiveDate", (new Date()).getTime());

                    XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(getBaseContext());
                    weaverDet = adapter.callSync("getWeaverDetails", paramMap);



                }
            }
/*			catch (InterruptedException e)
			{
				e.printStackTrace();
			} */
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {

        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            int[] ids = { };
            TextView textView;
            if (weaverDet != null) {
                Map weaverDetails = (Map)((Map)weaverDet).get("weaverDetails");
                Map loomDetails= (Map)((Map)weaverDetails).get("loomDetails");

                Map silk = (Map)((Map)loomDetails).get("SILK_YARN");
                Map cotton_40above = (Map)((Map)loomDetails).get("COTTON_40ABOVE");
                Map cotton_40upto = (Map)((Map)loomDetails).get("COTTON_UPTO40");
                Map wool_10to39 = (Map)((Map)loomDetails).get("WOOLYARN_10STO39NM");
                Map wool_40above = (Map)((Map)loomDetails).get("WOOLYARN_40SNMABOVE");
                Map wool_10below = (Map)((Map)loomDetails).get("WOOLYARN_BELOW10NM");

                prefEditor.putString("SILK_YARN", ""+silk.get("avlQuota"));
                prefEditor.putString("COTTON_40ABOVE", ""+cotton_40above.get("avlQuota"));
                prefEditor.putString("COTTON_UPTO40", ""+cotton_40upto.get("avlQuota"));
                prefEditor.putString("WOOLYARN_10STO39NM", ""+wool_10to39.get("avlQuota"));
                prefEditor.putString("WOOLYARN_40SNMABOVE", ""+wool_40above.get("avlQuota"));
                prefEditor.putString("WOOLYARN_BELOW10NM", ""+wool_10below.get("avlQuota"));
                prefEditor.apply();
                Object[] BranchMapList = (Object[])weaverDetails.get("customerBranchList");


                // Log.v("asda",""+BranchMapList)
                for (int i=0; i < BranchMapList.length; ++i) {
                    //indentItemNHDCs = new ArrayList<IndentItemNHDC>();
                    // Map indentMap = (Map) BranchMapList[i];
                    Log.v("Key","asd "+BranchMapList[i]);
                    branchids.add((String)BranchMapList[i]);

                }

                String[] stockArr = new String[branchids.size()];
                stockArr = branchids.toArray(stockArr);

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(IndentCreationActivity.this,
                        android.R.layout.simple_spinner_item,stockArr);

                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                branches.setAdapter(adapter3);

                final String[] finalStockArr = stockArr;
                branches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        prodStoreId = finalStockArr[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
        }
    }
}
