package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.AccidentesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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


public class NuevoAccidenteFragment extends Fragment {
	
	private View rootView;
	private FragmentActivity myContext;
	
	private EditText editFecha;
	private ImageView imagenCalendario;
	private EditText editKilometros;
	private EditText editLugar;
	private Spinner spinnerVehiculo;
	private EditText editObservaciones;
	private Button btnGuardar;
	private Button btnCancelar;
	
	private ArrayList<String> vehiculos = new ArrayList<String>();
	private AccidentesSQLiteHelper accidentesHelper;
	private SQLiteDatabase baseDatos;
	private ArrayList<DetalleVehiculo> listaVehiculos;
	private DetalleAccidente detalleEnEdicion = null;


	public NuevoAccidenteFragment() {

	}

	public static NuevoAccidenteFragment newInstance(DetalleAccidente da) {
		Bundle args = new Bundle();
		args.putInt("IdDetalleAccidente", da.getIdDetalleAccidente());
		args.putString("Fecha", Util.convierteDateEnString(da.getFecha()));
		args.putString("Lugar", da.getLugar());
		args.putDouble("Kilometros", da.getKilometros());
		args.putString("Observaciones", da.getObservaciones());
		args.putInt("IdVehiculo", da.getIdVehiculo());
		args.putInt("position", da.getPosicion());

		NuevoAccidenteFragment frag = new NuevoAccidenteFragment();
		frag.setArguments(args);
		return frag;
	}


	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			Bundle arguments = getArguments();
			if(arguments != null) {
				detalleEnEdicion = new DetalleAccidente();
				detalleEnEdicion.setIdDetalleAccidente(arguments.getInt("IdDetalleAccidente"));
				detalleEnEdicion.setFecha(Util.convierteStringEnDate(arguments.getString("Fecha")));
				detalleEnEdicion.setLugar(arguments.getString("Lugar"));
				detalleEnEdicion.setKilometros(arguments.getDouble("Kilometros"));
				detalleEnEdicion.setObservaciones(arguments.getString("Observaciones"));
				detalleEnEdicion.setIdVehiculo(arguments.getInt("IdVehiculo"));
				detalleEnEdicion.setPosicion(arguments.getInt("position"));
			}

			rootView = inflater.inflate(R.layout.fragment_nuevo_accidente, container, false);
			editFecha = (EditText)rootView.findViewById(R.id.nue_acc_edit_fecha);
			editKilometros = (EditText)rootView.findViewById(R.id.nue_acc_edit_kms);
			editLugar = (EditText)rootView.findViewById(R.id.nue_acc_edit_lugar);
			editObservaciones = (EditText)rootView.findViewById(R.id.nue_acc_edit_obs);
			spinnerVehiculo = (Spinner)rootView.findViewById(R.id.nue_acc_spinner_vehiculo);
			
			imagenCalendario = (ImageView)rootView.findViewById(R.id.nue_acc_imageView1);
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
			
			vehiculos.add("Seleccione un vehiculo");
			listaVehiculos = recuperaDatosVehiculos();
			for(DetalleVehiculo dv : listaVehiculos)
				vehiculos.add(dv.getModelo());
			ArrayAdapter<String> adapterVehiculo = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, vehiculos);
			spinnerVehiculo.setAdapter(adapterVehiculo);

			if(detalleEnEdicion != null) {
				editFecha.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
				editKilometros.setText(detalleEnEdicion.getKilometros().toString());
				editLugar.setText(detalleEnEdicion.getLugar());
				editObservaciones.setText(detalleEnEdicion.getObservaciones());
				spinnerVehiculo.setSelection(detalleEnEdicion.getIdVehiculo());
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return rootView;
	}

	private void guardaDatosDelAccidente(DetalleAccidente accidente) {
		try {
			boolean resultado = false;
			AccidentesSQLiteHelper accidentesHelper = new AccidentesSQLiteHelper(myContext, Constantes.TABLA_ACCIDENTES, null, 1);
			if(accidente.getIdDetalleAccidente() == null)
				resultado = accidentesHelper.insertarAccidente(accidente);
			else
				resultado = accidentesHelper.actualizarAccidente(accidente);

			String texto = new String();
			if(resultado)
				texto = "Datos guardados correctamente";
			else
				texto = "Error al guardar los datos";
			Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();

			if(resultado)
				actualizaListaAccidentes();

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.nuevo_accidente, menu);
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
	
	private boolean compruebacionDatos(DetalleAccidente da) {
		boolean resp = true;
		String mensaje = null;
		if(da == null) {
			mensaje = "Debe introducir los datos a guardar";
			resp = false;
		}else if(da.getFecha() == null) {
			mensaje = "Debe introducir la fecha del accidente";
			resp = false;
		}else if(da.getKilometros() == null || da.getKilometros() <= 0) {
			mensaje = "Debe introducir los kilometros";
			resp = false;
		}else if(da.getIdVehiculo() == null) {
			mensaje = "Debe seleccionar una de sus vehiculos";
			resp = false;
		}
		if(resp == false)
			Toast.makeText(myContext, mensaje, Toast.LENGTH_SHORT).show();
		return resp;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.action_guardar:
				DetalleAccidente da = null;

				if(detalleEnEdicion != null) {
					da = detalleEnEdicion;

				}else {
					da = new DetalleAccidente();
					da.setIdDetalleAccidente(null);
					da.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
					String kms = editKilometros.getText().toString();
					if (kms != null && kms.length() > 0)
						da.setKilometros(Double.valueOf(kms));
					da.setLugar(editLugar.getText().toString());
					da.setObservaciones(editObservaciones.getText().toString());

					int seleccionado = spinnerVehiculo.getSelectedItemPosition() - 1;
					DetalleVehiculo dv = null;
					if (seleccionado >= 0) {
						dv = listaVehiculos.get(seleccionado);
						da.setIdVehiculo(dv.getIdVehiculo());
					}
				}

				if(compruebacionDatos(da)) {
					guardaDatosDelAccidente(da);
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		Util.cargaFondoDePantalla(myContext);
	}

	private void actualizaListaAccidentes() {
		Fragment fragment = new AccidentesFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}
}
