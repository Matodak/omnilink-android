package net.homeip.mleclerc.omnilinkanclient.model.omnilink;

import net.homeip.mleclerc.omnilinkanclient.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class OmniLinkPreferenceActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferenceManager manager = getPreferenceManager();
		manager.setSharedPreferencesName("preferences");
		addPreferencesFromResource(R.xml.preferences);
	}
}
