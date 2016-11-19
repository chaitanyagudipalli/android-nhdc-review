/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.vasista.vsales;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import in.vasista.atom.AtomActivity;
import in.vasista.nhdcapp.R;
import in.vasista.vsales.payment.PaymentListFragment;
import in.vasista.vsales.stocks.StockListFragment;

/**
 * This is the activity for feature 3 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class StockActivity extends DashboardAppCompatActivity
{

/**
 * onCreate
 *
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 */
    ProgressBar progressBar;
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    //setContentView (R.layout.activity_f3);
    //setTitleFromActivityLabel (R.id.title_text);
//	setContentView(R.layout.payment_layout);
    setContentChildView(R.layout.stock_layout);
    setSalesDashboardTitle(R.string.stocks);

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        onOptionsItemSelected(menu.findItem(R.id.action_refresh));
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_refresh:
                item.setActionView(R.layout.progressbar);
                progressBar=(ProgressBar) item.getActionView().findViewById(R.id.menuitem_progress);
                FragmentManager fm = getFragmentManager();
                StockListFragment stockListFragment = (StockListFragment) fm.findFragmentById(R.id.stock_list_fragment);
                stockListFragment.syncStocks(item);
                return true;
            case  R.id.homeSearch:
                final FrameLayout inputSearchFrame = (FrameLayout) findViewById(R.id.inputSearchFrame);
                if (inputSearchFrame.isShown()) {
                    inputSearchFrame.setVisibility(View.GONE);
                }
                else {
                    inputSearchFrame.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return false;
    }
} // end class
