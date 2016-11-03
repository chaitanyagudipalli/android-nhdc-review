package in.vasista.vsales.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import in.vasista.nhdcapp.R;


public class UserPreferenceFragment extends PreferenceFragment{

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		addPreferencesFromResource(R.xml.userpreferences);		
	}
}
