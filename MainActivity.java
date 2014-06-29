package edu.armstrong.fonefinder;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	EditText text;
	SharedPreferences FFPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FFPrefs = getSharedPreferences("FoneFinder", MODE_PRIVATE);
		text = (EditText)findViewById(R.id.editText1);
		text.setText(FFPrefs.getString(getString(R.string.prefsname), ""));
		
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = FFPrefs.edit();
				editor.putString(getString(R.string.prefsname), text.getText().toString());
				editor.commit();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
