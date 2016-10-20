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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.List;

import in.vasista.nhdc.R;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentCreationActivity;
import in.vasista.vsales.indent.IndentListFragment;
import in.vasista.vsales.supplier.SupplierPO;
import in.vasista.vsales.supplier.SupplierPOListFragment;
import in.vasista.vsales.sync.ServerSync;

/**
 * This is the activity for feature 2 in the dashboard application.
 * It displays some text and provides a way to get back to the home activity.
 *
 */

public class SuppPOActivity extends DashboardAppCompatActivity
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
//private MenuItem menuItem;
ProgressBar progressBar;
	FloatingActionButton fab;
	public boolean refresh;
	Menu m;
	boolean fetchIndents;
protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    //setContentView (R.layout.activity_f2);
    //setTitleFromActivityLabel (R.id.title_text);
	setContentChildView(R.layout.activity_supppo);

	refresh = false;
	if(getIntent().getBooleanExtra("indent_refresh",false)){
		refresh = true;
	}

	setSalesDashboardTitle(R.string.title_feature1_plurer);

	fab = (FloatingActionButton) findViewById(R.id.fab);
	fab.show();
	//fab.setImageResource(R.drawable.title_upload);

	fab.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = new Intent(SuppPOActivity.this,IndentCreationActivity.class);
			// editMode = true;
			invalidateOptionsMenu();
			//fab.hide();
			startActivity(intent);
		}
	});
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		m=menu;
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.homeSearch);
		if (fetchIndents || refresh) {
			onOptionsItemSelected(m.findItem(R.id.action_refresh));
		}

		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case R.id.action_refresh:
				item.setActionView(R.layout.progressbar);
				progressBar=(ProgressBar) item.getActionView().findViewById(R.id.menuitem_progress);
				FragmentManager fm = getFragmentManager();
				SupplierPOListFragment indentListFragment = (SupplierPOListFragment) fm.findFragmentById(R.id.indent_list_fragment);
				indentListFragment.syncSupplierPOs(item);
				return true;


		}
		return false;
	}
protected void onResume ()
{
   super.onResume ();
   PODataSource datasource = new PODataSource(this);
   datasource.open();
   List<SupplierPO> supplierPOs = datasource.getAllSuppPOs();
	datasource.close();
   fetchIndents = false;
   if (supplierPOs.size() > 0) {
	   SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	   for (int i=0; i < supplierPOs.size(); ++i) {
		   SupplierPO indent = supplierPOs.get(i);
//		   boolean isInactiveIndent =  fmt.format(new Date()).compareTo(fmt.format(indent.getSupplyDate())) > 0;
//		   if (isInactiveIndent) {
//			   fetchIndents = true;
//			   break;
//		   }
	   }
   }
   else {     
	   fetchIndents = true;
   }

	if (fetchIndents || refresh) {
	   FragmentManager fm = getFragmentManager();
	   SupplierPOListFragment indentListFragment = (SupplierPOListFragment) fm.findFragmentById(R.id.indent_list_fragment);
	   ServerSync serverSync = new ServerSync(this);
	   serverSync.updateSupplierPOs(null, progressBar, indentListFragment);

   }


}
    
} // end class
