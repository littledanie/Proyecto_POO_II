package com.gestiona.commerce.model.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * Entidad DatosPersonales (Persona compartida según decisión D2).
 * 
 * Centraliza la identidad de usuarios, clientes y proveedores,
 * evitando la redundancia de datos personales en el sistema.
 */
public class DatosPersonales implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDatosPersonales;
    private String nombres;
    private String apellidos;
    private String dni;
    private Date fechaNacimiento;

    private Integer idSexo;
    private Sexo sexo;

    private Integer idEstadoCivil;
    private EstadoCivil estadoCivil;

    // Canales de contacto y ubicación vinculados
    private String correoPrincipal;
    private String telefonoPrincipal;
    private String direccionPrincipal;
    private Integer idDistrito;
    private Distrito distrito;

    private String estado;

    public DatosPersonales() {
        this.estado = "Activo";
    }

    public DatosPersonales(String nombres, String apellidos, String dni) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.estado = "Activo";
    }

    public String getNombreCompleto() {
        if (nombres == null && apellidos == null) return "";
        if (nombres == null) return apellidos;
        if (apellidos == null) return nombres;
        return nombres + " " + apellidos;
    }

    public String getNombreDistrito() {
        return (distrito != null) ? distrito.getNombreDistrito() : null;
    }

    // --- Getters y Setters ---

    public Integer getIdDatosPersonales() {
        return idDatosPersonales;
    }

    public void setIdDatosPersonales(Integer idDatosPersonales) {
        this.idDatosPersonales = idDatosPersonales;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(Integer idSexo) {
        this.idSexo = idSexo;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Integer getIdEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadoCivil(Integer idEstadoCivil) {
        this.idEstadoCivil = idEstadoCivil;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCorreoPrincipal() {
        return correoPrincipal;
    }

    public void setCorreoPrincipal(String correoPrincipal) {
        this.correoPrincipal = correoPrincipal;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getDireccionPrincipal() {
        return direccionPrincipal;
    }

    public void setDireccionPrincipal(String direccionPrincipal) {
        this.direccionPrincipal = direccionPrincipal;
    }

    public Integer getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(Integer idDistrito) {
        this.idDistrito = idDistrito;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (" + dni + ")";
    }
}
