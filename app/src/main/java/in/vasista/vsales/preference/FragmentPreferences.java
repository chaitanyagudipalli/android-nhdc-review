package in.vasista.vsales.preference;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Locale;

import in.vasista.global.GlobalApplication;
import in.vasista.nhdcapp.R;
import in.vasista.vsales.SplashScreenActivity;

public class FragmentPreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override 
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.preference_headers, target);  
  }
  @Override
  protected boolean isValidFragment(String fragmentName) {
    return true;
  }
  Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
    LinearLayout content = (LinearLayout) root.getChildAt(0);
    LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.toolbar, null);

    root.removeAllViews();
    toolbarContainer.addView(content);
    root.addView(toolbarContainer);

      mToolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
      mToolbar.setTitle(R.string.action_settings);
      mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
      mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          finish();
        }
      });

    PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //Toast.makeText(getApplicationContext(), "Pref changed " + key, Toast.LENGTH_LONG).show();

    if (key.equalsIgnoreCase("password")||key.equalsIgnoreCase("serverURL")||key.equalsIgnoreCase("userName")||key.equalsIgnoreCase("tenantId") ||key.equalsIgnoreCase("lang_preference")){
      ((GlobalApplication)getApplication()).setPrefChange(true);
    }

    if(key.equalsIgnoreCase("lang_preference")){
      Log.v("Locale",sharedPreferences.getString("lang_preference","en"));
      Locale myLocale = new Locale(sharedPreferences.getString("lang_preference","en"));
      Resources res = getResources();
      DisplayMetrics dm = res.getDisplayMetrics();
      Configuration conf = res.getConfiguration();
      conf.locale = myLocale;
      res.updateConfiguration(conf, dm);

      Intent i = new Intent(this, SplashScreenActivity.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
      finish();

    }

  }
}