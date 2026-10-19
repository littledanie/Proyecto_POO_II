package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad EstadoCivil (Catálogo de personas).
 */
public class EstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEstadoCivil;
    private String descripcion;
    private String estado;

    public EstadoCivil() {
    }

    public EstadoCivil(Integer idEstadoCivil, String descripcion) {
        this.idEstadoCivil = idEstadoCivil;
        this.descripcion = descripcion;
        this.estado = "Activo";
    }

    public Integer getIdEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadoCivil(Integer idEstadoCivil) {
        this.idEstadoCivil = idEstadoCivil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
