package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.DatosPersonales;
import com.gestiona.commerce.model.entity.Proveedor;
import java.util.List;

/**
 * Interfaz Repository para la entidad comercial Proveedor.
 */
public interface ProveedorRepository {
    List<Proveedor> listarTodos();
    Proveedor buscarPorId(int id);
    boolean registrar(Proveedor proveedor, DatosPersonales contacto);
    boolean actualizar(Proveedor proveedor, DatosPersonales contacto);
    boolean eliminarLogico(int id);
}
