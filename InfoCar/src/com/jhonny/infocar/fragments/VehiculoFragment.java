package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.DepthPageTransformer;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleVehiculo;
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
import android.util.Log;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class VehiculoFragment extends Fragment {
	
	private ArrayList<DetalleVehiculo> vehiculos;
	private View rootView;
	private Dialog editDialog;
	private FragmentActivity myContext;
	private ArrayList<DetalleVehiculoFragment> listaDetalles;

	private ArrayAdapter<String> adapterMarcas;
	private ArrayAdapter<String> adapterCarburantes;
	private ArrayAdapter<String> adapterTiposVeh;
	private TypedArray arrayMarcas;
	private TypedArray arrayCarburantes;
	private TypedArray arrayTiposVeh;
	private Spinner spinnerMarcas = null;
	private Spinner spinnerCarburantes = null;
	private Spinner spinnerTiposVeh = null;
	private ArrayList<String> listaMarcas = new ArrayList<String>();
	private ArrayList<String> listaTiposVeh = new ArrayList<String>();
	private ArrayList<String> listaCarburantes = new ArrayList<String>();

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
		cargaFondoDePantalla();
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

    private void actualizarListadoDeVehiculos() {
        Fragment fragment = new VehiculoFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }

    private boolean comprobacionDatosVehiculo(DetalleVehiculo dv) {
        boolean result = true;
        try {

        }catch(Exception ex) {
            ex.printStackTrace();;
        }
        return result;
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
			if(imageView != null)
				imageView.setImageDrawable(image);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
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
