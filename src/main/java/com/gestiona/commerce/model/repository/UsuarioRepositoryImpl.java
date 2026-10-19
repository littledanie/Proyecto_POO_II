package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC pura del Patrón Repository para Usuario (UsuarioRepositoryImpl).
 * 
 * Reglas de arquitectura y calidad aplicadas:
 * - Acceso a datos centralizado a través del patrón Singleton ConexionBD.
 * - Uso estricto de PreparedStatement para evitar inyección SQL (SQLi).
 * - Manejo de transacciones ACID (commit/rollback) en operaciones compuestas.
 * - Eliminación lógica obligatoria (estado = 'Inactivo') según AGENTS.md §6.6.
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {

    // =========================================================================
    // BLOQUE 1: Obtención de conexión a través del patrón creacional Singleton
    // =========================================================================
    private Connection getConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    // =========================================================================
    // BLOQUE 2: Lectura de todos los usuarios con sus entidades relacionadas
    // =========================================================================
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, u.id_rol, " +
                     "       u.id_datos_personales, u.ultimo_acceso, u.intentos_fallidos, " +
                     "       u.bloqueado, u.estado, " +
                     "       r.nombre_rol, r.descripcion AS rol_descripcion, " +
                     "       dp.nombres, dp.apellidos, dp.dni, dp.fecha_nacimiento, dp.id_sexo, dp.id_estado_civil, " +
                     "       s.descripcion AS sexo_desc, ec.descripcion AS ec_desc, " +
                     "       ce.correo, t.numero_telefono, " +
                     "       dir.calle_avenida, dir.numero, d.id_distrito, d.nombre_distrito " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "LEFT JOIN datos_personales dp ON u.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN sexo s ON dp.id_sexo = s.id_sexo " +
                     "LEFT JOIN estado_civil ec ON dp.id_estado_civil = ec.id_estado_civil " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "ORDER BY u.id_usuario ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuarioCompleto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // =========================================================================
    // BLOQUE 3: Búsqueda individual por ID y por Username
    // =========================================================================
    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, u.id_rol, " +
                     "       u.id_datos_personales, u.ultimo_acceso, u.intentos_fallidos, " +
                     "       u.bloqueado, u.estado, " +
                     "       r.nombre_rol, r.descripcion AS rol_descripcion, " +
                     "       dp.nombres, dp.apellidos, dp.dni, dp.fecha_nacimiento, dp.id_sexo, dp.id_estado_civil, " +
                     "       s.descripcion AS sexo_desc, ec.descripcion AS ec_desc, " +
                     "       ce.correo, t.numero_telefono, " +
                     "       dir.calle_avenida, dir.numero, d.id_distrito, d.nombre_distrito " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "LEFT JOIN datos_personales dp ON u.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN sexo s ON dp.id_sexo = s.id_sexo " +
                     "LEFT JOIN estado_civil ec ON dp.id_estado_civil = ec.id_estado_civil " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "WHERE u.id_usuario = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuarioCompleto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error al buscar usuario por ID (" + id + "): " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, u.id_rol, " +
                     "       u.id_datos_personales, u.ultimo_acceso, u.intentos_fallidos, " +
                     "       u.bloqueado, u.estado, " +
                     "       r.nombre_rol, r.descripcion AS rol_descripcion, " +
                     "       dp.nombres, dp.apellidos, dp.dni, dp.fecha_nacimiento, dp.id_sexo, dp.id_estado_civil, " +
                     "       s.descripcion AS sexo_desc, ec.descripcion AS ec_desc, " +
                     "       ce.correo, t.numero_telefono, " +
                     "       dir.calle_avenida, dir.numero, d.id_distrito, d.nombre_distrito " +
                     "FROM usuario u " +
                     "JOIN rol r ON u.id_rol = r.id_rol " +
                     "LEFT JOIN datos_personales dp ON u.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN sexo s ON dp.id_sexo = s.id_sexo " +
                     "LEFT JOIN estado_civil ec ON dp.id_estado_civil = ec.id_estado_civil " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "WHERE u.nombre_usuario = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuarioCompleto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error al buscar usuario por Username: " + e.getMessage());
        }
        return null;
    }

    // =========================================================================
    // BLOQUE 4: Registro transaccional de Usuario + Datos Personales (ACID)
    // =========================================================================
    @Override
    public boolean registrar(Usuario usuario, DatosPersonales dp) {
        String sqlDP = "INSERT INTO datos_personales (nombres, apellidos, dni, fecha_nacimiento, id_sexo, id_estado_civil) " +
                       "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_datos_personales";

        String sqlCorreo = "INSERT INTO correo_electronico (id_datos_personales, correo, tipo_correo) VALUES (?, ?, 'personal')";
        String sqlTelefono = "INSERT INTO telefono (id_datos_personales, numero_telefono, tipo_telefono) VALUES (?, ?, 'móvil')";
        String sqlDireccion = "INSERT INTO direccion (id_datos_personales, id_distrito, calle_avenida, numero, tipo_direccion) VALUES (?, ?, ?, ?, 'domicilio')";

        String sqlUsuario = "INSERT INTO usuario (nombre_usuario, contrasena_hash, id_rol, id_datos_personales, intentos_fallidos, bloqueado) " +
                            "VALUES (?, ?, ?, ?, 0, false)";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false); // Inicio de transacción atómica

            // 1. Insertar datos personales y recuperar la clave generada
            int idGeneradoDP = 0;
            try (PreparedStatement psDP = con.prepareStatement(sqlDP)) {
                psDP.setString(1, dp.getNombres());
                psDP.setString(2, dp.getApellidos());
                psDP.setString(3, dp.getDni());
                psDP.setDate(4, dp.getFechaNacimiento());
                if (dp.getIdSexo() != null) psDP.setInt(5, dp.getIdSexo()); else psDP.setNull(5, Types.INTEGER);
                if (dp.getIdEstadoCivil() != null) psDP.setInt(6, dp.getIdEstadoCivil()); else psDP.setNull(6, Types.INTEGER);

                try (ResultSet rs = psDP.executeQuery()) {
                    if (rs.next()) {
                        idGeneradoDP = rs.getInt(1);
                    }
                }
            }

            if (idGeneradoDP <= 0) {
                con.rollback();
                return false;
            }

            // 2. Insertar correo electrónico
            if (dp.getCorreoPrincipal() != null && !dp.getCorreoPrincipal().trim().isEmpty()) {
                try (PreparedStatement psC = con.prepareStatement(sqlCorreo)) {
                    psC.setInt(1, idGeneradoDP);
                    psC.setString(2, dp.getCorreoPrincipal().trim());
                    psC.executeUpdate();
                }
            }

            // 3. Insertar teléfono
            if (dp.getTelefonoPrincipal() != null && !dp.getTelefonoPrincipal().trim().isEmpty()) {
                try (PreparedStatement psT = con.prepareStatement(sqlTelefono)) {
                    psT.setInt(1, idGeneradoDP);
                    psT.setString(2, dp.getTelefonoPrincipal().trim());
                    psT.executeUpdate();
                }
            }

            // 4. Insertar dirección
            if (dp.getIdDistrito() != null) {
                try (PreparedStatement psD = con.prepareStatement(sqlDireccion)) {
                    psD.setInt(1, idGeneradoDP);
                    psD.setInt(2, dp.getIdDistrito());
                    psD.setString(3, dp.getDireccionPrincipal() != null ? dp.getDireccionPrincipal() : "Principal");
                    psD.setString(4, "S/N");
                    psD.executeUpdate();
                }
            }

            // 5. Insertar cuenta de usuario vinculada
            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setString(1, usuario.getNombreUsuario());
                psU.setString(2, usuario.getContrasenaHash());
                psU.setInt(3, usuario.getIdRol());
                psU.setInt(4, idGeneradoDP);
                psU.executeUpdate();
            }

            con.commit(); // Confirmación de todos los cambios
            return true;

        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error en transacción de registro: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // =========================================================================
    // BLOQUE 5: Actualización transaccional de Usuario + Datos Personales
    // =========================================================================
    @Override
    public boolean actualizar(Usuario usuario, DatosPersonales dp) {
        String sqlUsuarioConClave = "UPDATE usuario SET id_rol = ?, contrasena_hash = ?, fecha_actualizacion = now() WHERE id_usuario = ?";
        String sqlUsuarioSinClave = "UPDATE usuario SET id_rol = ?, fecha_actualizacion = now() WHERE id_usuario = ?";

        String sqlDP = "UPDATE datos_personales SET nombres = ?, apellidos = ?, dni = ?, fecha_nacimiento = ?, " +
                       "       id_sexo = ?, id_estado_civil = ?, fecha_actualizacion = now() " +
                       "WHERE id_datos_personales = ?";

        String sqlUpsertCorreo = "INSERT INTO correo_electronico (id_datos_personales, correo, tipo_correo) VALUES (?, ?, 'personal') " +
                                 "ON CONFLICT DO NOTHING";
        String sqlUpdateCorreo = "UPDATE correo_electronico SET correo = ? WHERE id_datos_personales = ?";

        String sqlUpsertTelefono = "INSERT INTO telefono (id_datos_personales, numero_telefono, tipo_telefono) VALUES (?, ?, 'móvil') " +
                                   "ON CONFLICT DO NOTHING";
        String sqlUpdateTelefono = "UPDATE telefono SET numero_telefono = ? WHERE id_datos_personales = ?";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);

            // 1. Actualizar usuario
            boolean actualizaClave = usuario.getContrasenaHash() != null && !usuario.getContrasenaHash().trim().isEmpty();
            String sqlU = actualizaClave ? sqlUsuarioConClave : sqlUsuarioSinClave;
            try (PreparedStatement psU = con.prepareStatement(sqlU)) {
                psU.setInt(1, usuario.getIdRol());
                if (actualizaClave) {
                    psU.setString(2, usuario.getContrasenaHash());
                    psU.setInt(3, usuario.getIdUsuario());
                } else {
                    psU.setInt(2, usuario.getIdUsuario());
                }
                psU.executeUpdate();
            }

            // 2. Actualizar datos personales si están asociados
            if (usuario.getIdDatosPersonales() != null && dp != null) {
                try (PreparedStatement psDP = con.prepareStatement(sqlDP)) {
                    psDP.setString(1, dp.getNombres());
                    psDP.setString(2, dp.getApellidos());
                    psDP.setString(3, dp.getDni());
                    psDP.setDate(4, dp.getFechaNacimiento());
                    if (dp.getIdSexo() != null) psDP.setInt(5, dp.getIdSexo()); else psDP.setNull(5, Types.INTEGER);
                    if (dp.getIdEstadoCivil() != null) psDP.setInt(6, dp.getIdEstadoCivil()); else psDP.setNull(6, Types.INTEGER);
                    psDP.setInt(7, usuario.getIdDatosPersonales());
                    psDP.executeUpdate();
                }

                // Actualizar correo
                if (dp.getCorreoPrincipal() != null) {
                    try (PreparedStatement psC = con.prepareStatement(sqlUpdateCorreo)) {
                        psC.setString(1, dp.getCorreoPrincipal());
                        psC.setInt(2, usuario.getIdDatosPersonales());
                        int rows = psC.executeUpdate();
                        if (rows == 0) {
                            try (PreparedStatement psInsC = con.prepareStatement(sqlUpsertCorreo)) {
                                psInsC.setInt(1, usuario.getIdDatosPersonales());
                                psInsC.setString(2, dp.getCorreoPrincipal());
                                psInsC.executeUpdate();
                            }
                        }
                    }
                }

                // Actualizar teléfono
                if (dp.getTelefonoPrincipal() != null) {
                    try (PreparedStatement psT = con.prepareStatement(sqlUpdateTelefono)) {
                        psT.setString(1, dp.getTelefonoPrincipal());
                        psT.setInt(2, usuario.getIdDatosPersonales());
                        int rows = psT.executeUpdate();
                        if (rows == 0) {
                            try (PreparedStatement psInsT = con.prepareStatement(sqlUpsertTelefono)) {
                                psInsT.setInt(1, usuario.getIdDatosPersonales());
                                psInsT.setString(2, dp.getTelefonoPrincipal());
                                psInsT.executeUpdate();
                            }
                        }
                    }
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error al actualizar usuario: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // =========================================================================
    // BLOQUE 6: Eliminación LÓGICA (estado = 'Inactivo', según AGENTS.md §6.6)
    // =========================================================================
    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE usuario SET estado = 'Inactivo', fecha_actualizacion = now() WHERE id_usuario = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error en eliminación lógica: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // BLOQUE 7: Modificación de Bloqueo de Cuenta (Desbloqueo tras 3 intentos)
    // =========================================================================
    @Override
    public boolean cambiarEstadoBloqueo(int id, boolean bloqueado) {
        String sql = "UPDATE usuario SET bloqueado = ?, intentos_fallidos = ?, fecha_actualizacion = now() WHERE id_usuario = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, bloqueado);
            ps.setInt(2, bloqueado ? 3 : 0); // Si se desbloquea, se reinician intentos a 0
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioRepositoryImpl] Error al cambiar estado de bloqueo: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // BLOQUE 8: Carga de catálogos auxiliares para formularios
    // =========================================================================
    @Override
    public List<Rol> listarRoles() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol, descripcion FROM rol WHERE estado = 'Activo' ORDER BY id_rol";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Sexo> listarSexos() {
        List<Sexo> lista = new ArrayList<>();
        String sql = "SELECT id_sexo, descripcion FROM sexo WHERE estado = 'Activo' ORDER BY id_sexo";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Sexo(rs.getInt("id_sexo"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<EstadoCivil> listarEstadosCiviles() {
        List<EstadoCivil> lista = new ArrayList<>();
        String sql = "SELECT id_estado_civil, descripcion FROM estado_civil WHERE estado = 'Activo' ORDER BY id_estado_civil";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new EstadoCivil(rs.getInt("id_estado_civil"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Distrito> listarDistritos() {
        List<Distrito> lista = new ArrayList<>();
        String sql = "SELECT id_distrito, nombre_distrito FROM distrito WHERE estado = 'Activo' ORDER BY nombre_distrito";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Distrito(rs.getInt("id_distrito"), rs.getString("nombre_distrito")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // =========================================================================
    // Método auxiliar de mapeo
    // =========================================================================
    private Usuario mapearUsuarioCompleto(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setIdRol(rs.getInt("id_rol"));
        u.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        u.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        u.setBloqueado(rs.getBoolean("bloqueado"));
        u.setEstado(rs.getString("estado"));

        // Rol
        Rol r = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"), rs.getString("rol_descripcion"));
        u.setRol(r);

        // Datos Personales
        int idDP = rs.getInt("id_datos_personales");
        if (!rs.wasNull()) {
            u.setIdDatosPersonales(idDP);
            DatosPersonales dp = new DatosPersonales();
            dp.setIdDatosPersonales(idDP);
            dp.setNombres(rs.getString("nombres"));
            dp.setApellidos(rs.getString("apellidos"));
            dp.setDni(rs.getString("dni"));
            dp.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            dp.setIdSexo(rs.getInt("id_sexo"));
            dp.setIdEstadoCivil(rs.getInt("id_estado_civil"));

            if (rs.getString("sexo_desc") != null) {
                dp.setSexo(new Sexo(rs.getInt("id_sexo"), rs.getString("sexo_desc")));
            }
            if (rs.getString("ec_desc") != null) {
                dp.setEstadoCivil(new EstadoCivil(rs.getInt("id_estado_civil"), rs.getString("ec_desc")));
            }

            dp.setCorreoPrincipal(rs.getString("correo"));
            dp.setTelefonoPrincipal(rs.getString("numero_telefono"));
            dp.setDireccionPrincipal(rs.getString("calle_avenida"));

            int idDist = rs.getInt("id_distrito");
            if (!rs.wasNull()) {
                dp.setIdDistrito(idDist);
                dp.setDistrito(new Distrito(idDist, rs.getString("nombre_distrito")));
            }

            u.setDatosPersonales(dp);
        }
        return u;
    }
}
