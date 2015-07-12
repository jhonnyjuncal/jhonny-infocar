package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.AccidentesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by jhonny on 22/05/2015.
 */
public class DetalleAccidenteFragment extends Fragment {

    private View rootView = null;
    private FragmentActivity myContext;
    private ArrayList<DetalleAccidente> accidentes;
    private ArrayList<DetalleVehiculo> listaVehiculos;
    private ArrayList<DetalleAccidenteFragment> listaDetalles;

    private TypedArray arrayMarcas;
    private DetalleAccidente detalleEnEdicion;
    private Dialog editDialog;

    private EditText textFecha;
    private Spinner spinnerModelos;
    private EditText textKms;
    private EditText textLugar;
    private EditText textObservaciones;


    public static DetalleAccidenteFragment newInstance(Bundle arguments) {
        DetalleAccidenteFragment f = new DetalleAccidenteFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public DetalleAccidenteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_detalle_accidente, container, false);

            Bundle arguments = getArguments();
            if(arguments != null) {
                Integer position = arguments.getInt("position");
                if(position != null) {
                    accidentes = recuperaDatosAccidentes();
                    listaVehiculos = recuperaDatosVehiculos();

                    if(accidentes != null) {
                        arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);

                        detalleEnEdicion = accidentes.get(position);
                        detalleEnEdicion.setPosicion(position);
                        DetalleVehiculo dv = null;
                        for (DetalleVehiculo vehiculo : listaVehiculos) {
                            if (vehiculo.getIdVehiculo().equals(detalleEnEdicion.getIdVehiculo())) {
                                dv = vehiculo;
                                break;
                            }
                        }

                        TextView textViewKms = (TextView) rootView.findViewById(R.id.det_acc_textView3);
                        textViewKms.setText(detalleEnEdicion.getKilometros().toString());
                        TextView textViewLugar = (TextView) rootView.findViewById(R.id.det_acc_textView5);
                        textViewLugar.setText(detalleEnEdicion.getLugar());
                        TextView textViewMarca = (TextView) rootView.findViewById(R.id.det_acc_textView7);
                        Integer numMarca = dv.getMarca();
                        String marcaSeleccionada = arrayMarcas.getString(numMarca);
                        arrayMarcas.recycle();
                        textViewMarca.setText(marcaSeleccionada);
                        TextView textViewModelo = (TextView) rootView.findViewById(R.id.det_acc_textView9);
                        textViewModelo.setText(dv.getModelo());
                        TextView textViewObservaciones = (TextView) rootView.findViewById(R.id.det_acc_textView11);
                        textViewObservaciones.setText(detalleEnEdicion.getObservaciones());

                        /*
                        Button btnEditar = (Button)rootView.findViewById(R.id.det_acc_button_edit);
                        btnEditar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vista) {
                                Fragment fragment = NuevoAccidenteFragment.newInstance(detalleEnEdicion, position);
                                if(fragment != null) {
                                    FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                                }
                            }
                        });

                        Button btnEliminar = (Button)rootView.findViewById(R.id.det_acc_button_delete);
                        btnEliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vista) {
                                eliminarAccidente(detalleEnEdicion);
                                actualizaListaAccidentes();
                            }
                        });

                        ImageView imgEditar = (ImageView) rootView.findViewById(R.id.imageView_editar);
                        imgEditar.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LinearLayout linear1 = (LinearLayout) view.getParent();
                                LinearLayout linear2 = (LinearLayout) linear1.getParent();
                                LinearLayout linear3 = (LinearLayout) linear2.getParent();
                                detalleEnEdicion = accidentes.get(linear3.getId());

                                editDialog = new Dialog(rootView.getContext());
                                editDialog.setContentView(R.layout.edicion_accidente);
                                editDialog.setTitle("Edicion de accidente");

                                textFecha = (EditText) editDialog.findViewById(R.id.edit_acc_fecha);
                                final String fechaEnEdicion = Util.convierteDateEnString(detalleEnEdicion.getFecha());
                                textFecha.setText(fechaEnEdicion);
                                ArrayList<String> vehiculos = new ArrayList<String>();
                                for (DetalleVehiculo veh : listaVehiculos) {
                                    String marca = arrayMarcas.getString(veh.getMarca());
                                    vehiculos.add(marca + " " + veh.getModelo());
                                }
                                ArrayAdapter<String> adapterVehiculos = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, vehiculos);
                                spinnerModelos = (Spinner) editDialog.findViewById(R.id.edit_acc_spinner_vehiculo);
                                spinnerModelos.setAdapter(adapterVehiculos);
                                textKms = (EditText) editDialog.findViewById(R.id.edit_acc_kms);
                                textKms.setText(detalleEnEdicion.getKilometros().toString());
                                textLugar = (EditText) editDialog.findViewById(R.id.edit_acc_lugar);
                                textLugar.setText(detalleEnEdicion.getLugar());
                                textObservaciones = (EditText) editDialog.findViewById(R.id.edit_acc_obs);
                                textObservaciones.setText(detalleEnEdicion.getObservaciones());

                                ImageView imgFecha = (ImageView) editDialog.findViewById(R.id.edit_acc_imageView1);
                                imgFecha.setOnClickListener(new View.OnClickListener() {
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

                                        DatePickerDialog dp = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                textFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                            }
                                        }, year, month, day);

                                        dp.show();
                                    }
                                });

                                Button btnGuardar = (Button) editDialog.findViewById(R.id.edit_acc_btn_guardar);
                                btnGuardar.setOnClickListener(new View.OnClickListener() {
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
                                        actualizaListaAccidentes();
                                        editDialog.dismiss();
                                    }
                                });

                                Button btnCancelar = (Button) editDialog.findViewById(R.id.edit_acc_btn_cancelar);
                                btnCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View vista) {
                                        editDialog.cancel();
                                    }
                                });
                                editDialog.show();
                            }
                        });

                        ImageView imgBorrar = (ImageView) rootView.findViewById(R.id.imageView_borrar);
                        imgBorrar.setOnClickListener(new android.view.View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    LinearLayout linear1 = (LinearLayout) view.getParent();
                                    LinearLayout linear2 = (LinearLayout) linear1.getParent();
                                    LinearLayout linear3 = (LinearLayout) linear2.getParent();
                                    detalleEnEdicion = accidentes.get(linear3.getId());

                                    AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                                    builder.setCancelable(true);
                                    builder.setTitle("Eliminar registro");
                                    builder.setMessage("¿Seguro que desea borrar este accidente?");
                                    builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(eliminarAccidente(detalleEnEdicion)) {
                                                actualizaListaAccidentes();
                                                dialog.dismiss();
                                                Toast.makeText(myContext, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();

                                            }else {
                                                Toast.makeText(myContext, "Ha ocurrido un error al guardar", Toast.LENGTH_SHORT).show();
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
        }catch(Exception ex) {
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
        inflater.inflate(R.menu.detalle_accidente, menu);

        if(accidentes != null && accidentes.size() == 0) {
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
                fragment = new NuevoMantenimientoFragment();
                if(fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.action_editar:
                if(accidentes != null) {
                    detalleEnEdicion = accidentes.get(detalleEnEdicion.getPosicion());
                    fragment = NuevoAccidenteFragment.newInstance(detalleEnEdicion);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.action_eliminar:
                eliminarAccidente(detalleEnEdicion);
                actualizaListaAccidentes();
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

    private ArrayList<DetalleAccidente> recuperaDatosAccidentes() {
        ArrayList<DetalleAccidente> detalles = new ArrayList<DetalleAccidente>();
        try {
            AccidentesSQLiteHelper accidentesHelper = new AccidentesSQLiteHelper(myContext, Constantes.TABLA_ACCIDENTES, null, 1);
            detalles.addAll(accidentesHelper.getAccidentes());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return detalles;
    }

    private boolean eliminarAccidente(DetalleAccidente da) {
        boolean resp = false;
        try {
            AccidentesSQLiteHelper accidentesHelper = new AccidentesSQLiteHelper(myContext, Constantes.TABLA_ACCIDENTES, null, 1);
            resp = accidentesHelper.borrarAccidente(da);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private void actualizaListaAccidentes() {
        Fragment fragment = new AccidentesFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
