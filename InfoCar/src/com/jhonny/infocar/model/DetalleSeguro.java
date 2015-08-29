package com.jhonny.infocar.model;

import java.util.Date;

/**
 * Created by jhonny on 28/08/2015.
 */
public class DetalleSeguro {

    private Integer idSeguro;
    private Integer idVehiculo;
    private Date fecha;
    private String compania;
    private Integer tipoSeguro;
    private String numeroPoliza;
    private boolean alerta;


    public DetalleSeguro() {

    }


    public Integer getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(Integer idSeguro) {
        this.idSeguro = idSeguro;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public Integer getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(Integer tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public boolean getAlerta() {
        return alerta;
    }

    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }
}
