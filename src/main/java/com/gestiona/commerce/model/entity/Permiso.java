package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad que representa un Permiso del sistema (RBAC).
 * 
 * Esquema 3FN (Etapa 3): permiso(id_permiso, nombre_permiso, descripcion, +AUD).
 */
public class Permiso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPermiso;
    private String nombrePermiso;
    private String descripcion;
    private String estado;

    public Permiso() {
    }

    public Permiso(Integer idPermiso, String nombrePermiso, String descripcion) {
        this.idPermiso = idPermiso;
        this.nombrePermiso = nombrePermiso;
        this.descripcion = descripcion;
        this.estado = "Activo";
    }

    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
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
        return "Permiso{" +
                "idPermiso=" + idPermiso +
                ", nombrePermiso='" + nombrePermiso + '\'' +
                '}';
    }
}
