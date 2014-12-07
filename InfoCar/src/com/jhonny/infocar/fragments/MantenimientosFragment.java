package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.adapters.DetalleMantenimientosAdapter;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;


public class MantenimientosFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ListView listaMantenimientos;
	private DetalleMantenimientosAdapter adapter;
	private ArrayList<DetalleMantenimiento> mantenimientos;
	private Spinner filtro;
	
	
	public MantenimientosFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
        setHasOptionsMenu(true);
        
        fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_mantenimiento);
        filtro = (Spinner)fragmento.findViewById(R.id.mant_spinner1);
        listaMantenimientos = (ListView)fragmento.findViewById(R.id.mant_listView1);
        mantenimientos = recuperaDatosMantenimiento();
        adapter = new DetalleMantenimientosAdapter(rootView.getContext(), mantenimientos);
        listaMantenimientos.setAdapter(adapter);
        
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
			DetalleMantenimiento dm1 = new DetalleMantenimiento();
			dm1.setId(0);
			dm1.setFecha(new Date());
			dm1.setKms(Double.valueOf(20000));
			dm1.setObservaciones("Revision de los 20mil kms");
			dm1.setPrecio(Double.valueOf(220));
			dm1.setTaller("Ford de santiago");
			dm1.setTipo("Mantenimiento");
			detalles.add(dm1);
			
			DetalleMantenimiento dm2 = new DetalleMantenimiento();
			dm2.setId(1);
			dm2.setFecha(new Date());
			dm2.setKms(Double.valueOf(40000));
			dm2.setObservaciones("Revision de los 40mil kms");
			dm2.setPrecio(Double.valueOf(250));
			dm2.setTaller("Ford de coru�a");
			dm2.setTipo("Mantenimiento");
			detalles.add(dm2);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
}
