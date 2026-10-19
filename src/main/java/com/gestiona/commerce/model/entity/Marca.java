package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Marca (Catálogo de productos).
 */
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMarca;
    private String nombreMarca;
    private String estado;

    public Marca() {
        this.estado = "Activo";
    }

    public Marca(Integer idMarca, String nombreMarca) {
        this.idMarca = idMarca;
        this.nombreMarca = nombreMarca;
        this.estado = "Activo";
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getNombre() {
        return nombreMarca;
    }

    public void setNombre(String nombre) {
        this.nombreMarca = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreMarca;
    }
}
