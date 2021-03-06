package com.jhonny.infocar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import java.util.Locale;
import org.joda.time.DateTime;
import com.jhonny.infocar.FileUtil;


/**
 * Created by jhonny on 18/04/2015.
 */
public class AcercaFragment extends Fragment {

    private View rootView;
    private FrameLayout fragmento;
    private FragmentActivity myContext;


    public AcercaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_acerca, container, false);

            // version de la aplicacion
            PackageInfo pInfo = myContext.getPackageManager().getPackageInfo(myContext.getPackageName(), 0);
            TextView textoVersion = (TextView)rootView.findViewById(R.id.acer_textView3);
            textoVersion.setText(pInfo.versionName);

            // fecha de creacion
            TextView textoFecha = (TextView)rootView.findViewById(R.id.acer_textView5);
            DateTime fecha = new DateTime("2014-10-19");
            Locale locale = getResources().getConfiguration().locale;
            textoFecha.setText(FileUtil.getFechaFormateada(fecha.toDate(), locale));

            // link de mi perfil en google+
            TextView link = (TextView)rootView.findViewById(R.id.acer_textView7);
            link.setMovementMethod(LinkMovementMethod.getInstance());

            // link de facebook
            /*
            ImageView imgFacebook = (ImageView)rootView.findViewById(R.id.acer_imageView2);
            imgFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://www.face book.com/androiddetective"));
                    startActivity(intent);
                }
            });

            // link de google plus
            ImageView imgGoogle = (ImageView)rootView.findViewById(R.id.acer_imageView3);
            imgGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://plus.google.com/103927277529482513436"));
                    startActivity(intent);
                }
            });
            */
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
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
