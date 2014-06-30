package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AccidentesFragment extends Fragment {
	
	public AccidentesFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accidentes, container, false);
        return rootView;
    }
}
