package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleAccidente {
	
	private Integer idDetalleAccidente;
	private Date fecha;
	private String lugar;
	private Double kilometros;
	private String observaciones;
	private Integer idVehiculo;
	private Integer posicion;
	
	
	public Integer getIdDetalleAccidente() {
		return idDetalleAccidente;
	}
	
	public void setIdDetalleAccidente(Integer idDetalleAccidente) {
		this.idDetalleAccidente = idDetalleAccidente;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getLugar() {
		return lugar;
	}
	
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	
	public Double getKilometros() {
		return kilometros;
	}
	
	public void setKilometros(Double kilometros) {
		this.kilometros = kilometros;
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
