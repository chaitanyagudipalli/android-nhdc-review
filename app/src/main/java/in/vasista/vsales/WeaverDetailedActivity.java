package in.vasista.vsales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.preference.FragmentPreferences;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class WeaverDetailedActivity extends DashboardAppCompatActivity {

    Object weaverDet;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_weaver_detailed);

        setPageTitle("Weaver details");

        new LoadViewTask().execute();
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
            int[] ids = { R.id.partyName, R.id.address, R.id.passbook,R.id.issueDate,R.id.partyType,
                    R.id.depot,R.id.doa,R.id.totallooms};
            TextView textView;
            if (weaverDet != null) {
                Map weaverDetails = (Map)((Map)weaverDet).get("weaverDetails");
                Map addressMap = (Map)((Map)weaverDetails).get("addressMap");

                String[] values = {(String)weaverDetails.get("partyName"),
                        (String)addressMap.get("address1"),
                        (String)weaverDetails.get("passBookNo"),
                        (String)weaverDetails.get("issueDate"),
                        (String)weaverDetails.get("partyType"),
                        (String)weaverDetails.get("isDepot"),
                        (String)weaverDetails.get("DOA"),
                        ""+weaverDetails.get("totalLooms")};

                for (int i=0;i<ids.length;i++){
                    textView = (TextView) findViewById(ids[i]);
                    textView.setText(values[i]);
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
