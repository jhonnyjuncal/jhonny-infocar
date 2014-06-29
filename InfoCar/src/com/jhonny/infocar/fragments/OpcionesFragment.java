package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OpcionesFragment extends Fragment {
	
	public OpcionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mantenimiento, container, false);
        return rootView;
    }
}
