package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Cliente (vinculada a datos_personales según decisión D2).
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idCliente;
    private Integer idDatosPersonales;
    private DatosPersonales datosPersonales;
    private String tipoCliente; // Natural, Empresa
    private String ruc;
    private String razonSocial;
    private String estado;

    public Cliente() {
        this.tipoCliente = "Natural";
        this.estado = "Activo";
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdDatosPersonales() {
        return idDatosPersonales;
    }

    public void setIdDatosPersonales(Integer idDatosPersonales) {
        this.idDatosPersonales = idDatosPersonales;
    }

    public DatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(DatosPersonales datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreOEmpresa() {
        if ("Empresa".equalsIgnoreCase(tipoCliente) && razonSocial != null && !razonSocial.trim().isEmpty()) {
            return razonSocial;
        }
        return datosPersonales != null ? datosPersonales.getNombreCompleto() : "";
    }

    @Override
    public String toString() {
        return getNombreOEmpresa();
    }
}
