package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Categoria (Catálogo de productos).
 */
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCategoria;
    private String nombreCategoria;
    private String descripcion;
    private String estado;

    public Categoria() {
        this.estado = "Activo";
    }

    public Categoria(Integer idCategoria, String nombreCategoria, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.estado = "Activo";
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombre() {
        return nombreCategoria;
    }

    public void setNombre(String nombre) {
        this.nombreCategoria = nombre;
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
        return nombreCategoria;
    }
}
