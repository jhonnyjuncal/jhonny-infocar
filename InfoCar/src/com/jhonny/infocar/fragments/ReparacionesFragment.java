package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleReparacion;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class ReparacionesFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaReparaciones;
	private LinearLayout layoutReparaciones;
	private ArrayList<DetalleReparacion> reparaciones;
	private View rootView;
	
	
	public ReparacionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
		setHasOptionsMenu(true);
		
		fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_reparaciones);
		vistaReparaciones = (ScrollView)fragmento.findViewById(R.id.rep_scrollView1);
		layoutReparaciones = (LinearLayout)vistaReparaciones.findViewById(R.id.rep_linear);
		reparaciones = recuperaDatosReparaciones();
		
		int i = 0;
		for(DetalleReparacion detalle : reparaciones) {
			View vista = inflater.inflate(R.layout.detalle_reparacion, layoutReparaciones, false);
			
			vista.setId(i);
			TextView tv1 = (TextView)vista.findViewById(R.id.det_rep_textView1);
			tv1.setText(detalle.getFecha().toString());
			TextView tv2 = (TextView)vista.findViewById(R.id.det_rep_textView3);
			tv2.setText(String.valueOf(detalle.getFecha().getTime()));
			TextView tv3 = (TextView)vista.findViewById(R.id.det_rep_textView5);
			tv3.setText(detalle.getKilometros().toString());
			TextView tv4 = (TextView)vista.findViewById(R.id.det_rep_textView7);
			tv4.setText(detalle.getPrecio().toString());
			TextView tv5 = (TextView)vista.findViewById(R.id.det_rep_textView9);
			tv5.setText(String.valueOf(detalle.getIdTipoReparacion()));
			TextView tv6 = (TextView)vista.findViewById(R.id.det_rep_textView11);
			tv6.setText(detalle.getTaller());
			
			ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView_editar);
			imgEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					
					DetalleReparacion dr = reparaciones.get(linear3.getId());
					Toast.makeText(rootView.getContext(), "editar: " + linear3.getId() + " - lista: " + dr.getIdDetalleReparacion(), Toast.LENGTH_SHORT).show();
				}
			});
			
			ImageView imgBorrar = (ImageView)vista.findViewById(R.id.imageView_borrar);
			imgBorrar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					
					DetalleReparacion dr = reparaciones.get(linear3.getId());
					Toast.makeText(rootView.getContext(), "borrar: " + linear3.getId() + " - lista: " + dr.getIdDetalleReparacion(), Toast.LENGTH_SHORT).show();
				}
			});
			layoutReparaciones.addView(vista, i);
			i++;
		}
		return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.reparaciones, menu);
	}
	
	private ArrayList<DetalleReparacion> recuperaDatosReparaciones() {
		ArrayList<DetalleReparacion> lista = new ArrayList<DetalleReparacion>();
		
		try {
			DetalleReparacion dr1 = new DetalleReparacion();
			dr1.setIdDetalleReparacion(0);
			dr1.setFecha(new Date());
			dr1.setKilometros(25000.0);
			dr1.setPrecio(250.0);
			dr1.setTaller("Ford");
			dr1.setObservaciones("Reparacion en el taller ford de madrid");
			dr1.setIdTipoReparacion(1);
			dr1.setIdVehiculo(0);
			lista.add(dr1);
			
			DetalleReparacion dr2 = new DetalleReparacion();
			dr2.setIdDetalleReparacion(1);
			dr2.setFecha(new Date());
			dr2.setKilometros(36000.0);
			dr2.setPrecio(350.0);
			dr2.setTaller("Audi");
			dr2.setObservaciones("Reparacion en el taller audi de barcelona");
			dr2.setIdTipoReparacion(2);
			dr2.setIdVehiculo(0);
			lista.add(dr2);
			
			DetalleReparacion dr3 = new DetalleReparacion();
			dr3.setIdDetalleReparacion(2);
			dr3.setFecha(new Date());
			dr3.setKilometros(47000.0);
			dr3.setPrecio(280.0);
			dr3.setTaller("Volkswagen");
			dr3.setObservaciones("cambio repentino de filtro de aceite y filtros de aire");
			dr3.setIdTipoReparacion(3);
			dr3.setIdVehiculo(0);
			lista.add(dr3);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}
}
