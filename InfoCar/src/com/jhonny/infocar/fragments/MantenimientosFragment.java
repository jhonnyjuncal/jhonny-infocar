package com.jhonny.infocar.fragments;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MantenimientosFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaMantenimientos;
	private LinearLayout layoutMantenimientos;
	private View rootView;
	private Context myContext;
	
	private ArrayList<DetalleMantenimiento> mantenimientos;
	private Dialog editDialog;
	private Button botonNuevo;
	private TypedArray arrayTiposMantenimientos = null;
	private TypedArray arrayMarcas = null;
	private ArrayList<String> listaTiposMantenimientos = new ArrayList<String>();
	private ArrayList<String> listaVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
	private ArrayAdapter<String> adapterTipoMantenimientos;
	private ArrayAdapter<String> adapterVehiculos;
	private MantenimientosSQLiteHelper mantenimientosHelper;
	private SQLiteDatabase baseDatos;
	
	private EditText textFecha;
	private ImageView imgCalendar;
	private EditText textKms;
	private EditText textPrecio;
	private Spinner spinnerTipoMant;
	private Spinner spinnerVehiculo;
	private EditText textTaller;
	private EditText textObservaciones;
	private DetalleMantenimiento mant;
	
	
	public MantenimientosFragment() {
		
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
		setHasOptionsMenu(true);
		
		fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_mantenimiento);
		vistaMantenimientos = (ScrollView)fragmento.findViewById(R.id.mant_scrollView1);
		layoutMantenimientos = (LinearLayout)vistaMantenimientos.findViewById(R.id.mant_linear);
		mantenimientos = recuperaDatosMantenimiento();
		
        botonNuevo = (Button)rootView.findViewById(R.id.mant_boton_nuevo);
        botonNuevo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new NuevoMantenimientoFragment();
				FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
	    		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
			}
		});
        
        arrayTiposMantenimientos = getResources().obtainTypedArray(R.array.TIPOS_MANTENIMIENTOS);
        arrayTiposMantenimientos.recycle();
        for(int i=0; i<arrayTiposMantenimientos.length(); i++)
        	listaTiposMantenimientos.add(arrayTiposMantenimientos.getString(i));
        
        arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
        
        misVehiculos = recuperaDatosVehiculos();
        for(DetalleVehiculo dv : misVehiculos) {
        	String marca = arrayMarcas.getString(dv.getMarca());
        	listaVehiculos.add(marca + " " + dv.getModelo());
        }
        
        int i = 0;
        for(DetalleMantenimiento dm : mantenimientos) {
        	View vista = inflater.inflate(R.layout.detalle_mantenimiento, layoutMantenimientos, false);
        	
        	vista.setId(i);
        	TextView tv1 = (TextView)vista.findViewById(R.id.det_mant_textView1);
        	String marcaymodelo = null;
        	for(DetalleVehiculo dv : misVehiculos) {
        		if(dv.getIdVehiculo().equals(dm.getIdVehiculo())) {
        			marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
        			break;
        		}
        	}
        	tv1.setText(marcaymodelo);
        	TextView tv2 = (TextView)vista.findViewById(R.id.det_mant_textView3);
        	DateFormat df = DateFormat.getDateInstance();
        	tv2.setText(df.format(dm.getFecha()));
        	TextView tv3 = (TextView)vista.findViewById(R.id.det_mant_textView5);
        	tv3.setText(dm.getKilometros().toString());
        	TextView tv4 = (TextView)vista.findViewById(R.id.det_mant_textView7);
        	tv4.setText(dm.getPrecio().toString());
        	TextView tv5 = (TextView)vista.findViewById(R.id.det_mant_textView9);
        	String mantenimientoSeleccionado = listaTiposMantenimientos.get(dm.getTipoMantenimiento());
        	tv5.setText(mantenimientoSeleccionado);
        	TextView tv6 = (TextView)vista.findViewById(R.id.det_mant_textView11);
        	tv6.setText(dm.getTaller());
        	
        	
        	ImageView imgEditar = (ImageView)vista.findViewById(R.id.imageView_editar);
        	imgEditar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					mant = mantenimientos.get(linear3.getId());
					
					editDialog = new Dialog(rootView.getContext());
					editDialog.setContentView(R.layout.edicion_mantenimiento);
					editDialog.setTitle("Edicion de mantenimiento");
					
					textFecha = (EditText)editDialog.findViewById(R.id.edit_mant_editText1);
					final String fechaEnEdicion = Util.convierteDateEnString(mant.getFecha());
					textFecha.setText(fechaEnEdicion);
					imgCalendar = (ImageView)editDialog.findViewById(R.id.edit_veh_imageView1);
					imgCalendar.setOnClickListener(new OnClickListener() {
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
					textKms = (EditText)editDialog.findViewById(R.id.edit_mant_editText2);
					textKms.setText(mant.getKilometros().toString());
					textPrecio = (EditText)editDialog.findViewById(R.id.edit_mant_editText3);
					textPrecio.setText(mant.getPrecio().toString());
					adapterTipoMantenimientos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposMantenimientos);
					spinnerTipoMant = (Spinner)editDialog.findViewById(R.id.edit_mant_spinner_tipo);
					spinnerTipoMant.setAdapter(adapterTipoMantenimientos);
					spinnerTipoMant.setSelection(mant.getTipoMantenimiento());
					adapterVehiculos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaVehiculos);
					spinnerVehiculo = (Spinner)editDialog.findViewById(R.id.edit_mant_spinner_vehiculo);
					spinnerVehiculo.setAdapter(adapterVehiculos);
					
					textTaller = (EditText)editDialog.findViewById(R.id.edit_mant_editText4);
					textTaller.setText(mant.getTaller());
					textObservaciones = (EditText)editDialog.findViewById(R.id.edit_mant_editText5);
					textObservaciones.setText(mant.getObservaciones());
					
					Button btnGuardar = (Button)editDialog.findViewById(R.id.edit_mant_button_guardar);
					btnGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View vista) {
							int posicion = spinnerVehiculo.getSelectedItemPosition();
							DetalleVehiculo dv = misVehiculos.get(posicion);
							
							DetalleMantenimiento mantenimiento = new DetalleMantenimiento();
							mantenimiento.setIdDetalleMantenimiento(mant.getIdDetalleMantenimiento());
							mantenimiento.setFecha(Util.convierteStringEnDate(textFecha.getText().toString()));
							mantenimiento.setKilometros(Double.valueOf(textKms.getText().toString()));
							mantenimiento.setPrecio(Double.valueOf(textPrecio.getText().toString()));
							mantenimiento.setTaller(textTaller.getText().toString());
							mantenimiento.setObservaciones(textObservaciones.getText().toString());
							mantenimiento.setTipoMantenimiento(spinnerTipoMant.getSelectedItemPosition());
							mantenimiento.setIdVehiculo(dv.getIdVehiculo());
							
							guardaDatosDelMantenimiento(mantenimiento);
							actualizaListaMantenimientos();
							
							editDialog.dismiss();
						}
					});
					
					Button btnCancelar = (Button)editDialog.findViewById(R.id.edit_mant_button_cancelar);
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
        	imgBorrar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					LinearLayout linear1 = (LinearLayout)view.getParent();
					LinearLayout linear2 = (LinearLayout)linear1.getParent();
					LinearLayout linear3 = (LinearLayout)linear2.getParent();
					mant = mantenimientos.get(linear3.getId());
					
					AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
					builder.setMessage("¿Desea borrar el mantenimiento?");
					builder.setTitle("Borrar");
					builder.setPositiveButton(R.string.boton_aceptar, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							eliminarMantenimiento(mant);
							Toast.makeText(myContext, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
							actualizaListaMantenimientos();
						}
					});
					builder.setNegativeButton(R.string.boton_cancelar, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});
        	
        	layoutMantenimientos.addView(vista, i);
        	i++;
        }
        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.mantenimiento, menu);
	}
	
	private ArrayList<DetalleMantenimiento> recuperaDatosMantenimiento() {
		ArrayList<DetalleMantenimiento> detalles = new ArrayList<DetalleMantenimiento>();
		try {
			if(mantenimientosHelper == null)
				mantenimientosHelper = new MantenimientosSQLiteHelper(rootView.getContext(), Constantes.TABLA_MANTENIMIENTOS, null, 1);
			detalles.addAll(mantenimientosHelper.getMantenimientos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}
	
	private void guardaDatosDelMantenimiento(DetalleMantenimiento mant) {
		try {
			boolean resp = abrirBaseDeDatos();
			if(resp == false) {
				String texto = "Error al abrir o crear la tabla 'Mantenimientos'";
				Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_SHORT).show();
				return;
			}
			
			boolean resultado = insertarFila(mant);
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
			if(mantenimientosHelper == null)
				mantenimientosHelper = new MantenimientosSQLiteHelper(rootView.getContext(), Constantes.TABLA_ACCIDENTES, null, 1);
			baseDatos = mantenimientosHelper.getWritableDatabase();
			resultado = true;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultado;
	}
	
	private boolean insertarFila(DetalleMantenimiento mant) {
		boolean resp = false;
		try {
			ContentValues values = new ContentValues();
			values.put("idMantenimiento", mant.getIdDetalleMantenimiento());
			values.put("fecha", mant.getFecha().getTime());
			values.put("kms", mant.getKilometros());
			values.put("precio", mant.getPrecio());
			values.put("taller", mant.getTaller());
			values.put("tipoMantenimiento", mant.getTipoMantenimiento());
			values.put("observaciones", mant.getObservaciones());
			values.put("idVehiculo", mant.getIdVehiculo());
			
			if(mant.getIdDetalleMantenimiento() == null) {
				resp = (baseDatos.insert(Constantes.TABLA_MANTENIMIENTOS, null, values) > 0);
			}else {
				String[] argumentos = new String[1];
				argumentos[0] = String.valueOf(mant.getIdDetalleMantenimiento());
				resp = (baseDatos.update(Constantes.TABLA_MANTENIMIENTOS, values, "idMantenimiento = ?", argumentos) > 0);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return resp;
	}
	
	private void actualizaListaMantenimientos() {
		Fragment fragment = new MantenimientosFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}
	
	private boolean eliminarMantenimiento(DetalleMantenimiento dm) {
		abrirBaseDeDatos();
		String[] argumentos = new String[1];
		argumentos[0] = String.valueOf(dm.getIdDetalleMantenimiento());
		if(baseDatos.delete(Constantes.TABLA_MANTENIMIENTOS, "idMantenimiento = ?", argumentos) <= 0)
			return false;
		return true;
	}
}
