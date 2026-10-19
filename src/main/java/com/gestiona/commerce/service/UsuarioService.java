package com.gestiona.commerce.service;

import com.gestiona.commerce.model.entity.*;
import com.gestiona.commerce.model.repository.UsuarioRepository;
import com.gestiona.commerce.model.repository.UsuarioRepositoryImpl;
import com.gestiona.commerce.util.PasswordUtil;

import java.util.List;

/**
 * Servicio de Negocio para la Gestión de Cuentas y Usuarios (UsuarioService).
 * 
 * Aplica reglas de validación, orquesta el hashing BCrypt y consume el repositorio.
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Valida y registra un nuevo usuario en el sistema.
     */
    public String registrarUsuario(Usuario usuario, DatosPersonales dp, String contrasenaPlana) {
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()) {
            return "El nombre de usuario es obligatorio.";
        }
        if (contrasenaPlana == null || contrasenaPlana.trim().length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (usuario.getIdRol() == null || usuario.getIdRol() <= 0) {
            return "Debe seleccionar un rol válido.";
        }
        if (dp == null || dp.getNombres() == null || dp.getNombres().trim().isEmpty() ||
            dp.getApellidos() == null || dp.getApellidos().trim().isEmpty()) {
            return "Los nombres y apellidos de la persona son obligatorios.";
        }

        // Verificar unicidad de username
        if (usuarioRepository.buscarPorUsername(usuario.getNombreUsuario().trim()) != null) {
            return "El nombre de usuario '" + usuario.getNombreUsuario() + "' ya está registrado.";
        }

        // Cifrar la contraseña con BCrypt
        usuario.setContrasenaHash(PasswordUtil.hashear(contrasenaPlana));

        boolean exito = usuarioRepository.registrar(usuario, dp);
        return exito ? null : "Ocurrió un error al registrar el usuario en la base de datos.";
    }

    /**
     * Actualiza los datos de un usuario existente.
     */
    public String actualizarUsuario(Usuario usuario, DatosPersonales dp, String nuevaContrasenaPlana) {
        if (usuario.getIdUsuario() == null || usuario.getIdUsuario() <= 0) {
            return "Identificador de usuario inválido.";
        }
        if (usuario.getIdRol() == null || usuario.getIdRol() <= 0) {
            return "Debe seleccionar un rol válido.";
        }

        // Si se especificó una nueva contraseña, hashearla con BCrypt; si no, queda null para mantener la actual
        if (nuevaContrasenaPlana != null && !nuevaContrasenaPlana.trim().isEmpty()) {
            if (nuevaContrasenaPlana.trim().length() < 6) {
                return "La nueva contraseña debe tener al menos 6 caracteres.";
            }
            usuario.setContrasenaHash(PasswordUtil.hashear(nuevaContrasenaPlana));
        } else {
            usuario.setContrasenaHash(null);
        }

        boolean exito = usuarioRepository.actualizar(usuario, dp);
        return exito ? null : "No se pudo actualizar el usuario.";
    }

    /**
     * Eliminación lógica (estado = 'Inactivo').
     */
    public boolean eliminarLogico(int id) {
        return usuarioRepository.eliminarLogico(id);
    }

    /**
     * Desbloquea o bloquea una cuenta manualmente.
     */
    public boolean cambiarBloqueo(int id, boolean bloqueado) {
        return usuarioRepository.cambiarEstadoBloqueo(id, bloqueado);
    }

    // Acceso a catálogos auxiliares
    public List<Rol> listarRoles() {
        return usuarioRepository.listarRoles();
    }

    public List<Sexo> listarSexos() {
        return usuarioRepository.listarSexos();
    }

    public List<EstadoCivil> listarEstadosCiviles() {
        return usuarioRepository.listarEstadosCiviles();
    }

    public List<Distrito> listarDistritos() {
        return usuarioRepository.listarDistritos();
    }
}
