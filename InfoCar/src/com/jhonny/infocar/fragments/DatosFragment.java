package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.listener.CustomOnItemSelectedListener;
import com.jhonny.infocar.model.DetalleDatos;
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
	private RadioButton radioSexoHombre = null;
    private RadioButton radioSexoMujer = null;
	private EditText editEmail = null;
	
	private View rootView = null;
	private SQLiteDatabase baseDatos;
	private FragmentActivity myContext;
	private SharedPreferences propiedades = null;
    
	
	public static DatosFragment newInstance(Bundle arguments){
		DatosFragment f = new DatosFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

	public DatosFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.datos, menu);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_datos, container, false);

			editNombre = (EditText)rootView.findViewById(R.id.datos_editText1);
			editTelefono = (EditText)rootView.findViewById(R.id.datos_editText2);
	        spinnerEdades = (Spinner)rootView.findViewById(R.id.datos_spinner1);
            radioSexoHombre = (RadioButton)rootView.findViewById(R.id.datos_radioHombre);
            radioSexoMujer = (RadioButton)rootView.findViewById(R.id.datos_radioMujer);
	        editEmail = (EditText)rootView.findViewById(R.id.datos_editText3);

	        boolean mostrarBotonDespues = false;
	        if(getArguments() != null) {
	        	Bundle bundle = getArguments();
	        	mostrarBotonDespues = bundle.getBoolean("mostrarBotonDespues");
	        }

            ArrayList<Integer> edades = new ArrayList<Integer>();
            for(int i=1; i<= 99; i++)
                edades.add(i);
            adapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_item, edades);
            spinnerEdades.setAdapter(adapter);
            spinnerEdades.setOnItemSelectedListener(new CustomOnItemSelectedListener());
            spinnerEdades.setSelection(17);

	        final DetalleDatos datos = recuperaDetalleDatos();
	        if(datos != null) {
	        	editNombre.setText(datos.getNombre());
	        	editTelefono.setText(datos.getTelefono());
	        	spinnerEdades.setSelection(datos.getEdad() - 1);
                if(datos.isHombre()) {
                    radioSexoHombre.setChecked(true);
                    radioSexoMujer.setChecked(false);
                }else {
                    radioSexoHombre.setChecked(false);
                    radioSexoMujer.setChecked(true);
                }
	        	editEmail.setText(datos.getEmail());
	        }

	        Button botonGuardar = (Button)rootView.findViewById(R.id.datos_button1);
	        botonGuardar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
                    // comprobacion de los datos
                    String nombre = (String)editNombre.getText().toString();
                    String telefono = (String)editTelefono.getText().toString();
                    Integer edad = (Integer)spinnerEdades.getSelectedItemPosition() + 1;
                    boolean hombre = false;
                    if(radioSexoHombre != null && radioSexoHombre.isChecked())
                        hombre = true;
                    String email = (String)editEmail.getText().toString();

                    DetalleDatos dd = new DetalleDatos();
                    if(datos != null)
                        dd.setIdDetalleDatos(datos.getIdDetalleDatos());
                    dd.setNombre(nombre);
                    dd.setTelefono(telefono);
                    dd.setEdad(edad);
                    dd.setHombre(hombre);
                    dd.setEmail(email);
                    dd.setFechaAlta(new Date());

                    if(comprobacionDatos(dd)) {
                        // guardado de datos
                        guardaDatosPersonales(dd);
                    }
			    }
            });

	        Button botonGuardarDespues = (Button)rootView.findViewById(R.id.datos_button2);
	        if(mostrarBotonDespues) {
	        	botonGuardarDespues.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
                        propiedades = rootView.getContext().getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
                        if(propiedades != null) {
                            SharedPreferences.Editor editor = propiedades.edit();
                            editor.putBoolean(Constantes.INTRO_PERSONALES, true);
                            editor.commit();

                            Fragment fragment = null;
                            boolean dVehiculo = propiedades.getBoolean(Constantes.INTRO_VEHICULO, false);

                            if(dVehiculo == false) {
                                // redireccion a nuevoVehiculoFragment
                                Bundle arguments = new Bundle();
                                arguments.putBoolean("mostrarBotonDespues", true);
                                fragment = NuevoVehiculoFragment.newInstance(arguments);

                            }else {
                                // redireccion a la pantalla principal
                                fragment = new PrincipalFragment();
                            }

                            FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                        }
					}
				});
	        }else{
	        	botonGuardarDespues.setVisibility(View.INVISIBLE);
	        }
		}catch(Exception ex) {
			ex.printStackTrace();
		}

        return rootView;
    }

    /**
     * Inserta en la base de datos el objeto DetalleDatos
     * @param dd
     */
	public void guardaDatosPersonales(DetalleDatos dd) {
		try {
            boolean resultado = false;
            DatosSQLiteHelper datosHelper = new DatosSQLiteHelper(myContext, Constantes.TABLA_DATOS, null, 1);
            if(dd.getIdDetalleDatos() == null)
                resultado = datosHelper.insertarDatos(dd);
            else
                resultado = datosHelper.actualizarDatos(dd);

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
            Fragment fragment = null;
			if(resultado) {
				propiedades = rootView.getContext().getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
				if(propiedades != null) {
					boolean primeraVez = propiedades.getBoolean(Constantes.PRIMERA_VEZ, true);
					if(primeraVez) {
						Bundle bundle = new Bundle();
						bundle.putBoolean("mostrarBotonDespues", true);
						fragment = NuevoVehiculoFragment.newInstance(bundle);

                        SharedPreferences.Editor editor = propiedades.edit();
                        editor.putBoolean(Constantes.PRIMERA_VEZ, false);
                        editor.commit();
					}
				}
			}else {
                fragment = new NuevoVehiculoFragment();
            }

            FragmentManager manager = myContext.getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container_principal, fragment).commit();

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean comprobacionDatos(DetalleDatos dd) {
		String texto = null;
		boolean resp = true;
		if(dd.getNombre() == null || dd.getNombre().length() == 0) {
			texto = new String("Debe introducir un nombre");
			resp = false;

		}else if(dd.getTelefono() == null || dd.getTelefono().length() == 0) {
			texto = new String("Debe introducir el telefono");
			resp = false;

		}else if(dd.getEmail() == null || dd.getEmail().length() == 0) {
			texto = new String("Debe introducir el e-mail");
			resp = false;
		}
		if(resp == false)
			Toast.makeText(myContext, texto, Toast.LENGTH_SHORT).show();
		return resp;
	}

	private DetalleDatos recuperaDetalleDatos() {
		DetalleDatos dd = null;
		try {
            DatosSQLiteHelper datosHelper = new DatosSQLiteHelper(myContext, Constantes.TABLA_DATOS, null, 1);
			dd = datosHelper.getDatos();
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		return dd;
	}
}
