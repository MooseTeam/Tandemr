package moose.tandemr;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	/**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainActivity newInstance() {
    	MainActivity fragment = new MainActivity();
        return fragment;
    }

	/**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    
    public NavigationDrawerFragment getNavigationDrawerFragment(){
    	return mNavigationDrawerFragment;
    }
    
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

	private BluetoothAdapter BluetoothAdapter = null;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;
	
	private Bundle mSavedInstanceState;
	
	private static final String STATE_SELECTED_PROFILE = "selected_profile";
    
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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
	    if (savedInstanceState == null) {
	        FragmentManager fragmentManager = getSupportFragmentManager();
	    	fragmentManager.beginTransaction()
	        .replace(R.id.container, FindPeople.newInstance())
	        .commit();
	    }
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
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
        case 1:
        	fragmentManager.beginTransaction()
            .replace(R.id.container, FindPeople.newInstance())
            .commit();
            break;
        case 2:
        	fragmentManager.beginTransaction()
            .replace(R.id.container, ProfileActivity.newInstance())
            .commit();
            break;
        case 3:      	
        	fragmentManager.beginTransaction()
            .replace(R.id.container, AroundYou.newInstance())
            .commit();
            break;
        case 4:
        	fragmentManager.beginTransaction()
            .replace(R.id.container, FilterInterest.newInstance())
            .commit();
            break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 2:
                mTitle = getString(R.string.title_section1);
                break;
            case 3:
                mTitle = getString(R.string.title_section2);
                break;
            case 4:
                mTitle = getString(R.string.title_section3);
                break;
            case 5:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    
    //Changing Fragment using buttons instead of the navdrawer
  	public void selectFrag(View view){
  		FragmentManager fragmentManager = getSupportFragmentManager();

  		if(view == findViewById(R.id.welcome_button)){
  			fragmentManager.beginTransaction()
              .replace(R.id.container, ProfileActivity.newInstance())
              .commit();
  		}

  		else if(view == findViewById(R.id.btn_done)) {
  			fragmentManager.beginTransaction()
              .replace(R.id.container, AroundYou.newInstance())
              .commit();
  		}
  	}

	//Changing Fragment using buttons instead of the navdrawer
	//- from ProfileActivity
	public void selectFrag(Button btn){
		FragmentManager fragmentManager = getSupportFragmentManager();

		if(btn == findViewById(R.id.btn_done)) {
			fragmentManager.beginTransaction()
            .replace(R.id.container, AroundYou.newInstance())
            .commit();
		}
	}
}