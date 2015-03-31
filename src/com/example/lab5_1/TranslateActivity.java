package com.example.lab5_1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * TranslateActivity.java
 * 
 * Translates input from a text field to or from Robber Language depending on
 * the translation setting. Option to clear the text on the context menu, the
 * phone menu or as an action item. Saves both of the translated texts to
 * SharedPreferences.
 */
public class TranslateActivity extends Activity {
	static final String TRANSLATION_OUTPUT = "translationOutput";

	private int translation;
	private SharedPreferences settings;
	private EditText editTextOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate);
		// Show the Up button in the action bar.
		setupActionBar();

		// Get the translation setting
		Intent intent = getIntent();
		translation = intent.getExtras().getInt(
				MainActivity.TRANSLATION_SETTING);

		setTitle(translation);

		editTextOutput = (EditText) findViewById(R.id.editText_translate_output);
		settings = getSharedPreferences(TRANSLATION_OUTPUT, MODE_PRIVATE);

		// Enable context menu for output text
		registerForContextMenu(editTextOutput);

		loadOutput();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translate_output_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.clear:
			clearOutput();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.translate_output_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear:
			clearOutput();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		saveOutput();
	}

	// Translates the user input from editTextInput to or from
	// Robber Language depending on the translation setting.
	// Appends the translated text to editTextOutput.
	// If no input, a toast warning will show. Also clears the input text.
	public void translate(View view) {
		EditText editTextInput = (EditText) findViewById(R.id.editText_translate_input);
		String inputText = editTextInput.getText().toString();

		if (inputText.isEmpty())
			Toast.makeText(this, R.string.nothing_to_translate,
					Toast.LENGTH_SHORT).show();
		else {
			if (translation == R.string.translate_to)
				inputText = toRobberLanguage(inputText);
			else
				inputText = fromRobberLanguage(inputText);

			editTextOutput.append(inputText + "\n");

			editTextInput.getText().clear();
		}
	}

	// Translates a string to Robber Language and returns it
	private String toRobberLanguage(String plainText) {
		String robberText = "";

		for (int i = 0; i < plainText.length(); i++) {
			char ch = plainText.charAt(i);

			if (isConsonant(ch))
				robberText += ch + "o" + Character.toLowerCase(ch);
			else
				robberText += ch;
		}
		return robberText;
	}

	// Translates a robber text to plain text and returns it
	private String fromRobberLanguage(String robberText) {
		String plainText = "";

		for (int i = 0; i < robberText.length(); i++) {
			char ch = robberText.charAt(i);

			plainText += ch;

			if (isConsonant(ch) && i + 3 <= robberText.length()
					&& robberText.charAt(i + 1) == 'o'
					&& robberText.charAt(i + 2) == ch)
				i += 2;
		}
		return plainText;
	}

	// Returns true if char is a consonant
	private boolean isConsonant(char ch) {
		ch = Character.toLowerCase(ch);

		return ch == 'b' || ch == 'c' || ch == 'd' || ch == 'f' || ch == 'g'
				|| ch == 'h' || ch == 'j' || ch == 'k' || ch == 'l'
				|| ch == 'm' || ch == 'n' || ch == 'p' || ch == 'q'
				|| ch == 'r' || ch == 's' || ch == 't' || ch == 'v'
				|| ch == 'w' || ch == 'x' || ch == 'z';
	}

	// Save output text
	private void saveOutput() {
		SharedPreferences.Editor editor = settings.edit();
		if (translation == R.string.translate_to)
			editor.putString("to", getOutput());
		else
			editor.putString("from", getOutput());

		editor.commit();
	}

	// Load output text
	private void loadOutput() {
		if (translation == R.string.translate_to)
			setOutput(settings.getString("to", ""));
		else
			setOutput(settings.getString("from", ""));
	}

	// Clears the output text
	private void clearOutput() {
		editTextOutput.getText().clear();
	}

	// Sets the output text to a string
	private void setOutput(String text) {
		editTextOutput.setText(text);
	}

	// Returns output text
	private String getOutput() {
		return editTextOutput.getText().toString();
	}
}
