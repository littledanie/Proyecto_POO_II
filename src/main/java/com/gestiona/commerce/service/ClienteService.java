package com.gestiona.commerce.service;

import com.gestiona.commerce.model.entity.Cliente;
import com.gestiona.commerce.model.entity.DatosPersonales;
import com.gestiona.commerce.model.repository.ClienteRepository;
import com.gestiona.commerce.model.repository.ClienteRepositoryImpl;
import java.util.List;

/**
 * Servicio de negocio para la gestión de Clientes.
 */
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepositoryImpl();
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarPorId(int id) {
        return clienteRepository.buscarPorId(id);
    }

    public String registrar(Cliente cliente, DatosPersonales dp) {
        if ("Empresa".equalsIgnoreCase(cliente.getTipoCliente())) {
            if (cliente.getRuc() == null || cliente.getRuc().trim().length() != 11) {
                return "Para clientes con razón social o empresa, el RUC debe tener exactamente 11 dígitos.";
            }
            if (cliente.getRazonSocial() == null || cliente.getRazonSocial().trim().isEmpty()) {
                return "La razón social es obligatoria para clientes tipo Empresa.";
            }
        }
        if (dp == null || dp.getNombres() == null || dp.getNombres().trim().isEmpty()) {
            return "El nombre o contacto del cliente es obligatorio.";
        }

        boolean ok = clienteRepository.registrar(cliente, dp);
        return ok ? null : "Error al registrar cliente en base de datos.";
    }

    public String actualizar(Cliente cliente, DatosPersonales dp) {
        if ("Empresa".equalsIgnoreCase(cliente.getTipoCliente())) {
            if (cliente.getRuc() == null || cliente.getRuc().trim().length() != 11) {
                return "El RUC debe tener 11 dígitos.";
            }
        }
        boolean ok = clienteRepository.actualizar(cliente, dp);
        return ok ? null : "Error al actualizar cliente.";
    }

    public boolean eliminarLogico(int id) {
        return clienteRepository.eliminarLogico(id);
    }
}
