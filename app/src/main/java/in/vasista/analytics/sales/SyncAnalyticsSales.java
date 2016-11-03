package in.vasista.analytics.sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

/**
 * Created by Bekkam on 25/4/16.
 */

public class SyncAnalyticsSales extends AsyncTask<Void, Integer, Void> {

    private Context ctx;
    ProgressDialog progressDialog;
    String totalRevenue = "";
    String rsString ="";
    ArrayList<Map> subList = null;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> qua = new ArrayList<String>();
    ArrayList<String> rev = new ArrayList<String>();

    ArrayList<Item> items = new ArrayList<Item>();

    boolean isEmpty = false;

    public SyncAnalyticsSales(Context context) {
        this.ctx = context;
        progressDialog = new ProgressDialog(ctx);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        rsString = ctx.getResources().getString(R.string.Rs);

        //show progress dialog
        if (!progressDialog.isShowing()){
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        System.out.println("insideSyncAnalytics_doinBackground");

        try
        {
            Map paramMap = new HashMap();

            paramMap.put("subscriptionTypeId", SalesActivity.filtersubscriptionTypeId);
            paramMap.put("facilityId", SalesActivity.filterFacilityID);
            paramMap.put("fromDate", SalesActivity.filteredFromDate);
            paramMap.put("thruDate", SalesActivity.filteredToDate);

            XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(ctx);
            Object result = adapter.callSync("getPeriodTotalsAPI", paramMap);

            if (result != null) {
                Map  periodTotals = (Map)((Map)result).get("periodTotals");

                if (periodTotals.size() > 0) {

                    double currentRevenue = Double.parseDouble(periodTotals.get("totalRevenue").toString());
                    totalRevenue = String.format(rsString +" %.2f", currentRevenue);

                    if (periodTotals.get("productTotals") != null) {

                        Map productTotalsList = (Map) periodTotals.get("productTotals");

                        ArrayList list = new ArrayList(productTotalsList.values());
                        HashMap<String, ArrayList> catagoryMap = new HashMap<String, ArrayList>();

                        for (int i = 0; i < list.size(); i++) {
                            Map mymap = (Map) list.get(i);

                           String productCategoryName = (String) mymap.get("productCategory");
                            productCategoryName = productCategoryName.trim();
                            subList = new ArrayList<Map>();

                            if (catagoryMap.keySet().isEmpty()){
                                subList.add(mymap);

                                catagoryMap.put(productCategoryName, subList);

                            } else if (!catagoryMap.keySet().isEmpty()){

                                if(catagoryMap.get(productCategoryName) != null) {
                                    subList = catagoryMap.get(productCategoryName);
                                }

                                subList.add(mymap);

                                catagoryMap.put(productCategoryName, subList);

                            }
                        }

                        System.out.println(catagoryMap.size() + " " + "perticularValue " + catagoryMap);
                        String finalProductCategoryName = "", finalProductCategoryTQ, finalProudectCategoryTR;

                        Set set2 = catagoryMap.entrySet();
                        Iterator iterator2 = set2.iterator();
                        while(iterator2.hasNext()) {
                            Map.Entry me2 = (Map.Entry)iterator2.next();
                            if (me2.getKey().equals("MILK")) {
                                ArrayList obj = catagoryMap.remove("MILK");
                                catagoryMap.put("A11MILK", obj);

                                /*System.out.print(me2.getKey() + ": ");
                                System.out.println(me2.getValue());*/
                            }
                        }

                        Map<String, ArrayList> sortedMap = new TreeMap<String, ArrayList>(catagoryMap);


                        ArrayList eachCategoryList = new ArrayList(sortedMap.values());
                        ArrayList eachCaregoryKeys = new ArrayList(sortedMap.keySet());

                        List subEachCategoryMap = null;

                        for (int sizeEachCategory = 0; sizeEachCategory < eachCategoryList.size(); sizeEachCategory++){


                            String title = (String) eachCaregoryKeys.get(sizeEachCategory);
                            if (title.contains("A11"))
                               title =  title.substring(3, 7);

                            items.add(new SectionItem(title, "", ""));

                            subEachCategoryMap = (List) eachCategoryList.get(sizeEachCategory);

                            for (int eachSize = 0; eachSize < subEachCategoryMap.size(); eachSize++){


                                Map subMap = (Map) subEachCategoryMap.get(eachSize);
                                finalProductCategoryName = (String) subMap.get("name");

                                Map subCategoryMap = (Map) subMap.get("supplyTypeTotals");
                                Map subCategoryCash = (Map) subCategoryMap.get("CASH");

                                finalProductCategoryTQ = subCategoryCash.get("packetQuantity").toString();
                                finalProudectCategoryTR = subCategoryCash.get("totalRevenue").toString();

                                name.add(finalProductCategoryName);
                                qua.add(finalProductCategoryTQ);
                                rev.add(finalProudectCategoryTR);

                                //items.add(new EntryItem(finalProductCategoryName, finalProductCategoryTQ, finalProudectCategoryTR));


                            }

                            Collections.sort(qua);
                            for (int i =0; ( i < qua.size() && i < name.size() && i < rev.size()); i++) {
                                    items.add(new EntryItem(name.get(i), qua.get(i), rev.get(i)));
                            }
                            qua.clear();
                            name.clear();
                            rev.clear();
                        }

                    }else {
                        isEmpty = true;
                    }
                } else {
                    isEmpty = true;
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


        if (progressDialog.isShowing()){
            progressDialog.dismiss();
            //progressDialog.hide();
        }
        if (isEmpty){
            SalesActivity.revenue.setText(rsString +" 0.00");
            Toast.makeText(ctx, "No Sales Found For the Day", Toast.LENGTH_LONG).show();
        } else {

            SalesActivity.revenue.setText(totalRevenue);
            SalesActivity.filterLayout.setVisibility(View.GONE);

            EntryAdapter adapter = new EntryAdapter(ctx, items);
            SalesActivity.recyclerView.setAdapter(adapter);
        }

    }

}