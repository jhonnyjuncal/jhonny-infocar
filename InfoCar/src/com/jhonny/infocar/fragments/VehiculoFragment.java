package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.model.DetalleVehiculo;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


public class VehiculoFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaVehiculos;
	private LinearLayout layoutVehiculos;
	private ArrayList<DetalleVehiculo> vehiculos;
	private View rootView;
	private Dialog editDialog;
	
	
	public VehiculoFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_vehiculo, container, false);
        setHasOptionsMenu(true);
        
        fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_vehiculo);
        vistaVehiculos = (ScrollView)fragmento.findViewById(R.id.veh_scrollView1);
        layoutVehiculos = (LinearLayout)vistaVehiculos.findViewById(R.id.veh_linear);
        vehiculos = recuperaDatosVehiculos();
        
        int i = 0;
        for(DetalleVehiculo dv : vehiculos) {
        	View vista = inflater.inflate(R.layout.detalle_vehiculo, layoutVehiculos, false);
        	
        	vista.setId(i);
        	TextView tv1 = (TextView)vista.findViewById(R.id.det_veh_textView1);
        	tv1.setText(dv.getFechaCompra().toString());
        	TextView tv2 = (TextView)vista.findViewById(R.id.det_veh_textView3);
        	tv2.setText(String.valueOf(dv.getMarca()));
        	TextView tv3 = (TextView)vista.findViewById(R.id.det_veh_textView5);
        	tv3.setText(dv.getModelo());
        	TextView tv4 = (TextView)vista.findViewById(R.id.det_veh_textView7);
        	tv4.setText(dv.getFechaCompra().toString());
        	TextView tv5 = (TextView)vista.findViewById(R.id.det_veh_textView9);
        	tv5.setText(String.valueOf(dv.getTipoVehiculo()));
        	TextView tv6 = (TextView)vista.findViewById(R.id.det_veh_textView11);
        	tv6.setText(dv.getMatricula());
        	TextView tv7 = (TextView)vista.findViewById(R.id.det_veh_textView13);
        	tv7.setText(String.valueOf(dv.getTipoCarburante()));
        	
        	ImageView imgItv = (ImageView)vista.findViewById(R.id.imageView2);
        	imgItv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
        	
        	ImageView imgSeguro = (ImageView)vista.findViewById(R.id.imageView3);
        	imgSeguro.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
        	
        	ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView4);
        	imgEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View vista) {
					LinearLayout linear1 = (LinearLayout)vista.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					LinearLayout linear4 = (LinearLayout)linear3.getParent();
					DetalleVehiculo dv = vehiculos.get(linear4.getId());
					
					editDialog = new Dialog(rootView.getContext());
					editDialog.setContentView(R.layout.edicion_vehiculo);
					editDialog.setTitle("Edicion de vehiculo");
					
					Spinner spinnerMarca = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_marca);
					spinnerMarca.setSelection(dv.getMarca());
					EditText textModelo = (EditText)editDialog.findViewById(R.id.edit_veh_editText1);
					textModelo.setText(dv.getModelo());
					EditText textKilometros = (EditText)editDialog.findViewById(R.id.edit_veh_editText2);
					textKilometros.setText(dv.getKilometros().toString());
					EditText textFecha = (EditText)editDialog.findViewById(R.id.edit_veh_editText3);
					textFecha.setText(dv.getFechaCompra().toString());
					Spinner spinnerTipoVeh = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_tipo);
					spinnerTipoVeh.setSelection(dv.getTipoVehiculo());
					Spinner spinnerCarburante = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_carburante);
					spinnerCarburante.setSelection(dv.getTipoCarburante());
					EditText textMatricula = (EditText)editDialog.findViewById(R.id.edit_veh_editText4);
					textMatricula.setText(dv.getMatricula());
					
					Button btnGuardar = (Button)editDialog.findViewById(R.id.edit_veh_button_guardar);
					btnGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							editDialog.dismiss();
						}
					});
					
					Button btnCancelar = (Button)editDialog.findViewById(R.id.edit_veh_button_cancelar);
					btnCancelar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							editDialog.cancel();
						}
					});
					
					editDialog.show();
				}
			});
        	
        	ImageView imgBorrar = (ImageView)vista.findViewById(R.id.imageView5);
        	imgBorrar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
        	layoutVehiculos.addView(vista, i);
        	i++;
        }
        
        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.vehiculo, menu);
	}
	
	private ArrayList<DetalleVehiculo> recuperaDatosVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		
		try {
			DetalleVehiculo dv1 = new DetalleVehiculo();
			dv1.setIdVehiculo(0);
			dv1.setMarca(1);
			dv1.setModelo("Focus");
			dv1.setKilometros(25000.0);
			dv1.setFechaCompra(new Date());
			dv1.setTipoVehiculo(1);
			dv1.setTipoCarburante(1);
			dv1.setIdSeguro(1);
			dv1.setIdItv(0);
			lista.add(dv1);
			
			DetalleVehiculo dv2 = new DetalleVehiculo();
			dv2.setIdVehiculo(1);
			dv2.setMarca(3);
			dv2.setModelo("Audi");
			dv2.setKilometros(10000.0);
			dv2.setFechaCompra(new Date());
			dv2.setTipoVehiculo(1);
			dv2.setTipoCarburante(1);
			dv2.setIdSeguro(2);
			dv2.setIdItv(0);
			lista.add(dv2);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}
}
