package moose.tandemr;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class MainActivity extends ActionBarActivity {

	private String[] optionsMenu;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private CharSequence sectionTittle= null;
	private CharSequence appTittle;
	private ActionBarDrawerToggle drawerToggle;
	
	ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;


	private BluetoothAdapter BluetoothAdapter = null;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;
	
	int before=1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get local Bluetooth adapter
		BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (BluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!BluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

		}
		
		Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(), 
			    R.drawable.moose_100);
		//bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false);
		BitmapDrawable icon = new BitmapDrawable(this.getResources(),bitmap);
		//navDrawerItems.get(0).setIcon(icon);

		
		//get from the xmls the elements which we need
		optionsMenu = getResources().getStringArray(R.array.nav_drawer_items);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.right_drawer);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem("[User]", icon,true));
		// Find People
		navDrawerItems.add(new NavDrawerItem(optionsMenu[0],null,false));
		// Photos
		navDrawerItems.add(new NavDrawerItem(optionsMenu[1], null, false));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(optionsMenu[2], null, false));
		// Pages
		navDrawerItems.add(new NavDrawerItem(optionsMenu[3], null, false));
		
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		drawerList.setAdapter(adapter);

		//fill the NavDrawer with the required information.
		/*drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
				(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
						android.R.layout.simple_list_item_activated_1 :
							android.R.layout.simple_list_item_1, optionsMenu));//http://www.sgoliver.net/blog/?p=4104*/

		
		//when the app is launched, it shows the fragment FindPeople by default.
		//First charging profileactivity to refresh and get the last image in 
		//charged in the profile
		Fragment fragment= new ProfileActivity();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		fragment= new FindPeople();
		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		sectionTittle=optionsMenu[1];
		getSupportActionBar().setTitle(sectionTittle);
		drawerList.setItemChecked(1, true);
		
		//navDrawerItems.get(0).setIcon(icon);

		
		//make the onclicklistener of the elements in the NavDrawer and shows, detects what is the element
		//used, and changes the view of the Fragments to see the needed one.
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {

				Fragment fragment = null;

				switch (position) {
				case 0:
					drawerList.setItemChecked(before, true);

					break;
				case 1:
					fragment = new FindPeople();
					break;
				case 2:
					fragment = new ProfileActivity();
					break;
				case 3:
					fragment = new AroundYou();
					break;
				case 4:
					fragment = new FilterInterest();
					break;
				}

				if(position!=0){
					before=position;

					FragmentManager fragmentManager =
							getSupportFragmentManager();

					fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();

					drawerList.setItemChecked(position, true);

					sectionTittle = optionsMenu[position-1];
					getSupportActionBar().setTitle(sectionTittle);

					drawerLayout.closeDrawer(drawerList);
				}
			}
		});

		appTittle = getTitle();

		//ActionBarDrawerToggle provides a handy way to tie together the functionality of DrawerLayout
		//and the framework ActionBar to implement the recommended design for navigation drawers.
		drawerToggle = new ActionBarDrawerToggle(this,
				drawerLayout,
				R.string.drawer_open,
				R.string.drawer_close) {
			
			//Instructions when the NavDrawer is closed and when it is open
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(sectionTittle);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(appTittle);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};
	}

	/**
	 * On Bluetooth activity result
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

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
		//create an aditional button to substitute the original NavigationDrawerToggle
		//and gives it the same utility. if the NavDrawer is open, close it, and if not, open it.
		if (item != null && item.getItemId() == R.id.action_compose) {
			if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				drawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				drawerLayout.openDrawer(Gravity.RIGHT);
			}
			return true;
		}    	
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		//synchronize the navdrawer with the drawerlayout
		drawerToggle.syncState();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	//Changing Fragment using buttons instead of the navdrawer
	public void selectFrag(View view) {
		Fragment fr=null;

		if(view == findViewById(R.id.welcome_button)){
			fr = new ProfileActivity();
			sectionTittle=optionsMenu[2];
			getSupportActionBar().setTitle(sectionTittle);
			drawerList.setItemChecked(2, true);
			before=2;
		}

		else if(view == findViewById(R.id.btn_done)) {
			fr = new AroundYou();
			sectionTittle=optionsMenu[3];
			getSupportActionBar().setTitle(sectionTittle);
			drawerList.setItemChecked(3, true);
			before=3;
		}

		FragmentManager fm = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

}
