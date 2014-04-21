package com.jhonny.infocar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MenuOpciones extends Fragment {
	
	public static Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
	}
	
	public void muestraManteniminetos(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
	
	public void muestraReparaciones(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
	
	public void muestraAccidentes(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
	
	public void muestraDatos(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
	
	public void muestraEstadisticas(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
	
	public void muestraOpciones(View view){
		Intent intent = new Intent(MenuOpciones.context, MantenimientoActivity.class);
		startActivity(intent);
	}
}
