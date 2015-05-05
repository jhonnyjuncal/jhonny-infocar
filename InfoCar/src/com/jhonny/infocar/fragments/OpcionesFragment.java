package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;


public class OpcionesFragment extends Fragment {
	
	public OpcionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_opciones, container, false);
        setHasOptionsMenu(true);

		final CheckBox checkNotificaciones = (CheckBox)rootView.findViewById(R.id.checkBoxNotificaciones);
		final CheckBox checkFondo1 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo1);
		final CheckBox checkFondo2 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo2);
		final CheckBox checkFondo3 = (CheckBox)rootView.findViewById(R.id.checkBoxFondo3);

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
			}
		});

		checkFondo2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkFondo1.isChecked())
					checkFondo1.setChecked(false);
				if(checkFondo3.isChecked())
					checkFondo3.setChecked(false);
			}
		});

		checkFondo3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkFondo1.isChecked())
					checkFondo1.setChecked(false);
				if(checkFondo2.isChecked())
					checkFondo2.setChecked(false);
			}
		});


        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.opciones, menu);
	}
}
