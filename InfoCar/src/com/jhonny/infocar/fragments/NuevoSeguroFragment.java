package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleSeguro;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.SeguroSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by jhonny on 29/08/2015.
 */
public class NuevoSeguroFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private DetalleSeguro detalleSeguro;
    private ImageView imagenCalendar;
    private EditText editFecha;
    private EditText editCompania;
    private EditText editTipoSeguro;
    private EditText editNumeroPoliza;
    private Switch switchAlerta;


    public static NuevoMantenimientoFragment newInstance(DetalleSeguro detalleSeguro) {
        Bundle args = new Bundle();
        args.putInt("idSeguro", detalleSeguro.getIdSeguro());
        args.putInt("idVehiculo", detalleSeguro.getIdVehiculo());
        args.putLong("fecha", detalleSeguro.getFecha().getTime());
        args.putString("compania", detalleSeguro.getCompania());
        args.putInt("tipoSeguro", detalleSeguro.getTipoSeguro());
        args.putString("numeroPoliza", detalleSeguro.getNumeroPoliza());
        args.putBoolean("alerta", detalleSeguro.getAlerta());

        NuevoMantenimientoFragment frag = new NuevoMantenimientoFragment();
        frag.setArguments(args);
        return frag;
    }

    public NuevoSeguroFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_nuevo_seguro, container, false);
            detalleSeguro = new DetalleSeguro();

            DetalleVehiculo vehiculoActual = null;
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();
            ArrayList<DetalleVehiculo> listaVehiculos = recuperaDatosVehiculos();
            if(listaVehiculos != null) {
                vehiculoActual = listaVehiculos.get(posicion);
                detalleSeguro.setIdVehiculo(vehiculoActual.getIdVehiculo());
            }

            Bundle args = getArguments();
            if(args != null) {
                detalleSeguro = new DetalleSeguro();
                detalleSeguro.setIdSeguro(args.getInt("idSeguro"));
                detalleSeguro.setIdVehiculo(args.getInt("idVehiculo"));
                detalleSeguro.setFecha(new Date(args.getLong("fecha")));
                detalleSeguro.setCompania(args.getString("compania"));
                detalleSeguro.setTipoSeguro(args.getInt("tipoSeguro"));
                detalleSeguro.setNumeroPoliza(args.getString("numeroPoliza"));
                detalleSeguro.setAlerta(args.getBoolean("alerta"));
            }

            editFecha = (EditText)rootView.findViewById(R.id.edit_seguro_fecha);
            editFecha.setText(Util.convierteDateEnString(detalleSeguro.getFecha()));
            editCompania = (EditText)rootView.findViewById(R.id.edit_seguro_compania);
            editCompania.setText(detalleSeguro.getCompania());
            editTipoSeguro = (EditText)rootView.findViewById(R.id.edit_seguro_tipo);
            editTipoSeguro.setText(Util.getDescripcionTipoSeguro(detalleSeguro.getTipoSeguro()));
            editNumeroPoliza = (EditText)rootView.findViewById(R.id.edit_seguro_poliza);
            editNumeroPoliza.setText(detalleSeguro.getNumeroPoliza());
            switchAlerta = (Switch)rootView.findViewById(R.id.seguro_switch1);
            switchAlerta.setChecked(detalleSeguro.getAlerta());

            imagenCalendar = (ImageView)rootView.findViewById(R.id.nue_seg_imageView1);
            imagenCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dp = new DatePickerDialog(myContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
                    dp.show();
                }
            });

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Util.cargaFondoDePantalla(myContext);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    volver();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.nuevo_seguro, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        myContext.setTitle("Nuevo seguro");
        super.onAttach(activity);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guardar:
                try {
                    detalleSeguro.setFecha(Util.convierteStringEnDate(editFecha.getText().toString()));
                    detalleSeguro.setCompania(editCompania.getText().toString());
                    detalleSeguro.setTipoSeguro(editTipoSeguro.getText().length());
                    detalleSeguro.setNumeroPoliza(editNumeroPoliza.getText().toString());
                    detalleSeguro.setAlerta(switchAlerta.isChecked());

                    if(guardaDatosDelSeguro(detalleSeguro)) {
                        volver();
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean guardaDatosDelSeguro(DetalleSeguro detalleSeguro) {
        boolean resp = false;
        try  {
            SeguroSQLiteHelper helper = new SeguroSQLiteHelper(rootView.getContext(), Constantes.TABLA_SEGURO, null, 1);
            resp = helper.guardarDatosDelSeguro(detalleSeguro);

            String texto = null;
            if(resp)
                texto = "Datos guardados correctamente";
            else
                texto = "Error al guardar los datos";
            Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private void volver() {
        Fragment fragment = new SeguroFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
