package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleDatos {
	
	private Integer idDetalleDatos;
	private String nombre;
	private String telefono;
	private Integer edad;
	private boolean hombre;
	private String email;
	private Date fechaAlta;
	
	
	public Integer getIdDetalleDatos() {
		return idDetalleDatos;
	}
	
	public void setIdDetalleDatos(Integer idDetalleDatos) {
		this.idDetalleDatos = idDetalleDatos;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public Integer getEdad() {
		return edad;
	}
	
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	
	public boolean isHombre() {
		return hombre;
	}
	
	public void setHombre(boolean hombre) {
		this.hombre = hombre;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
}
