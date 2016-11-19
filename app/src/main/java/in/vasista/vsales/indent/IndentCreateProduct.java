package in.vasista.vsales.indent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.adapter.ProductAutoAdapter;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.sync.ServerSync;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class IndentCreateProduct extends DashboardAppCompatActivity implements View.OnKeyListener {

    private Map productsMap = new HashMap<String, Product>();
    Spinner uom;Button addIndent;
    List<HashMap> list;

    private static final String[]uoms = {"KGS", "Bale", "Half Bale","Bundle"};

    AutoCompleteTextView actv1;

    String productId = "",quantity= "",remarks = "", baleQuantity = "",
            bundleWeight = "", bundleUnitPrice = "", yarnUOM = "", basicPrice = "",
            serviceCharge = "",serviceChargeAmt = "";

    String supplierPartyId = "", schemeType = "", category_type = "", total_amount = "";
    String spec = "";
    Intent i;

    LinearLayout cottonLayout;

    IndentsDataSource datasource;
    SharedPreferences.Editor prefEditor;

    Object weaverDet;SharedPreferences prefs;

    EditText totalweight,unitprice;
    EditText bundleUnitPriceTv,bundlewt,quantitynos, specificaton;

    TextView totalAmt,selectProduct,qtyKg,unitPriceText;
    HashMap<String,String> hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_indent_create_product);
        setPageTitle(getString(R.string.add_product));
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefEditor = prefs.edit();

        prefEditor.putInt("IndentId",0);
        prefEditor.apply();

        i = getIntent();
        category_type = i.getStringExtra("category_type");
        supplierPartyId = i.getStringExtra("supplierPartyId");
        schemeType = i.getStringExtra("schemeType");

//        Log.v("category_type",category_type);
        new LoadViewTask().execute();
        setUpProducts(category_type);

        list = new ArrayList<>();

        uom = (Spinner)findViewById(R.id.uom);
        addIndent= (Button)findViewById(R.id.addIndent);
        cottonLayout = (LinearLayout) findViewById(R.id.cottonLayout);

        bundleUnitPriceTv = (EditText) findViewById(R.id.bundleUnitPrice);
        bundlewt = (EditText) findViewById(R.id.bundlewt);
        quantitynos = (EditText)findViewById(R.id.quantitynos);
        totalAmt = (TextView) findViewById(R.id.totalAmt);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        selectProduct = (TextView)findViewById(R.id.selectProduct);
        builder.append(getString(R.string.select_product));
        int start = builder.length();
        builder.append(" *");
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        selectProduct.setText(builder);

        qtyKg = (TextView)findViewById(R.id.qtyKg);
        builder.clear();
        builder.append(getString(R.string.qty_kg));
        start = builder.length();
        builder.append(" *");
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        qtyKg.setText(builder);

        unitPriceText = (TextView)findViewById(R.id.unitPriceText);
        builder.clear();
        builder.append(getString(R.string.unit_price_kg));
        start = builder.length();
        builder.append(" *");
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        unitPriceText.setText(builder);


        // Disable edit text editing
        totalweight = (EditText) findViewById(R.id.totalweight);
        unitprice = (EditText) findViewById(R.id.unitprice);

        specificaton = (EditText) findViewById(R.id.specifications);


        if(category_type!=null &&   category_type.equalsIgnoreCase("COTTON")){
            cottonLayout.setVisibility(View.VISIBLE);

            totalweight.setKeyListener(null);unitprice.setKeyListener(null);

            bundlewt.setText("4.54");

            bundleUnitPriceTv.setOnKeyListener(this);
            bundlewt.setOnKeyListener(this);
            quantitynos.setOnKeyListener(this);

        }else{
            totalweight.setOnKeyListener(this);
            unitprice.setOnKeyListener(this);
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
                if(category_type.equalsIgnoreCase("COTTON")) {
                    if (bundlewt.getText().toString().equalsIgnoreCase("") || quantitynos.getText().toString().equalsIgnoreCase("") ||
                            bundleUnitPriceTv.getText().toString().equalsIgnoreCase("")) {

                    } else {

                        changeListener();
                    }
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
                total_amount = ((TextView) findViewById(R.id.totalAmt)).getText().toString();
                spec = specificaton.getText().toString();
                 hm = new HashMap<String, String>();
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

                /**
                 * Quota Calc
                 * */
                try {
                    ProductsDataSource productsDataSource = new ProductsDataSource(IndentCreateProduct.this);
                    productsDataSource.open();
                    Product p = productsDataSource.getproductDetails(Integer.parseInt(productId));
                    productsDataSource.close();
                    int quota = prefs.getInt(p.getScheme(), 0);
                    int usedquota = 0;

                    datasource = new IndentsDataSource(IndentCreateProduct.this);
                    datasource.open();
                    List<IndentItemNHDC> indentItems = datasource.getIndentItems((int) i.getLongExtra("indent_id", 0));
                    for (int i = 0; i < indentItems.size(); i++) {
                        IndentItemNHDC indentItemNHDC = indentItems.get(i);
                        productsDataSource.open();
                        Product p_i = productsDataSource.getproductDetails(Integer.parseInt(indentItemNHDC.getProductId()));
                        productsDataSource.close();
                        if (p_i.getScheme().equalsIgnoreCase(p.getScheme()))
                            usedquota += Integer.parseInt(indentItemNHDC.getQuantity());
                    }

                    usedquota += (int) Float.parseFloat(quantity);

                    Log.v("Quota", p.getScheme() + " :: " + quota);
                    Log.v("Used Quota", "" + usedquota);
                    if (usedquota > quota) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                IndentCreateProduct.this);
                        alert.setTitle("Quota Exceeded");
                        alert.setPositiveButton("Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        addProductToIndent();
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

                    } else {
                        addProductToIndent();
                    }
                }catch (NumberFormatException e){

                }

            }
        });
    }

    private void addProductToIndent(){
        IndentItemNHDC IN = new IndentItemNHDC(0,(int)i.getLongExtra("indent_id",0),productId,quantity, remarks, baleQuantity, bundleWeight, bundleUnitPrice, yarnUOM, basicPrice, serviceCharge, serviceChargeAmt,total_amount, 0,0,0,0,0,0,0, spec);
        list.add(hm);

        int id = (int) datasource.insertIndentItem((int)i.getLongExtra("indent_id",0),IN);
        IN.setId(id);
        IN.setIndentId((int)i.getLongExtra("indent_id",0));

        datasource.updateTotalAmount((int)i.getLongExtra("indent_id",0),Double.parseDouble(total_amount));

        datasource.close();

        prefEditor.putInt("IndentId",(int)i.getLongExtra("indent_id",0));
        prefEditor.apply();

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(addIndent.getWindowToken(), 0);

        finish();
    }

    private void setUpProducts(String category){
        if (category == null)
            return;
        ProductsDataSource productsDataSource = new ProductsDataSource(this);
        productsDataSource.open();
        List<Product> productList;
        if(category.equalsIgnoreCase("SILK") || category.equalsIgnoreCase("COTTON"))
            productList = productsDataSource.getProducts(category);
        else
            productList = productsDataSource.getOtherProducts();

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
                addIndent.setEnabled(true);
                addIndent.setClickable(true);
                Log.v("add",""+addIndent.isEnabled());
                productId = product.getId();
                //actv.setVisibility(View.GONE);

            }

        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(category_type.equalsIgnoreCase("COTTON")) {
            if (bundlewt.getText().toString().equalsIgnoreCase("") || quantitynos.getText().toString().equalsIgnoreCase("") ||
                    bundleUnitPriceTv.getText().toString().equalsIgnoreCase(""))
                return false;
            changeListener();
        }else{
            if (unitprice.getText().toString().equalsIgnoreCase("") || totalweight.getText().toString().equalsIgnoreCase("") )
                return false;
            changeSilkListener();
        }
        return false;
    }
    public void changeSilkListener(){

        float unit_price_kgs = Float.parseFloat(unitprice.getText().toString());
        float qty_kgs = Float.parseFloat(totalweight.getText().toString());

        float total_amount = unit_price_kgs * qty_kgs;
        totalAmt.setText(BigDecimal.valueOf(total_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

    }
    public void changeListener(){
        try {


            float bundle_wt = Float.parseFloat(bundlewt.getText().toString());
            float qty_nos = Float.parseFloat(quantitynos.getText().toString());
            float unit_price_bundle = Float.parseFloat(bundleUnitPriceTv.getText().toString());

            //Log.v("upedata","bundle_wt"+bundle_wt+"qty_nos"+qty_nos+"unit_price_bundle"+unit_price_bundle);
            float unit_price_kgs = 0.0f, qty_kgs = 0.0f, total_amount;

            if (yarnUOM.equalsIgnoreCase("KGS")) {

                unit_price_kgs = unit_price_bundle;
                qty_kgs = qty_nos;

            } else if (yarnUOM.equalsIgnoreCase("Bale")) {

                unit_price_kgs = unit_price_bundle / bundle_wt;
                qty_kgs = 40 * qty_nos * bundle_wt;

            } else if (yarnUOM.equalsIgnoreCase("Half-Bale")) {
                unit_price_kgs = unit_price_bundle / bundle_wt;
                qty_kgs = 20 * qty_nos * bundle_wt;

            } else if (yarnUOM.equalsIgnoreCase("Bundle")) {
                unit_price_kgs = unit_price_bundle / bundle_wt;
                qty_kgs = qty_nos * bundle_wt;
            }
            total_amount = qty_kgs * unit_price_kgs;

            totalAmt.setText(BigDecimal.valueOf(total_amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            totalweight.setText(BigDecimal.valueOf(qty_kgs).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            unitprice.setText(BigDecimal.valueOf(unit_price_kgs).setScale(2, BigDecimal.ROUND_HALF_UP).toString());


        }catch (NumberFormatException e){

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
                Map loomMap = (Map)((Map)weaverDetails).get("loomDetails");

                String[] values = {};

                for (int i=0;i<ids.length;i++){
                    textView = (TextView) findViewById(ids[i]);
                    textView.setText(values[i]);
                }

                if(category_type!=null && category_type.equalsIgnoreCase("SILK")){
                    TableRow silkrow;
                    silkrow = (TableRow)findViewById(R.id.silk_row);
                    silkrow.setVisibility(View.VISIBLE);
                }else if(category_type!=null && category_type.equalsIgnoreCase("COTTON")){
                    TableRow c40a,c40b;
                    c40a = (TableRow)findViewById(R.id.cotton_row_40a);
                    c40b = (TableRow)findViewById(R.id.cotton_row_40b);
                    c40a.setVisibility(View.VISIBLE);c40b.setVisibility(View.VISIBLE);
                }else{
                    TableRow w10a,w40a,w10b;
                    w10a = (TableRow)findViewById(R.id.wool_row_10a);
                    w40a = (TableRow)findViewById(R.id.wool_row_40a);
                    w10b = (TableRow)findViewById(R.id.wool_row_10b);
                    w10a.setVisibility(View.VISIBLE);
                    w40a.setVisibility(View.VISIBLE);w10b.setVisibility(View.VISIBLE);
                }

                Map loomDetails= (Map)((Map)weaverDetails).get("loomDetails");

                Map silk = (Map)((Map)loomDetails).get("SILK_YARN");
                Map cotton_40above = (Map)((Map)loomDetails).get("COTTON_40ABOVE");
                Map cotton_40upto = (Map)((Map)loomDetails).get("COTTON_UPTO40");
                Map wool_10to39 = (Map)((Map)loomDetails).get("WOOLYARN_10STO39NM");
                Map wool_40above = (Map)((Map)loomDetails).get("WOOLYARN_40SNMABOVE");
                Map wool_10below = (Map)((Map)loomDetails).get("WOOLYARN_BELOW10NM");

                int[] loom_ids = {R.id.c40a,R.id.c40an,R.id.c40ae,R.id.c40ab,R.id.c40au,
                        R.id.c40u,R.id.c40un,R.id.c40ue,R.id.c40ub,R.id.c40uu,
                        R.id.silk,R.id.silkn,R.id.silke,R.id.silkb,R.id.silku,
                        R.id.w10a,R.id.w10an,R.id.w10ae,R.id.w10ab,R.id.w10au,
                        R.id.w40a,R.id.w40an,R.id.w40ae,R.id.w40ab,R.id.w40au,
                        R.id.w10b,R.id.w10bn,R.id.w10be,R.id.w10bb,R.id.w10bu};
                String[] loom_values={""+cotton_40above.get("description"),""+cotton_40above.get("loomQty"),""+cotton_40above.get("loomQuota"),""+cotton_40above.get("avlQuota"),""+cotton_40above.get("usedQuota"),
                        ""+cotton_40upto.get("description"),""+cotton_40upto.get("loomQty"),""+cotton_40upto.get("loomQuota"),""+cotton_40upto.get("avlQuota"),""+cotton_40upto.get("usedQuota"),
                        ""+silk.get("description"),""+silk.get("loomQty"),""+silk.get("loomQuota"),""+silk.get("avlQuota"),""+silk.get("usedQuota"),
                        ""+wool_10to39.get("description"),""+wool_10to39.get("loomQty"),""+wool_10to39.get("loomQuota"),""+wool_10to39.get("avlQuota"),""+wool_10to39.get("usedQuota"),
                        ""+wool_40above.get("description"),""+wool_40above.get("loomQty"),""+wool_40above.get("loomQuota"),""+wool_40above.get("avlQuota"),""+wool_40above.get("usedQuota"),
                        ""+wool_10below.get("description"),""+wool_10below.get("loomQty"),""+wool_10below.get("loomQuota"),""+wool_10below.get("avlQuota"),""+wool_10below.get("usedQuota")};
                for (int i=0;i<loom_ids.length;i++){
                    textView = (TextView) findViewById(loom_ids[i]);
                    textView.setText(loom_values[i]);
                }
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putInt("SILK_YARN",(int)silk.get("avlQuota"));
                prefEditor.putInt("COTTON_40ABOVE", (int)cotton_40above.get("avlQuota"));
                prefEditor.putInt("COTTON_UPTO40", (int)cotton_40upto.get("avlQuota"));
                prefEditor.putInt("WOOLYARN_10STO39NM", (int)wool_10to39.get("avlQuota"));
                prefEditor.putInt("WOOLYARN_40SNMABOVE", (int)wool_40above.get("avlQuota"));
                prefEditor.putInt("WOOLYARN_BELOW10NM", (int)wool_10below.get("avlQuota"));
                prefEditor.apply();

            }
        }
    }
}
