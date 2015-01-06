package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleReparacion;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ReparacionesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ReparacionesFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaReparaciones;
	private LinearLayout layoutReparaciones;
	private View rootView;
	private Dialog editDialog;
	private ReparacionesSQLiteHelper reparacionHelper;
	private ArrayAdapter<String> adapterReparaciones;
	private Spinner spinnerTipo;
	private Button botonNuevo;
	private DetalleReparacion detalleEnEdicion;
	private SQLiteDatabase baseDatos;
	private ReparacionesSQLiteHelper reparacionesHelper;
	
	private TypedArray arrayTiposReparaciones;
	private TypedArray arrayMarcas;
	private Context myContext;
	
	private ArrayList<DetalleReparacion> reparaciones;
	private ArrayList<String> listaReparaciones = new ArrayList<String>();
	private ArrayList<String> listaVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
	
	private EditText textFecha;
	private EditText textKms;
	private EditText textPrecio;
	private EditText textTaller;
	private EditText textObservaciones;
	
	
	public ReparacionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
		setHasOptionsMenu(true);
		
		fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_reparaciones);
		vistaReparaciones = (ScrollView)fragmento.findViewById(R.id.rep_scrollView1);
		layoutReparaciones = (LinearLayout)vistaReparaciones.findViewById(R.id.rep_linear);
		reparaciones = recuperaDatosReparaciones();
		
		botonNuevo = (Button)rootView.findViewById(R.id.rep_boton_nuevo);
		botonNuevo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new NuevaReparacionFragment();
				FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		
		arrayTiposReparaciones = getResources().obtainTypedArray(R.array.TIPOS_REPARACIONES);
		arrayTiposReparaciones.recycle();
		for(int i=0; i<arrayTiposReparaciones.length(); i++)
			listaReparaciones.add(arrayTiposReparaciones.getString(i));
		
		arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
		
		misVehiculos = recuperaDatosVehiculos();
		for(DetalleVehiculo dv : misVehiculos) {
        	String marca = arrayMarcas.getString(dv.getMarca());
        	listaVehiculos.add(marca + " " + dv.getModelo());
        }
		
		int i = 0;
		for(DetalleReparacion detalle : reparaciones) {
			View vista = inflater.inflate(R.layout.detalle_reparacion, layoutReparaciones, false);
			
			vista.setId(i);
			String marcaymodelo = null;
			for(DetalleVehiculo dv : misVehiculos) {
        		if(dv.getIdVehiculo().equals(detalle.getIdVehiculo())) {
        			marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
        			break;
        		}
        	}
			TextView tv1 = (TextView)vista.findViewById(R.id.det_rep_textView1);
			tv1.setText(marcaymodelo);
			TextView tv2 = (TextView)vista.findViewById(R.id.det_rep_textView3);
			tv2.setText(Util.convierteDateEnString(detalle.getFecha()));
			TextView tv3 = (TextView)vista.findViewById(R.id.det_rep_textView5);
			tv3.setText(detalle.getKilometros().toString());
			TextView tv4 = (TextView)vista.findViewById(R.id.det_rep_textView7);
			tv4.setText(detalle.getPrecio().toString());
			TextView tv5 = (TextView)vista.findViewById(R.id.det_rep_textView9);
			String tipoReparacionSeleccionada = listaReparaciones.get(detalle.getIdTipoReparacion());
			tv5.setText(tipoReparacionSeleccionada);
			TextView tv6 = (TextView)vista.findViewById(R.id.det_rep_textView11);
			tv6.setText(detalle.getTaller());
			
			ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView_editar);
			imgEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					detalleEnEdicion = reparaciones.get(linear3.getId());
					
					editDialog = new Dialog(rootView.getContext());
					editDialog.setContentView(R.layout.edicion_reparacion);
					editDialog.setTitle("Edicion de reparacion");
					
					textFecha = (EditText)editDialog.findViewById(R.id.edit_rep_fecha);
					textFecha.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
					textKms = (EditText)editDialog.findViewById(R.id.edit_rep_kms);
					textKms.setText(detalleEnEdicion.getKilometros().toString());
					textPrecio = (EditText)editDialog.findViewById(R.id.edit_rep_coste);
					textPrecio.setText(detalleEnEdicion.getPrecio().toString());
					spinnerTipo = (Spinner)editDialog.findViewById(R.id.edit_rep_spinner_tipo);
					adapterReparaciones = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaReparaciones);
					spinnerTipo.setAdapter(adapterReparaciones);
					spinnerTipo.setSelection(detalleEnEdicion.getIdTipoReparacion());
					textTaller = (EditText)editDialog.findViewById(R.id.edit_rep_taller);
					textTaller.setText(detalleEnEdicion.getTaller());
					textObservaciones = (EditText)editDialog.findViewById(R.id.edit_rep_obs);
					textObservaciones.setText(detalleEnEdicion.getObservaciones());
					
					Button botonGuardar = (Button)editDialog.findViewById(R.id.edit_rep_btn_guardar);
					botonGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							DetalleReparacion rep = new DetalleReparacion();
							rep.setIdDetalleReparacion(detalleEnEdicion.getIdDetalleReparacion());
							rep.setFecha(Util.convierteStringEnDate(textFecha.getText().toString()));
							rep.setKilometros(Double.valueOf(textKms.getText().toString()));
							rep.setPrecio(Double.valueOf(textPrecio.getText().toString()));
							rep.setIdTipoReparacion(spinnerTipo.getSelectedItemPosition());
							rep.setTaller(textTaller.getText().toString());
							rep.setObservaciones(textObservaciones.getText().toString());
							rep.setIdVehiculo(detalleEnEdicion.getIdVehiculo());
							
							guardaDatosDeLaReparacion(rep);
							actualizaListaReparaciones();
							editDialog.dismiss();
						}
					});
					
					Button botonCancelar = (Button)editDialog.findViewById(R.id.edit_rep_btn_cancelar);
					botonCancelar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							editDialog.cancel();
						}
					});
					
					editDialog.show();
				}
			});
			
			ImageView imgBorrar = (ImageView)vista.findViewById(R.id.imageView_borrar);
			imgBorrar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					
					DetalleReparacion dr = reparaciones.get(linear3.getId());
					Toast.makeText(rootView.getContext(), "borrar: " + linear3.getId() + " - lista: " + dr.getIdDetalleReparacion(), Toast.LENGTH_SHORT).show();
				}
			});
			layoutReparaciones.addView(vista, i);
			i++;
		}
		return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.reparaciones, menu);
	}
	
	private ArrayList<DetalleReparacion> recuperaDatosReparaciones() {
		ArrayList<DetalleReparacion> reparaciones = new ArrayList<DetalleReparacion>();
		try {
			if(reparacionHelper == null)
				reparacionHelper = new ReparacionesSQLiteHelper(rootView.getContext(), Constantes.TABLA_REPARACIONES, null, 1);
			reparaciones.addAll(reparacionHelper.getReparaciones());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return reparaciones;
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
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}
	
	private void guardaDatosDeLaReparacion(DetalleReparacion rep) {
		try {
			boolean resp = abrirBaseDeDatos();
			if(resp == false) {
				String texto = "Error al abrir o crear la tabla 'Accidentes'";
				Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
				return;
			}
			
			boolean resultado = insertarFila(rep);
			String texto = new String();
			if(resultado)
				texto = "Datos guardados correctamente";
			else
				texto = "Error al guardar los datos";
			
			actualizaListaReparaciones();
			Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean abrirBaseDeDatos() {
		boolean resultado = false;
		try {
			reparacionesHelper = new ReparacionesSQLiteHelper(rootView.getContext(), Constantes.TABLA_REPARACIONES, null, 1);
			baseDatos = reparacionesHelper.getWritableDatabase();
			resultado = true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultado;
	}
	
	private boolean insertarFila(DetalleReparacion dr) {
		boolean resp = false;
		try {
			ContentValues values = new ContentValues();
			values.put("idReparacion", dr.getIdDetalleReparacion());
			values.put("fecha", dr.getFecha().getTime());
			values.put("kms", dr.getKilometros());
			values.put("precio", dr.getPrecio());
			values.put("taller", dr.getTaller());
			values.put("idTipoReparacion", dr.getIdTipoReparacion());
			values.put("observaciones", dr.getObservaciones());
			values.put("idVehiculo", dr.getIdVehiculo());
			
			if(dr.getIdDetalleReparacion() == null) {
				resp = (baseDatos.insert(Constantes.TABLA_REPARACIONES, null, values) > 0);
			}else {
				String[] argumentos = new String[1];
				argumentos[0] = String.valueOf(dr.getIdDetalleReparacion());
				resp = (baseDatos.update(Constantes.TABLA_REPARACIONES, values, "idReparacion = ?", argumentos) > 0);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	private void actualizaListaReparaciones() {
		Fragment fragment = new ReparacionesFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}
}
