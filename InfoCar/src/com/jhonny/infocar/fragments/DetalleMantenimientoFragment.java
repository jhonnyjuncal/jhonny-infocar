package com.jhonny.infocar.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jhonny.infocar.R;


/**
 * Created by jhonny on 22/05/2015.
 */
public class DetalleMantenimientoFragment extends Fragment {

    private View rootView = null;


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
            rootView = inflater.inflate(R.layout.fragment_datos, container, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }
}
