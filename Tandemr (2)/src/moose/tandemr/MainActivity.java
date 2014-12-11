package moose.tandemr;


import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	private String[] optionsMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private CharSequence tituloSeccion= null;
	private CharSequence tituloApp;
	private ActionBarDrawerToggle drawerToggle;

	private BluetoothAdapter mBluetoothAdapter = null;
	private static final boolean D = true;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	public boolean isDrawerOpen(int gravity) {
		return drawerLayout != null && drawerLayout.isDrawerOpen(gravity);
	}

	public void closeDrawer(int gravity) {
		drawerLayout.closeDrawer(gravity);
	}

	public void openDrawer(int gravity) {
		drawerLayout.openDrawer(gravity);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

		}

		optionsMenu = getResources().getStringArray(R.array.nav_drawer_items);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.right_drawer);

		drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
				(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
						android.R.layout.simple_list_item_activated_1 :
							android.R.layout.simple_list_item_1, optionsMenu));//http://www.sgoliver.net/blog/?p=4104

		Fragment fragment= new FindPeople();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		tituloSeccion=optionsMenu[0];
		getSupportActionBar().setTitle(tituloSeccion);
		drawerList.setItemChecked(0, true);


		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {

				Fragment fragment = null;

				switch (position) {
				case 0:
					fragment = new FindPeople();
					break;
				case 1:
					fragment = new ProfileActivity();
					break;
				case 2:
					fragment = new AroundYou();
					break;
				}

				FragmentManager fragmentManager =
						getSupportFragmentManager();

				fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.commit();

				drawerList.setItemChecked(position, true);

				/*CharSequence*/ tituloSeccion = optionsMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

				drawerLayout.closeDrawer(drawerList);
			}
		});

		/*CharSequence */tituloApp = getTitle();


		/*ActionBarDrawerToggle*/ drawerToggle = new ActionBarDrawerToggle(this,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		/*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		 */


	}

	/**
	 * On Bluetooth activity result
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(D) Log.i("Bluetooth", "onActivityResult");
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect

			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect

			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session

			} else {
				// User did not enable Bluetooth or an error occurred
				Log.i("Bluetooth", "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		if (item != null && item.getItemId() == R.id.action_compose) {
			if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				drawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				drawerLayout.openDrawer(Gravity.RIGHT);
			}
			return true;
		}    	

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		/*int id = item.getItemId();
	        if (id == R.id.action_compose) {
	        	drawerLayout.openDrawer(Gravity.RIGHT);
	            return true;
	        }*/

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	public void selectFrag(View view) {
		Fragment fr=null;

		if(view == findViewById(R.id.welcome_button)){
			fr = new ProfileActivity();
			tituloSeccion=optionsMenu[1];
			getSupportActionBar().setTitle(tituloSeccion);
			drawerList.setItemChecked(1, true);
		}

		else if(view == findViewById(R.id.btn_done)) {
			fr = new AroundYou();
			tituloSeccion=optionsMenu[2];
			getSupportActionBar().setTitle(tituloSeccion);
			drawerList.setItemChecked(2, true);
		}

		FragmentManager fm = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
}
