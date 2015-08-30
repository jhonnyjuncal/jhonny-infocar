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
import com.jhonny.infocar.model.DetalleFichaTecnica;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.FichaTecnicaSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by jhonny on 09/08/2015.
 */
public class NuevaFichaTecnicaFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private DetalleFichaTecnica fichaTecnica = null;
    EditText editFecha = null;
    EditText editLugar = null;
    EditText editNeumaticos = null;
    EditText editPotencia = null;
    EditText editBastidor = null;
    private ImageView imagenCalendar;


    public NuevaFichaTecnicaFragment() {

    }

    public static NuevaFichaTecnicaFragment newInstance(DetalleFichaTecnica dft) {
        NuevaFichaTecnicaFragment fichaTecnica = new NuevaFichaTecnicaFragment();
        if(dft != null){
            Bundle args = new Bundle();
            args.putInt("IdFichaTecnica", dft.getIdFichaTecnica());
            args.putInt("IdVehiculo", dft.getIdVehiculo());
            args.putLong("FechaMatriculacion", dft.getFechaMatriculacion().getTime());
            args.putString("Lugar", dft.getLugar());
            args.putString("Neumaticos", dft.getNeumaticos());
            args.putString("Potencia", dft.getPotencia());
            args.putString("Bastidor", dft.getBastidor());
            fichaTecnica.setArguments(args);
        }
        return fichaTecnica;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_nueva_ficha_tecnica, container, false);
            fichaTecnica = new DetalleFichaTecnica();

            DetalleVehiculo vehiculoActual = null;
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();
            ArrayList<DetalleVehiculo> listaVehiculos = recuperaDatosVehiculos();
            if(listaVehiculos != null) {
                vehiculoActual = listaVehiculos.get(posicion);
                fichaTecnica.setIdVehiculo(vehiculoActual.getIdVehiculo());
            }

            editFecha = (EditText)rootView.findViewById(R.id.edit_ficha_fecha);
            editLugar = (EditText)rootView.findViewById(R.id.edit_ficha_lugar);
            editNeumaticos = (EditText)rootView.findViewById(R.id.edit_ficha_neumaticos);
            editPotencia = (EditText)rootView.findViewById(R.id.edit_ficha_potencia);
            editBastidor = (EditText)rootView.findViewById(R.id.edit_ficha_bastidor);

            imagenCalendar = (ImageView)rootView.findViewById(R.id.nue_ficha_imageView1);
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

            Bundle args = getArguments();
            if(args != null) {
                fichaTecnica.setIdFichaTecnica(args.getInt("IdFichaTecnica"));
                fichaTecnica.setIdVehiculo(args.getInt("IdVehiculo"));
                fichaTecnica.setFechaMatriculacion(new Date(args.getLong("FechaMatriculacion")));
                fichaTecnica.setLugar(args.getString("Lugar"));
                fichaTecnica.setNeumaticos(args.getString("Neumaticos"));
                fichaTecnica.setPotencia(args.getString("Potencia"));
                fichaTecnica.setBastidor(args.getString("Bastidor"));
            }

            editFecha.setText(Util.convierteDateEnString(fichaTecnica.getFechaMatriculacion()));
            editLugar.setText(fichaTecnica.getLugar());
            editNeumaticos.setText(fichaTecnica.getNeumaticos());
            editPotencia.setText(fichaTecnica.getPotencia());
            editBastidor.setText(fichaTecnica.getBastidor());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.nueva_ficha_tecnica, menu);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        myContext.setTitle(getResources().getString(R.string.title_activity_nueva_ficha_tecnica));
        super.onAttach(activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_guardar:
                try {
                    fichaTecnica.setFechaMatriculacion(Util.convierteStringEnDate(editFecha.getText().toString()));
                    fichaTecnica.setLugar(editLugar.getText().toString());
                    fichaTecnica.setNeumaticos(editNeumaticos.getText().toString());
                    fichaTecnica.setPotencia(editPotencia.getText().toString());
                    fichaTecnica.setBastidor(editBastidor.getText().toString());

                    if(guardaDatosDeLaFichaTecnica(fichaTecnica)) {
                        volver();
                    }
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    private boolean guardaDatosDeLaFichaTecnica(DetalleFichaTecnica dft) {
        boolean resp = false;
        try  {
            FichaTecnicaSQLiteHelper helper = new FichaTecnicaSQLiteHelper(rootView.getContext(), Constantes.TABLA_FICHA_TECNICA, null, 1);
            resp = helper.guardarDatosFichaTecnica(dft);

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
        Fragment fragment = new FichaTecnicaFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
