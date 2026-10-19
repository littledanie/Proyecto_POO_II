package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad Proveedor (vinculada a datos_personales como persona de contacto según decisión D2).
 */
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idProveedor;
    private Integer idDatosPersonales;
    private DatosPersonales contactoPersona;
    private String ruc;
    private String razonSocial;
    private String estado;

    public Proveedor() {
        this.estado = "Activo";
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Integer getIdDatosPersonales() {
        return idDatosPersonales;
    }

    public void setIdDatosPersonales(Integer idDatosPersonales) {
        this.idDatosPersonales = idDatosPersonales;
    }

    public DatosPersonales getContactoPersona() {
        return contactoPersona;
    }

    public void setContactoPersona(DatosPersonales contactoPersona) {
        this.contactoPersona = contactoPersona;
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

    @Override
    public String toString() {
        return razonSocial + " (" + ruc + ")";
    }
}
