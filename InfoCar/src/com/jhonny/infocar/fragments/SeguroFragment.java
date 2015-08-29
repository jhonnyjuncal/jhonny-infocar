package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;
import com.jhonny.infocar.model.DetalleSeguro;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.SeguroSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;

/**
 * Created by jhonny on 03/08/2015.
 */
public class SeguroFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private DetalleSeguro detalleSeguro;
    private boolean existeSeguro = false;
    private ArrayList<DetalleVehiculo> listaVehiculos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_seguro, container, false);
            myContext.invalidateOptionsMenu();

            detalleSeguro = recuperaDatosDelSeguro();
            if(detalleSeguro != null)
                existeSeguro = true;
            listaVehiculos = recuperaDatosVehiculos();
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();
            DetalleVehiculo veh = listaVehiculos.get(posicion);

            if(detalleSeguro == null)
                detalleSeguro = new DetalleSeguro();

            TextView textFecha = (TextView)rootView.findViewById(R.id.det_seg_textView2);
            textFecha.setText(Util.convierteDateEnString(detalleSeguro.getFecha()));
            TextView textCompania = (TextView)rootView.findViewById(R.id.det_seg_textView4);
            textCompania.setText(detalleSeguro.getCompania());
            TextView textTipoSeguro = (TextView)rootView.findViewById(R.id.det_seg_textView6);
            textTipoSeguro.setText(Util.getDescripcionTipoSeguro(detalleSeguro.getTipoSeguro()));
            TextView textPoliza = (TextView)rootView.findViewById(R.id.det_seg_textView8);
            textPoliza.setText(detalleSeguro.getNumeroPoliza());
            TextView textAlerta = (TextView)rootView.findViewById(R.id.det_seg_textView10);
            if(detalleSeguro.getAlerta())
                textAlerta.setText("Activada");
            else
                textAlerta.setText("Desactivada");

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.seguro, menu);

        if(existeSeguro) {
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
        myContext.setTitle("Seguro");
        super.onAttach(activity);
    }

    @Override
    public void onResume(){
        super.onResume();
        Util.cargaFondoDePantalla(myContext);
    }

    private DetalleSeguro recuperaDatosDelSeguro() {
        DetalleSeguro detalleSeguro = null;
        try {
            SeguroSQLiteHelper helper = new SeguroSQLiteHelper(myContext, Constantes.TABLA_SEGURO, null, 1);
            detalleSeguro = helper.getDatosDelSeguro();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return detalleSeguro;
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
        Fragment fragment = null;
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.menu_seguro_nuevo:
                fragment = new NuevoSeguroFragment();
                if (fragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.menu_seguro_editar:
                if(existeSeguro) {
                    fragment = NuevoSeguroFragment.newInstance(detalleSeguro);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.menu_seguro_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setCancelable(true);
                builder.setTitle("Eliminar ITV");
                builder.setMessage("¿Seguro que desea borrar los datos de la ITV?");
                builder.setPositiveButton("Eliminar", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if(eliminarDatosDelSeguro(detalleSeguro)) {
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

    private boolean eliminarDatosDelSeguro(DetalleSeguro detalleSeguro) {
        SeguroSQLiteHelper helper = new SeguroSQLiteHelper(myContext, Constantes.TABLA_SEGURO, null, 1);
        return helper.borrarDatosDelSeguro(detalleSeguro);
    }

    private void volver() {
        Fragment fragment = new ItvFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
