package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;

/**
 * Created by jhonny on 03/08/2015.
 */
public class SeguroFragment extends Fragment {

    private View rootView;
    private FragmentActivity myContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_seguro, container, false);

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume(){
        super.onResume();
        cargaFondoDePantalla();
    }

    private synchronized void cargaFondoDePantalla() {
        try {
            SharedPreferences prop = myContext.getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
            int fondoSeleccionado = 1;
            if(prop != null) {
                SharedPreferences.Editor editor = prop.edit();
                if(editor != null) {
                    if(prop.contains(Constantes.FONDO_PANTALLA)) {
                        fondoSeleccionado = prop.getInt(Constantes.FONDO_PANTALLA, 1);
                    }
                }
            }

            String imagen = Constantes.FONDO_1;
            switch(fondoSeleccionado) {
                case 1:
                    imagen = Constantes.FONDO_1;
                    break;
                case 2:
                    imagen = Constantes.FONDO_2;
                    break;
                case 3:
                    imagen = Constantes.FONDO_3;
                    break;
            }
            int imageResource1 = myContext.getApplicationContext().getResources().getIdentifier(imagen, "drawable", myContext.getApplicationContext().getPackageName());
            Drawable image = myContext.getResources().getDrawable(imageResource1);
            ImageView imageView = (ImageView)myContext.findViewById(R.id.fondo_principal);
            imageView.setImageDrawable(image);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
