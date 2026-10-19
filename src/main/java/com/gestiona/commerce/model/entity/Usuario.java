package com.gestiona.commerce.model.entity;

import java.io.Serializable;

/**
 * Entidad que representa a un usuario del sistema Gestiona Commerce.
 * 
 * NOTA DE ARQUITECTURA (Etapa 2):
 * En esta etapa inicial, el modelo refleja la tabla provisional 'usuarios'
 * con contraseña en texto plano. En la Etapa 3 evolucionará para incorporar
 * rol, control de intentos fallidos, estado de bloqueo y hash BCrypt.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idUsuario;
    private String nombreUsuario;
    private String contrasena;

    // Constructor por defecto
    public Usuario() {
    }

    // Constructor con campos principales
    public Usuario(Integer idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // Constructor para registro/consulta sin ID
    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }
}
