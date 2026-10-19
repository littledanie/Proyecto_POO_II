package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Sexo (Catálogo de personas).
 */
public class Sexo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSexo;
    private String descripcion;
    private String estado;

    public Sexo() {
    }

    public Sexo(Integer idSexo, String descripcion) {
        this.idSexo = idSexo;
        this.descripcion = descripcion;
        this.estado = "Activo";
    }

    public Integer getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(Integer idSexo) {
        this.idSexo = idSexo;
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
