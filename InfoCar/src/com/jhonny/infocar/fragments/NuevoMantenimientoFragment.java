package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class NuevoMantenimientoFragment extends Fragment {
	
	private View rootView;
	private Context myContext;
	
	private EditText editFecha;
	private ImageView imagenCalendario;
	private EditText editKms;
	private EditText editPrecio;
	private EditText editTaller;
	private Spinner spinnerTipo;
	private Spinner spinnerVehiculo;
	private EditText editObservaciones;
	private Button botonGuardar;
	private Button botonCancelar;
	
	private TypedArray arrayTiposMantenimientos = null;
	private TypedArray arrayMarcas = null;
	private ArrayList<String> listaTiposMantenimientos = new ArrayList<String>();
	private ArrayList<String> listaVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> vehiculos = new ArrayList<DetalleVehiculo>();
	
	private MantenimientosSQLiteHelper mantenimientosHelper;
	private SQLiteDatabase baseDatos;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_nuevo_mantenimiento, container, false);
		setHasOptionsMenu(true);
		
		try {
			editFecha = (EditText)rootView.findViewById(R.id.edit_mant_fecha);
			editKms = (EditText)rootView.findViewById(R.id.edit_mant_kms);
			editPrecio = (EditText)rootView.findViewById(R.id.edit_mant_precio);
			editTaller = (EditText)rootView.findViewById(R.id.edit_mant_taller);
			editObservaciones = (EditText)rootView.findViewById(R.id.edit_mant_observaciones);
			
			arrayTiposMantenimientos = getResources().obtainTypedArray(R.array.TIPOS_MANTENIMIENTOS);
			arrayTiposMantenimientos.recycle();
	        for(int i=0; i<arrayTiposMantenimientos.length(); i++)
	        	listaTiposMantenimientos.add(arrayTiposMantenimientos.getString(i));
			spinnerTipo = (Spinner)rootView.findViewById(R.id.spinner_mant_tipo);
			ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposMantenimientos);
			spinnerTipo.setAdapter(adapterTipo);
			
			vehiculos = recuperaDatosVehiculos();
			arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
			for(DetalleVehiculo vehiculo : vehiculos) {
				String marca = arrayMarcas.getString(vehiculo.getMarca());
				listaVehiculos.add(marca + " " + vehiculo.getModelo());
			}
			ArrayAdapter<String> adapterVehiculo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaVehiculos);
			spinnerVehiculo = (Spinner)rootView.findViewById(R.id.spinner_mant_vehiculo);
			spinnerVehiculo.setAdapter(adapterVehiculo);
			
			imagenCalendario = (ImageView)rootView.findViewById(R.id.image_mant_fecha);
			imagenCalendario.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					
					DatePickerDialog dp = new DatePickerDialog(rootView.getContext(), new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							editFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
						}
					}, year, month, day);
					
					dp.show();
				}
			});
			
			botonGuardar = (Button)rootView.findViewById(R.id.boton_mant_guardar);
			botonGuardar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						DetalleMantenimiento dm = new DetalleMantenimiento();
						dm.setIdDetalleMantenimiento(null);
						dm.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
						dm.setKilometros(Double.valueOf(editKms.getText().toString()));
						dm.setPrecio(Double.valueOf(editPrecio.getText().toString()));
						dm.setTaller(editTaller.getText().toString());
						dm.setObservaciones(editObservaciones.getText().toString());
						dm.setTipoMantenimiento(spinnerTipo.getSelectedItemPosition());
						dm.setIdVehiculo(vehiculos.get(spinnerVehiculo.getSelectedItemPosition()).getIdVehiculo());
						
						if(comprobacionDatosMantenimiento(dm)) {
							boolean resp = abrirBaseDeDatos();
							if(resp == false) {
								String texto = "Error al abrir o crear la tabla 'Accidentes'";
								Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
								return;
							}
							
							boolean resultado = insertarFila(dm);
							String texto = new String();
							if(resultado)
								texto = "Datos guardados correctamente";
							else
								texto = "Error al guardar los datos";
							Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();
							
							actualizarListadoDeMantenimientos();
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			
			botonCancelar = (Button)rootView.findViewById(R.id.boton_mant_cancelar);
			botonCancelar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					actualizarListadoDeMantenimientos();
				}
			});
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}
	
	private boolean insertarFila(DetalleMantenimiento dm) {
		boolean resp = false;
		try {
			ContentValues values = new ContentValues();
			values.put("idMantenimiento", dm.getIdDetalleMantenimiento());
			values.put("fecha", dm.getFecha().getTime());
			values.put("kms", dm.getKilometros());
			values.put("precio", dm.getPrecio());
			values.put("taller", dm.getTaller());
			values.put("tipoMantenimiento", dm.getTipoMantenimiento());
			values.put("observaciones", dm.getObservaciones());
			values.put("idVehiculo", dm.getIdVehiculo());
			
			if(dm.getIdDetalleMantenimiento() == null) {
				resp = (baseDatos.insert(Constantes.TABLA_MANTENIMIENTOS, null, values) > 0);
			}else {
				String[] argumentos = new String[1];
				argumentos[0] = String.valueOf(dm.getIdDetalleMantenimiento());
				resp = (baseDatos.update(Constantes.TABLA_ACCIDENTES, values, "idMantenimiento = ?", argumentos) > 0);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	private boolean comprobacionDatosMantenimiento(DetalleMantenimiento dm) {
		boolean resp = true;
		String mensaje = new String();
		
		if(dm.getFecha() == null) {
			mensaje = "Debe introducir la fecha del mantenimiento";
			resp = false;
			
		}else if(dm.getKilometros() == null || dm.getKilometros() <= 0) {
			mensaje = "Debe introducir los kilometros cuando se realizo el mantenimiento";
			resp = false;
			
		}else if(dm.getPrecio() == null || dm.getPrecio() <= 0) {
			mensaje = "Debe introducir el precio del mantenimiento";
			resp = false;
		}
		
		if(resp == false)
			Toast.makeText(myContext, mensaje, Toast.LENGTH_SHORT).show();
		return resp;
	}
	
	private ArrayList<DetalleVehiculo> recuperaDatosVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		try {
			VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(rootView.getContext(), Constantes.TABLA_VEHICULOS, null, 1);
			lista.addAll(vehiculosHelper.getVehiculos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}
	
	private boolean abrirBaseDeDatos() {
		boolean resultado = false;
		try {
			mantenimientosHelper = new MantenimientosSQLiteHelper(rootView.getContext(), Constantes.TABLA_MANTENIMIENTOS, null, 1);
			baseDatos = mantenimientosHelper.getWritableDatabase();
			resultado = true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultado;
	}
	
	private void actualizarListadoDeMantenimientos() {
		Fragment fragment = new MantenimientosFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}
}
