package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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


public class NuevoVehiculoFragment extends Fragment {
	
	private View rootView = null;
	private boolean mostrarBotonDespues = false;
	
	private Spinner spinnerMarcas = null;
	private EditText editModelo = null;
	private EditText editKms = null;
	private EditText editFecha = null;
	private ImageView imagenCalendario = null;
	private EditText editMatricula = null;
	private Spinner spinnerTipo = null;
	private Spinner spinnerCombustible = null;
	
	private ArrayAdapter<String> adapterMarcas;
	private ArrayAdapter<String> adapterTipo;
	private ArrayAdapter<String> adapterCarburante;
	private TypedArray arrayMarcas;
	private TypedArray arrayTiposVehiculos;
	private TypedArray arrayCarburantes;
	
	private SQLiteDatabase baseDatos;
	private FragmentActivity myContext;
	private SharedPreferences propiedades;
	
	
	public static NuevoVehiculoFragment newInstance(Bundle arguments){
		NuevoVehiculoFragment f = new NuevoVehiculoFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
	
	public NuevoVehiculoFragment() {
		
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		try {
			rootView = inflater.inflate(R.layout.fragment_nuevo_vehiculo, container, false);
			spinnerMarcas = (Spinner)rootView.findViewById(R.id.nue_veh_spinner1);
			editModelo = (EditText)rootView.findViewById(R.id.nue_veh_editText1);
			editKms = (EditText)rootView.findViewById(R.id.nue_veh_editText2);
			editFecha = (EditText)rootView.findViewById(R.id.nue_veh_editText3);
			editMatricula = (EditText)rootView.findViewById(R.id.nue_veh_editText4);
			spinnerTipo = (Spinner)rootView.findViewById(R.id.nue_veh_spinner2);
			spinnerCombustible = (Spinner)rootView.findViewById(R.id.nue_veh_spinner3);
			imagenCalendario = (ImageView)rootView.findViewById(R.id.nue_veh_imageView1);
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
							editFecha.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
						}
					}, year, month, day);
					dp.show();
				}
			});
			
			if(getArguments() != null) {
				Bundle bundle = getArguments();
				mostrarBotonDespues = bundle.getBoolean("mostrarBotonDespues");
			}
			
			/** Spinner de marcas de vehiculos */
			arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
			ArrayList<String> listaMarcas = new ArrayList<String>();
			for(int i=0; i<arrayMarcas.length(); i++) 
				listaMarcas.add(arrayMarcas.getString(i));
			adapterMarcas = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaMarcas);
			spinnerMarcas.setAdapter(adapterMarcas);
			arrayMarcas.recycle();
			
			/** Spinner de tipos de vehiculos */
			arrayTiposVehiculos = getResources().obtainTypedArray(R.array.TIPOS_VEHICULO);
			ArrayList<String> listaTipos = new ArrayList<String>();
			for(int i=0; i<arrayTiposVehiculos.length(); i++)
				listaTipos.add(arrayTiposVehiculos.getString(i));
			arrayTiposVehiculos.recycle();
			
			adapterTipo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTipos);
			spinnerTipo.setAdapter(adapterTipo);
			
			/** Spinner de la lista de carburantes */
			arrayCarburantes = getResources().obtainTypedArray(R.array.TIPOS_CARBURANTE);
			ArrayList<String> listaCarburantes = new ArrayList<String>();
			for(int i=0; i<arrayCarburantes.length(); i++)
				listaCarburantes.add(arrayCarburantes.getString(i));
			arrayCarburantes.recycle();
			
			adapterCarburante = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaCarburantes);
			spinnerCombustible.setAdapter(adapterCarburante);
			
			/** Boton guardar */
			Button botonGuardar = (Button)rootView.findViewById(R.id.btn_nuevo_veh);
			botonGuardar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						DetalleVehiculo dv = new DetalleVehiculo();
						dv.setIdVehiculo(null);
						dv.setMarca(spinnerMarcas.getSelectedItemPosition());
						dv.setModelo(editModelo.getText().toString());
						dv.setKilometros(Double.valueOf(editKms.getText().toString()));
						dv.setFechaCompra(Util.convierteStringEnDate(editFecha.getText().toString()));
						dv.setMatricula(editMatricula.getText().toString());
						dv.setTipoVehiculo(spinnerTipo.getSelectedItemPosition());
						dv.setTipoCarburante(spinnerCombustible.getSelectedItemPosition());
						
						if(comprobacionDatos(dv)) {
							guardaDatosDelVehiculo(dv);
						}
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			});
			
			/** Boton guardar despues */
			Button botonDespues = (Button)rootView.findViewById(R.id.btn_nuevo_despues);
			if(mostrarBotonDespues) {
				botonDespues.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							propiedades = rootView.getContext().getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
							if(propiedades != null) {
								SharedPreferences.Editor editor = propiedades.edit();
								editor.putBoolean(Constantes.INTRO_VEHICULO, true);
								editor.putBoolean(Constantes.PRIMERA_VEZ, false);
								editor.commit();
							}
							
							Fragment fragment = new PrincipalFragment();
							FragmentManager manager = myContext.getSupportFragmentManager();
							manager.beginTransaction().replace(R.id.container_principal, fragment).commit();
							
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}else {
				botonDespues.setVisibility(View.INVISIBLE);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.nuevo_vehiculo, menu);
	}
	
	
	private boolean comprobacionDatos(DetalleVehiculo dv) {
		boolean resultado = true;
		try {
			if(dv.getMarca() == null || dv.getMarca() == 0) {
				mostrarTexto("Debe seleccionar la marca del vehiculo");
				resultado = false;
			}else if(dv.getModelo() == null || dv.getModelo().length() == 0) {
				resultado = false;
				mostrarTexto("Debe introducir el modelo del vehiculo");
			}else if(dv.getKilometros() == null || dv.getKilometros() <= 0) {
				mostrarTexto("Debe introducir los kil�metros del vehiculo");
				resultado = false;
			}else if(dv.getFechaCompra() == null) {
				mostrarTexto("Debe introducir la fecha de compra del vehiculo");
				resultado = false;
			}else if(dv.getTipoVehiculo() == null || dv.getTipoVehiculo() == 0) {
				mostrarTexto("Debe seleccionar el tipo de vehiculo");
				resultado = false;
			}else if(dv.getTipoCarburante() == null || dv.getTipoCarburante() == 0) {
				mostrarTexto("Debe seleccionar el tipo de combustible");
				resultado = false;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultado;
	}
	
	
	private void mostrarTexto(String texto) {
		Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
	}
	
	
	private void guardaDatosDelVehiculo(DetalleVehiculo dv) {
		try {
            boolean resultado = false;
            VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
            if(dv.getIdVehiculo() == null)
                resultado = vehiculosHelper.insertarVehiculo(dv);
            else
                resultado = vehiculosHelper.actualizarVehiculo(dv);

			String texto = new String();
			if(resultado) {
				texto = "Datos guardados correctamente";
				SharedPreferences prop = rootView.getContext().getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
				if(prop != null) {
					SharedPreferences.Editor editor = prop.edit();
					editor.putBoolean(Constantes.INTRO_VEHICULO, true);
					editor.commit();
				}
			}else {
				texto = "Error al guardar los datos";
			}
            mostrarTexto(texto);
			
			// redireccion del fragmento solo cuando sea la primera ejecucion
			if(resultado && mostrarBotonDespues) {
				Fragment fragment = new PrincipalFragment();
                FragmentManager manager = myContext.getSupportFragmentManager();
				manager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}else {
				Fragment fragment = new VehiculoFragment();
				FragmentManager manager = myContext.getSupportFragmentManager();
				manager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
