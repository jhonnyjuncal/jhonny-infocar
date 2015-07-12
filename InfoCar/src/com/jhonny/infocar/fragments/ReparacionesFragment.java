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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ReparacionesFragment extends Fragment {
	
	private View rootView;
	private Dialog editDialog;
	private ArrayAdapter<String> adapterReparaciones;
	private Spinner spinnerTipo;
	private DetalleReparacion detalleEnEdicion;
	private ArrayList<DetalleReparacionFragment> listaDetalles;
	
	private TypedArray arrayTiposReparaciones;
	private TypedArray arrayMarcas;
	private FragmentActivity myContext;
	
	private ArrayList<DetalleReparacion> reparaciones;
	private ArrayList<String> listaReparaciones = new ArrayList<String>();
	private ArrayList<String> listaVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
	
	private EditText textFecha;
	private EditText textKms;
	private EditText textPrecio;
	private EditText textTaller;
	private EditText textObservaciones;

	private MyAdapter mAdapter;
	private ViewPager mPager;
	
	
	public ReparacionesFragment() {
		
	}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
			myContext.invalidateOptionsMenu();
			reparaciones = recuperaDatosReparaciones();

			if(reparaciones != null) {
				listaDetalles = new ArrayList<DetalleReparacionFragment>();
				for(int i=0; i<reparaciones.size(); i++) {
					Bundle arguments = new Bundle();
					arguments.putInt("position", i);
					listaDetalles.add(DetalleReparacionFragment.newInstance(arguments));
				}
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			mPager = (ViewPager)rootView.findViewById(R.id.rep_pager);
			mPager.setAdapter(mAdapter);
			mPager.setPageTransformer(true, new DepthPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.rep_indicator);
			cIndicator.setViewPager(mPager);

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
	
	private void actualizaListaReparaciones() {
		Fragment fragment = new ReparacionesFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.action_nuevo:
				fragment = new NuevaReparacionFragment();
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
