package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleItv;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.ItvSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by jhonny on 03/08/2015.
 */
public class ItvFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private ArrayList<DetalleVehiculo> listaVehiculos;
    private DetalleItv datosItv;
    private boolean existeItv = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_itv, container, false);
            myContext.invalidateOptionsMenu();

            datosItv = recuperaDatosItv();
            if(datosItv != null)
                existeItv = true;
            listaVehiculos = recuperaDatosVehiculos();
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();
            DetalleVehiculo veh = listaVehiculos.get(posicion);

            if(datosItv == null)
                datosItv = new DetalleItv();

            TextView textoFecha = (TextView)rootView.findViewById(R.id.det_itv_textView2);
            textoFecha.setText(Util.formateaFechaParaMostrar(datosItv.getFecha()));
            TextView textoFechaProxima = (TextView)rootView.findViewById(R.id.det_itv_textView4);
            textoFechaProxima.setText(Util.formateaFechaParaMostrar(datosItv.getFechaProxima()));
            TextView textoLugar = (TextView)rootView.findViewById(R.id.det_itv_textView6);
            textoLugar.setText(datosItv.getLugar());
            TextView textoObservaciones = (TextView)rootView.findViewById(R.id.det_itv_textView8);
            textoObservaciones.setText(datosItv.getObservaciones());

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.itv, menu);

        if(existeItv) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);

        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        myContext.setTitle("ITV");
        super.onAttach(activity);
    }

    @Override
    public void onResume(){
        super.onResume();
        Util.cargaFondoDePantalla(myContext);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.menu_itv_nuevo:
                fragment = new NuevaItvFragment();
                if (fragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.menu_itv_editar:
                if(existeItv) {
                    fragment = NuevaItvFragment.newInstance(datosItv);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.menu_itv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setCancelable(true);
                builder.setTitle("Eliminar ITV");
                builder.setMessage("¿Seguro que desea borrar los datos de la ITV?");
                builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if(eliminarItv(datosItv)) {
                                dialog.dismiss();
                                Toast.makeText(myContext, "Datos de la ITV eliminados correctamente", Toast.LENGTH_SHORT).show();
                                volver();

                            }else {
                                Toast.makeText(myContext, "Ha ocurrido un error al eliminar los datos de la ITV", Toast.LENGTH_SHORT).show();
                            }

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

    private DetalleItv recuperaDatosItv() {
        DetalleItv itv = new DetalleItv();
        try {
            ItvSQLiteHelper helper = new ItvSQLiteHelper(myContext, Constantes.TABLA_ITV, null, 1);
            itv = helper.getDatosDeLaItv();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return itv;
    }

    private boolean eliminarItv(DetalleItv itv) {
        ItvSQLiteHelper helper = new ItvSQLiteHelper(myContext, Constantes.TABLA_ITV, null, 1);
        return helper.borrarDatosDeLaItv(itv);
    }

    private void volver() {
        Fragment fragment = new ItvFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
