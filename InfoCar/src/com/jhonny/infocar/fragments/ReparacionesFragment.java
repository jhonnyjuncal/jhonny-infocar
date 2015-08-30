package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.DepthPageTransformer;
import com.jhonny.infocar.model.DetalleReparacion;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ReparacionesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import com.viewpagerindicator.CirclePageIndicator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ReparacionesFragment extends Fragment {
	
	private View rootView;
	private MyAdapter mAdapter;
	private FragmentActivity myContext;

	private ArrayList<DetalleReparacionFragment> listaDetalles;
	private ArrayList<DetalleReparacion> reparaciones;
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
	public static ViewPager paginadorReparaciones;
	
	
	public ReparacionesFragment() {
		
	}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
			myContext.invalidateOptionsMenu();
			reparaciones = recuperaDatosReparaciones();
			misVehiculos = recuperaDatosVehiculos();

			if(reparaciones != null) {
				listaDetalles = new ArrayList<DetalleReparacionFragment>();
				for(int i=0; i<reparaciones.size(); i++) {
					Bundle arguments = new Bundle();
					arguments.putInt("position", i);
					listaDetalles.add(DetalleReparacionFragment.newInstance(arguments));
				}
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			paginadorReparaciones = (ViewPager)rootView.findViewById(R.id.rep_pager);
			paginadorReparaciones.setAdapter(mAdapter);
			paginadorReparaciones.setPageTransformer(true, new DepthPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.rep_indicator);
			cIndicator.setViewPager(paginadorReparaciones);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.reparaciones, menu);
	}

	private ArrayList<DetalleReparacion> recuperaDatosReparaciones() {
		ArrayList<DetalleReparacion> reparaciones = new ArrayList<DetalleReparacion>();
		try {
            ReparacionesSQLiteHelper reparacionesHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
            reparaciones.addAll(reparacionesHelper.getReparaciones());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return reparaciones;
	}
	
	private ArrayList<DetalleVehiculo> recuperaDatosVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		try {
			VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(rootView.getContext(), Constantes.TABLA_VEHICULOS, null, 1);
			lista.addAll(vehiculosHelper.getVehiculos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}

	@Override
	public void onResume(){
		super.onResume();
		Util.cargaFondoDePantalla(myContext);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.menu_rep_nuevo:
				if(existeVehiculo()) {
					fragment = new NuevaReparacionFragment();
					if(fragment != null) {
						FragmentManager fragmentManager = myContext.getSupportFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
					}
				}else {
					Toast.makeText(myContext, getResources().getString(R.string.mensaje_crear_vehiculo_rep), Toast.LENGTH_LONG).show();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private boolean existeVehiculo() {
		if(misVehiculos != null && misVehiculos.size() > 0)
			return true;
		return false;
	}





	public static class MyAdapter extends FragmentStatePagerAdapter {
		private ArrayList<DetalleReparacionFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleReparacionFragment> listaDetalles) {
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
