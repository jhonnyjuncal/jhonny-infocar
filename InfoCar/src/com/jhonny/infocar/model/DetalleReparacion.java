package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleReparacion {
	
	private int idDetalleReparacion;
	private Date fecha;
	private Double kilometros;
	private Double precio;
	private int idTipoReparacion;
	private String taller;
	private String observaciones;
	private int idVehiculo;
	
	
	public int getIdDetalleReparacion() {
		return idDetalleReparacion;
	}
	
	public void setIdDetalleReparacion(int idDetalleReparacion) {
		this.idDetalleReparacion = idDetalleReparacion;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Double getKilometros() {
		return kilometros;
	}
	
	public void setKilometros(Double kilometros) {
		this.kilometros = kilometros;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public int getIdTipoReparacion() {
		return idTipoReparacion;
	}
	
	public void setIdTipoReparacion(int idTipoReparacion) {
		this.idTipoReparacion = idTipoReparacion;
	}
	
	public String getTaller() {
		return taller;
	}
	
	public void setTaller(String taller) {
		this.taller = taller;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public int getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
}
