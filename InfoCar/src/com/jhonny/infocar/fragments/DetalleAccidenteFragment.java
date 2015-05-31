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
public class DetalleAccidenteFragment extends Fragment {

    private View rootView = null;


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
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }
}
