package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleVehiculo {
	
	private Integer idVehiculo;
	private Integer marca;
	private String modelo;
	private Double kilometros;
	private Date fechaCompra;
	private String matricula;
	private Integer tipoVehiculo;
	private Integer tipoCarburante;
	private Integer idSeguro;
	private Integer idItv;
	private Integer posicion;
	
	
	public Integer getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(Integer idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
	
	public Integer getMarca() {
		return marca;
	}
	
	public void setMarca(Integer marca) {
		this.marca = marca;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public Double getKilometros() {
		return kilometros;
	}
	
	public void setKilometros(Double kilometros) {
		this.kilometros = kilometros;
	}
	
	public Date getFechaCompra() {
		return fechaCompra;
	}
	
	public void setFechaCompra(Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getMatricula() {
		return matricula;
	}
	
	public Integer getTipoVehiculo() {
		return tipoVehiculo;
	}
	
	public void setTipoVehiculo(Integer tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public Integer getTipoCarburante() {
		return tipoCarburante;
	}
	
	public void setTipoCarburante(Integer tipoCarburante) {
		this.tipoCarburante = tipoCarburante;
	}
	
	public Integer getIdSeguro() {
		return idSeguro;
	}
	
	public void setIdSeguro(Integer idSeguro) {
		this.idSeguro = idSeguro;
	}
	
	public Integer getIdItv() {
		return idItv;
	}
	
	public void setIdItv(Integer idItv) {
		this.idItv = idItv;
	}

	public Integer getPosicion() {
		return posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}
}
