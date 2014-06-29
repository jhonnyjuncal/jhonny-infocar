package com.jhonny.infocar;

import java.util.ArrayList;
import com.jhonny.infocar.adapters.NavDrawerListAdapter;
import com.jhonny.infocar.fragments.AccidentesFragment;
import com.jhonny.infocar.fragments.DatosFragment;
import com.jhonny.infocar.fragments.EstadisticasFragment;
import com.jhonny.infocar.fragments.MantenimientosFragment;
import com.jhonny.infocar.fragments.OpcionesFragment;
import com.jhonny.infocar.fragments.PrincipalFragment;
import com.jhonny.infocar.fragments.ReparacionesFragment;
import com.jhonny.infocar.model.NavDrawerItem;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class PrincipalActivity extends FragmentActivity {
	
	private static final int IAB_LEADERBOARD_WIDTH = 728;
	private static final int MED_BANNER_WIDTH = 480;
	private static final int BANNER_AD_WIDTH = 320;
	private static final int BANNER_AD_HEIGHT = 50;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	// nav drawer title
    private CharSequence mDrawerTitle;
    
    // used to store app title
    private CharSequence mTitle;
    
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		mTitle = mDrawerTitle = getTitle();
		
		// load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.menu_array);
        
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.menu_iconos);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_principal);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, "5"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, "+50"));
        
        // Recycle the typed array
        navMenuIcons.recycle();
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        // Set the adapter for the list view
        mDrawerList.setAdapter(adapter);
        // listener para los eventos
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
        	
            public void onDrawerClosed(View view) {
            	getActionBar().setTitle(mTitle);
            	// calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	getActionBar().setTitle(mDrawerTitle);
            	// calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        if(savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
	
	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	// if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        // Handle action bar actions click
        switch(item.getItemId()) {
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
	
	/**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    private void displayView(int position) {
    	// update the main content by replacing fragments
    	Fragment fragment = null;
    	
    	switch(position) {
    		case 0:
    			fragment = new PrincipalFragment();
    			break;
    		case 1:
    			fragment = new MantenimientosFragment();
    			break;
    		case 2:
    			fragment = new ReparacionesFragment();
    			break;
    		case 3:
    			fragment = new AccidentesFragment();
    			break;
    		case 4:
    			fragment = new DatosFragment();
    			break;
    		case 5:
    			fragment = new EstadisticasFragment();
    			break;
    		case 6:
    			fragment = new OpcionesFragment();
    			break;
    		default:
    			break;
    	}
    	
    	if(fragment != null) {
    		FragmentManager fragmentManager = getFragmentManager();
    		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    		
    		// update selected item and title, then close the drawer
    		mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            
    	}else {
    		// error in creating fragment
            Log.e("MainActivity", "Error creando el fragmento");
    	}
    }
    
    
    
    
    
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
    	
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		// display view for the selected nav drawer item
    		displayView(position);
    	}
    }
}
