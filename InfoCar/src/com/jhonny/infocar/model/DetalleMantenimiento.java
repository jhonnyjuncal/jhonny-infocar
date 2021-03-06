package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleMantenimiento {
	
	private Integer idDetalleMantenimiento;
	private Date fecha;
	private Double kilometros;
	private Double precio;
	private String taller;
	private Integer tipoMantenimiento;
	private String observaciones;
	private Integer idVehiculo;
	private Integer posicion;
	
	
	public Integer getIdDetalleMantenimiento() {
		return idDetalleMantenimiento;
	}
	
	public void setIdDetalleMantenimiento(Integer id) {
		this.idDetalleMantenimiento = id;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Integer getTipoMantenimiento() {
		return tipoMantenimiento;
	}
	
	public void setTipoMantenimiento(Integer tipo) {
		this.tipoMantenimiento = tipo;
	}
	
	public Double getKilometros() {
		return kilometros;
	}
	
	public void setKilometros(Double kms) {
		this.kilometros = kms;
	}
	
	public String getTaller() {
		return taller;
	}
	
	public void setTaller(String taller) {
		this.taller = taller;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
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

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
}
