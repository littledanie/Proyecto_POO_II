package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad UnidadMedida (Catálogo de productos).
 */
public class UnidadMedida implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idUnidadMedida;
    private String nombre;
    private String abreviatura;
    private String estado;

    public UnidadMedida() {
        this.estado = "Activo";
    }

    public UnidadMedida(Integer idUnidadMedida, String nombre, String abreviatura) {
        this.idUnidadMedida = idUnidadMedida;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.estado = "Activo";
    }

    public Integer getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " (" + abreviatura + ")";
    }
}
