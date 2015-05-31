package com.jhonny.infocar.fragments;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
	private FragmentActivity myContext;
	
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
	private ArrayList<DetalleMantenimientoFragment> listaDetalles;
	
	private EditText textFecha;
	private ImageView imgCalendar;
	private EditText textKms;
	private EditText textPrecio;
	private Spinner spinnerTipoMant;
	private Spinner spinnerVehiculo;
	private EditText textTaller;
	private EditText textObservaciones;
	private DetalleMantenimiento mant;

	private MyAdapter mAdapter;
	private ViewPager mPager;
	
	
	public MantenimientosFragment() {
		
	}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
			mantenimientos = recuperaDatosMantenimiento();

			if(mantenimientos != null) {
				listaDetalles = new ArrayList<DetalleMantenimientoFragment>();
				for(int i=0; i<mantenimientos.size(); i++) {
					listaDetalles.add(new DetalleMantenimientoFragment());
				}
			}

			arrayTiposMantenimientos = getResources().obtainTypedArray(R.array.TIPOS_MANTENIMIENTOS);
			arrayTiposMantenimientos.recycle();
			for (int i = 0; i < arrayTiposMantenimientos.length(); i++)
				listaTiposMantenimientos.add(arrayTiposMantenimientos.getString(i));

			arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
			misVehiculos = recuperaDatosVehiculos();
			for (DetalleVehiculo dv : misVehiculos) {
				String marca = arrayMarcas.getString(dv.getMarca());
				listaVehiculos.add(marca + " " + dv.getModelo());
			}

			int i = 0;
			for (DetalleMantenimiento dm : mantenimientos) {
				View vista = inflater.inflate(R.layout.fragment_detalle_mantenimiento, layoutMantenimientos, false);

				vista.setId(i);
				String marcaymodelo = null;
				for (DetalleVehiculo dv : misVehiculos) {
					if (dv.getIdVehiculo().equals(dm.getIdVehiculo())) {
						marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
						break;
					}
				}
				TextView tv1 = (TextView) vista.findViewById(R.id.det_mant_textView1);
				tv1.setText(marcaymodelo);
				TextView tv2 = (TextView) vista.findViewById(R.id.det_mant_textView3);
				DateFormat df = DateFormat.getDateInstance();
				tv2.setText(df.format(dm.getFecha()));
				TextView tv3 = (TextView) vista.findViewById(R.id.det_mant_textView5);
				tv3.setText(dm.getKilometros().toString());
				TextView tv4 = (TextView) vista.findViewById(R.id.det_mant_textView7);
				tv4.setText(dm.getPrecio().toString());
				TextView tv5 = (TextView) vista.findViewById(R.id.det_mant_textView9);
				String mantenimientoSeleccionado = listaTiposMantenimientos.get(dm.getTipoMantenimiento());
				tv5.setText(mantenimientoSeleccionado);
				TextView tv6 = (TextView) vista.findViewById(R.id.det_mant_textView11);
				tv6.setText(dm.getTaller());


				ImageView imgEditar = (ImageView) vista.findViewById(R.id.imageView_editar);
				imgEditar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						LinearLayout linear1 = (LinearLayout) view.getParent();
						LinearLayout linear2 = (LinearLayout) linear1.getParent();
						LinearLayout linear3 = (LinearLayout) linear2.getParent();
						mant = mantenimientos.get(linear3.getId());

						editDialog = new Dialog(rootView.getContext());
						editDialog.setContentView(R.layout.edicion_mantenimiento);
						editDialog.setTitle("Edicion de mantenimiento");

						textFecha = (EditText) editDialog.findViewById(R.id.edit_mant_editText1);
						final String fechaEnEdicion = Util.convierteDateEnString(mant.getFecha());
						textFecha.setText(fechaEnEdicion);
						imgCalendar = (ImageView) editDialog.findViewById(R.id.edit_veh_imageView1);
						imgCalendar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String[] fechaEdicion = fechaEnEdicion.split("/");
								final Calendar c = Calendar.getInstance();

								int year = c.get(Calendar.YEAR);
								int month = c.get(Calendar.MONTH);
								int day = c.get(Calendar.DAY_OF_MONTH);

								if (fechaEdicion != null && fechaEdicion.length > 0) {
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
						textKms = (EditText) editDialog.findViewById(R.id.edit_mant_editText2);
						textKms.setText(mant.getKilometros().toString());
						textPrecio = (EditText) editDialog.findViewById(R.id.edit_mant_editText3);
						textPrecio.setText(mant.getPrecio().toString());
						adapterTipoMantenimientos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposMantenimientos);
						spinnerTipoMant = (Spinner) editDialog.findViewById(R.id.edit_mant_spinner_tipo);
						spinnerTipoMant.setAdapter(adapterTipoMantenimientos);
						spinnerTipoMant.setSelection(mant.getTipoMantenimiento());
						adapterVehiculos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaVehiculos);
						spinnerVehiculo = (Spinner) editDialog.findViewById(R.id.edit_mant_spinner_vehiculo);
						spinnerVehiculo.setAdapter(adapterVehiculos);

						textTaller = (EditText) editDialog.findViewById(R.id.edit_mant_editText4);
						textTaller.setText(mant.getTaller());
						textObservaciones = (EditText) editDialog.findViewById(R.id.edit_mant_editText5);
						textObservaciones.setText(mant.getObservaciones());

						Button btnGuardar = (Button) editDialog.findViewById(R.id.edit_mant_button_guardar);
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

								if (comprobacionDatosMantenimiento(mantenimiento)) {
									guardaDatosDelMantenimiento(mantenimiento);
									actualizaListaMantenimientos();
									editDialog.dismiss();

								}
							}
						});

						Button btnCancelar = (Button) editDialog.findViewById(R.id.edit_mant_button_cancelar);
						btnCancelar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View vista) {
								editDialog.cancel();
							}
						});
						editDialog.show();
					}
				});


				ImageView imgBorrar = (ImageView) vista.findViewById(R.id.imageView_borrar);
				imgBorrar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							LinearLayout linear1 = (LinearLayout) view.getParent();
							LinearLayout linear2 = (LinearLayout) linear1.getParent();
							LinearLayout linear3 = (LinearLayout) linear2.getParent();
							mant = mantenimientos.get(linear3.getId());

							AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
							builder.setCancelable(true);
							builder.setTitle("Eliminar mantenimiento");
							builder.setMessage("¿Seguro que desea borrar este mantenimiento?");
							builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									eliminarMantenimiento(mant);
									actualizaListaMantenimientos();
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

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				//layoutMantenimientos.addView(vista, i);
				i++;
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			mPager = (ViewPager)rootView.findViewById(R.id.mant_pager);
			mPager.setAdapter(mAdapter);
			mPager.setPageTransformer(true, new ZoomOutPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.mant_indicator);
			cIndicator.setViewPager(mPager);

		}catch(Exception ex) {
			ex.printStackTrace();
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
            MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
			detalles.addAll(mantHelper.getMantenimientos());
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
            boolean resultado = false;
            MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
            if(mant.getIdDetalleMantenimiento() == null)
			    resultado = mantHelper.insertarMantenimiento(mant);
            else
                resultado = mantHelper.actualizarMantenimiento(mant);

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
	
	private void actualizaListaMantenimientos() {
		Fragment fragment = new MantenimientosFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
	}
	
	private boolean eliminarMantenimiento(DetalleMantenimiento dm) {
        MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
        return mantHelper.borrarMantenimiento(dm);
	}

    private boolean comprobacionDatosMantenimiento(DetalleMantenimiento dm) {
        boolean result = true;
        try {

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

	@Override
	public void onResume(){
		super.onResume();
		cargaFondoDePantalla();
	}

	private synchronized void cargaFondoDePantalla() {
		try {
			SharedPreferences prop = myContext.getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
			int fondoSeleccionado = 1;
			if(prop != null) {
				SharedPreferences.Editor editor = prop.edit();
				if(editor != null) {
					if(prop.contains(Constantes.FONDO_PANTALLA)) {
						fondoSeleccionado = prop.getInt(Constantes.FONDO_PANTALLA, 1);
					}
				}
			}

			String imagen = Constantes.FONDO_1;
			switch(fondoSeleccionado) {
				case 1:
					imagen = Constantes.FONDO_1;
					break;
				case 2:
					imagen = Constantes.FONDO_2;
					break;
				case 3:
					imagen = Constantes.FONDO_3;
					break;
			}
			int imageResource1 = myContext.getApplicationContext().getResources().getIdentifier(imagen, "drawable", myContext.getApplicationContext().getPackageName());
			Drawable image = myContext.getResources().getDrawable(imageResource1);
			ImageView imageView = (ImageView)myContext.findViewById(R.id.fondo_principal);
			imageView.setImageDrawable(image);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}






	public static class MyAdapter extends FragmentStatePagerAdapter {
		private ArrayList<DetalleMantenimientoFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleMantenimientoFragment> listaDetalles) {
			super(fm);
			this.listaDetalles = listaDetalles;
		}

		@Override
		public int getCount() {
			return listaDetalles.size();
		}

		@Override
		public Fragment getItem(int position) {
			return listaDetalles.get(position);
		}
	}
}
