package com.jhonny.infocar.model;

import java.util.Date;


public class DetalleVehiculo {
	
	private int idVehiculo;
	private int marca;
	private String modelo;
	private Double kilometros;
	private Date fechaCompra;
	private String matricula;
	private int tipoVehiculo;
	private int tipoCarburante;
	private int idSeguro;
	private int idItv;
	
	
	public int getIdVehiculo() {
		return idVehiculo;
	}
	
	public void setIdVehiculo(int idVehiculo) {
		this.idVehiculo = idVehiculo;
	}
	
	public int getMarca() {
		return marca;
	}
	
	public void setMarca(int marca) {
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
	
	public int getTipoVehiculo() {
		return tipoVehiculo;
	}
	
	public void setTipoVehiculo(int tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}
	
	public int getTipoCarburante() {
		return tipoCarburante;
	}
	
	public void setTipoCarburante(int tipoCarburante) {
		this.tipoCarburante = tipoCarburante;
	}
	
	public int getIdSeguro() {
		return idSeguro;
	}
	
	public void setIdSeguro(int idSeguro) {
		this.idSeguro = idSeguro;
	}
	
	public int getIdItv() {
		return idItv;
	}
	
	public void setIdItv(int idItv) {
		this.idItv = idItv;
	}
}
