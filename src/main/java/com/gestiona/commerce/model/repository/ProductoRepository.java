package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.Categoria;
import com.gestiona.commerce.model.entity.Marca;
import com.gestiona.commerce.model.entity.Producto;
import com.gestiona.commerce.model.entity.UnidadMedida;
import java.util.List;

/**
 * Interfaz Repository para productos, mercadería, insumos y servicios.
 */
public interface ProductoRepository {
    List<Producto> listarTodos();
    Producto buscarPorId(int id);
    boolean registrar(Producto producto);
    boolean actualizar(Producto producto);
    boolean eliminarLogico(int id);
    boolean ajustarStock(int id, int nuevoStock);

    List<Categoria> listarCategorias();
    List<Marca> listarMarcas();
    List<UnidadMedida> listarUnidadesMedida();
}
