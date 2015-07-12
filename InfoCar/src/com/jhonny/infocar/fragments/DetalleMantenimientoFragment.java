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
import com.jhonny.infocar.model.DetalleMantenimiento;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.MantenimientosSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by jhonny on 22/05/2015.
 */
public class DetalleMantenimientoFragment extends Fragment {

    private View rootView = null;
    private FragmentActivity myContext;
    private Integer position = null;

    private ArrayList<DetalleMantenimiento> mantenimientos;
    private TypedArray arrayTiposMantenimientos = null;
    private TypedArray arrayMarcas = null;

    private ArrayList<String> listaTiposMantenimientos = new ArrayList<String>();
    private ArrayList<DetalleVehiculo> misVehiculos = new ArrayList<DetalleVehiculo>();
    private ArrayList<String> listaVehiculos = new ArrayList<String>();
    private ArrayAdapter<String> adapterTipoMantenimientos;
    private ArrayAdapter<String> adapterVehiculos;
    private DetalleMantenimiento detalleEnEdicion;

    private EditText textFecha;
    private ImageView imgCalendar;
    private EditText textKms;
    private EditText textPrecio;
    private Spinner spinnerTipoMant;
    private Spinner spinnerVehiculo;
    private EditText textTaller;
    private EditText textObservaciones;
    private DetalleMantenimiento mant;
    private Dialog editDialog;


    public static DetalleMantenimientoFragment newInstance(Bundle arguments) {
        DetalleMantenimientoFragment f = new DetalleMantenimientoFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public DetalleMantenimientoFragment () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_detalle_mantenimiento, container, false);

            Bundle arguments = getArguments();
            if(arguments != null) {
                position = arguments.getInt("position");
                if(position != null) {
                    mantenimientos = recuperaDatosMantenimiento();

                    if(mantenimientos != null) {
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

                        detalleEnEdicion = mantenimientos.get(position);
                        detalleEnEdicion.setPosicion(position);

                        String marcaymodelo = null;
                        for (DetalleVehiculo dv : misVehiculos) {
                            if (dv.getIdVehiculo().equals(detalleEnEdicion.getIdVehiculo())) {
                                marcaymodelo = arrayMarcas.getString(dv.getMarca()) + " " + dv.getModelo();
                                break;
                            }
                        }

                        TextView tv2 = (TextView) rootView.findViewById(R.id.det_mant_textView1);
                        DateFormat df = DateFormat.getDateInstance();
                        tv2.setText(df.format(detalleEnEdicion.getFecha()));
                        TextView tv3 = (TextView) rootView.findViewById(R.id.det_mant_textView3);
                        tv3.setText(detalleEnEdicion.getKilometros().toString());
                        TextView tv4 = (TextView) rootView.findViewById(R.id.det_mant_textView5);
                        tv4.setText(detalleEnEdicion.getPrecio().toString());
                        TextView tv5 = (TextView) rootView.findViewById(R.id.det_mant_textView7);
                        String mantenimientoSeleccionado = listaTiposMantenimientos.get(detalleEnEdicion.getTipoMantenimiento());
                        tv5.setText(mantenimientoSeleccionado);
                        TextView tv6 = (TextView) rootView.findViewById(R.id.det_mant_textView9);
                        tv6.setText(detalleEnEdicion.getTaller());

                        /*
                        ImageView imgEditar = (ImageView) rootView.findViewById(R.id.imageView_editar);
                        imgEditar.setOnClickListener(new View.OnClickListener() {
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
                                imgCalendar.setOnClickListener(new View.OnClickListener() {
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
                                btnGuardar.setOnClickListener(new View.OnClickListener() {
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
                        imgBorrar.setOnClickListener(new View.OnClickListener() {
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
                if(mantenimientos != null) {
                    detalleEnEdicion = mantenimientos.get(detalleEnEdicion.getPosicion());
                    fragment = NuevoMantenimientoFragment.newInstance(detalleEnEdicion);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.action_eliminar:
                eliminarMantenimiento(detalleEnEdicion);
                actualizaListaMantenimientos();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.detalle_mantenimiento, menu);

        if(mantenimientos != null && mantenimientos.size() == 0) {
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);

        }else {
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
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

    private boolean comprobacionDatosMantenimiento(DetalleMantenimiento dm) {
        boolean result = true;
        try {

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private boolean eliminarMantenimiento(DetalleMantenimiento dm) {
        MantenimientosSQLiteHelper mantHelper = new MantenimientosSQLiteHelper(myContext, Constantes.TABLA_MANTENIMIENTOS, null, 1);
        return mantHelper.borrarMantenimiento(dm);
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

    private void actualizaListaMantenimientos() {
        Fragment fragment = new MantenimientosFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
