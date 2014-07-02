package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.listener.CustomOnItemSelectedListener;
import com.jhonny.infocar.sql.DatosSQLiteHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


public class DatosFragment extends Fragment {
	
	private ArrayAdapter<Integer> adapter;
	private EditText editNombre = null;
	private EditText editTelefono = null;
	private Spinner spinnerEdades = null;
	private RadioButton radioHombre = null;
	private View rootView = null;
	
	private SQLiteDatabase baseDatos;
	private FragmentActivity myContext;
	
	
	public DatosFragment() {
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.datos, menu);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_datos, container, false);
        spinnerEdades = (Spinner)rootView.findViewById(R.id.datos_spinner1);
        editNombre = (EditText)rootView.findViewById(R.id.datos_editText1);
        editTelefono = (EditText)rootView.findViewById(R.id.datos_editText2);
        radioHombre = (RadioButton)rootView.findViewById(R.id.datos_radioHombre);
        
        Button botonGuardar = (Button)rootView.findViewById(R.id.datos_button1);
        botonGuardar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// comprobacion de los datos
				String nombre = (String)editNombre.getText().toString();
				String telefono = (String)editTelefono.getText().toString();
				
				if(nombre == null || nombre.length() == 0) {
					String texto = new String("Debe introducir un nombre");
					Toast.makeText(view.getContext(), texto, Toast.LENGTH_SHORT).show();
					return;
					
				}else if(telefono == null || telefono.length() == 0) {
					String texto = new String("Debe introducir el telefono");
					Toast.makeText(view.getContext(), texto, Toast.LENGTH_SHORT).show();
					return;
				}
				
				Integer edad = (Integer)spinnerEdades.getSelectedItemPosition() + 1;
				boolean hombre = radioHombre.isChecked();
				
				// guardado de datos
				guardaDatosPersonales(nombre, telefono, edad, hombre);
			}
        });
        
        ArrayList<Integer> edades = new ArrayList<Integer>();
        for(int i=1; i<= 99; i++) {
        	edades.add(i);
        }
        
        adapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_item, edades);
        spinnerEdades.setAdapter(adapter);
        spinnerEdades.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinnerEdades.setSelection(17);
        
        return rootView;
    }
	
	/**
	 * Guarda los valores de la DatosActivity
	 * @param nombre
	 * @param telefono
	 * @param edad
	 * @param esHombre
	 */
	public void guardaDatosPersonales(String nombre, String telefono, Integer edad, boolean esHombre) {
		try {
			// abre la bbdd
			boolean resp = abrirBaseDeDatos();
			if(resp == false) {
				String texto = "Error al abrir o crear la tabla 'Datos'";
				Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
				return;
			}
			
			boolean resultado = insertarFila(nombre, telefono, edad, esHombre);
			String texto = new String();
			if(resultado) {
				texto = "Datos guardados correctamente";
				SharedPreferences prop = rootView.getContext().getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
				if(prop != null) {
					SharedPreferences.Editor editor = prop.edit();
					editor.putBoolean(Constantes.INTRO_PERSONALES, true);
					editor.commit();
				}
				
			}else {
				texto = "Error al guardar los datos";
			}
			
			Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
			
			// redireccion del fragmento solo cuando sea la primera ejecucion
			if(resultado) {
				Fragment fragment = new NuevoVehiculoFragment();
				FragmentManager manager = myContext.getSupportFragmentManager();
				manager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	private boolean abrirBaseDeDatos() {
		boolean resp = false;
		
		try {
			DatosSQLiteHelper datosHelper = new DatosSQLiteHelper(rootView.getContext(), Constantes.TABLA_DATOS, null, 1);
			baseDatos = datosHelper.getReadableDatabase();
			
			if(baseDatos != null) {
				resp = true;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	
	private boolean insertarFila(String nombre, String telefono, Integer edad, boolean esHombre) {
		boolean resp = false;
		try {
			ContentValues values = new ContentValues();
			values.put("nombre", nombre);
			values.put("telefono", telefono);
			values.put("edad", edad);
			values.put("sexo", esHombre);
			
			resp = (baseDatos.insert(Constantes.TABLA_DATOS, null, values) > 0);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
}
