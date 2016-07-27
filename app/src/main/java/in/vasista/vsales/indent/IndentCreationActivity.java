package in.vasista.vsales.indent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.adapter.ProductAutoAdapter;
import in.vasista.vsales.adapter.SupplierAutoAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.sync.ServerSync;

public class IndentCreationActivity extends DashboardAppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner category,scheme,uom;

    private static final String[]paths = {"Silk", "Cotton", "Other"};
    private static final String[]schemes = {"MGPS + 10%", "MGPS", "General"};
    List<HashMap> list;
    Button addIndent,submitindent;

    String supplierPartyId = "", schemeType = "", category_type = "",billingType = "Direct";
    long indent_id;

    FloatingActionButton fab;

    IndentsDataSource datasource;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_creation);
        setPageTitle("Create Indent");
        submitindent= (Button)findViewById(R.id.submitindent);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        //fab.setImageResource(R.drawable.title_upload);

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

                Indent indent =new Indent(0,"","","",false,supplierPartyId,"","","","",supplyDate,"NOT_UPLOADED",0.0,0.0,0.0);
                datasource = new IndentsDataSource(IndentCreationActivity.this);
                datasource.open();
                indent_id = datasource.insertIndent(indent);
                datasource.close();
                showSnackBar("Indent created. Please add products.");
                fab.show();
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

    }

    private Map productMap = new HashMap<String, Supplier>();
    AutoCompleteTextView actv;

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


    boolean editMode = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.indent_menu, menu);
        menu.findItem(R.id.action_indent_done).setVisible(false);
        menu.findItem(R.id.action_indent_upload).setVisible(false);
        if (editMode){
            menu.findItem(R.id.action_indent_upload).setVisible(true);
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

                ServerSync serverSync = new ServerSync(IndentCreationActivity.this);
                serverSync.uploadNHDCIndent(item, null, list,supplierPartyId,schemeType);

                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
