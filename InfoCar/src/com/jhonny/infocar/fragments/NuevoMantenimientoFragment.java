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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private FragmentActivity myContext;
	private DetalleMantenimiento detalleEnEdicion;
	
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


	public static NuevoMantenimientoFragment newInstance(DetalleMantenimiento dm) {
		Bundle args = new Bundle();
		args.putInt("IdDetalleMantenimiento", dm.getIdDetalleMantenimiento());
		args.putString("Fecha", Util.convierteDateEnString(dm.getFecha()));
		args.putDouble("Kilometros", dm.getKilometros());
		args.putDouble("Precio", dm.getPrecio());
		args.putString("Taller", dm.getTaller());
		args.putInt("TipoMantenimiento", dm.getTipoMantenimiento());
		args.putString("Observaciones", dm.getObservaciones());
		args.putInt("IdVehiculo", dm.getIdVehiculo());
		
		NuevoMantenimientoFragment frag = new NuevoMantenimientoFragment();
		frag.setArguments(args);
		return frag;
	}

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		try {
			rootView = inflater.inflate(R.layout.fragment_nuevo_mantenimiento, container, false);

			Bundle arguments = getArguments();
			if(arguments != null) {
				detalleEnEdicion = new DetalleMantenimiento();
				detalleEnEdicion.setIdDetalleMantenimiento(arguments.getInt("IdDetalleMantenimiento"));
				detalleEnEdicion.setFecha(Util.convierteStringEnDate(arguments.getString("Fecha")));
				detalleEnEdicion.setKilometros(arguments.getDouble("Kilometros"));
				detalleEnEdicion.setPrecio(arguments.getDouble("Precio"));
				detalleEnEdicion.setTaller(arguments.getString("Taller"));
				detalleEnEdicion.setTipoMantenimiento(arguments.getInt("TipoMantenimiento"));
				detalleEnEdicion.setObservaciones(arguments.getString("Observaciones"));
				detalleEnEdicion.setIdVehiculo(arguments.getInt("IdVehiculo"));
			}

			editFecha = (EditText)rootView.findViewById(R.id.edit_mant_fecha);
			editKms = (EditText)rootView.findViewById(R.id.edit_mant_kms);
			editPrecio = (EditText)rootView.findViewById(R.id.edit_mant_precio);
			editTaller = (EditText)rootView.findViewById(R.id.edit_mant_taller);
			editObservaciones = (EditText)rootView.findViewById(R.id.edit_mant_observaciones);
			
			arrayTiposMantenimientos = getResources().obtainTypedArray(R.array.TIPOS_MANTENIMIENTOS);
	        for(int i=0; i<arrayTiposMantenimientos.length(); i++)
	        	listaTiposMantenimientos.add(arrayTiposMantenimientos.getString(i));
			arrayTiposMantenimientos.recycle();
			spinnerTipo = (Spinner)rootView.findViewById(R.id.spinner_mant_tipo);
			ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposMantenimientos);
			spinnerTipo.setAdapter(adapterTipo);

			listaVehiculos.add(getResources().getString(R.string.label_add_vehiculo));
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

			/*
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
                            MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
							boolean resultado = mantHelper.insertarMantenimiento(dm);

							String texto = "";
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
			*/

			if(detalleEnEdicion != null) {
				editFecha.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
				editKms.setText(detalleEnEdicion.getKilometros().toString());
				editPrecio.setText(detalleEnEdicion.getPrecio().toString());
				editTaller.setText(detalleEnEdicion.getTaller());
				editObservaciones.setText(detalleEnEdicion.getObservaciones());
				spinnerTipo.setSelection(detalleEnEdicion.getTipoMantenimiento());
				spinnerVehiculo.setSelection(detalleEnEdicion.getIdVehiculo());
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		myContext.setTitle(getResources().getString(R.string.title_activity_nuevo_mantenimiento));
		super.onAttach(activity);
	}
	
	private boolean comprobacionDatosMantenimiento(DetalleMantenimiento dm) {
		boolean resp = true;
		String mensaje = new String();

		if(dm == null) {
			mensaje = getResources().getString(R.string.mensaje_validacion_datos_vacios);
			resp = false;
		}else if(dm.getFecha() == null) {
			mensaje = getResources().getString(R.string.mensaje_validacion_fecha_man_valido);
			resp = false;
		}else if(dm.getKilometros() == null || dm.getKilometros() < 0) {
			mensaje = getResources().getString(R.string.mensaje_validacion_kilom_man_valido);
			resp = false;
		}else if(dm.getPrecio() == null || dm.getPrecio() < 0) {
			mensaje = getResources().getString(R.string.mensaje_validacion_precio_valido);
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
	
	private void actualizarListadoDeMantenimientos() {
		Fragment fragment = new MantenimientosFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.nuevo_mantenimiento, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.action_guardar:
				try {
					if(detalleEnEdicion == null) {
						detalleEnEdicion = new DetalleMantenimiento();
						detalleEnEdicion.setIdDetalleMantenimiento(null);
					}
					detalleEnEdicion.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
					detalleEnEdicion.setKilometros(Double.valueOf(editKms.getText().toString()));
					detalleEnEdicion.setPrecio(Double.valueOf(editPrecio.getText().toString()));
					detalleEnEdicion.setTaller(editTaller.getText().toString());
					detalleEnEdicion.setObservaciones(editObservaciones.getText().toString());
					detalleEnEdicion.setTipoMantenimiento(spinnerTipo.getSelectedItemPosition());
					detalleEnEdicion.setIdVehiculo(vehiculos.get(spinnerVehiculo.getSelectedItemPosition()-1).getIdVehiculo());

					if(comprobacionDatosMantenimiento(detalleEnEdicion)) {
						MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);

						boolean resultado = false;
						if(detalleEnEdicion.getIdDetalleMantenimiento() == null)
							resultado = mantHelper.insertarMantenimiento(detalleEnEdicion);
						else
							resultado = mantHelper.actualizarMantenimiento(detalleEnEdicion);

						String texto = "";
						if(resultado)
							texto = getResources().getString(R.string.mensaje_guardar_ok);
						else
							texto = getResources().getString(R.string.mensaje_guardar_error);
						Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();

						actualizarListadoDeMantenimientos();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Util.cargaFondoDePantalla(myContext);

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
					actualizarListadoDeMantenimientos();
					return true;
				}
				return false;
			}
		});
	}
}
