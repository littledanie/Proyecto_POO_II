package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC del Patrón Repository para Producto / Mercadería.
 */
public class ProductoRepositoryImpl implements ProductoRepository {

    private Connection getConexion() throws SQLException {
        return ConexionBD.getInstancia().getConexion();
    }

    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.descripcion, p.tipo, " +
                     "       p.id_categoria, cat.nombre_categoria, " +
                     "       p.id_marca, m.nombre_marca, " +
                     "       p.id_unidad_medida, um.nombre AS um_nombre, um.abreviatura AS um_abrev, " +
                     "       p.precio_compra, p.precio_venta, p.stock_actual, p.stock_minimo, p.estado " +
                     "FROM producto p " +
                     "JOIN categoria cat ON p.id_categoria = cat.id_categoria " +
                     "LEFT JOIN marca m ON p.id_marca = m.id_marca " +
                     "JOIN unidad_medida um ON p.id_unidad_medida = um.id_unidad_medida " +
                     "ORDER BY p.id_producto ASC";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.descripcion, p.tipo, " +
                     "       p.id_categoria, cat.nombre_categoria, " +
                     "       p.id_marca, m.nombre_marca, " +
                     "       p.id_unidad_medida, um.nombre AS um_nombre, um.abreviatura AS um_abrev, " +
                     "       p.precio_compra, p.precio_venta, p.stock_actual, p.stock_minimo, p.estado " +
                     "FROM producto p " +
                     "JOIN categoria cat ON p.id_categoria = cat.id_categoria " +
                     "LEFT JOIN marca m ON p.id_marca = m.id_marca " +
                     "JOIN unidad_medida um ON p.id_unidad_medida = um.id_unidad_medida " +
                     "WHERE p.id_producto = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearProducto(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registrar(Producto p) {
        String sql = "INSERT INTO producto (codigo, nombre, descripcion, tipo, id_categoria, id_marca, " +
                     "                     id_unidad_medida, precio_compra, precio_venta, stock_actual, stock_minimo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getTipo());
            ps.setInt(5, p.getIdCategoria());
            if (p.getIdMarca() != null) ps.setInt(6, p.getIdMarca()); else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, p.getIdUnidadMedida());
            ps.setBigDecimal(8, p.getPrecioCompra());
            ps.setBigDecimal(9, p.getPrecioVenta());
            ps.setInt(10, p.getStockActual());
            ps.setInt(11, p.getStockMinimo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoRepositoryImpl] Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, descripcion = ?, tipo = ?, " +
                     "                   id_categoria = ?, id_marca = ?, id_unidad_medida = ?, " +
                     "                   precio_compra = ?, precio_venta = ?, stock_actual = ?, " +
                     "                   stock_minimo = ?, fecha_actualizacion = now() " +
                     "WHERE id_producto = ?";

        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setString(4, p.getTipo());
            ps.setInt(5, p.getIdCategoria());
            if (p.getIdMarca() != null) ps.setInt(6, p.getIdMarca()); else ps.setNull(6, Types.INTEGER);
            ps.setInt(7, p.getIdUnidadMedida());
            ps.setBigDecimal(8, p.getPrecioCompra());
            ps.setBigDecimal(9, p.getPrecioVenta());
            ps.setInt(10, p.getStockActual());
            ps.setInt(11, p.getStockMinimo());
            ps.setInt(12, p.getIdProducto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoRepositoryImpl] Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        String sql = "UPDATE producto SET estado = 'Inactivo', fecha_actualizacion = now() WHERE id_producto = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean ajustarStock(int id, int nuevoStock) {
        String sql = "UPDATE producto SET stock_actual = ?, fecha_actualizacion = now() WHERE id_producto = ?";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Categoria> listarCategorias() {
        List<Categoria> list = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre_categoria, descripcion FROM categoria WHERE estado = 'Activo' ORDER BY nombre_categoria";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre_categoria"), rs.getString("descripcion")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Marca> listarMarcas() {
        List<Marca> list = new ArrayList<>();
        String sql = "SELECT id_marca, nombre_marca FROM marca WHERE estado = 'Activo' ORDER BY nombre_marca";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Marca(rs.getInt("id_marca"), rs.getString("nombre_marca")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<UnidadMedida> listarUnidadesMedida() {
        List<UnidadMedida> list = new ArrayList<>();
        String sql = "SELECT id_unidad_medida, nombre, abreviatura FROM unidad_medida WHERE estado = 'Activo' ORDER BY nombre";
        try (Connection con = getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new UnidadMedida(rs.getInt("id_unidad_medida"), rs.getString("nombre"), rs.getString("abreviatura")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipo(rs.getString("tipo"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setCategoria(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre_categoria"), null));

        int idMarca = rs.getInt("id_marca");
        if (!rs.wasNull()) {
            p.setIdMarca(idMarca);
            p.setMarca(new Marca(idMarca, rs.getString("nombre_marca")));
        }

        p.setIdUnidadMedida(rs.getInt("id_unidad_medida"));
        p.setUnidadMedida(new UnidadMedida(rs.getInt("id_unidad_medida"), rs.getString("um_nombre"), rs.getString("um_abrev")));

        p.setPrecioCompra(rs.getBigDecimal("precio_compra"));
        p.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
