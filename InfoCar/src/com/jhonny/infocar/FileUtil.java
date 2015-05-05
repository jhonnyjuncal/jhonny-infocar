package com.jhonny.infocar;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.Days;


public class FileUtil implements Serializable{
	
	private static final long serialVersionUID = -3721769765641235234L;
	
	
	/**
	 * Calcula la cantidad de dias entre 2 fechas
	 * @param fecha
	 * @return int dias
	 */
	public static int cantidadDeDiasDiferencia(Date fecha){
		int dias = 0;
		try{
			DateTime fechaActual = new DateTime();
			DateTime fechaCoordenada = new DateTime(fecha);
			dias = Days.daysBetween(fechaCoordenada, fechaActual).getDays();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return dias;
	}
	
	
	/**
	 * Devuelve la fecha formateada dependiendo de la configuracion del telefono
	 * @param fecha
	 * @param locale
	 * @return fecha formateada
	 */
	public static String getFechaFormateada(Date fecha, Locale locale){
		String resultado = "";
		try{
			DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			resultado = dateFormatter.format(fecha);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultado;
	}
	
	
	/**
	 * Devuelve la hora formateada dependiendo de la configuracion del telefono
	 * @param fecha
	 * @param locale
	 * @return hora formateada
	 */
	public static String getHoraFormateada(Date fecha, Locale locale){
		String resultado = "";
		try{
			DateFormat dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
			resultado = dateFormatter.format(fecha);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultado;
	}
}
