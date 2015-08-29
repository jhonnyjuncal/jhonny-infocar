package com.jhonny.infocar.model;

import java.util.Date;

/**
 * Created by jhonny on 09/08/2015.
 */
public class DetalleItv {

    private Integer idDetalleItv;
    private Integer idVehiculo;
    private Date fecha;
    private Date fechaProxima;
    private String lugar;
    private String observaciones;


    public Integer getIdDetalleItv() {
        return idDetalleItv;
    }

    public void setIdDetalleItv(Integer idDetalleItv) {
        this.idDetalleItv = idDetalleItv;
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

    public Date getFechaProxima() {
        return fechaProxima;
    }

    public void setFechaProxima(Date fechaProxima) {
        this.fechaProxima = fechaProxima;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
