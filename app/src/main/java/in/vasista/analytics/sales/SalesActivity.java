package in.vasista.analytics.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.DashboardAppCompatActivity;


/**
 * Created by Bekkam on 21/4/16.
 */
public class SalesActivity extends DashboardAppCompatActivity implements AdapterView.OnItemSelectedListener {


    static TextView systemDate, revenue;
    static RelativeLayout filterLayout;
    TextView filterFromDate, filterToDate;
    EditText facilityID;
    public static String filterFacilityID, filtersubscriptionTypeId, filteredFromDate, filteredToDate;
    Spinner amPMSpinner;
    Button search;
    //public static RecyclerView recyclerView, subRecyclerView;
    public static ListView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentChildView(R.layout.analytics_sales);
        actionBarHomeEnabled();

        systemDate = (TextView) findViewById(R.id.system_date_tv);
        revenue = (TextView) findViewById(R.id.system_revenue_tv);

        Date date = new Date();
        String nowAsString = new SimpleDateFormat("dd-MMM-yyyy").format(date);
        systemDate.setText(nowAsString);
        revenue.append(" 0.00");

        filterLayout = (RelativeLayout) findViewById(R.id.as_header_filter);

        filterFromDate = (TextView) findViewById(R.id.system_date_fromfilter);
        filterToDate = (TextView) findViewById(R.id.system_date_tofilter);

        facilityID =(EditText) findViewById(R.id.as_facilityid);
        filterFacilityID = facilityID.getText().toString();
        amPMSpinner = (Spinner) findViewById(R.id.spinner);

        search = (Button) findViewById(R.id.as_search);

        recyclerView = (ListView) findViewById(R.id.category_recycler_view);
       /* recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(FloatingActionButton.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.as_spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amPMSpinner.setAdapter(adapter);
        amPMSpinner.setOnItemSelectedListener(this);


        filterFromDate.setText(nowAsString);
        filterToDate.setText(nowAsString);


        filterFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(filterFromDate);
            }
        });

        filterToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(filterToDate);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SalesActivity.this, "Wait", Toast.LENGTH_LONG).show();

                SyncAnalyticsSales syncAnalyticsSales = new SyncAnalyticsSales(SalesActivity.this);
                syncAnalyticsSales.execute();

            }
        });

        SyncAnalyticsSales syncAnalyticsSales = new SyncAnalyticsSales(SalesActivity.this);
        syncAnalyticsSales.execute();
    }

    private void showDatePicker(final TextView textView) {

        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US); //2015-11-12 00:00:00
        final SimpleDateFormat setTvDateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(SalesActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                textView.setText(setTvDateFormatter.format(newDate.getTime()).trim());
                systemDate.setText(filterFromDate.getText().toString());
                //System.out.println("sqltimestamp " + textView.getText().toString());

                filteredFromDate = dateFormatter.format(newDate.getTime()).trim();
                filteredToDate = dateFormatter.format(newDate.getTime()).trim();

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        fromDatePickerDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,R.id.action_filter,0,R.string.action_filter);
        menu.getItem(0).setIcon(R.drawable.ic_filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.getItem(0).setTitle("Filters");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.action_filter:
                //filter = 1;

                if (filterLayout.isShown()){
                    filterLayout.setVisibility(View.GONE);
                }else {
                    filterLayout.setVisibility(View.VISIBLE);
                }

                return true;

        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(SalesActivity.this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        filtersubscriptionTypeId = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
