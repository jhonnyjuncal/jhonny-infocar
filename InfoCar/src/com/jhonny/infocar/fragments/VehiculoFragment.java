package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleVehiculo;
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


public class VehiculoFragment extends Fragment {
	
	private ArrayList<DetalleVehiculo> vehiculos;
	private View rootView;
	private FragmentActivity myContext;
	private ArrayList<DetalleVehiculoFragment> listaDetalles;

	private MyAdapter mAdapter;
	public static ViewPager paginadorVehiculos;

	
	public VehiculoFragment() {
		
	}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_vehiculo, container, false);
			vehiculos = recuperaDatosVehiculos();

			if(vehiculos != null) {
				listaDetalles = new ArrayList<DetalleVehiculoFragment>();
				for(int i=0; i<vehiculos.size(); i++) {
					Bundle arguments = new Bundle();
					arguments.putInt("position", i);
					listaDetalles.add(DetalleVehiculoFragment.newInstance(arguments));
				}
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			paginadorVehiculos = (ViewPager)rootView.findViewById(R.id.veh_pager);
			paginadorVehiculos.setAdapter(mAdapter);
			paginadorVehiculos.setPageTransformer(true, new ZoomOutPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.veh_indicator);
			cIndicator.setViewPager(paginadorVehiculos);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.vehiculo, menu);
	}

	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume(){
		super.onResume();
		Util.cargaFondoDePantalla(myContext);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.menu_veh_nuevo:
				fragment = new NuevoVehiculoFragment();
				if(fragment != null) {
					FragmentManager fragmentManager = myContext.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}




	public static class MyAdapter extends FragmentStatePagerAdapter {
		private ArrayList<DetalleVehiculoFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleVehiculoFragment> listaDetalles) {
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
