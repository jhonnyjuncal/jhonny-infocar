package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleMantenimiento {
	
	private Integer id;
	private Date fecha;
	private String tipo;
	private Double kms;
	private String taller;
	private Double precio;
	private String observaciones;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Double getKms() {
		return kms;
	}
	
	public void setKms(Double kms) {
		this.kms = kms;
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
}
