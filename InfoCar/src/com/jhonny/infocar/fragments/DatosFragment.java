package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DatosFragment extends Fragment {
	
	public DatosFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos, container, false);
        return rootView;
    }
}
