package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.adapters.DetalleMantenimientosAdapter;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MantenimientosFragment extends Fragment {
	
	private ListView listaVehiculos;
	private DetalleMantenimientosAdapter adapter;
	private ArrayList<DetalleMantenimiento> mantenimientos;
	
	
	public MantenimientosFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
//        listaVehiculos = (ListView)rootView.findViewById(R.id.fragment_mantenimiento);
//        mantenimientos = recuperaDatosMantenimiento();
//        adapter = new DetalleMantenimientosAdapter(rootView.getContext(), mantenimientos);
//        
//        listaVehiculos.setAdapter(adapter);
        
        return rootView;
    }
	
	private ArrayList<DetalleMantenimiento> recuperaDatosMantenimiento() {
		ArrayList<DetalleMantenimiento> detalles = null;
		
		try {
			detalles = new ArrayList<DetalleMantenimiento>();
			
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
			dm2.setTaller("Ford de coruña");
			dm2.setTipo("Mantenimiento");
			detalles.add(dm2);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
}
