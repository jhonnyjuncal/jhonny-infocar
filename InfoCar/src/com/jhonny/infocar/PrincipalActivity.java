package com.jhonny.infocar;

import java.util.ArrayList;
import com.jhonny.infocar.adapters.NavDrawerListAdapter;
import com.jhonny.infocar.fragments.AccidentesFragment;
import com.jhonny.infocar.fragments.AcercaFragment;
import com.jhonny.infocar.fragments.DatosFragment;
import com.jhonny.infocar.fragments.EstadisticasFragment;
import com.jhonny.infocar.fragments.MantenimientosFragment;
import com.jhonny.infocar.fragments.NuevoVehiculoFragment;
import com.jhonny.infocar.fragments.OpcionesFragment;
import com.jhonny.infocar.fragments.PrincipalFragment;
import com.jhonny.infocar.fragments.ReparacionesFragment;
import com.jhonny.infocar.fragments.VehiculoFragment;
import com.jhonny.infocar.model.NavDrawerItem;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


public class PrincipalActivity extends FragmentActivity {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	// nav drawer title
    private CharSequence mDrawerTitle;
    
    // used to store app title
    private CharSequence mTitle;
    
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private boolean dPersonales = true;
    private boolean dVehiculo = true;


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
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, "+50"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(8, -1)));
        
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
        if(android.os.Build.VERSION.SDK_INT >= 14) {
        	habilitaBotonBarraDeAccion();
        }

        if(android.os.Build.VERSION.SDK_INT >= 11) {
        	habilitaDisplayBarraDeAccion();
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
        	
            public void onDrawerClosed(View view) {
            	if(android.os.Build.VERSION.SDK_INT >= 11) {
            		setTituloBarraDeAccion(mTitle);
            		invalidarOpcionesDeMenu();
            	}
            }

            public void onDrawerOpened(View drawerView) {
            	if(android.os.Build.VERSION.SDK_INT >= 11) {
            		setTituloBarraDeAccion(mDrawerTitle);
            		invalidarOpcionesDeMenu();
            	}
            }
        };
        
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // lectura del fichero de configuracion de la aplicacion
        SharedPreferences propiedades = getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
        
        // redireccion si fuese necesario
        if(propiedades != null) {
        	dPersonales = propiedades.getBoolean(Constantes.INTRO_PERSONALES, false);
        	if(dPersonales == false) {
        		displayView(5, true);
        		return;
        	}
        	
        	dVehiculo = propiedades.getBoolean(Constantes.INTRO_VEHICULO, false);
        	if(dVehiculo == false) {
        		displayView(9, false);
        		return;
        	}
        }
        
        displayView(0, true);
	}

    /*
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
	*/

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//    	// if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        MenuItem item = menu.findItem(R.id.action_settings);
//        if(item != null)
//        	item.setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }


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
        if(android.os.Build.VERSION.SDK_INT >= 11) {
        	setTituloBarraDeAccion(mTitle);
        }
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
    
    private void displayView(int position, boolean estaEnMenu) {
    	// update the main content by replacing fragments
    	Fragment fragment = null;

    	switch(position) {
    		case 0:
    			fragment = new PrincipalFragment();
    			break;
    		case 1:
    			fragment = new VehiculoFragment();
    			break;
    		case 2:
    			fragment = new MantenimientosFragment();
    			break;
    		case 3:
    			fragment = new ReparacionesFragment();
    			break;
    		case 4:
    			fragment = new AccidentesFragment();
    			break;
    		case 5:
                if(dPersonales == false) {
                    Bundle arguments1 = new Bundle();
                    arguments1.putBoolean("mostrarBotonDespues", true);
                    fragment = DatosFragment.newInstance(arguments1);
                }else {
                    fragment = new DatosFragment();
                }
    			break;
            /*
    		case 6:
    			fragment = new EstadisticasFragment();
    			break;
    		*/
    		case 6:
    			fragment = new OpcionesFragment();
    			break;
            case 7:
                fragment = new AcercaFragment();
                break;
    		case 8:
                if(dPersonales == false) {
                    Bundle arguments2 = new Bundle();
                    arguments2.putBoolean("mostrarBotonDespues", true);
                    fragment = NuevoVehiculoFragment.newInstance(arguments2);
                }else {
                    if(dVehiculo == false) {
                        //primera vez
                        Bundle arguments3 = new Bundle();
                        arguments3.putBoolean("mostrarBotonDespues", true);
                        fragment = NuevoVehiculoFragment.newInstance(arguments3);

                    }else {
                        //resto de veces
                        fragment = new NuevoVehiculoFragment();
                    }
                }
    			break;

    		default:
    			break;
    	}
    	
    	if(fragment != null) {
    		FragmentManager fragmentManager = getSupportFragmentManager();
    		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    		
    		if(estaEnMenu) {
    			// update selected item and title, then close the drawer
    			mDrawerList.setItemChecked(position, true);
    			mDrawerList.setSelection(position);
    		}
    		setTitle(navMenuTitles[position]);
    		mDrawerLayout.closeDrawer(mDrawerList);
    		
    	}else {
    		// error in creating fragment
            Log.e("MainActivity", "Error creando el fragmento");
    	}
    }
    
    @TargetApi(14)
    public void habilitaBotonBarraDeAccion() {
    	if(getActionBar() != null)
    		getActionBar().setHomeButtonEnabled(true);
    }

    @TargetApi(11)
    public void habilitaDisplayBarraDeAccion() {
    	if(getActionBar() != null)
    		getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @TargetApi(11)
    public void setTituloBarraDeAccion(CharSequence titulo) {
    	if(getActionBar() != null)
    		getActionBar().setTitle(mTitle);
    }
    
    @TargetApi(11)
    public void invalidarOpcionesDeMenu() {
    	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }

    @Override
    public void onResume(){
        super.onResume();
        cargaFondoDePantalla();
    }

    private synchronized void cargaFondoDePantalla() {
        try {
            SharedPreferences prop = getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
            int fondoSeleccionado = 1;
            if(prop != null) {
                SharedPreferences.Editor editor = prop.edit();
                if(editor != null) {
                    if(prop.contains(Constantes.FONDO_PANTALLA)) {
                        fondoSeleccionado = prop.getInt(Constantes.FONDO_PANTALLA, 1);
                    }
                }
            }

            String imagen = Constantes.FONDO_1;
            switch(fondoSeleccionado) {
                case 1:
                    imagen = Constantes.FONDO_1;
                    break;
                case 2:
                    imagen = Constantes.FONDO_2;
                    break;
                case 3:
                    imagen = Constantes.FONDO_3;
                    break;
            }
            int imageResource1 = getApplicationContext().getResources().getIdentifier(imagen, "drawable", getApplicationContext().getPackageName());
            Drawable image = getResources().getDrawable(imageResource1);
            ImageView imageView = (ImageView)findViewById(R.id.fondo_principal);
            imageView.setImageDrawable(image);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    
    
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
    	
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		// display view for the selected nav drawer item
    		displayView(position, true);
    	}
    }
}
