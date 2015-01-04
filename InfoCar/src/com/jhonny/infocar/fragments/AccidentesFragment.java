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
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AccidentesFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaAccidentes;
	private LinearLayout layoutAccidentes;
	private ArrayList<DetalleAccidente> accidentes;
	private View rootView;
	private Dialog editDialog;
	private Context myContext;
	private ArrayList<DetalleVehiculo> listaVehiculos;
	private TypedArray arrayMarcas;
	
	private EditText textFecha;
	private Spinner spinnerModelos;
	private EditText textKms;
	private EditText textLugar;
	private EditText textObservaciones;
	
	private SQLiteDatabase baseDatos;
	private DetalleAccidente detalleEnEdicion;
	private AccidentesSQLiteHelper accidentesHelper;
	
	
	public AccidentesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_accidentes, container, false);
		setHasOptionsMenu(true);
		
		fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_accidentes);
		vistaAccidentes = (ScrollView)fragmento.findViewById(R.id.acc_scrollView1);
		layoutAccidentes = (LinearLayout)vistaAccidentes.findViewById(R.id.acc_linear);
		accidentes = recuperaDatosAccidentes();
		listaVehiculos = recuperaDatosVehiculos();
		arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
		
		Button btnNuevo = (Button)fragmento.findViewById(R.id.acc_boton_nuevo);
		btnNuevo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new NuevoAccidenteFragment();
				FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
	    		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
		
		
		int i = 0;
		for(DetalleAccidente acc : accidentes) {
			View vista = inflater.inflate(R.layout.detalle_accidente, layoutAccidentes, false);
			
			vista.setId(i);
			DetalleVehiculo dv = null;
			for(DetalleVehiculo vehiculo : listaVehiculos) {
				if(vehiculo.getIdVehiculo().equals(acc.getIdVehiculo())) {
					dv = vehiculo;
					break;
				}
			}
			
			TextView textViewTitulo = (TextView)vista.findViewById(R.id.det_acc_textView1);
			textViewTitulo.setText(Util.convierteDateEnString(acc.getFecha()));
			TextView textViewKms = (TextView)vista.findViewById(R.id.det_acc_textView3);
			textViewKms.setText(acc.getKilometros().toString());
			TextView textViewLugar = (TextView)vista.findViewById(R.id.det_acc_textView5);
			textViewLugar.setText(acc.getLugar());
			TextView textViewMarca = (TextView)vista.findViewById(R.id.det_acc_textView7);
			textViewMarca.setText(arrayMarcas.getString(dv.getMarca()));
			TextView textViewModelo = (TextView)vista.findViewById(R.id.det_acc_textView9);
			textViewModelo.setText(dv.getModelo());
			TextView textViewObservaciones = (TextView)vista.findViewById(R.id.det_acc_textView11);
			textViewObservaciones.setText(acc.getObservaciones());
			
			ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView_editar);
			imgEditar.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					detalleEnEdicion = accidentes.get(linear3.getId());
					
					editDialog = new Dialog(rootView.getContext());
					editDialog.setContentView(R.layout.edicion_accidente);
					editDialog.setTitle("Edicion de accidente");
					
					textFecha = (EditText)editDialog.findViewById(R.id.edit_acc_fecha);
					final String fechaEnEdicion = Util.convierteDateEnString(detalleEnEdicion.getFecha());
					textFecha.setText(fechaEnEdicion);
					ArrayList<String> vehiculos = new ArrayList<String>();
					for(DetalleVehiculo veh : listaVehiculos) {
						String marca = arrayMarcas.getString(veh.getMarca());
						vehiculos.add(marca + " " + veh.getModelo());
					}
					ArrayAdapter<String> adapterVehiculos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, vehiculos);
					spinnerModelos = (Spinner)editDialog.findViewById(R.id.edit_acc_spinner_vehiculo);
					spinnerModelos.setAdapter(adapterVehiculos);
					textKms = (EditText)editDialog.findViewById(R.id.edit_acc_kms);
					textKms.setText(detalleEnEdicion.getKilometros().toString());
					textLugar = (EditText)editDialog.findViewById(R.id.edit_acc_lugar);
					textLugar.setText(detalleEnEdicion.getLugar());
					textObservaciones = (EditText)editDialog.findViewById(R.id.edit_acc_obs);
					textObservaciones.setText(detalleEnEdicion.getObservaciones());
					
					ImageView imgFecha = (ImageView)editDialog.findViewById(R.id.edit_acc_imageView1);
					imgFecha.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String[] fechaEdicion = fechaEnEdicion.split("/");
							final Calendar c = Calendar.getInstance();
							
							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);
							
							if(fechaEdicion != null && fechaEdicion.length > 0) {
								year = Integer.valueOf(fechaEdicion[2]);
								month = Integer.valueOf(fechaEdicion[1]) - 1;
								day = Integer.valueOf(fechaEdicion[0]);
							}
							
							DatePickerDialog dp = new DatePickerDialog(rootView.getContext(), new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									textFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
								}
							}, year, month, day);
							
							dp.show();
						}
					});
					
					Button btnGuardar = (Button)editDialog.findViewById(R.id.edit_acc_btn_guardar);
					btnGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View vista) {
							int posicion = spinnerModelos.getSelectedItemPosition();
							DetalleVehiculo nuevoVehiculo = listaVehiculos.get(posicion);
							
							DetalleAccidente accidente = new DetalleAccidente();
							accidente.setIdDetalleAccidente(detalleEnEdicion.getIdDetalleAccidente());
							accidente.setFecha(Util.convierteStringEnDate(textFecha.getText().toString()));
							accidente.setKilometros(Double.valueOf(textKms.getText().toString()));
							accidente.setLugar(textLugar.getText().toString());
							accidente.setObservaciones(textObservaciones.getText().toString());
							accidente.setIdVehiculo(nuevoVehiculo.getIdVehiculo());
							
							guardaDatosDelAccidente(accidente);
							
							editDialog.dismiss();
						}
					});
					
					Button btnCancelar = (Button)editDialog.findViewById(R.id.edit_acc_btn_cancelar);
					btnCancelar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View vista) {
							editDialog.cancel();
						}
					});
					editDialog.show();
				}
			});
			
			ImageView imgBorrar = (ImageView)vista.findViewById(R.id.imageView_borrar);
			imgBorrar.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
					builder.setCancelable(true);
					builder.setTitle("Eliminar registro");
					builder.setMessage("¿Seguro que desea borrar este accidente?");
					builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.setNegativeButton("Cancelar", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					builder.show();
				}
			});
        	layoutAccidentes.addView(vista, i);
        	i++;
        }
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.accidentes, menu);
	}
	
	private ArrayList<DetalleAccidente> recuperaDatosAccidentes() {
		ArrayList<DetalleAccidente> detalles = new ArrayList<DetalleAccidente>();
		
		try {
			abrirBaseDeDatos();
			detalles.addAll(accidentesHelper.getAccidentes());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
	
	private void guardaDatosDelAccidente(DetalleAccidente accidente) {
		try {
			boolean resp = abrirBaseDeDatos();
			if(resp == false) {
				String texto = "Error al abrir o crear la tabla 'Accidentes'";
				Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
				return;
			}
			
			boolean resultado = insertarFila(accidente);
			String texto = new String();
			if(resultado)
				texto = "Datos guardados correctamente";
			else
				texto = "Error al guardar los datos";
			Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean abrirBaseDeDatos() {
		boolean resultado = false;
		try {
			accidentesHelper = new AccidentesSQLiteHelper(rootView.getContext(), Constantes.TABLA_ACCIDENTES, null, 1);
			baseDatos = accidentesHelper.getWritableDatabase();
			resultado = true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultado;
	}
	
	private boolean insertarFila(DetalleAccidente da) {
		boolean resp = false;
		try {
			ContentValues values = new ContentValues();
			values.put("idDetalleAccidente", da.getIdDetalleAccidente());
			values.put("fecha", da.getFecha().getTime());
			values.put("lugar", da.getLugar());
			values.put("kilometros", da.getKilometros());
			values.put("observaciones", da.getObservaciones());
			values.put("idVehiculo", da.getIdVehiculo());
			
			if(da.getIdDetalleAccidente() == null) {
				resp = (baseDatos.insert(Constantes.TABLA_ACCIDENTES, null, values) > 0);
			}else {
				String[] argumentos = new String[1];
				argumentos[0] = String.valueOf(da.getIdDetalleAccidente());
				resp = (baseDatos.update(Constantes.TABLA_ACCIDENTES, values, "idDetalleAccidente = ?", argumentos) > 0);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
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
}
