package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.sql.AccidentesSQLiteHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
	
	private EditText textFecha;
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
			TextView tv1 = (TextView)vista.findViewById(R.id.det_acc_textView1);
			tv1.setText(acc.getFecha().toString());
			TextView tv2 = (TextView)vista.findViewById(R.id.det_acc_textView3);
			tv2.setText(acc.getKilometros().toString());
			TextView tv3 = (TextView)vista.findViewById(R.id.det_acc_textView5);
			tv3.setText(acc.getLugar());
			TextView tv4 = (TextView)vista.findViewById(R.id.det_acc_textView7);
			tv4.setText(acc.getObservaciones());
			
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
					textFecha.setText(detalleEnEdicion.getFecha().toString());
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
							final Calendar c = Calendar.getInstance();
							int year = c.get(Calendar.YEAR);
							int month = c.get(Calendar.MONTH);
							int day = c.get(Calendar.DAY_OF_MONTH);
							
							DatePickerDialog dp = new DatePickerDialog(rootView.getContext(), new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
									textFecha.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
								}
							}, year, month, day);
							dp.show();
						}
					});
					
					Button btnGuardar = (Button)editDialog.findViewById(R.id.edit_acc_btn_guardar);
					btnGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View vista) {
							DetalleAccidente accidente = new DetalleAccidente();
							accidente.setIdDetalleAccidente(detalleEnEdicion.getIdDetalleAccidente());
							String fechaIntroducida = textFecha.getText().toString();
							String[] valores = fechaIntroducida.split("/");
							accidente.setFecha(new Date(Integer.valueOf(valores[2]), Integer.valueOf(valores[1]), Integer.valueOf(valores[0])));
							accidente.setKilometros(Double.valueOf(textKms.getText().toString()));
							accidente.setLugar(textLugar.getText().toString());
							accidente.setObservaciones(textObservaciones.getText().toString());
							accidente.setIdVehiculo(detalleEnEdicion.getIdVehiculo());
							
							guardaDatosDelAccidente(accidente);
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
			
			editDialog.dismiss();
			
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
			
			resp = (baseDatos.insert(Constantes.TABLA_ACCIDENTES, null, values) > 0);
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
}
