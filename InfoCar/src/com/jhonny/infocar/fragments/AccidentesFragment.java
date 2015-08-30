package com.jhonny.infocar.fragments;

import java.util.ArrayList;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.animations.ZoomOutPageTransformer;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.model.DetalleVehiculo;
import com.jhonny.infocar.sql.AccidentesSQLiteHelper;
import com.jhonny.infocar.sql.VehiculosSQLiteHelper;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class AccidentesFragment extends Fragment {
	
	private View rootView;
    private FragmentActivity myContext;
    private TypedArray arrayMarcas;

    private ArrayList<DetalleAccidente> accidentes;
    private ArrayList<DetalleAccidenteFragment> listaDetalles;
    private ArrayList<DetalleVehiculo> listaVehiculos;


    private MyAdapter mAdapter;
    public static ViewPager paginadorAccidentes;

	
	public AccidentesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        try {
            rootView = inflater.inflate(R.layout.fragment_accidentes, container, false);
            accidentes = recuperaDatosAccidentes();
            listaVehiculos = recuperaDatosVehiculos();

            if(accidentes != null) {
                listaDetalles = new ArrayList<DetalleAccidenteFragment>();
                for(int i=0; i<accidentes.size(); i++) {
                    Bundle arguments = new Bundle();
                    arguments.putInt("position", i);
                    listaDetalles.add(DetalleAccidenteFragment.newInstance(arguments));
                }
            }

            mAdapter = new MyAdapter(getFragmentManager(), listaDetalles);
            paginadorAccidentes = (ViewPager)rootView.findViewById(R.id.acc_pager);
            paginadorAccidentes.setAdapter(mAdapter);
            paginadorAccidentes.setPageTransformer(true, new ZoomOutPageTransformer());

            CirclePageIndicator cIndicator = (CirclePageIndicator)rootView.findViewById(R.id.acc_indicator);
            cIndicator.setViewPager(paginadorAccidentes);

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.accidentes, menu);
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

    private ArrayList<DetalleAccidente> recuperaDatosAccidentes() {
        ArrayList<DetalleAccidente> detalles = new ArrayList<DetalleAccidente>();
        try {
            AccidentesSQLiteHelper accidentesHelper = new AccidentesSQLiteHelper(myContext, Constantes.TABLA_ACCIDENTES, null, 1);
            detalles.addAll(accidentesHelper.getAccidentes());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return detalles;
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

        switch(item.getItemId()) {
            case R.id.menu_acc_nuevo:
                if(existeVehilo()) {
                    fragment = new NuevoAccidenteFragment();
                    if(fragment != null) {
                        FragmentManager fragmentManager = myContext.getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container_principal, fragment).commit();
                    }
                }else {
                    Toast.makeText(myContext, R.string.mensaje_crear_vehiculo_acc, Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean existeVehilo() {
        if(listaVehiculos != null && listaVehiculos.size() > 0)
            return true;
        return false;
    }





    public static class MyAdapter extends FragmentStatePagerAdapter {
        private ArrayList<DetalleAccidenteFragment> listaDetalles;

        public MyAdapter(FragmentManager fm, ArrayList<DetalleAccidenteFragment> listaDetalles) {
            super(fm);
            this.listaDetalles = listaDetalles;
        }

        @Override
        public int getCount() {
            return listaDetalles.size();
        }

        @Override
        public Fragment getItem(int position) {
            return listaDetalles.get(position);
        }
    }
}
