package com.jhonny.infocar.adapters;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.R;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class DetalleMantenimientosAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<DetalleMantenimiento> detalles;
	
	
	public DetalleMantenimientosAdapter(Context context, ArrayList<DetalleMantenimiento> detalles) {
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
            convertView = mInflater.inflate(R.layout.mant_detalle, null);
		}
		
		if(detalles != null && !detalles.isEmpty()) {
			DetalleMantenimiento dm = detalles.get(position);
			Date fecha = dm.getFecha();
			
			TextView txtTitulo = (TextView)convertView.findViewById(R.id.det_textView1);
			txtTitulo.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(fecha));
			TextView txtFecha = (TextView)convertView.findViewById(R.id.det_textView3);
			txtFecha.setText(DateFormat.getInstance().format(fecha));
			TextView txtKms = (TextView)convertView.findViewById(R.id.det_textView5);
			txtKms.setText(dm.getKms().toString());
			TextView txtCoste = (TextView)convertView.findViewById(R.id.det_textView7);
			txtCoste.setText(dm.getPrecio().toString());
			TextView txtTipo = (TextView)convertView.findViewById(R.id.det_textView9);
			txtTipo.setText(dm.getTipo());
			TextView txtTaller = (TextView)convertView.findViewById(R.id.det_textView11);
			txtTaller.setText(dm.getTaller());
		}
		return convertView;
	}
}
