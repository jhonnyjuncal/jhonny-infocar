package com.jhonny.infocar.fragments;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


public class MantenimientosFragment extends Fragment {
	
	private FrameLayout fragmento;
	private ScrollView vistaMantenimientos;
	private LinearLayout layoutMantenimientos;
	private ArrayList<DetalleMantenimiento> mantenimientos;
	private View rootView;
	private Dialog editDialog;
	private TypedArray arrayTiposMantenimientos = null;
	private ArrayAdapter<String> adapterTipoMantenimientos;
	private Spinner tipoMantenimiento;
	private ArrayList<String> listaTiposMantenimientos = new ArrayList<String>();
	
	
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
        
        arrayTiposMantenimientos = getResources().obtainTypedArray(R.array.TIPOS_MANTENIMIENTOS);
        arrayTiposMantenimientos.recycle();
        for(int i=0; i<arrayTiposMantenimientos.length(); i++)
        	listaTiposMantenimientos.add(arrayTiposMantenimientos.getString(i));
        
        
        int i = 0;
        for(DetalleMantenimiento dm : mantenimientos) {
        	View vista = inflater.inflate(R.layout.detalle_mantenimiento, layoutMantenimientos, false);
        	
        	vista.setId(i);
        	TextView tv1 = (TextView)vista.findViewById(R.id.det_mant_textView1);
        	tv1.setText(dm.getFecha().toString());
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
					DetalleMantenimiento dm = mantenimientos.get(linear3.getId());
					
					editDialog = new Dialog(rootView.getContext());
					editDialog.setContentView(R.layout.edicion_mantenimiento);
					editDialog.setTitle("Edicion de mantenimiento");
					
					adapterTipoMantenimientos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposMantenimientos);
					
					EditText textFecha = (EditText)editDialog.findViewById(R.id.edit_mant_editText1);
					DateFormat df = DateFormat.getDateInstance();
					textFecha.setText(df.format(dm.getFecha()));
					EditText textKms = (EditText)editDialog.findViewById(R.id.edit_mant_editText2);
					textKms.setText(dm.getKilometros().toString());
					EditText textPrecio = (EditText)editDialog.findViewById(R.id.edit_mant_editText3);
					textPrecio.setText(dm.getPrecio().toString());
					tipoMantenimiento = (Spinner)editDialog.findViewById(R.id.edit_mant_spinner_tipo);
					tipoMantenimiento.setAdapter(adapterTipoMantenimientos);
					tipoMantenimiento.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							String tipoSeleccionado = arrayTiposMantenimientos.getString(position);
							String texto = "Tipo seleccionado: " + tipoSeleccionado;
							Log.d("MantenimientosFragment", texto);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							Log.d("MantenimientosFragment", "Nada seleccionado...");
						}
					});
					tipoMantenimiento.setSelection(dm.getTipoMantenimiento());
					
					EditText textTaller = (EditText)editDialog.findViewById(R.id.edit_mant_editText4);
					textTaller.setText(dm.getTaller());
					EditText textObservaciones = (EditText)editDialog.findViewById(R.id.edit_mant_editText5);
					textObservaciones.setText(dm.getObservaciones());
					
					Button btnGuardar = (Button)editDialog.findViewById(R.id.edit_mant_button_guardar);
					btnGuardar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View vista) {
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
			DetalleMantenimiento dm1 = new DetalleMantenimiento();
			dm1.setIdDetalleMantenimiento(0);
			dm1.setFecha(new Date());
			dm1.setKilometros(Double.valueOf(20000));
			dm1.setObservaciones("Revision de los 20mil kms");
			dm1.setPrecio(Double.valueOf(220));
			dm1.setTaller("Ford de santiago");
			dm1.setTipoMantenimiento(1);
			detalles.add(dm1);
			
			DetalleMantenimiento dm2 = new DetalleMantenimiento();
			dm2.setIdDetalleMantenimiento(1);
			dm2.setFecha(new Date());
			dm2.setKilometros(Double.valueOf(40000));
			dm2.setObservaciones("Revision de los 40mil kms");
			dm2.setPrecio(Double.valueOf(250));
			dm2.setTaller("Ford de coruña");
			dm2.setTipoMantenimiento(0);
			detalles.add(dm2);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return detalles;
	}
}
