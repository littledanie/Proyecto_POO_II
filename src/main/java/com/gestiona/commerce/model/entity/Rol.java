package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad que representa un Rol en el sistema (RBAC).
 * 
 * Esquema 3FN (Etapa 3): rol(id_rol, nombre_rol, descripcion, +AUD).
 */
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idRol;
    private String nombreRol;
    private String descripcion;
    private String estado;
    private java.util.Set<String> permisos = new java.util.HashSet<>();

    public Rol() {
    }

    public Rol(Integer idRol, String nombreRol, String descripcion) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.estado = "Activo";
    }

    public java.util.Set<String> getPermisos() {
        return permisos != null ? permisos : java.util.Collections.emptySet();
    }

    public void setPermisos(java.util.Set<String> permisos) {
        this.permisos = permisos != null ? permisos : new java.util.HashSet<>();
    }

    public boolean tienePermiso(String nombrePermiso) {
        return permisos != null && permisos.contains(nombrePermiso);
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
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
        return "Rol{" +
                "idRol=" + idRol +
                ", nombreRol='" + nombreRol + '\'' +
                '}';
    }
}
