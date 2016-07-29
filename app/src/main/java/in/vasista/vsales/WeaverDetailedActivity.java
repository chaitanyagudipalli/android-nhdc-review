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
                Log.v("adsa",""+weaverDetails);

                String[] values = {prefs.getString(MainActivity.USER_FULLNAME, ""),
                        (String)addressMap.get("address1"),
                        (String)weaverDetails.get("passBookNo"),
                        (String)weaverDetails.get("issueDate"),
                        (String)weaverDetails.get("partyType"),
                        (String)weaverDetails.get("isDepot"),
                        (String)weaverDetails.get("DOA"),
                        ""+(BigDecimal)weaverDetails.get("totalLooms")};

                for (int i=0;i<ids.length;i++){
                    textView = (TextView) findViewById(ids[i]);
                    textView.setText(values[i]);
                }


            }
        }
    }
}
