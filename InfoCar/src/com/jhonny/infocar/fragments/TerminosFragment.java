package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jhonny.infocar.R;


/**
 * Created by jhonny on 09/05/2015.
 */
public class TerminosFragment extends Fragment {

    private View rootView = null;
    private FragmentActivity myContext;


    public TerminosFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_terminos, container, false);
        setHasOptionsMenu(true);

        Button btnVolver = (Button)rootView.findViewById(R.id.botonVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OpcionesFragment();
                FragmentManager fragmentManager = ((FragmentActivity) myContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext = (FragmentActivity)activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
