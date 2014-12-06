package moose.tandemr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = (Button) findViewById(R.id.welcome_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				Intent intent =  new Intent(MainActivity.this, ProfileActivity.class);

				startActivity(intent);
			}
		});

		/*Beginning of temporary code : implementation of the button that allows to see another 
		 * user's profile*/
		Button tmp_button = (Button) findViewById(R.id.tmp_button_foreign_view);
		tmp_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			    android.support.v4.app.FragmentManager fragmentManager =  getSupportFragmentManager();
			    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    ForeignProfileActivity fragment = new ForeignProfileActivity();
			    fragmentTransaction.replace(R.id.main_activity, fragment);
			    fragmentTransaction.commit();
			}
		});
		/*end of the temporary code*/
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
