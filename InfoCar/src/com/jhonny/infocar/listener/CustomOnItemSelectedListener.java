package com.jhonny.infocar.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String texto = new String("posicion: " + position);
		Toast.makeText(parent.getContext(), texto, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
