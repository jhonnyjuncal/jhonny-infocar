package com.jhonny.infocar.model;

import java.util.Date;

/**
 * Created by jhonny on 08/08/2015.
 */
public class DetalleFichaTecnica {

    private Integer idFichaTecnica;
    private Integer idVehiculo;
    private Date fechaMatriculacion;
    private String lugar;
    private String neumaticos;
    private String potencia;
    private String bastidor;


    public Integer getIdFichaTecnica() {
        return idFichaTecnica;
    }

    public void setIdFichaTecnica(Integer idFichaTecnica) {
        this.idFichaTecnica = idFichaTecnica;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Date getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(Date fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNeumaticos() {
        return neumaticos;
    }

    public void setNeumaticos(String neumaticos) {
        this.neumaticos = neumaticos;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }

    public String getBastidor() {
        return bastidor;
    }

    public void setBastidor(String bastidor) {
        this.bastidor = bastidor;
    }
}
