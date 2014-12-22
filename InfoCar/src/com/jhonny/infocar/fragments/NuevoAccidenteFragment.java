package com.jhonny.infocar.fragments;

import com.jhonny.infocar.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NuevoAccidenteFragment extends Fragment {
	
	private View rootView;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		try {
			rootView = inflater.inflate(R.layout.fragment_nuevo_accidente, container, false);
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return rootView;
	}
}
