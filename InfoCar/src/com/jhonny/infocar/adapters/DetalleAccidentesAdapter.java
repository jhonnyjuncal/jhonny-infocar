package com.jhonny.infocar.adapters;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleAccidente;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class DetalleAccidentesAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<DetalleAccidente> detalles;
	
	
	public DetalleAccidentesAdapter(Context context, ArrayList<DetalleAccidente> detalles) {
		this.context = context;
		this.detalles = detalles;
	}
	
	@Override
	public int getCount() {
		return this.detalles.size();
	}

	@Override
	public Object getItem(int position) {
		return this.detalles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.detalle_accidente, null);
		}
		
		if(detalles != null && !detalles.isEmpty()) {
			DetalleAccidente da = detalles.get(position);
			Date fecha = da.getFecha();
			
			TextView txtTitulo = (TextView)convertView.findViewById(R.id.det_acc_textView1);
			txtTitulo.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(fecha));
			TextView txtFecha = (TextView)convertView.findViewById(R.id.det_acc_textView3);
			txtFecha.setText(da.getKilometros().toString());
			TextView txtKms = (TextView)convertView.findViewById(R.id.det_acc_textView5);
			txtKms.setText(da.getLugar());
			TextView txtCoste = (TextView)convertView.findViewById(R.id.det_acc_textView7);
			txtCoste.setText(da.getObservaciones());
		}
		return convertView;
	}
}
