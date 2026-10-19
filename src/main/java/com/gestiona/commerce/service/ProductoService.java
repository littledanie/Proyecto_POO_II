package com.gestiona.commerce.service;

import com.gestiona.commerce.model.entity.Categoria;
import com.gestiona.commerce.model.entity.Marca;
import com.gestiona.commerce.model.entity.Producto;
import com.gestiona.commerce.model.entity.UnidadMedida;
import com.gestiona.commerce.model.repository.ProductoRepository;
import com.gestiona.commerce.model.repository.ProductoRepositoryImpl;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio de negocio para la gestión de Productos, Insumos y Servicios.
 */
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService() {
        this.productoRepository = new ProductoRepositoryImpl();
    }

    public List<Producto> listarTodos() {
        return productoRepository.listarTodos();
    }

    public List<Producto> listarStockCritico() {
        return productoRepository.listarStockCritico();
    }

    public Producto buscarPorId(int id) {
        return productoRepository.buscarPorId(id);
    }

    public String registrar(Producto p) {
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) {
            return "El código de producto es obligatorio.";
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            return "El nombre del producto es obligatorio.";
        }
        if (p.getPrecioVenta() == null || p.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            return "El precio de venta debe ser mayor o igual a 0.";
        }
        if (p.getIdCategoria() == null || p.getIdCategoria() <= 0) {
            return "Debe seleccionar una categoría.";
        }
        if (p.getIdUnidadMedida() == null || p.getIdUnidadMedida() <= 0) {
            return "Debe seleccionar una unidad de medida.";
        }

        boolean ok = productoRepository.registrar(p);
        return ok ? null : "Error al registrar producto en la base de datos.";
    }

    public String actualizar(Producto p) {
        if (p.getIdProducto() == null || p.getIdProducto() <= 0) {
            return "Identificador de producto inválido.";
        }
        boolean ok = productoRepository.actualizar(p);
        return ok ? null : "Error al actualizar producto.";
    }

    public boolean eliminarLogico(int id) {
        return productoRepository.eliminarLogico(id);
    }

    public boolean ajustarStock(int id, int nuevoStock) {
        return productoRepository.ajustarStock(id, nuevoStock);
    }

    public List<Categoria> listarCategorias() {
        return productoRepository.listarCategorias();
    }

    public List<Marca> listarMarcas() {
        return productoRepository.listarMarcas();
    }

    public List<UnidadMedida> listarUnidadesMedida() {
        return productoRepository.listarUnidadesMedida();
    }
}
