package com.jhonny.infocar.fragments;

import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.R;
import com.jhonny.infocar.Util;

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


public class PrincipalFragment extends Fragment {

	private FragmentActivity myContext;


	public PrincipalFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

	/*
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.principal, menu);
	}
	*/

	@Override
	public void onResume(){
		super.onResume();
		Util.cargaFondoDePantalla(myContext);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		myContext = (FragmentActivity)activity;
	}
}
