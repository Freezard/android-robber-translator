package com.example.lab5_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * MainActivity.java
 * 
 * Displays a menu where the user can navigate to TranslateActivity, show an
 * about dialog, or quit the application.
 * 
 * Plays music and resumes the track position when returning to the activity. A
 * user setting for turning off the music is available in the phone's menu or
 * the overflow menu.
 */
public class MainActivity extends Activity {
	static final String STATE_TRACK_POS = "com.example.Lab5_1.CURRENT_POS";
	static final String TRANSLATION_SETTING = "com.example.Lab5_1.TRANSLATION_SETTING";

	private MediaPlayer mediaPlayer;
	private boolean playMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Play music depending on the user preference
		mediaPlayer = MediaPlayer.create(this, R.raw.uncharted_worlds);
		
		if (playMusic) {
			if (savedInstanceState != null) {
				int savedPos = savedInstanceState.getInt(STATE_TRACK_POS);
				mediaPlayer.seekTo(savedPos);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// If user clicks the settings item on the menu, show the
		// user settings
		case R.id.action_settings:
			Intent i = new Intent(this, UserSettingsActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		// Animates the layout after being stopped
		ViewGroup menuLayout = (ViewGroup) findViewById(R.layout.activity_main);
		menuLayout.startLayoutAnimation();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (mediaPlayer == null && playMusic)
			mediaPlayer = MediaPlayer.create(this, R.raw.uncharted_worlds);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (playMusic)
			mediaPlayer.pause();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (playMusic) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Get the user preference for playing music
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		playMusic = sharedPref.getBoolean(
				UserSettingsActivity.KEY_PREF_PLAY_MUSIC, true);

		if (playMusic)
			mediaPlayer.start();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the media player's current track position
		if (playMusic)
			savedInstanceState.putInt(STATE_TRACK_POS,
					mediaPlayer.getCurrentPosition());

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	// Starts TranslateActivity with the chosen translation setting
	public void translate(View view) {
		int translationSetting = 0;

		if (view == findViewById(R.id.button_translate_to))
			translationSetting = R.string.translate_to;
		else if (view == findViewById(R.id.button_translate_from))
			translationSetting = R.string.translate_from;

		Intent intent = new Intent(this, TranslateActivity.class);
		intent.putExtra(TRANSLATION_SETTING, translationSetting);
		startActivity(intent);
	}

	// Shows an about dialog explaining the Robber Language
	public void about(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(R.string.about);
		alertDialogBuilder
				.setMessage(R.string.dialog_message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// close dialog
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	// Exits the application
	public void quit(View view) {
		finish();
	}

}
