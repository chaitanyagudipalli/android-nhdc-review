package in.vasista.vsales;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.vasista.nhdc.R;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;

public class SupplierDetailedActivity extends DashboardAppCompatActivity {

    Object weaverDet;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.activity_supp_detailed);

        setPageTitle("Supplier details");

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
                    weaverDet = adapter.callSync("getSupplierDetails", paramMap);



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
            int[] ids = { R.id.partyName, R.id.address, R.id.email,R.id.contactNumber,R.id.cstNum,
                    R.id.tanNum,R.id.panNum};
            TextView textView;
            if (weaverDet != null) {
                Map supplierDetails = (Map)((Map)weaverDet).get("supplierDetails");
                Map addressMap = (Map)((Map)supplierDetails).get("addressMap");

                String[] values = {(String)supplierDetails.get("partyName"),
                        (String)addressMap.get("address1"),
                        (String)supplierDetails.get("email"),
                        (String)supplierDetails.get("contactNumber"),
                        (String)supplierDetails.get("cstNum"),
                        (String)supplierDetails.get("tanNum"),
                        (String)supplierDetails.get("panNum")};

                for (int i=0;i<ids.length;i++){
                    textView = (TextView) findViewById(ids[i]);
                    textView.setText(values[i]);
                }

            }
        }
    }
}
