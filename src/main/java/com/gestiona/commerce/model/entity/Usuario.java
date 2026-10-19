package com.gestiona.commerce.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entidad que representa a un Usuario en el sistema Gestiona Commerce.
 * 
 * Esquema 3FN (Etapa 3):
 * usuario(id_usuario, nombre_usuario, contrasena_hash, id_rol, ultimo_acceso,
 *         intentos_fallidos, bloqueado, token_recuperacion, +AUD)
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idUsuario;
    private String nombreUsuario;
    private String contrasenaHash;
    private Integer idRol;
    private Rol rol; // Objeto de relación con Rol
    private Timestamp ultimoAcceso;
    private int intentosFallidos;
    private boolean bloqueado;
    private String estado;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String nombreUsuario, String contrasenaHash, Integer idRol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.idRol = idRol;
        this.intentosFallidos = 0;
        this.bloqueado = false;
        this.estado = "Activo";
    }

    // --- Getters y Setters ---

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", idRol=" + idRol +
                ", bloqueado=" + bloqueado +
                '}';
    }
}
