package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PrincipalFragment extends Fragment {

	private FragmentActivity myContext;
	private String[] navMenuTitles;


	public PrincipalFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        setHasOptionsMenu(true);

		navMenuTitles = getResources().getStringArray(R.array.menu_array);

		Button boton_vehiculos = (Button)rootView.findViewById(R.id.btn_vehiculos);
		boton_vehiculos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myContext.setTitle(navMenuTitles[1]);
				Fragment fragment = new VehiculoFragment();
				FragmentManager fragmentManager = myContext.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		Button boton_mantenimientos = (Button)rootView.findViewById(R.id.btn_mantenimientos);
		boton_mantenimientos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myContext.setTitle(navMenuTitles[2]);
				Fragment fragment = new MantenimientosFragment();
				FragmentManager fragmentManager = myContext.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		Button boton_reparaciones = (Button)rootView.findViewById(R.id.btn_reparaciones);
		boton_reparaciones.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myContext.setTitle(navMenuTitles[3]);
				Fragment fragment = new ReparacionesFragment();
				FragmentManager fragmentManager = myContext.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		Button boton_accidentes = (Button)rootView.findViewById(R.id.btn_accidentes);
		boton_accidentes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myContext.setTitle(navMenuTitles[4]);
				Fragment fragment = new AccidentesFragment();
				FragmentManager fragmentManager = myContext.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		Button boton_opciones = (Button)rootView.findViewById(R.id.btn_opciones);
		boton_opciones.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myContext.setTitle(navMenuTitles[6]);
				Fragment fragment = new OpcionesFragment();
				FragmentManager fragmentManager = myContext.getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});

        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.principal, menu);
	}

	@Override
	public void onResume(){
		super.onResume();
		Util.cargaFondoDePantalla(myContext);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}
}
