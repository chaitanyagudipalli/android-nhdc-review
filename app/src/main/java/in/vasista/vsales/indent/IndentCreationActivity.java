package in.vasista.vsales.indent;

import android.content.Context;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.adapter.ProductAutoAdapter;
import in.vasista.vsales.adapter.SupplierAutoAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.sync.ServerSync;

public class IndentCreationActivity extends DashboardAppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner category,scheme,uom;
    private static final String[]paths = {"Silk", "Cotton", "Other"};
    private static final String[]schemes = {"MGPS + 10%", "MGPS", "General"};
    private static final String[]uoms = {"KGS", "Bale", "Half Bale","Bundle"};
    Boolean silk = false, cotton = false, other = false;
    List<HashMap> list;
    Button addIndent;
    String productId = "",quantity= "",remarks = "", baleQuantity = "",
            bundleWeight = "", bundleUnitPrice = "", yarnUOM = "", basicPrice = "",
            serviceCharge = "",serviceChargeAmt = "";
    String supplierPartyId = "", schemeType = "";
    TextView indentinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_creation);
        setPageTitle("Create Indent");

        list = new ArrayList<>();

        addIndent= (Button)findViewById(R.id.addIndent);


        indentinfo = (TextView)findViewById(R.id.indentinfo);
        indentinfo.setText("Selected :"+list.size());

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

        uom = (Spinner)findViewById(R.id.uom);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,uoms);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uom.setAdapter(adapter3);
        uom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        yarnUOM = "Kgs";
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        yarnUOM = "Bale";
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        yarnUOM = "Half-Bale";
                        break;
                    case 3:
                        // Whatever you want to happen when the thrid item gets selected
                        yarnUOM = "Bundle";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setupSuppliers();
        setUpProducts("SILK");silk =true;
        addIndent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = ((EditText)findViewById(R.id.totalweight)).getText().toString();
                remarks = ((EditText)findViewById(R.id.specifications)).getText().toString();
                baleQuantity = ((EditText)findViewById(R.id.quantitynos)).getText().toString();
                bundleWeight = ((EditText)findViewById(R.id.bundlewt)).getText().toString();
                bundleUnitPrice = ((EditText)findViewById(R.id.bundleUnitPrice)).getText().toString();
                basicPrice = ((EditText)findViewById(R.id.unitprice)).getText().toString();
                HashMap<String,String> hm = new HashMap<String, String>();
                hm.put("productId",productId);
                hm.put("quantity",quantity);
                hm.put("remarks",remarks);
                hm.put("baleQuantity",baleQuantity);
                hm.put("bundleWeight",bundleWeight);
                hm.put("bundleUnitPrice",bundleUnitPrice);hm.put("yarnUOM",yarnUOM);
                hm.put("basicPrice",basicPrice);hm.put("serviceCharge",serviceCharge);
                hm.put("serviceChargeAmt",serviceChargeAmt);


                IndentItemNHDC IN = new IndentItemNHDC(productId,quantity, remarks, baleQuantity, bundleWeight, bundleUnitPrice, yarnUOM, basicPrice, serviceCharge, serviceChargeAmt);
                list.add(hm);
                editMode = true;
                invalidateOptionsMenu();
                indentinfo.setText("Selected :"+list.size());
            }
        });
    }

    private Map productMap = new HashMap<String, Supplier>();
    private Map productsMap = new HashMap<String, Product>();
    AutoCompleteTextView actv,actv1;

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
        //actv.setVisibility(View.GONE);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                actv.clearFocus();
                Supplier supplier =  (Supplier)parent.getItemAtPosition(position);
                actv.setText(supplier.getName());
                supplierPartyId =supplier.getId();
                //actv.setVisibility(View.GONE);
            }

        });



    }

    private void setUpProducts(String category){
        ProductsDataSource productsDataSource = new ProductsDataSource(this);
        productsDataSource.open();
        List<Product> productList;
        if(category == "SILK" || category == "COTTON")
            productList = productsDataSource.getProducts(category);
        else
            productList = productsDataSource.getOtherProducts();

        Log.v("Upendra","count "+productList.size());
        productsDataSource.close();
        for (int i = 0; i < productList.size(); ++i) {
            Product p = productList.get(i);
            productsMap.put(p.getId(), p);
        }

        final String[] suppliers = new String[productList.size()];
        int index = 0;
        for (Product product : productList) {
            suppliers[index] = product.getId();
            index++;
        }
        final IndentCreationActivity mainActivity = this;
        final ProductAutoAdapter adapter = new ProductAutoAdapter(this, R.layout.autocomplete_item, productList);
        actv1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteProduct);
        actv1.setAdapter(adapter);
        //actv.setVisibility(View.GONE);

        actv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(actv.getWindowToken(), 0);
                actv1.clearFocus();
                Product product =  (Product)parent.getItemAtPosition(position);
                actv1.setText(product.getName());
                productId = product.getId();
                //actv.setVisibility(View.GONE);
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
                setUpProducts("SILK");silk = true;cottonLayout.setVisibility(View.GONE);
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                setUpProducts("COTTON"); cotton = true;cottonLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                setUpProducts("OTHER"); other = true;cottonLayout.setVisibility(View.GONE);
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
}
