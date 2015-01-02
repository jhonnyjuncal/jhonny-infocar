package com.jhonny.infocar;

import java.util.Calendar;
import java.util.Date;
//import android.content.res.TypedArray;


public class Util {
	
//	private static TypedArray arrayMarcas;
//	private static TypedArray arrayCarburantes;
//	private static TypedArray arrayTiposVeh;
	
	
	static {
//		arrayMarcas = getResources().obtainTypedArray(R.array.MARCAS_VEHICULO);
//		arrayCarburantes = getResources().obtainTypedArray(R.array.TIPOS_CARBURANTE);
//		arrayTiposVeh = getResources().obtainTypedArray(R.array.TIPOS_VEHICULO);
	}
	
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
			int a�o = cal.get(Calendar.YEAR);
			
			fechaConvertida = dia + "/" + mes + "/" + a�o;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return fechaConvertida;
	}
	
	
}