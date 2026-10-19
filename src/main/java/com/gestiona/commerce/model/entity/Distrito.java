package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Distrito (Catálogo territorial).
 */
public class Distrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDistrito;
    private String nombreDistrito;
    private Integer idProvincia;
    private String estado;

    public Distrito() {
    }

    public Distrito(Integer idDistrito, String nombreDistrito) {
        this.idDistrito = idDistrito;
        this.nombreDistrito = nombreDistrito;
        this.estado = "Activo";
    }

    public Integer getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(Integer idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    public Integer getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Integer idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreDistrito;
    }
}
