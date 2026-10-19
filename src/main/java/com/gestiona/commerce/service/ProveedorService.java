package com.gestiona.commerce.service;

import com.gestiona.commerce.model.entity.DatosPersonales;
import com.gestiona.commerce.model.entity.Proveedor;
import com.gestiona.commerce.model.repository.ProveedorRepository;
import com.gestiona.commerce.model.repository.ProveedorRepositoryImpl;
import java.util.List;

/**
 * Servicio de negocio para la gestión de Proveedores.
 */
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService() {
        this.proveedorRepository = new ProveedorRepositoryImpl();
    }

    public List<Proveedor> listarTodos() {
        return proveedorRepository.listarTodos();
    }

    public Proveedor buscarPorId(int id) {
        return proveedorRepository.buscarPorId(id);
    }

    public String registrar(Proveedor proveedor, DatosPersonales contacto) {
        if (proveedor.getRuc() == null || proveedor.getRuc().trim().length() != 11) {
            return "El RUC del proveedor es obligatorio y debe tener 11 dígitos.";
        }
        if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty()) {
            return "La razón social del proveedor es obligatoria.";
        }
        if (contacto == null || contacto.getNombres() == null || contacto.getNombres().trim().isEmpty()) {
            return "El nombre de la persona de contacto es obligatorio.";
        }

        boolean ok = proveedorRepository.registrar(proveedor, contacto);
        return ok ? null : "Error al registrar el proveedor en la base de datos.";
    }

    public String actualizar(Proveedor proveedor, DatosPersonales contacto) {
        if (proveedor.getRuc() == null || proveedor.getRuc().trim().length() != 11) {
            return "El RUC debe tener 11 dígitos.";
        }
        boolean ok = proveedorRepository.actualizar(proveedor, contacto);
        return ok ? null : "Error al actualizar proveedor.";
    }

    public boolean eliminarLogico(int id) {
        return proveedorRepository.eliminarLogico(id);
    }
}
