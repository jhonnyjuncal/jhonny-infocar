package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleAccidente;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class AccidentesFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaAccidentes;
	private LinearLayout layoutAccidentes;
	private ArrayList<DetalleAccidente> accidentes;
	private View rootView;
	
	
	public AccidentesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_accidentes, container, false);
		setHasOptionsMenu(true);
		
		fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_accidentes);
		vistaAccidentes = (ScrollView)fragmento.findViewById(R.id.acc_scrollView1);
		layoutAccidentes = (LinearLayout)vistaAccidentes.findViewById(R.id.acc_linear);
		accidentes = recuperaDatosAccidentes();
		
		int i = 0;
		for(DetalleAccidente acc : accidentes) {
			View vista = inflater.inflate(R.layout.detalle_accidente, layoutAccidentes, false);
			
			vista.setId(i);
			TextView tv1 = (TextView)vista.findViewById(R.id.det_acc_textView1);
			tv1.setText(acc.getFecha().toString());
			TextView tv2 = (TextView)vista.findViewById(R.id.det_acc_textView3);
			tv2.setText(acc.getKilometros().toString());
			TextView tv3 = (TextView)vista.findViewById(R.id.det_acc_textView5);
			tv3.setText(acc.getLugar());
			TextView tv4 = (TextView)vista.findViewById(R.id.det_acc_textView7);
			tv4.setText(acc.getObservaciones());
			
			ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView_editar);
			imgEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					
					DetalleAccidente da = accidentes.get(linear3.getId());
					Toast.makeText(rootView.getContext(), "editar: " + linear3.getId() + " - lista: " + da.getIdDetalleAccidente(), Toast.LENGTH_SHORT).show();
				}
			});
			
			ImageView imgBorrar = (ImageView)vista.findViewById(R.id.imageView_borrar);
			imgBorrar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					
					DetalleAccidente da = accidentes.get(linear3.getId());
					Toast.makeText(rootView.getContext(), "borrar: " + view.getId() + " - lista: " + da.getIdDetalleAccidente(), Toast.LENGTH_SHORT).show();
				}
			});
        	layoutAccidentes.addView(vista, i);
        	i++;
        }
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.accidentes, menu);
	}
	
	private ArrayList<DetalleAccidente> recuperaDatosAccidentes() {
		ArrayList<DetalleAccidente> detalles = new ArrayList<DetalleAccidente>();
		
		try {
			DetalleAccidente da1 = new DetalleAccidente();
			da1.setIdDetalleAccidente(0);
			da1.setFecha(new Date());
			da1.setLugar("lugar de prueba");
			da1.setKilometros(54010.0);
			da1.setObservaciones("Prueba de observaciones para el desarollo");
			da1.setIdVehiculo(0);
			detalles.add(da1);
			
			DetalleAccidente da2 = new DetalleAccidente();
			da2.setIdDetalleAccidente(1);
			da2.setFecha(new Date());
			da2.setLugar("otra de prueba");
			da2.setKilometros(64900.0);
			da2.setObservaciones("otra observaciones para mis pruebas");
			da2.setIdVehiculo(0);
			detalles.add(da2);
			
			DetalleAccidente da3 = new DetalleAccidente();
			da3.setIdDetalleAccidente(2);
			da3.setFecha(new Date());
			da3.setLugar("mas pruebas");
			da3.setKilometros(79512.8);
			da3.setObservaciones("mas observacionnes de pruebas de para el desarrollo de mi aplicacion que me hara ganar dinero");
			da3.setIdVehiculo(0);
			detalles.add(da3);
			
			DetalleAccidente da4 = new DetalleAccidente();
			da4.setIdDetalleAccidente(3);
			da4.setFecha(new Date());
			da4.setLugar("la ultima");
			da4.setKilometros(99000.0);
			da4.setObservaciones("ultima prueba para probar la maquetacion de los componentes añadidos");
			da4.setIdVehiculo(0);
			detalles.add(da4);
			
			DetalleAccidente da5 = new DetalleAccidente();
			da5.setIdDetalleAccidente(4);
			da5.setFecha(new Date());
			da5.setLugar("la ultima");
			da5.setKilometros(120000.0);
			da5.setObservaciones("otra ultima prueba de datos y de maquetacion para el componente de lalista de accidentes");
			da5.setIdVehiculo(0);
			detalles.add(da5);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
}
