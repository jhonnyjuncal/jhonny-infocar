package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;


/**
 * Created by jhonny on 22/05/2015.
 */
public class DetalleVehiculoFragment extends Fragment {

    private View rootView = null;
    private FragmentActivity myContext;
    private Dialog editDialog;
    private static Integer positionEnLaLista = null;

    private ArrayList<String> listaMarcas = new ArrayList<String>();
    private ArrayList<String> listaTiposVeh = new ArrayList<String>();
    private ArrayList<String> listaCarburantes = new ArrayList<String>();
    private ArrayList<DetalleVehiculo> vehiculos;

    private TypedArray arrayMarcas;
    private TypedArray arrayCarburantes;
    private TypedArray arrayTiposVeh;
    private ArrayAdapter<String> adapterMarcas;
    private ArrayAdapter<String> adapterCarburantes;
    private ArrayAdapter<String> adapterTiposVeh;
    private Spinner spinnerMarcas = null;
    private Spinner spinnerCarburantes = null;
    private Spinner spinnerTiposVeh = null;
    private DetalleVehiculo detalleEnEdicion;


    public static DetalleVehiculoFragment newInstance(Bundle arguments) {
        DetalleVehiculoFragment f = new DetalleVehiculoFragment();
        if(arguments != null){
            Integer position = (Integer)arguments.get("position");
            positionEnLaLista = position;
            f.setArguments(arguments);
        }
        return f;
    }

    public DetalleVehiculoFragment () {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_detalle_vehiculo, container, false);

            Bundle arguments = getArguments();
            if(arguments != null) {
                Integer position = arguments.getInt("position");
                if(position != null) {
                    vehiculos = recuperaDatosVehiculos();
                    myContext.invalidateOptionsMenu();

                    if(vehiculos != null) {
                        arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
                        for (int i = 0; i < arrayMarcas.length(); i++)
                            listaMarcas.add(arrayMarcas.getString(i));
                        arrayMarcas.recycle();

                        arrayTiposVeh = getResources().obtainTypedArray(R.array.TIPOS_VEHICULO);
                        for (int i = 0; i < arrayTiposVeh.length(); i++)
                            listaTiposVeh.add(arrayTiposVeh.getString(i));
                        arrayTiposVeh.recycle();

                        arrayCarburantes = getResources().obtainTypedArray(R.array.TIPOS_CARBURANTE);
                        for (int i = 0; i < arrayCarburantes.length(); i++)
                            listaCarburantes.add(arrayCarburantes.getString(i));
                        arrayCarburantes.recycle();

                        detalleEnEdicion = vehiculos.get(position);
                        detalleEnEdicion.setPosicion(position);

                        TextView tv1 = (TextView)rootView.findViewById(R.id.det_veh_textView2);
                        String marcaVehiculo = listaMarcas.get(detalleEnEdicion.getMarca());
                        tv1.setText(marcaVehiculo);
                        TextView tv2 = (TextView)rootView.findViewById(R.id.det_veh_textView4);
                        tv2.setText(detalleEnEdicion.getModelo());
                        TextView tv3 = (TextView)rootView.findViewById(R.id.det_veh_textView6);
                        tv3.setText(detalleEnEdicion.getKilometros().toString());
                        TextView tv4 = (TextView)rootView.findViewById(R.id.det_veh_textView8);
                        tv4.setText(Util.formateaFechaParaMostrar(detalleEnEdicion.getFechaCompra()));
                        TextView tv5 = (TextView)rootView.findViewById(R.id.det_veh_textView10);
                        String tipoVehiculo = listaTiposVeh.get(detalleEnEdicion.getTipoVehiculo());
                        tv5.setText(tipoVehiculo);
                        TextView tv6 = (TextView)rootView.findViewById(R.id.det_veh_textView12);
                        tv6.setText(detalleEnEdicion.getMatricula());
                        TextView tv7 = (TextView)rootView.findViewById(R.id.det_veh_textView14);
                        String tipoCarburante = listaCarburantes.get(detalleEnEdicion.getTipoCarburante());
                        tv7.setText(tipoCarburante);

                        ImageView imgItv = (ImageView)rootView.findViewById(R.id.img1);
                        imgItv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Fragment fragment = new ItvFragment();
                                    FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();

                                }catch(Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        ImageView imgFichaTecnica = (ImageView)rootView.findViewById(R.id.img2);
                        imgFichaTecnica.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Fragment fragment = new FichaTecnicaFragment();
                                    FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();

                                }catch(Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        ImageView imgSeguro = (ImageView)rootView.findViewById(R.id.img3);
                        imgSeguro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Fragment fragment = new SeguroFragment();
                                    FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();

                                }catch(Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });


                        /*
                        ImageView imgItv = (ImageView) rootView.findViewById(R.id.imageView2);
                        imgItv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("VehiculoFragment", "Mostrar fragmento de la ITV");
                            }
                        });

                        ImageView imgSeguro = (ImageView) rootView.findViewById(R.id.imageView3);
                        imgSeguro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("VehiculoFragment", "Mostrar fragmento de los datos del seguro");
                            }
                        });

                        ImageView imgEditar = (ImageView) rootView.findViewById(R.id.imageView4);
                        imgEditar.setOnClickListener(new View.OnClickListener() {
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
                                btnGuardar.setOnClickListener(new View.OnClickListener() {
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
                                btnCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        editDialog.cancel();
                                    }
                                });

                                editDialog.show();
                            }
                        });

                        ImageView imgBorrar = (ImageView) rootView.findViewById(R.id.imageView5);
                        imgBorrar.setOnClickListener(new View.OnClickListener() {
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
                        */
                    }
                }
            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.detalle_vehiculo, menu);

        if(vehiculos != null && vehiculos.size() == 0) {
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);

        }else {
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.menu_det_veh_nuevo:
                fragment = new NuevoVehiculoFragment();
                if(fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.menu_det_veh_editar:
                if(vehiculos != null) {
                    int actual = VehiculoFragment.paginadorVehiculos.getCurrentItem();
                    detalleEnEdicion = vehiculos.get(actual);
                    fragment = NuevoVehiculoFragment.newInstance(detalleEnEdicion);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.menu_det_veh_eliminar:
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setCancelable(true);
                builder.setTitle(getResources().getString(R.string.titulo_eliminar_vehiculo));
                builder.setMessage(getResources().getString(R.string.mensaje_pregunta_borrar_vehiculo));
                builder.setPositiveButton(getResources().getString(R.string.texto_boton_eliminar), new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int actual = VehiculoFragment.paginadorVehiculos.getCurrentItem();
                        detalleEnEdicion = vehiculos.get(actual);
                        if (eliminarVehiculo(detalleEnEdicion)) {
                            actualizarListadoDeVehiculos();
                            dialog.dismiss();
                            Toast.makeText(myContext, getResources().getString(R.string.mensaje_borrar_ok), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(myContext, getResources().getString(R.string.mensaje_borrar_error), Toast.LENGTH_SHORT).show();
                        }

                        actualizarListadoDeVehiculos();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.texto_boton_cancelar), new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    private boolean comprobacionDatosVehiculo(DetalleVehiculo dv) {
        boolean result = true;
        try {

        }catch(Exception ex) {
            ex.printStackTrace();;
        }
        return result;
    }

    private void actualizarListadoDeVehiculos() {
        Fragment fragment = new VehiculoFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }

    private boolean eliminarVehiculo(DetalleVehiculo dv) {
        VehiculosSQLiteHelper vehHelper = new VehiculosSQLiteHelper(myContext, Constantes.TABLA_VEHICULOS, null, 1);
        return vehHelper.borrarVehiculo(dv);
    }
}
