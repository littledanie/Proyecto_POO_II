package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC del Patrón Repository para Cliente.
 */
public class ClienteRepositoryImpl implements ClienteRepository {

    private Connection getConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.id_cliente, c.id_datos_personales, c.tipo_cliente, c.ruc, c.razon_social, c.estado, " +
                     "       dp.nombres, dp.apellidos, dp.dni, dp.fecha_nacimiento, dp.id_sexo, dp.id_estado_civil, " +
                     "       ce.correo, t.numero_telefono, dir.calle_avenida, d.id_distrito, d.nombre_distrito " +
                     "FROM cliente c " +
                     "JOIN datos_personales dp ON c.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "ORDER BY c.id_cliente ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ClienteRepositoryImpl] Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT c.id_cliente, c.id_datos_personales, c.tipo_cliente, c.ruc, c.razon_social, c.estado, " +
                     "       dp.nombres, dp.apellidos, dp.dni, dp.fecha_nacimiento, dp.id_sexo, dp.id_estado_civil, " +
                     "       ce.correo, t.numero_telefono, dir.calle_avenida, d.id_distrito, d.nombre_distrito " +
                     "FROM cliente c " +
                     "JOIN datos_personales dp ON c.id_datos_personales = dp.id_datos_personales " +
                     "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
                     "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
                     "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
                     "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
                     "WHERE c.id_cliente = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registrar(Cliente cliente, DatosPersonales dp) {
        String sqlDP = "INSERT INTO datos_personales (nombres, apellidos, dni, fecha_nacimiento, id_sexo, id_estado_civil) " +
                       "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_datos_personales";
        String sqlCorreo = "INSERT INTO correo_electronico (id_datos_personales, correo, tipo_correo) VALUES (?, ?, 'personal')";
        String sqlTel = "INSERT INTO telefono (id_datos_personales, numero_telefono, tipo_telefono) VALUES (?, ?, 'móvil')";
        String sqlDir = "INSERT INTO direccion (id_datos_personales, id_distrito, calle_avenida, numero, tipo_direccion) VALUES (?, ?, ?, 'S/N', 'domicilio')";
        String sqlCliente = "INSERT INTO cliente (id_datos_personales, tipo_cliente, ruc, razon_social) VALUES (?, ?, ?, ?)";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);

            int idGeneradoDP = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlDP)) {
                ps.setString(1, dp.getNombres());
                ps.setString(2, dp.getApellidos());
                ps.setString(3, dp.getDni());
                ps.setDate(4, dp.getFechaNacimiento());
                if (dp.getIdSexo() != null) ps.setInt(5, dp.getIdSexo()); else ps.setNull(5, Types.INTEGER);
                if (dp.getIdEstadoCivil() != null) ps.setInt(6, dp.getIdEstadoCivil()); else ps.setNull(6, Types.INTEGER);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) idGeneradoDP = rs.getInt(1);
                }
            }

            if (idGeneradoDP <= 0) {
                con.rollback();
                return false;
            }

            if (dp.getCorreoPrincipal() != null && !dp.getCorreoPrincipal().trim().isEmpty()) {
                try (PreparedStatement psC = con.prepareStatement(sqlCorreo)) {
                    psC.setInt(1, idGeneradoDP);
                    psC.setString(2, dp.getCorreoPrincipal().trim());
                    psC.executeUpdate();
                }
            }

            if (dp.getTelefonoPrincipal() != null && !dp.getTelefonoPrincipal().trim().isEmpty()) {
                try (PreparedStatement psT = con.prepareStatement(sqlTel)) {
                    psT.setInt(1, idGeneradoDP);
                    psT.setString(2, dp.getTelefonoPrincipal().trim());
                    psT.executeUpdate();
                }
            }

            if (dp.getIdDistrito() != null) {
                try (PreparedStatement psD = con.prepareStatement(sqlDir)) {
                    psD.setInt(1, idGeneradoDP);
                    psD.setInt(2, dp.getIdDistrito());
                    psD.setString(3, dp.getDireccionPrincipal() != null ? dp.getDireccionPrincipal() : "Principal");
                    psD.executeUpdate();
                }
            }

            try (PreparedStatement psCli = con.prepareStatement(sqlCliente)) {
                psCli.setInt(1, idGeneradoDP);
                psCli.setString(2, cliente.getTipoCliente());
                psCli.setString(3, cliente.getRuc());
                psCli.setString(4, cliente.getRazonSocial());
                psCli.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[ClienteRepositoryImpl] Error al registrar cliente: " + e.getMessage());
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
    public boolean actualizar(Cliente cliente, DatosPersonales dp) {
        String sqlCli = "UPDATE cliente SET tipo_cliente = ?, ruc = ?, razon_social = ?, fecha_actualizacion = now() WHERE id_cliente = ?";
        String sqlDP = "UPDATE datos_personales SET nombres = ?, apellidos = ?, dni = ?, fecha_nacimiento = ?, " +
                       "       id_sexo = ?, id_estado_civil = ?, fecha_actualizacion = now() WHERE id_datos_personales = ?";

        Connection con = null;
        try {
            con = getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psCli = con.prepareStatement(sqlCli)) {
                psCli.setString(1, cliente.getTipoCliente());
                psCli.setString(2, cliente.getRuc());
                psCli.setString(3, cliente.getRazonSocial());
                psCli.setInt(4, cliente.getIdCliente());
                psCli.executeUpdate();
            }

            if (cliente.getIdDatosPersonales() != null && dp != null) {
                try (PreparedStatement psDP = con.prepareStatement(sqlDP)) {
                    psDP.setString(1, dp.getNombres());
                    psDP.setString(2, dp.getApellidos());
                    psDP.setString(3, dp.getDni());
                    psDP.setDate(4, dp.getFechaNacimiento());
                    if (dp.getIdSexo() != null) psDP.setInt(5, dp.getIdSexo()); else psDP.setNull(5, Types.INTEGER);
                    if (dp.getIdEstadoCivil() != null) psDP.setInt(6, dp.getIdEstadoCivil()); else psDP.setNull(6, Types.INTEGER);
                    psDP.setInt(7, cliente.getIdDatosPersonales());
                    psDP.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("[ClienteRepositoryImpl] Error al actualizar cliente: " + e.getMessage());
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
        String sql = "UPDATE cliente SET estado = 'Inactivo', fecha_actualizacion = now() WHERE id_cliente = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setIdDatosPersonales(rs.getInt("id_datos_personales"));
        c.setTipoCliente(rs.getString("tipo_cliente"));
        c.setRuc(rs.getString("ruc"));
        c.setRazonSocial(rs.getString("razon_social"));
        c.setEstado(rs.getString("estado"));

        DatosPersonales dp = new DatosPersonales();
        dp.setIdDatosPersonales(rs.getInt("id_datos_personales"));
        dp.setNombres(rs.getString("nombres"));
        dp.setApellidos(rs.getString("apellidos"));
        dp.setDni(rs.getString("dni"));
        dp.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        dp.setIdSexo(rs.getInt("id_sexo"));
        dp.setIdEstadoCivil(rs.getInt("id_estado_civil"));
        dp.setCorreoPrincipal(rs.getString("correo"));
        dp.setTelefonoPrincipal(rs.getString("numero_telefono"));
        dp.setDireccionPrincipal(rs.getString("calle_avenida"));

        int idDist = rs.getInt("id_distrito");
        if (!rs.wasNull()) {
            dp.setIdDistrito(idDist);
            dp.setDistrito(new Distrito(idDist, rs.getString("nombre_distrito")));
        }

        c.setDatosPersonales(dp);
        return c;
    }
}
