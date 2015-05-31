package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.DepthPageTransformer;
import com.jhonny.infocar.model.DetalleReparacion;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ReparacionesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
	private ArrayAdapter<String> adapterReparaciones;
	private Spinner spinnerTipo;
	private Button botonNuevo;
	private DetalleReparacion detalleEnEdicion;
	private ArrayList<DetalleReparacionFragment> listaDetalles;
	
	private TypedArray arrayTiposReparaciones;
	private TypedArray arrayMarcas;
	private FragmentActivity myContext;
	
	private ArrayList<DetalleReparacion> reparaciones;
	private ArrayList<String> listaReparaciones = new ArrayList<String>();
	private ArrayList<String> listaVehiculos = new ArrayList<String>();
	private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
	
	private EditText textFecha;
	private EditText textKms;
	private EditText textPrecio;
	private EditText textTaller;
	private EditText textObservaciones;

	private MyAdapter mAdapter;
	private ViewPager mPager;
	
	
	public ReparacionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
			fragmento = (FrameLayout)rootView.findViewById(R.id.fragment_reparaciones);
			reparaciones = recuperaDatosReparaciones();

			if(reparaciones != null) {
				listaDetalles = new ArrayList<DetalleReparacionFragment>();
				for(int i=0; i<reparaciones.size(); i++) {
					listaDetalles.add(new DetalleReparacionFragment());
				}
			}

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
				View vista = inflater.inflate(R.layout.fragment_detalle_reparacion, layoutReparaciones, false);

				vista.setId(i);
				String marcaymodelo = null;
				for (DetalleVehiculo dv : misVehiculos) {
					if (dv.getIdVehiculo().equals(detalle.getIdVehiculo())) {
						marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
						break;
					}
				}
				TextView tv1 = (TextView) vista.findViewById(R.id.det_rep_textView1);
				tv1.setText(marcaymodelo);
				TextView tv2 = (TextView) vista.findViewById(R.id.det_rep_textView3);
				tv2.setText(Util.convierteDateEnString(detalle.getFecha()));
				TextView tv3 = (TextView) vista.findViewById(R.id.det_rep_textView5);
				tv3.setText(detalle.getKilometros().toString());
				TextView tv4 = (TextView) vista.findViewById(R.id.det_rep_textView7);
				tv4.setText(detalle.getPrecio().toString());
				TextView tv5 = (TextView) vista.findViewById(R.id.det_rep_textView9);
				String tipoReparacionSeleccionada = listaReparaciones.get(detalle.getIdTipoReparacion());
				tv5.setText(tipoReparacionSeleccionada);
				TextView tv6 = (TextView) vista.findViewById(R.id.det_rep_textView11);
				tv6.setText(detalle.getTaller());

				ImageView imgEditar = (ImageView) vista.findViewById(R.id.imageView_editar);
				imgEditar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						LinearLayout linear1 = (LinearLayout) view.getParent();
						LinearLayout linear2 = (LinearLayout) linear1.getParent();
						LinearLayout linear3 = (LinearLayout) linear2.getParent();
						detalleEnEdicion = reparaciones.get(linear3.getId());

						editDialog = new Dialog(rootView.getContext());
						editDialog.setContentView(R.layout.edicion_reparacion);
						editDialog.setTitle("Edicion de reparacion");

						textFecha = (EditText) editDialog.findViewById(R.id.edit_rep_fecha);
						textFecha.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
						textKms = (EditText) editDialog.findViewById(R.id.edit_rep_kms);
						textKms.setText(detalleEnEdicion.getKilometros().toString());
						textPrecio = (EditText) editDialog.findViewById(R.id.edit_rep_coste);
						textPrecio.setText(detalleEnEdicion.getPrecio().toString());
						spinnerTipo = (Spinner) editDialog.findViewById(R.id.edit_rep_spinner_tipo);
						adapterReparaciones = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaReparaciones);
						spinnerTipo.setAdapter(adapterReparaciones);
						spinnerTipo.setSelection(detalleEnEdicion.getIdTipoReparacion());
						textTaller = (EditText) editDialog.findViewById(R.id.edit_rep_taller);
						textTaller.setText(detalleEnEdicion.getTaller());
						textObservaciones = (EditText) editDialog.findViewById(R.id.edit_rep_obs);
						textObservaciones.setText(detalleEnEdicion.getObservaciones());

						Button botonGuardar = (Button) editDialog.findViewById(R.id.edit_rep_btn_guardar);
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

								//guardaDatosDeLaReparacion(rep);
								ReparacionesSQLiteHelper reparacionesHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
								boolean resultado = reparacionesHelper.actualizarReparacion(rep);

								String texto = "";
								if (resultado) {
									texto = "Datos guardados correctamente";
								} else {
									texto = "No se han podido almacenar los datos";
								}
								Toast.makeText(myContext, texto, Toast.LENGTH_SHORT).show();

								actualizaListaReparaciones();
								editDialog.dismiss();
							}
						});

						Button botonCancelar = (Button) editDialog.findViewById(R.id.edit_rep_btn_cancelar);
						botonCancelar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								editDialog.cancel();
							}
						});

						editDialog.show();
					}
				});

				ImageView imgBorrar = (ImageView) vista.findViewById(R.id.imageView_borrar);
				imgBorrar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						try {
							LinearLayout linear1 = (LinearLayout) view.getParent();
							LinearLayout linear2 = (LinearLayout) linear1.getParent();
							LinearLayout linear3 = (LinearLayout) linear2.getParent();
							final DetalleReparacion dr = reparaciones.get(linear3.getId());

							AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
							builder.setCancelable(true);
							builder.setTitle("Eliminar reparación");
							builder.setMessage("¿Seguro que desea borrar esta reparación?");
							builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										ReparacionesSQLiteHelper reparacionesHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
										boolean resultado = reparacionesHelper.borrarReparacion(dr);

										String texto = "";
										if (resultado) {
											texto = "La reparación ha sido borrada con exito";
										} else {
											texto = "Ha ocurrido un error al intentar eliminar los datos de la reparación";
										}
										Toast.makeText(myContext, texto, Toast.LENGTH_SHORT).show();
										actualizaListaReparaciones();

									} catch (Exception ex) {
										ex.printStackTrace();
									}
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
				//layoutReparaciones.addView(vista, i);
				i++;
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			mPager = (ViewPager)rootView.findViewById(R.id.rep_pager);
			mPager.setAdapter(mAdapter);
			mPager.setPageTransformer(true, new DepthPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.rep_indicator);
			cIndicator.setViewPager(mPager);

		}catch (Exception ex) {
			ex.printStackTrace();
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
            ReparacionesSQLiteHelper reparacionesHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
            reparaciones.addAll(reparacionesHelper.getReparaciones());
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
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}
	
	private void actualizaListaReparaciones() {
		Fragment fragment = new ReparacionesFragment();
		FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
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
		private ArrayList<DetalleReparacionFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleReparacionFragment> listaDetalles) {
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
