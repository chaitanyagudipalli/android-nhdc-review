package in.vasista.vsales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;

import java.util.Arrays;

import in.vasista.nhdcapp.R;

public class LanguageActivity extends AppCompatActivity {

    String[] SPINNERLIST = {"English", "Hindi"};
    String[] SPINNERLISTID = {"en", "hi"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPINNERLIST = getResources().getStringArray(R.array.lang_list_preference);
        SPINNERLISTID = getResources().getStringArray(R.array.langvalues_list_preference);
        setContentView(R.layout.activity_language);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LanguageActivity.this);
        final SharedPreferences.Editor editor=settings.edit();

        final Intent i = new Intent(LanguageActivity.this,SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(settings.getBoolean("opened",false)){
            startActivity(i);
            finish();
        }

        int index = Arrays.asList(SPINNERLISTID).indexOf(settings.getString("lang_preference","en"));


        AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.dropdown);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                editor.putString("lang_preference",SPINNERLISTID[position]);
                editor.putBoolean("opened",true);

                editor.apply();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(i);
                finish();
            }
        });


    }
}
