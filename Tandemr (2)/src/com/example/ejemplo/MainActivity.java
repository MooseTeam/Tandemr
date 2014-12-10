package com.example.ejemplo;


import android.support.v7.app.ActionBarActivity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
  private CharSequence tituloSeccion;
  private CharSequence tituloApp;
  private ActionBarDrawerToggle drawerToggle;
  
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

      optionsMenu = new String[] {"Find People", "Profile", "Foreign Profile"};
      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawerList = (ListView) findViewById(R.id.right_drawer);

      drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
    		    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ?
    		    android.R.layout.simple_list_item_activated_1 :
    		    android.R.layout.simple_list_item_1, optionsMenu));//http://www.sgoliver.net/blog/?p=4104
      
      Fragment fragment= new FindPeople();
      FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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
                      fragment = new ForeignProfileActivity();
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

		if(view == findViewById(R.id.welcome_button))
			fr = new ProfileActivity();
		
		else if(view == findViewById(R.id.btn_done)) {
			fr = new AroundYou();
		}
		
		FragmentManager fm = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fr);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

	}
    
}



