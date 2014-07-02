package com.jhonny.infocar.listener;

import com.jhonny.infocar.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class DrawerItemClickListener implements ListView.OnItemClickListener {
	
	private FragmentManager fragmentManager;
	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	
	
	public DrawerItemClickListener(FragmentManager fragmentManager, ListView mDrawerList, DrawerLayout mDrawerLayout){
		this.fragmentManager = fragmentManager;
		this.mDrawerList = mDrawerList;
		this.mDrawerLayout = mDrawerLayout;
	}
	
	@Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		Fragment fragment = null;
		
		// Create a new fragment and specify the planet to show based on position
	    switch(position){
	    	case 0:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.id.fragment_mantenimiento, fragment).commit();
	    		break;
	    		
	    	case 1:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.layout.fragment_reparaciones, fragment).commit();
	    		break;
	    		
	    	case 2:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.id.fragment_accidentes, fragment).commit();
	    		break;
	    		
	    	case 3:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.id.fragment_datos, fragment).commit();
	    		break;
	    		
	    	case 4:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.id.fragment_estadisticas, fragment).commit();
	    		break;
	    		
	    	case 5:
	    		fragment = new Fragment();
	    		fragmentManager.beginTransaction().replace(R.id.fragment_opciones, fragment).commit();
	    		break;
	    }
	    
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    mDrawerLayout.closeDrawer(mDrawerList);
    }
}
