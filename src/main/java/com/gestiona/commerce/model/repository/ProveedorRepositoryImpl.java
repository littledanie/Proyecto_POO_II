package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC del Patrón Repository para Proveedor.
 */
public class ProveedorRepositoryImpl implements ProveedorRepository {

    private Connection getConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public List<Proveedor> listarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT pr.id_proveedor, pr.id_datos_personales, pr.ruc, pr.razon_social, pr.estado, " +
                     "       dp.nombres, dp.apellidos, dp.dni, " +
                     "       ce.correo, t.numero_telefono, dir.calle_avenida, d.id_distrito, d.nombre_distrito " +
                     "FROM proveedor pr " +
                     "JOIN datos_personales dp ON pr.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "ORDER BY pr.id_proveedor ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProveedor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Proveedor buscarPorId(int id) {
        String sql = "SELECT pr.id_proveedor, pr.id_datos_personales, pr.ruc, pr.razon_social, pr.estado, " +
                     "       dp.nombres, dp.apellidos, dp.dni, " +
                     "       ce.correo, t.numero_telefono, dir.calle_avenida, d.id_distrito, d.nombre_distrito " +
                     "FROM proveedor pr " +
                     "JOIN datos_personales dp ON pr.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "WHERE pr.id_proveedor = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearProveedor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registrar(Proveedor proveedor, DatosPersonales contacto) {
        String sqlDP = "INSERT INTO datos_personales (nombres, apellidos, dni) VALUES (?, ?, ?) RETURNING id_datos_personales";
        String sqlCorreo = "INSERT INTO correo_electronico (id_datos_personales, correo, tipo_correo) VALUES (?, ?, 'laboral')";
        String sqlTel = "INSERT INTO telefono (id_datos_personales, numero_telefono, tipo_telefono) VALUES (?, ?, 'móvil')";
        String sqlDir = "INSERT INTO direccion (id_datos_personales, id_distrito, calle_avenida, numero, tipo_direccion) VALUES (?, ?, ?, 'S/N', 'fiscal')";
        String sqlProv = "INSERT INTO proveedor (id_datos_personales, ruc, razon_social) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);

            int idGeneradoDP = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlDP)) {
                ps.setString(1, contacto.getNombres());
                ps.setString(2, contacto.getApellidos());
                ps.setString(3, contacto.getDni());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idGeneradoDP = rs.getInt(1);
                }
            }

            if (idGeneradoDP <= 0) {
                con.rollback();
                return false;
            }

            if (contacto.getCorreoPrincipal() != null && !contacto.getCorreoPrincipal().trim().isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(sqlCorreo)) {
                    ps.setInt(1, idGeneradoDP);
                    ps.setString(2, contacto.getCorreoPrincipal().trim());
                    ps.executeUpdate();
                }
            }

            if (contacto.getTelefonoPrincipal() != null && !contacto.getTelefonoPrincipal().trim().isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(sqlTel)) {
                    ps.setInt(1, idGeneradoDP);
                    ps.setString(2, contacto.getTelefonoPrincipal().trim());
                    ps.executeUpdate();
                }
            }

            if (contacto.getIdDistrito() != null) {
                try (PreparedStatement ps = con.prepareStatement(sqlDir)) {
                    ps.setInt(1, idGeneradoDP);
                    ps.setInt(2, contacto.getIdDistrito());
                    ps.setString(3, contacto.getDireccionPrincipal() != null ? contacto.getDireccionPrincipal() : "Fiscal");
                    ps.executeUpdate();
                }
            }

            try (PreparedStatement psP = con.prepareStatement(sqlProv)) {
                psP.setInt(1, idGeneradoDP);
                psP.setString(2, proveedor.getRuc());
                psP.setString(3, proveedor.getRazonSocial());
                psP.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[ProveedorRepositoryImpl] Error al registrar proveedor: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    @Override
    public boolean actualizar(Proveedor proveedor, DatosPersonales contacto) {
        String sqlP = "UPDATE proveedor SET ruc = ?, razon_social = ?, fecha_actualizacion = now() WHERE id_proveedor = ?";
        String sqlDP = "UPDATE datos_personales SET nombres = ?, apellidos = ?, dni = ?, fecha_actualizacion = now() WHERE id_datos_personales = ?";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlP)) {
                ps.setString(1, proveedor.getRuc());
                ps.setString(2, proveedor.getRazonSocial());
                ps.setInt(3, proveedor.getIdProveedor());
                ps.executeUpdate();
            }

            if (proveedor.getIdDatosPersonales() != null && contacto != null) {
                try (PreparedStatement psDP = con.prepareStatement(sqlDP)) {
                    psDP.setString(1, contacto.getNombres());
                    psDP.setString(2, contacto.getApellidos());
                    psDP.setString(3, contacto.getDni());
                    psDP.setInt(4, proveedor.getIdDatosPersonales());
                    psDP.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[ProveedorRepositoryImpl] Error al actualizar proveedor: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE proveedor SET estado = 'Inactivo', fecha_actualizacion = now() WHERE id_proveedor = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        Proveedor p = new Proveedor();
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setIdDatosPersonales(rs.getInt("id_datos_personales"));
        p.setRuc(rs.getString("ruc"));
        p.setRazonSocial(rs.getString("razon_social"));
        p.setEstado(rs.getString("estado"));

        DatosPersonales dp = new DatosPersonales();
        dp.setIdDatosPersonales(rs.getInt("id_datos_personales"));
        dp.setNombres(rs.getString("nombres"));
        dp.setApellidos(rs.getString("apellidos"));
        dp.setDni(rs.getString("dni"));
        dp.setCorreoPrincipal(rs.getString("correo"));
        dp.setTelefonoPrincipal(rs.getString("numero_telefono"));
        dp.setDireccionPrincipal(rs.getString("calle_avenida"));

        int idDist = rs.getInt("id_distrito");
        if (!rs.wasNull()) {
            dp.setIdDistrito(idDist);
            dp.setDistrito(new Distrito(idDist, rs.getString("nombre_distrito")));
        }

        p.setContactoPersona(dp);
        return p;
    }
}
