package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class MantenimientosFragment extends Fragment {
	
	private View rootView;
	private FragmentActivity myContext;
	
	private ArrayList<DetalleMantenimiento> mantenimientos;
	private ArrayList<DetalleMantenimientoFragment> listaDetalles;
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();

	private MyAdapter mAdapter;
	public static ViewPager paginadorMantenimientos;
	
	
	public MantenimientosFragment() {
		
	}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
			mantenimientos = recuperaDatosMantenimiento();
			misVehiculos = recuperaDatosVehiculos();

			if(mantenimientos != null) {
				listaDetalles = new ArrayList<DetalleMantenimientoFragment>();
				for(int i=0; i<mantenimientos.size(); i++) {
					Bundle arguments = new Bundle();
					arguments.putInt("position", i);
					listaDetalles.add(DetalleMantenimientoFragment.newInstance(arguments));
				}
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			paginadorMantenimientos = (ViewPager)rootView.findViewById(R.id.mant_pager);
			paginadorMantenimientos.setAdapter(mAdapter);
			paginadorMantenimientos.setPageTransformer(true, new ZoomOutPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.mant_indicator);
			cIndicator.setViewPager(paginadorMantenimientos);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.mantenimiento, menu);
	}

	private ArrayList<DetalleMantenimiento> recuperaDatosMantenimiento() {
		ArrayList<DetalleMantenimiento> detalles = new ArrayList<DetalleMantenimiento>();
		try {
            MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
			detalles.addAll(mantHelper.getMantenimientos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}

	@Override
	public void onResume(){
		super.onResume();
		cargaFondoDePantalla();
	}

	private synchronized void cargaFondoDePantalla() {
		try {
			SharedPreferences prop = myContext.getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
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
			int imageResource1 = myContext.getApplicationContext().getResources().getIdentifier(imagen, "drawable", myContext.getApplicationContext().getPackageName());
			Drawable image = myContext.getResources().getDrawable(imageResource1);
			ImageView imageView = (ImageView)myContext.findViewById(R.id.fondo_principal);
			imageView.setImageDrawable(image);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.menu_mant_nuevo:
				if(existeVehilo()) {
					fragment = new NuevoMantenimientoFragment();
					if(fragment != null) {
						FragmentManager fragmentManager = myContext.getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
					}
				}else {
					Toast.makeText(myContext, getResources().getString(R.string.mensaje_crear_vehiculo_man), Toast.LENGTH_LONG).show();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean existeVehilo() {
		if(misVehiculos != null && misVehiculos.size() > 0)
			return true;
		return false;
	}

	private ArrayList<DetalleVehiculo> recuperaDatosVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		try {
			VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
			lista.addAll(vehiculosHelper.getVehiculos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}






	public static class MyAdapter extends FragmentStatePagerAdapter {
		private ArrayList<DetalleMantenimientoFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleMantenimientoFragment> listaDetalles) {
			super(fm);
			this.listaDetalles = listaDetalles;
		}

		@Override
		public int getCount() {
			return listaDetalles.size();
		}

		@Override
		public Fragment getItem(int position) {
			return listaDetalles.get(position);
		}
	}
}
