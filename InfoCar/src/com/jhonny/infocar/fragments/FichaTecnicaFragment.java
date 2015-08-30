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
import com.jhonny.infocar.model.DetalleFichaTecnica;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.FichaTecnicaSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import java.util.ArrayList;


/**
 * Created by jhonny on 03/08/2015.
 */
public class FichaTecnicaFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;

    private ArrayList<DetalleFichaTecnica> listaFichasTecnicas;
    private DetalleFichaTecnica fichaTecnica;
    private ArrayList<DetalleVehiculo> listaVehiculos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_ficha_tecnica, container, false);
            myContext.invalidateOptionsMenu();

            listaFichasTecnicas = recuperaDatosFichasTecnicas();
            listaVehiculos = recuperaDatosVehiculos();
            int posicion = VehiculoFragment.paginadorVehiculos.getCurrentItem();

            if(listaFichasTecnicas != null && listaVehiculos != null) {
                DetalleVehiculo veh = listaVehiculos.get(posicion);
                for(DetalleFichaTecnica ficha : listaFichasTecnicas) {
                    if(ficha.getIdVehiculo().equals(veh.getIdVehiculo())) {
                        fichaTecnica = ficha;
                    }
                }
            }

            if(fichaTecnica == null)
                fichaTecnica = new DetalleFichaTecnica();

            TextView textFechaMat = (TextView)rootView.findViewById(R.id.det_ficha_textView2);
            textFechaMat.setText(Util.formateaFechaParaMostrar(fichaTecnica.getFechaMatriculacion()));
            TextView textLugar = (TextView)rootView.findViewById(R.id.det_ficha_textView4);
            textLugar.setText(fichaTecnica.getLugar());
            TextView textNeumaticos = (TextView)rootView.findViewById(R.id.det_ficha_textView6);
            textNeumaticos.setText(fichaTecnica.getNeumaticos());
            TextView textPotencia = (TextView)rootView.findViewById(R.id.det_ficha_textView8);
            textPotencia.setText(fichaTecnica.getPotencia());
            TextView textBastidor = (TextView)rootView.findViewById(R.id.det_ficha_textView10);
            textBastidor.setText(fichaTecnica.getBastidor());

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.ficha_tecnica, menu);

        if (listaFichasTecnicas != null && listaFichasTecnicas.size() == 0) {
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
        myContext.setTitle(getResources().getString(R.string.title_activity_ficha_tecnica));
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

        switch (item.getItemId()) {
            case R.id.menu_ficha_nuevo:
                fragment = new NuevaFichaTecnicaFragment();
                if (fragment != null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container_principal, fragment).commit();
                }
                return true;

            case R.id.menu_ficha_editar:
                if(listaFichasTecnicas != null) {
                    fragment = NuevaFichaTecnicaFragment.newInstance(fichaTecnica);
                    if (fragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                    return true;
                }else {
                    return false;
                }

            case R.id.menu_ficha_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
                builder.setCancelable(true);
                builder.setTitle(getResources().getString(R.string.titulo_eliminar_ficha));
                builder.setMessage(getResources().getString(R.string.mensaje_pregunta_borrar_ficha));
                builder.setPositiveButton(getResources().getString(R.string.texto_boton_eliminar), new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (eliminarFichaTecnica(fichaTecnica)) {
                                dialog.dismiss();
                                Toast.makeText(myContext, getResources().getString(R.string.mensaje_borrar_ok), Toast.LENGTH_SHORT).show();
                                volver();

                            } else {
                                Toast.makeText(myContext, getResources().getString(R.string.mensaje_borrar_error), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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

    private ArrayList<DetalleFichaTecnica> recuperaDatosFichasTecnicas() {
        ArrayList<DetalleFichaTecnica> lista = new ArrayList<DetalleFichaTecnica>();
        try {
            FichaTecnicaSQLiteHelper helper = new FichaTecnicaSQLiteHelper(myContext, Constantes.TABLA_FICHA_TECNICA, null, 1);
            lista.addAll(helper.getFichasTecnicas());

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return lista;
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

    private boolean eliminarFichaTecnica(DetalleFichaTecnica dft) {
        boolean result = false;
        try {
            FichaTecnicaSQLiteHelper repHelper = new FichaTecnicaSQLiteHelper(myContext, Constantes.TABLA_FICHA_TECNICA, null, 1);
            result = repHelper.borrarDatosFichaTecnica(dft);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private void volver() {
        Fragment fragment = new VehiculoFragment();
        FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
    }
}
