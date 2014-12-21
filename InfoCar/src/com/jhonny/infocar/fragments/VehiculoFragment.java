package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleVehiculo;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
	
	private ArrayAdapter<String> adapterMarcas;
	private ArrayAdapter<String> adapterCarburantes;
	private ArrayAdapter<String> adapterTiposVeh;
	private TypedArray arrayMarcas;
	private TypedArray arrayCarburantes;
	private TypedArray arrayTiposVeh;
	private Spinner spinnerMarcas = null;
	private Spinner spinnerCarburantes = null;
	private Spinner spinnerTiposVeh = null;
	private ArrayList<String> listaMarcas = new ArrayList<String>();
	private ArrayList<String> listaTiposVeh = new ArrayList<String>();
	private ArrayList<String> listaCarburantes = new ArrayList<String>();
	
	
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
        
        arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
		arrayMarcas.recycle();
		for(int i=0; i<arrayMarcas.length(); i++)
			listaMarcas.add(arrayMarcas.getString(i));
		
		arrayTiposVeh = getResources().obtainTypedArray(R.array.TIPOS_VEHICULO);
		arrayTiposVeh.recycle();
		for(int i=0; i<arrayTiposVeh.length(); i++)
			listaTiposVeh.add(arrayTiposVeh.getString(i));
		
		arrayCarburantes = getResources().obtainTypedArray(R.array.TIPOS_CARBURANTE);
		arrayCarburantes.recycle();
		for(int i=0; i<arrayCarburantes.length(); i++)
			listaCarburantes.add(arrayCarburantes.getString(i));
		
        
        int i = 0;
        for(DetalleVehiculo dv : vehiculos) {
        	View vista = inflater.inflate(R.layout.detalle_vehiculo, layoutVehiculos, false);
        	
        	vista.setId(i);
        	TextView tv1 = (TextView)vista.findViewById(R.id.det_veh_textView1);
        	tv1.setText(dv.getFechaCompra().toString());
        	TextView tv2 = (TextView)vista.findViewById(R.id.det_veh_textView3);
        	String marcaVehiculo = listaMarcas.get(dv.getMarca());
        	tv2.setText(marcaVehiculo);
        	TextView tv3 = (TextView)vista.findViewById(R.id.det_veh_textView5);
        	tv3.setText(dv.getModelo());
        	TextView tv4 = (TextView)vista.findViewById(R.id.det_veh_textView7);
        	tv4.setText(dv.getFechaCompra().toString());
        	TextView tv5 = (TextView)vista.findViewById(R.id.det_veh_textView9);
        	String tipoVehiculo = listaTiposVeh.get(dv.getTipoVehiculo());
        	tv5.setText(tipoVehiculo);
        	TextView tv6 = (TextView)vista.findViewById(R.id.det_veh_textView11);
        	tv6.setText(dv.getMatricula());
        	TextView tv7 = (TextView)vista.findViewById(R.id.det_veh_textView13);
        	String tipoCarburante = listaCarburantes.get(dv.getTipoCarburante());
        	tv7.setText(tipoCarburante);
        	
        	ImageView imgItv = (ImageView)vista.findViewById(R.id.imageView2);
        	imgItv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("VehiculoFragment", "Mostrar fragmento de la ITV");
				}
			});
        	
        	ImageView imgSeguro = (ImageView)vista.findViewById(R.id.imageView3);
        	imgSeguro.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("VehiculoFragment", "Mostrar fragmento de los datos del seguro");
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
					
					adapterMarcas = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaMarcas);
					spinnerMarcas = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_marca);
					spinnerMarcas.setAdapter(adapterMarcas);
					spinnerMarcas.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							String marcaSeleccionada = arrayMarcas.getString(position);
							String texto = "Marca seleccionada: " + marcaSeleccionada;
							Log.d("VehiculoFragment", texto);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					spinnerMarcas.setSelection(dv.getMarca());
					
					EditText textModelo = (EditText)editDialog.findViewById(R.id.edit_veh_editText1);
					textModelo.setText(dv.getModelo());
					EditText textKilometros = (EditText)editDialog.findViewById(R.id.edit_veh_editText2);
					textKilometros.setText(dv.getKilometros().toString());
					EditText textFecha = (EditText)editDialog.findViewById(R.id.edit_veh_editText3);
					textFecha.setText(Constantes.SDF.format(dv.getFechaCompra()));
					
					adapterTiposVeh = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposVeh);
					spinnerTiposVeh = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_tipo);
					spinnerTiposVeh.setAdapter(adapterTiposVeh);
					spinnerTiposVeh.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							String tipoSeleccionado = arrayTiposVeh.getString(position);
							String texto = "Tipo vehiculo seleccionado: " + tipoSeleccionado;
							Log.d("VehiculoFragment", texto);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					spinnerTiposVeh.setSelection(dv.getTipoVehiculo());
					
					adapterCarburantes = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaCarburantes);
					spinnerCarburantes = (Spinner)editDialog.findViewById(R.id.edit_veh_spinner_carburante);
					spinnerCarburantes.setAdapter(adapterCarburantes);
					spinnerCarburantes.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							String carburanteSeleccionado = arrayCarburantes.getString(position);
							String texto = "Carburante seleccionado: " + carburanteSeleccionado;
							Log.d("VehiculoFragment", texto);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							Log.d("VehiculoFragment", "Nada seleccionado...");
						}
					});
					spinnerCarburantes.setSelection(dv.getTipoCarburante());
					
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
