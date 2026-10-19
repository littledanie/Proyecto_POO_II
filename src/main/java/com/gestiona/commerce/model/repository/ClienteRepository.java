package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.Cliente;
import com.gestiona.commerce.model.entity.DatosPersonales;
import java.util.List;

/**
 * Interfaz Repository para la entidad comercial Cliente.
 */
public interface ClienteRepository {
    List<Cliente> listarTodos();
    Cliente buscarPorId(int id);
    boolean registrar(Cliente cliente, DatosPersonales dp);
    boolean actualizar(Cliente cliente, DatosPersonales dp);
    boolean eliminarLogico(int id);
}
