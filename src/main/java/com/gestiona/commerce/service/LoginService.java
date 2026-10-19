package com.gestiona.commerce.service;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.Rol;
import com.gestiona.commerce.model.entity.Usuario;
import com.gestiona.commerce.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servicio de Autenticación con BCrypt y Control de Intentos Fallidos (Etapa 3).
 * 
 * Reglas de negocio obligatorias:
 * - Uso del patrón creacional Singleton (ConexionBD.getInstancia().getConexion()).
 * - Consultas parametrizadas (PreparedStatement) para prevenir inyección SQL.
 * - Validación de contraseñas mediante hash seguro BCrypt.
 * - Bloqueo automático de cuenta en BD al acumular 3 intentos fallidos consecutivos.
 * - Restablecimiento de contador de intentos y registro de último acceso al iniciar sesión.
 */
public class LoginService {

    private static final int LIMITE_INTENTOS = 3;

    /**
     * Procesa la autenticación del usuario aplicando las validaciones de seguridad de la Etapa 3.
     * 
     * @param nombreUsuario Nombre de usuario ingresado
     * @param contrasena Contraseña en texto plano
     * @return ResultadoLogin con el estado (éxito, credenciales inválidas o cuenta bloqueada)
     * @throws SQLException Si ocurre un error de comunicación con Supabase
     */
    public ResultadoLogin autenticar(String nombreUsuario, String contrasena) throws SQLException {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            return ResultadoLogin.credencialesInvalidas(LIMITE_INTENTOS);
        }

        String sqlBuscar = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, u.id_rol, " +
                           "       u.intentos_fallidos, u.bloqueado, u.estado, " +
                           "       r.nombre_rol, r.descripcion AS rol_descripcion " +
                           "FROM usuario u " +
                           "JOIN rol r ON u.id_rol = r.id_rol " +
                           "WHERE u.nombre_usuario = ? AND u.estado = 'Activo'";

        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sqlBuscar)) {

            ps.setString(1, nombreUsuario.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // Usuario no existe en el sistema: error genérico por seguridad
                    return ResultadoLogin.credencialesInvalidas(LIMITE_INTENTOS);
                }

                // Construcción de la entidad Usuario con su Rol asociado
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                usuario.setIdRol(rs.getInt("id_rol"));
                usuario.setIntentosFallidos(rs.getInt("intentos_fallidos"));
                usuario.setBloqueado(rs.getBoolean("bloqueado"));
                usuario.setEstado(rs.getString("estado"));

                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                rol.setDescripcion(rs.getString("rol_descripcion"));
                rol.setPermisos(cargarPermisosDeRol(con, rs.getInt("id_rol")));
                usuario.setRol(rol);

                // 1. Verificación previa de bloqueo: si ya está bloqueado, no permitir acceso
                if (usuario.isBloqueado()) {
                    return ResultadoLogin.cuentaBloqueada(
                        "Cuenta bloqueada. Contacte al administrador del sistema."
                    );
                }

                // 2. Validación de contraseña con BCrypt
                boolean passwordValido = PasswordUtil.verificar(contrasena, usuario.getContrasenaHash());

                if (passwordValido) {
                    // Contraseña correcta: restablecer intentos y actualizar último acceso
                    restablecerIntentosYActualizarAcceso(con, usuario.getIdUsuario());
                    usuario.setIntentosFallidos(0);
                    return ResultadoLogin.exito(usuario);
                } else {
                    // Contraseña incorrecta: registrar intento fallido
                    int nuevosIntentos = usuario.getIntentosFallidos() + 1;
                    boolean debeBloquear = nuevosIntentos >= LIMITE_INTENTOS;

                    actualizarIntentosFallidos(con, usuario.getIdUsuario(), nuevosIntentos, debeBloquear);

                    if (debeBloquear) {
                        return ResultadoLogin.cuentaBloqueada(
                            "Cuenta bloqueada. Contacte al administrador del sistema."
                        );
                    } else {
                        int intentosRestantes = LIMITE_INTENTOS - nuevosIntentos;
                        return ResultadoLogin.credencialesInvalidas(intentosRestantes);
                    }
                }
            }
        }
    }

    /**
     * Restablece el contador de intentos a 0 y actualiza la fecha/hora de último acceso.
     */
    private void restablecerIntentosYActualizarAcceso(Connection con, int idUsuario) throws SQLException {
        String sql = "UPDATE usuario SET intentos_fallidos = 0, ultimo_acceso = now() WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    /**
     * Actualiza el número de intentos fallidos y el indicador de bloqueo si corresponde.
     */
    private void actualizarIntentosFallidos(Connection con, int idUsuario, int intentos, boolean bloquear) throws SQLException {
        String sql = "UPDATE usuario SET intentos_fallidos = ?, bloqueado = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, intentos);
            ps.setBoolean(2, bloquear);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        }
    }

    private java.util.Set<String> cargarPermisosDeRol(Connection con, int idRol) {
        java.util.Set<String> permisos = new java.util.HashSet<>();
        String sql = "SELECT p.nombre_permiso FROM permiso p " +
                     "JOIN rol_permiso rp ON p.id_permiso = rp.id_permiso " +
                     "WHERE rp.id_rol = ? AND p.estado = 'Activo'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permisos.add(rs.getString("nombre_permiso"));
                }
            }
        } catch (SQLException e) {
            System.err.println("[LoginService] Error al cargar permisos: " + e.getMessage());
        }
        return permisos;
    }
}
