package in.vasista.vsales.indent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.adapter.ProductAutoAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.supplier.Supplier;

public class IndentCreateProduct extends DashboardAppCompatActivity {

    private Map productsMap = new HashMap<String, Product>();
    Spinner uom;Button addIndent;
    List<HashMap> list;

    private static final String[]uoms = {"KGS", "Bale", "Half Bale","Bundle"};

    AutoCompleteTextView actv1;

    String productId = "",quantity= "",remarks = "", baleQuantity = "",
            bundleWeight = "", bundleUnitPrice = "", yarnUOM = "", basicPrice = "",
            serviceCharge = "",serviceChargeAmt = "";

    String supplierPartyId = "", schemeType = "", category_type = "";

    LinearLayout cottonLayout;

    IndentsDataSource datasource;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_create_product);
        setPageTitle("Add product");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefEditor = prefs.edit();

        prefEditor.putInt("IndentId",0);
        prefEditor.apply();

        final Intent i = getIntent();
        category_type = i.getStringExtra("category_type");
        supplierPartyId = i.getStringExtra("supplierPartyId");
        schemeType = i.getStringExtra("schemeType");

        Log.v("category_type",category_type);

        setUpProducts(category_type);

        list = new ArrayList<>();

        uom = (Spinner)findViewById(R.id.uom);
        addIndent= (Button)findViewById(R.id.addIndent);
        cottonLayout = (LinearLayout) findViewById(R.id.cottonLayout);


        if(category_type.equalsIgnoreCase("COTTON")){
            cottonLayout.setVisibility(View.VISIBLE);
        }



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
                hm.put("bundleUnitPrice",bundleUnitPrice);
                hm.put("yarnUOM",yarnUOM);
                hm.put("basicPrice",basicPrice);
                hm.put("serviceCharge",serviceCharge);
                hm.put("serviceChargeAmt",serviceChargeAmt);


                IndentItemNHDC IN = new IndentItemNHDC(productId,quantity, remarks, baleQuantity, bundleWeight, bundleUnitPrice, yarnUOM, basicPrice, serviceCharge, serviceChargeAmt);
                list.add(hm);
                datasource = new IndentsDataSource(IndentCreateProduct.this);
                datasource.open();
                long id = datasource.insertIndentItem(i.getLongExtra("indent_id",0),IN);
                IN.setId(id);
                datasource.close();

                prefEditor.putInt("IndentId",(int)i.getLongExtra("indent_id",0));
                prefEditor.apply();

                invalidateOptionsMenu();
                finish();
            }
        });
    }

    private void setUpProducts(String category){
        ProductsDataSource productsDataSource = new ProductsDataSource(this);
        productsDataSource.open();
        List<Product> productList;
        if(category.equalsIgnoreCase("SILK") || category.equalsIgnoreCase("COTTON"))
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
        final IndentCreateProduct mainActivity = this;
        final ProductAutoAdapter adapter = new ProductAutoAdapter(this, R.layout.autocomplete_item, productList);
        actv1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteProduct);
        actv1.setAdapter(adapter);
        //actv.setVisibility(View.GONE);

        actv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(actv1.getWindowToken(), 0);
                actv1.clearFocus();
                Product product =  (Product)parent.getItemAtPosition(position);
                actv1.setText(product.getName());
                productId = product.getId();
                //actv.setVisibility(View.GONE);
            }

        });
    }
}
