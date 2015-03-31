package com.example.lab5_1;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

/**
 * UserSettingsActivity.java
 * 
 * Uses preferences.xml for user settings. Setting for playing music or not.
 */
public class UserSettingsActivity extends PreferenceActivity {
	static final String KEY_PREF_PLAY_MUSIC = "pref_play_music";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}