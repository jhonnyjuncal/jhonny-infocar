package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.animations.DepthPageTransformer;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleVehiculo;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class VehiculoFragment extends Fragment {
	
	private FrameLayout fragmento;
	//private LinearLayout vistaVehiculos;
	private LinearLayout layoutVehiculos;
	private ArrayList<DetalleVehiculo> vehiculos;
	private View rootView;
	private Dialog editDialog;
	private FragmentActivity myContext;
	private ArrayList<DetalleVehiculoFragment> listaDetalles;
	
	private ArrayAdapter<String> adapterMarcas;
	private ArrayAdapter<String> adapterCarburantes;
	private ArrayAdapter<String> adapterTiposVeh;
	private TypedArray arrayMarcas;
	private TypedArray arrayCarburantes;
	private TypedArray arrayTiposVeh;
	private Spinner spinnerMarcas = null;
	private Spinner spinnerCarburantes = null;
	private Spinner spinnerTiposVeh = null;
	private ArrayList<String> listaMarcas = new ArrayList<String>();
	private ArrayList<String> listaTiposVeh = new ArrayList<String>();
	private ArrayList<String> listaCarburantes = new ArrayList<String>();

	private MyAdapter mAdapter;
	private ViewPager mPager;

	
	public VehiculoFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		try {
			rootView = inflater.inflate(R.layout.fragment_vehiculo, container, false);
			//fragmento = (FrameLayout) rootView.findViewById(R.id.fragment_vehiculo);
			//layoutVehiculos = (LinearLayout) fragmento.findViewById(R.id.veh_linear);
			vehiculos = recuperaDatosVehiculos();

			if(vehiculos != null) {
				listaDetalles = new ArrayList<DetalleVehiculoFragment>();
				for(int i=0; i<vehiculos.size(); i++) {
					listaDetalles.add(new DetalleVehiculoFragment());
				}
			}

			arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
			arrayMarcas.recycle();
			for (int i = 0; i < arrayMarcas.length(); i++)
				listaMarcas.add(arrayMarcas.getString(i));

			arrayTiposVeh = getResources().obtainTypedArray(R.array.TIPOS_VEHICULO);
			arrayTiposVeh.recycle();
			for (int i = 0; i < arrayTiposVeh.length(); i++)
				listaTiposVeh.add(arrayTiposVeh.getString(i));

			arrayCarburantes = getResources().obtainTypedArray(R.array.TIPOS_CARBURANTE);
			arrayCarburantes.recycle();
			for (int i = 0; i < arrayCarburantes.length(); i++)
				listaCarburantes.add(arrayCarburantes.getString(i));

			int i = 0;
			for(DetalleVehiculo dv : vehiculos) {
				View vista = inflater.inflate(R.layout.fragment_detalle_vehiculo, layoutVehiculos, false);
				vista.setId(i);
				TextView tv1 = (TextView) vista.findViewById(R.id.det_veh_textView1);
				tv1.setText(dv.getFechaCompra().toString());
				TextView tv2 = (TextView) vista.findViewById(R.id.det_veh_textView3);
				String marcaVehiculo = listaMarcas.get(dv.getMarca());
				tv2.setText(marcaVehiculo);
				TextView tv3 = (TextView) vista.findViewById(R.id.det_veh_textView5);
				tv3.setText(dv.getModelo());
				TextView tv4 = (TextView) vista.findViewById(R.id.det_veh_textView7);
				tv4.setText(dv.getFechaCompra().toString());
				TextView tv5 = (TextView) vista.findViewById(R.id.det_veh_textView9);
				String tipoVehiculo = listaTiposVeh.get(dv.getTipoVehiculo());
				tv5.setText(tipoVehiculo);
				TextView tv6 = (TextView) vista.findViewById(R.id.det_veh_textView11);
				tv6.setText(dv.getMatricula());
				TextView tv7 = (TextView) vista.findViewById(R.id.det_veh_textView13);
				String tipoCarburante = listaCarburantes.get(dv.getTipoCarburante());
				tv7.setText(tipoCarburante);

				ImageView imgItv = (ImageView) vista.findViewById(R.id.imageView2);
				imgItv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d("VehiculoFragment", "Mostrar fragmento de la ITV");
					}
				});

				ImageView imgSeguro = (ImageView) vista.findViewById(R.id.imageView3);
				imgSeguro.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d("VehiculoFragment", "Mostrar fragmento de los datos del seguro");
					}
				});

				ImageView imgEditar = (ImageView) vista.findViewById(R.id.imageView4);
				imgEditar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View vista) {
						LinearLayout linear1 = (LinearLayout) vista.getParent();
						LinearLayout linear2 = (LinearLayout) linear1.getParent();
						LinearLayout linear3 = (LinearLayout) linear2.getParent();
						LinearLayout linear4 = (LinearLayout) linear3.getParent();
						final DetalleVehiculo dv = vehiculos.get(linear4.getId());

						editDialog = new Dialog(rootView.getContext());
						editDialog.setContentView(R.layout.edicion_vehiculo);
						editDialog.setTitle("Edicion de vehiculo");

						adapterMarcas = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaMarcas);
						spinnerMarcas = (Spinner) editDialog.findViewById(R.id.edit_veh_spinner_marca);
						spinnerMarcas.setAdapter(adapterMarcas);
						spinnerMarcas.setSelection(dv.getMarca());

						final EditText textModelo = (EditText) editDialog.findViewById(R.id.edit_veh_editText1);
						textModelo.setText(dv.getModelo());
						final EditText textKilometros = (EditText) editDialog.findViewById(R.id.edit_veh_editText2);
						textKilometros.setText(dv.getKilometros().toString());
						final EditText textFecha = (EditText) editDialog.findViewById(R.id.edit_veh_editText3);
						textFecha.setText(Util.convierteDateEnString(dv.getFechaCompra()));

						adapterTiposVeh = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaTiposVeh);
						spinnerTiposVeh = (Spinner) editDialog.findViewById(R.id.edit_veh_spinner_tipo);
						spinnerTiposVeh.setAdapter(adapterTiposVeh);
						spinnerTiposVeh.setSelection(dv.getTipoVehiculo());

						adapterCarburantes = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, listaCarburantes);
						spinnerCarburantes = (Spinner) editDialog.findViewById(R.id.edit_veh_spinner_carburante);
						spinnerCarburantes.setAdapter(adapterCarburantes);
						spinnerCarburantes.setSelection(dv.getTipoCarburante());

						final EditText textMatricula = (EditText) editDialog.findViewById(R.id.edit_veh_editText4);
						textMatricula.setText(dv.getMatricula());

						Button btnGuardar = (Button) editDialog.findViewById(R.id.edit_veh_button_guardar);
						btnGuardar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								DetalleVehiculo dvEditado = new DetalleVehiculo();
								dvEditado.setIdVehiculo(dv.getIdVehiculo());
								dvEditado.setMarca(spinnerMarcas.getSelectedItemPosition());
								dvEditado.setModelo(textModelo.getText().toString());
								dvEditado.setKilometros(Double.valueOf(textKilometros.getText().toString()));
								dvEditado.setFechaCompra(Util.convierteStringEnDate(textFecha.getText().toString()));
								dvEditado.setMatricula(textMatricula.getText().toString());
								dvEditado.setTipoVehiculo(spinnerTiposVeh.getSelectedItemPosition());
								dvEditado.setTipoCarburante(spinnerCarburantes.getSelectedItemPosition());
								dvEditado.setIdSeguro(null);
								dvEditado.setIdItv(null);

								if (comprobacionDatosVehiculo(dvEditado)) {
									VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
									boolean resp = vehiculosHelper.actualizarVehiculo(dvEditado);

									String texto = "";
									if (resp) {
										texto = "Datos guardados correctamente";
									} else {
										texto = "No se han podido almacenar los datos";
									}
									Toast.makeText(myContext, texto, Toast.LENGTH_SHORT).show();

									actualizarListadoDeVehiculos();
									editDialog.dismiss();
								}
							}
						});

						Button btnCancelar = (Button) editDialog.findViewById(R.id.edit_veh_button_cancelar);
						btnCancelar.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								editDialog.cancel();
							}
						});

						editDialog.show();
					}
				});

				ImageView imgBorrar = (ImageView) vista.findViewById(R.id.imageView5);
				imgBorrar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View view) {
						try {
							LinearLayout linear1 = (LinearLayout) view.getParent();
							LinearLayout linear2 = (LinearLayout) linear1.getParent();
							LinearLayout linear3 = (LinearLayout) linear2.getParent();
							LinearLayout linear4 = (LinearLayout) linear3.getParent();
							final DetalleVehiculo dv = vehiculos.get(linear4.getId());

							AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
							builder.setCancelable(true);
							builder.setTitle("Eliminar registro");
							builder.setMessage("¿Seguro que desea borrar este vehiculo?");
							builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
									boolean resp = vehiculosHelper.borrarVehiculo(dv);

									String texto = "";
									if (resp)
										texto = "Los datos del vehiculo han sido eliminados correctamente";
									else
										texto = "No se han podido eliminar los datos del vehiculo";
									Toast.makeText(myContext, texto, Toast.LENGTH_SHORT).show();

									actualizarListadoDeVehiculos();
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
				//layoutVehiculos.addView(vista, i);
				i++;
			}

			mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
			mPager = (ViewPager)rootView.findViewById(R.id.veh_pager);
			mPager.setAdapter(mAdapter);
			mPager.setPageTransformer(true, new ZoomOutPageTransformer());

			CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.veh_indicator);
			cIndicator.setViewPager(mPager);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.vehiculo, menu);
	}
	
	@Override
	public void onAttach(Activity activity) {
		myContext = (FragmentActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume(){
		super.onResume();
		cargaFondoDePantalla();
	}
	
	private ArrayList<DetalleVehiculo> recuperaDatosVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		
		try {
			VehiculosSQLiteHelper vehiculosHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
			lista.addAll(vehiculosHelper.getVehiculos());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

    private void actualizarListadoDeVehiculos() {
        Fragment fragment = new VehiculoFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }

    private boolean comprobacionDatosVehiculo(DetalleVehiculo dv) {
        boolean result = true;
        try {

        }catch(Exception ex) {
            ex.printStackTrace();;
        }
        return result;
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
			if(imageView != null)
				imageView.setImageDrawable(image);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment fragment = null;

		switch(item.getItemId()) {
			case R.id.action_nuevo:
				fragment = new NuevoVehiculoFragment();
				if(fragment != null) {
					FragmentManager fragmentManager = myContext.getSupportFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
				}
				return true;

			case R.id.action_ordenar:
				return true;

			case R.id.action_borrar_todo:
				return true;

			case R.id.action_ayuda:
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}




	public static class MyAdapter extends FragmentStatePagerAdapter {
		private ArrayList<DetalleVehiculoFragment> listaDetalles;

		public MyAdapter(FragmentManager fm, ArrayList<DetalleVehiculoFragment> listaDetalles) {
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
