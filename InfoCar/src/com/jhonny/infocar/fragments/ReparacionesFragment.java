package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ReparacionesFragment extends Fragment {
	
	public ReparacionesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reparaciones, container, false);
        return rootView;
    }
}
