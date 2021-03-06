package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleReparacion;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ReparacionesSQLiteHelper;
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


public class NuevaReparacionFragment extends Fragment {
	
	private View rootView;
	private FragmentActivity myContext;
	
	private EditText editFecha;
	private ImageView imagenCalendar;
	private EditText editKms;
	private EditText editPrecio;
	private EditText editTaller;
	private Spinner spinnerTipo;
	private Spinner spinnerVehiculo;
	private EditText editObservaciones;

	private TypedArray arrayTiposReparacion = null;
	private TypedArray arrayMarcas = null;
	private ArrayList<String> listaTiposReparacion = new ArrayList<String>();
	private ArrayList<String> listaMisVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> vehiculos = new ArrayList<DetalleVehiculo>();
	private DetalleReparacion detalleEnEdicion;



	public static NuevaReparacionFragment newInstance(DetalleReparacion dr) {
		NuevaReparacionFragment frag = new NuevaReparacionFragment();
		if(dr != null) {
			Bundle args = new Bundle();
			args.putInt("IdDetalleReparacion", dr.getIdDetalleReparacion());
			args.putString("Fecha", Util.convierteDateEnString(dr.getFecha()));
			args.putDouble("Kilometros", dr.getKilometros());
			args.putDouble("Precio", dr.getPrecio());
			args.putInt("IdTipoReparacion", dr.getIdTipoReparacion());
			args.putString("Taller", dr.getTaller());
			args.putString("Observaciones", dr.getObservaciones());
			args.putInt("IdVehiculo", dr.getIdVehiculo());
			frag.setArguments(args);
		}
		return frag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		try {
			rootView = inflater.inflate(R.layout.fragment_nueva_reparacion, container, false);

			Bundle arguments = getArguments();
			if(arguments != null) {
				detalleEnEdicion = new DetalleReparacion();
				detalleEnEdicion.setIdDetalleReparacion(arguments.getInt("IdDetalleReparacion"));
				detalleEnEdicion.setFecha(Util.convierteStringEnDate(arguments.getString("Fecha")));
				detalleEnEdicion.setKilometros(arguments.getDouble("Kilometros"));
				detalleEnEdicion.setPrecio(arguments.getDouble("Precio"));
				detalleEnEdicion.setIdTipoReparacion(arguments.getInt("IdTipoReparacion"));
				detalleEnEdicion.setTaller(arguments.getString("Taller"));
				detalleEnEdicion.setObservaciones(arguments.getString("Observaciones"));
				detalleEnEdicion.setIdVehiculo(arguments.getInt("IdVehiculo"));
			}

			editFecha = (EditText)rootView.findViewById(R.id.nue_rep_edit_fecha);
			editKms = (EditText)rootView.findViewById(R.id.nue_rep_edit_kms);
			editPrecio = (EditText)rootView.findViewById(R.id.nue_rep_edit_precio);
			editTaller = (EditText)rootView.findViewById(R.id.nue_rep_edit_taller);
			editObservaciones = (EditText)rootView.findViewById(R.id.nue_rep_edit_obs);
			
			arrayTiposReparacion = getResources().obtainTypedArray(R.array.TIPOS_REPARACIONES);
			arrayTiposReparacion.recycle();
			for(int i=0; i<arrayTiposReparacion.length(); i++)
				listaTiposReparacion.add(arrayTiposReparacion.getString(i));
			spinnerTipo = (Spinner)rootView.findViewById(R.id.nue_rep_spinner_tipo);
			ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposReparacion);
			spinnerTipo.setAdapter(adapterTipo);

			vehiculos = recuperaDatosVehiculos();
			arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
			arrayMarcas.recycle();
			listaMisVehiculos.add(getResources().getString(R.string.label_add_vehiculo));
			for(DetalleVehiculo vehiculo : vehiculos) {
				String marca = arrayMarcas.getString(vehiculo.getMarca());
				listaMisVehiculos.add(marca + " " + vehiculo.getModelo());
			}
			spinnerVehiculo = (Spinner)rootView.findViewById(R.id.nue_rep_spinner_vehiculo);
			ArrayAdapter<String> adapterVehiculos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaMisVehiculos);
			spinnerVehiculo.setAdapter(adapterVehiculos);

			/*
			botonGuardar = (Button)rootView.findViewById(R.id.nue_rep_boton_guardar);
			botonGuardar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DetalleReparacion dr = new DetalleReparacion();
					dr.setIdDetalleReparacion(null);
					dr.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
					dr.setKilometros(Double.valueOf(editKms.getText().toString()));
					dr.setPrecio(Double.valueOf(editPrecio.getText().toString()));
					dr.setTaller(editTaller.getText().toString());
					dr.setObservaciones(editObservaciones.getText().toString());
					dr.setIdVehiculo(vehiculos.get(spinnerVehiculo.getSelectedItemPosition()-1).getIdVehiculo());
					dr.setIdTipoReparacion(spinnerTipo.getSelectedItemPosition());
					
					if(comprobacionDatos(dr)) {
						guardaDatosDeLaReparacion(dr);
                        actualizaListaReparaciones();
					}
				}
			});
			*/

			/*
			botonCancelar = (Button)rootView.findViewById(R.id.nue_rep_boton_cancelar);
			botonCancelar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    actualizaListaReparaciones();
				}
			});
			*/
			
			imagenCalendar = (ImageView)rootView.findViewById(R.id.nue_rep_imageView1);
			imagenCalendar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					
					DatePickerDialog dp = new DatePickerDialog(myContext, new OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							editFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
						}
					}, year, month, day);
					dp.show();
				}
			});

			if(detalleEnEdicion != null) {
				editFecha.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
				editKms.setText(detalleEnEdicion.getKilometros().toString());
				editPrecio.setText(detalleEnEdicion.getPrecio().toString());
				editTaller.setText(detalleEnEdicion.getTaller());
				editObservaciones.setText(detalleEnEdicion.getObservaciones());
				spinnerTipo.setSelection(detalleEnEdicion.getIdTipoReparacion());
				spinnerVehiculo.setSelection(detalleEnEdicion.getIdVehiculo());
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.nuevo_accidente, menu);
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		myContext.setTitle(getResources().getString(R.string.title_activity_nueva_reparacion));
		super.onAttach(activity);
	}
	
	private boolean comprobacionDatos(DetalleReparacion dr) {
		boolean resp = true;
		String mensaje = null;
		if(dr.getFecha() == null) {
			mensaje = getResources().getString(R.string.mensaje_validacion_fecha_rep_valida);
			resp = false;
		}else if(dr.getPrecio() == null || dr.getPrecio() < 0) {
			mensaje = getResources().getString(R.string.mensaje_validacion_precio_valido);
			resp = false;
		}else if(dr.getIdTipoReparacion() == 0) {
			mensaje = getResources().getString(R.string.mensaje_validacion_tipo_rep_valido);
			resp = false;
		}else if(dr.getIdVehiculo() == 0) {
			mensaje = getResources().getString(R.string.mensaje_validacion_seleccione_veh);
			resp = false;
		}
		if(resp == false)
			Toast.makeText(myContext, mensaje, Toast.LENGTH_SHORT).show();
		return resp;
	}
	
	private void guardaDatosDeLaReparacion(DetalleReparacion dr) {
		ReparacionesSQLiteHelper reparacionesHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
		boolean resultado = reparacionesHelper.insertarReparacion(dr);

		String texto = null;
		if(resultado)
			texto = getResources().getString(R.string.mensaje_guardar_ok);
		else
			texto = getResources().getString(R.string.mensaje_guardar_error);
		Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();
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

    private void actualizaListaReparaciones() {
        Fragment fragment = new ReparacionesFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.action_guardar:
				try {
					DetalleReparacion dr = new DetalleReparacion();
					dr.setIdDetalleReparacion(null);
					dr.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
					dr.setKilometros(Double.valueOf(editKms.getText().toString()));
					dr.setPrecio(Double.valueOf(editPrecio.getText().toString()));
					dr.setTaller(editTaller.getText().toString());
					dr.setObservaciones(editObservaciones.getText().toString());
					dr.setIdVehiculo(vehiculos.get(spinnerVehiculo.getSelectedItemPosition()-1).getIdVehiculo());
					dr.setIdTipoReparacion(spinnerTipo.getSelectedItemPosition());

					if(comprobacionDatos(dr)) {
						guardaDatosDeLaReparacion(dr);
						actualizaListaReparaciones();
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
					actualizaListaReparaciones();
					return true;
				}
				return false;
			}
		});
	}
}
