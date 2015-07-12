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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleReparacion;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.ReparacionesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;


/**
 * Created by jhonny on 22/05/2015.
 */
public class DetalleReparacionFragment extends Fragment {

    private View rootView = null;
    private ArrayList<DetalleReparacion> reparaciones;
    private DetalleReparacion detalleEnEdicion;
    private Dialog editDialog;

    private TypedArray arrayTiposReparaciones;
    private TypedArray arrayMarcas;
    private FragmentActivity myContext;

    private ArrayList<String> listaReparaciones = new ArrayList<String>();
    private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
    private ArrayList<String> listaVehiculos = new ArrayList<String>();

    private EditText textFecha;
    private EditText textKms;
    private EditText textPrecio;
    private EditText textTaller;
    private EditText textObservaciones;
    private Spinner spinnerTipo;
    private ArrayAdapter<String> adapterReparaciones;


    public static DetalleReparacionFragment newInstance(Bundle arguments) {
        DetalleReparacionFragment f = new DetalleReparacionFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public DetalleReparacionFragment () {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_detalle_reparacion, container, false);

            Bundle arguments = getArguments();
            if(arguments != null) {
                Integer position = arguments.getInt("position");
                if(position != null) {
                    reparaciones = recuperaDatosReparaciones();
                    misVehiculos = recuperaDatosVehiculos();

                    if(reparaciones != null && reparaciones.size() > 0) {
                        arrayTiposReparaciones = getResources().obtainTypedArray(R.array.TIPOS_REPARACIONES);
                        arrayTiposReparaciones.recycle();
                        for(int i=0; i<arrayTiposReparaciones.length(); i++)
                            listaReparaciones.add(arrayTiposReparaciones.getString(i));

                        arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
                        for(DetalleVehiculo dv : misVehiculos) {
                            String marca = arrayMarcas.getString(dv.getMarca());
                            listaVehiculos.add(marca + " " + dv.getModelo());
                        }

                        detalleEnEdicion = reparaciones.get(position);
                        detalleEnEdicion.setPosicion(position);

                        String marcaymodelo = null;
                        for (DetalleVehiculo dv : misVehiculos) {
                            if (dv.getIdVehiculo().equals(detalleEnEdicion.getIdVehiculo())) {
                                marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
                                break;
                            }
                        }

                        TextView tv2 = (TextView)rootView.findViewById(R.id.det_rep_textView2);
                        tv2.setText(Util.convierteDateEnString(detalleEnEdicion.getFecha()));
                        TextView tv3 = (TextView)rootView.findViewById(R.id.det_rep_textView4);
                        tv3.setText(detalleEnEdicion.getKilometros().toString());
                        TextView tv4 = (TextView)rootView.findViewById(R.id.det_rep_textView6);
                        tv4.setText(detalleEnEdicion.getPrecio().toString());
                        TextView tv5 = (TextView)rootView.findViewById(R.id.det_rep_textView8);
                        String tipoReparacionSeleccionada = listaReparaciones.get(detalleEnEdicion.getIdTipoReparacion());
                        tv5.setText(tipoReparacionSeleccionada);
                        TextView tv6 = (TextView)rootView.findViewById(R.id.det_rep_textView10);
                        tv6.setText(detalleEnEdicion.getTaller());

                        /*
                        ImageView imgEditar = (ImageView) rootView.findViewById(R.id.imageView_editar);
                        imgEditar.setOnClickListener(new View.OnClickListener() {
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
                                botonGuardar.setOnClickListener(new View.OnClickListener() {
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
                                botonCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editDialog.cancel();
                                    }
                                });

                                editDialog.show();
                            }
                        });

                        ImageView imgBorrar = (ImageView) rootView.findViewById(R.id.imageView_borrar);
                        imgBorrar.setOnClickListener(new View.OnClickListener() {
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
        inflater.inflate(R.menu.detalle_reparacion, menu);

        if(reparaciones != null && reparaciones.size() == 0) {
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
            case R.id.action_nuevo:
                fragment = new NuevaReparacionFragment();
                if(fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.action_editar:
                if(reparaciones != null) {
                    detalleEnEdicion = reparaciones.get(detalleEnEdicion.getPosicion());
                    fragment = NuevaReparacionFragment.newInstance(detalleEnEdicion);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.action_eliminar:
                eliminarReparacion(detalleEnEdicion);
                actualizaListaReparaciones();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void actualizaListaReparaciones() {
        Fragment fragment = new ReparacionesFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }

    private boolean eliminarReparacion(DetalleReparacion dr) {
        ReparacionesSQLiteHelper repHelper = new ReparacionesSQLiteHelper(myContext, Constantes.TABLA_REPARACIONES, null, 1);
        return repHelper.borrarReparacion(dr);
    }
}
