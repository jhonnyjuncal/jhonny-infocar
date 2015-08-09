package com.jhonny.infocar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Util {
	
	public static Date convierteStringEnDate(String fecha) {
		Calendar cal = Calendar.getInstance();
		try {
			if(fecha == null || fecha.length() <= 1)
				return null;
			String[] fechaPartida = fecha.split("/");
			cal.set(Integer.valueOf(fechaPartida[2]), Integer.valueOf(fechaPartida[1]), Integer.valueOf(fechaPartida[0]), 0, 0, 0);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cal.getTime();
	}
	
	public static String convierteDateEnString(Date fecha) {
		String fechaConvertida = new String();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			
			int dia = cal.get(Calendar.DAY_OF_MONTH);
			int mes = cal.get(Calendar.MONTH) + 1;
			int ano = cal.get(Calendar.YEAR);
			
			fechaConvertida = dia + "/" + mes + "/" + ano;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return fechaConvertida;
	}

	public static synchronized void cargaFondoDePantalla(FragmentActivity myContext) {
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

	public static String formateaFechaParaMostrar(Date fecha) {
		String result = "";
		try {
			DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
			result = format.format(fecha);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
