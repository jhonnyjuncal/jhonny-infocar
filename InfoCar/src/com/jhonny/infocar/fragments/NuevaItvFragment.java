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
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleItv;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ItvSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by jhonny on 09/08/2015.
 */
public class NuevaItvFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private DetalleItv datosItv;
    private EditText editUltimaFecha;
    private EditText editProximaFecha;
    private EditText editLugar;
    private EditText editObservaciones;
    private ImageView imagenCalendar1;
    private ImageView imagenCalendar2;


    public NuevaItvFragment() {

    }

    public static NuevaItvFragment newInstance(DetalleItv itv) {
        NuevaItvFragment nif = new NuevaItvFragment();

        if(itv != null) {
            Bundle argumentos = new Bundle();
            argumentos.putInt("IdDetalleItv", itv.getIdDetalleItv());
            argumentos.putInt("IdVehiculo", itv.getIdVehiculo());
            argumentos.putLong("Fecha", itv.getFecha().getTime());
            argumentos.putLong("FechaProxima", itv.getFechaProxima().getTime());
            argumentos.putString("Lugar", itv.getLugar());
            argumentos.putString("Observaciones", itv.getObservaciones());

            nif.setArguments(argumentos);
        }
        return nif;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_nueva_itv, container, false);
            datosItv = new DetalleItv();

            DetalleVehiculo vehiculoActual = null;
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();
            ArrayList<DetalleVehiculo> listaVehiculos = recuperaDatosVehiculos();
            if(listaVehiculos != null) {
                vehiculoActual = listaVehiculos.get(posicion);
                datosItv.setIdVehiculo(vehiculoActual.getIdVehiculo());
            }

            Bundle args = getArguments();
            if(args != null) {
                datosItv = new DetalleItv();
                datosItv.setIdDetalleItv(args.getInt("IdDetalleItv"));
                datosItv.setIdVehiculo(args.getInt("IdVehiculo"));
                datosItv.setFecha(new Date(args.getLong("Fecha")));
                datosItv.setFechaProxima(new Date(args.getLong("FechaProxima")));
                datosItv.setLugar(args.getString("Lugar"));
                datosItv.setObservaciones(args.getString("Observaciones"));
            }

            editUltimaFecha = (EditText)rootView.findViewById(R.id.edit_itv_ultima_fecha);
            editProximaFecha = (EditText)rootView.findViewById(R.id.edit_itv_proxima_fecha);
            editLugar = (EditText)rootView.findViewById(R.id.edit_itv_lugar);
            editObservaciones = (EditText)rootView.findViewById(R.id.edit_itv_observaciones);

            imagenCalendar1 = (ImageView)rootView.findViewById(R.id.nue_itv_imageView1);
            imagenCalendar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dp = new DatePickerDialog(myContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editUltimaFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
                    dp.show();
                }
            });

            imagenCalendar2 = (ImageView)rootView.findViewById(R.id.nue_itv_imageView2);
            imagenCalendar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dp = new DatePickerDialog(myContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editProximaFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
                    dp.show();
                }
            });

            editUltimaFecha.setText(Util.convierteDateEnString(datosItv.getFecha()));
            editProximaFecha.setText(Util.convierteDateEnString(datosItv.getFechaProxima()));
            editLugar.setText(datosItv.getLugar());
            editObservaciones.setText(datosItv.getObservaciones());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guardar:
                try {
                    datosItv.setFecha(Util.convierteStringEnDate(editUltimaFecha.getText().toString()));
                    datosItv.setFechaProxima(Util.convierteStringEnDate(editProximaFecha.getText().toString()));
                    datosItv.setLugar(editLugar.getText().toString());
                    datosItv.setObservaciones(editObservaciones.getText().toString());

                    if(guardaDatosDeLaFichaTecnica(datosItv)) {
                        volver();
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.nueva_itv, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        myContext.setTitle(getResources().getString(R.string.title_activity_nueva_itv));
        super.onAttach(activity);
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

    private boolean guardaDatosDeLaFichaTecnica(DetalleItv itv) {
        boolean resp = false;
        try  {
            ItvSQLiteHelper helper = new ItvSQLiteHelper(rootView.getContext(), Constantes.TABLA_ITV, null, 1);
            resp = helper.guardarDatosItv(itv);

            String texto = null;
            if(resp)
                texto = getResources().getString(R.string.mensaje_guardar_ok);
            else
                texto = getResources().getString(R.string.mensaje_guardar_error);
            Toast.makeText(rootView.getContext(), texto, Toast.LENGTH_LONG).show();

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private void volver() {
        Fragment fragment = new ItvFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
