package com.jhonny.infocar.fragments;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class OpcionesFragment extends Fragment {

	private FragmentActivity myContext;
	private int fondoSeleccionado = 1;
	private SharedPreferences prop = null;
	private CheckBox checkNotificaciones = null;
	private CheckBox checkFondo1 = null;
	private CheckBox checkFondo2 = null;
	private CheckBox checkFondo3 = null;


	public OpcionesFragment() {
		
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_opciones, container, false);
        setHasOptionsMenu(true);
		prop = myContext.getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);

		checkNotificaciones = (CheckBox)rootView.findViewById(R.id.checkBoxNotificaciones);
		checkFondo1 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo1);
		checkFondo2 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo2);
		checkFondo3 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo3);

		checkNotificaciones.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkNotificaciones.isChecked()) {

				}else {

				}
			}
		});

		checkFondo1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkFondo2.isChecked())
					checkFondo2.setChecked(false);
				if(checkFondo3.isChecked())
					checkFondo3.setChecked(false);
				guardaFondoDePantalla(1);
				cargaFondoDePantalla();
			}
		});

		checkFondo2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkFondo1.isChecked())
					checkFondo1.setChecked(false);
				if (checkFondo3.isChecked())
					checkFondo3.setChecked(false);
				guardaFondoDePantalla(2);
				cargaFondoDePantalla();
			}
		});

		checkFondo3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkFondo1.isChecked())
					checkFondo1.setChecked(false);
				if(checkFondo2.isChecked())
					checkFondo2.setChecked(false);
				guardaFondoDePantalla(3);
				cargaFondoDePantalla();
			}
		});

		TextView textViewPolitica = (TextView)rootView.findViewById(R.id.textView29);
		textViewPolitica.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new PoliticaFragment();
				FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});

		TextView textViewTerminos = (TextView)rootView.findViewById(R.id.textView31);
		textViewTerminos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new TerminosFragment();
				FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});

        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.opciones, menu);
	}

	@Override
	public void onResume(){
		super.onResume();
		cargaFondoDePantalla();
	}

	private synchronized void cargaFondoDePantalla() {
		try {
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
					checkFondo1.setChecked(true);
					checkFondo2.setChecked(false);
					checkFondo3.setChecked(false);
					break;
				case 2:
					imagen = Constantes.FONDO_2;
					checkFondo1.setChecked(false);
					checkFondo2.setChecked(true);
					checkFondo3.setChecked(false);
					break;
				case 3:
					imagen = Constantes.FONDO_3;
					checkFondo1.setChecked(false);
					checkFondo2.setChecked(false);
					checkFondo3.setChecked(true);
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

	private synchronized void guardaFondoDePantalla(int fondo) {
		try {
			if(prop != null) {
				SharedPreferences.Editor editor = prop.edit();
				if(editor != null) {
					editor.putInt(Constantes.FONDO_PANTALLA, fondo);
					editor.commit();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
