package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleReparacion {
	
	private Integer idDetalleReparacion;
	private Date fecha;
	private Double kilometros;
	private Double precio;
	private Integer idTipoReparacion;
	private String taller;
	private String observaciones;
	private Integer idVehiculo;
	
	
	public Integer getIdDetalleReparacion() {
		return idDetalleReparacion;
	}
	
	public void setIdDetalleReparacion(Integer idDetalleReparacion) {
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
	
	public Integer getIdTipoReparacion() {
		return idTipoReparacion;
	}
	
	public void setIdTipoReparacion(Integer idTipoReparacion) {
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
	
	public Integer getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(Integer idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
}
