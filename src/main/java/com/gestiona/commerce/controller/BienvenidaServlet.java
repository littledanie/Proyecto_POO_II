package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.Producto;
import com.gestiona.commerce.model.entity.Usuario;
import com.gestiona.commerce.service.ClienteService;
import com.gestiona.commerce.service.ProductoService;
import com.gestiona.commerce.service.ProveedorService;
import com.gestiona.commerce.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controlador de Bienvenida / Dashboard principal.
 */
@WebServlet(name = "BienvenidaServlet", urlPatterns = {"/bienvenida"})
public class BienvenidaServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final ClienteService clienteService = new ClienteService();
    private final ProveedorService proveedorService = new ProveedorService();
    private final ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        // Cargar métricas e indicadores de negocio según permisos
        if (usuario.tienePermiso("GESTIONAR_USUARIOS")) {
            long totalUsuarios = usuarioService.listarTodos().stream()
                    .filter(u -> "Activo".equalsIgnoreCase(u.getEstado())).count();
            request.setAttribute("totalUsuariosActivos", totalUsuarios);
        }

        if (usuario.tienePermiso("VER_CLIENTES") || usuario.tienePermiso("GESTIONAR_CLIENTES")) {
            long totalClientes = clienteService.listarTodos().stream()
                    .filter(c -> "Activo".equalsIgnoreCase(c.getEstado())).count();
            request.setAttribute("totalClientesActivos", totalClientes);
        }

        if (usuario.tienePermiso("VER_PROVEEDORES") || usuario.tienePermiso("GESTIONAR_PROVEEDORES")) {
            long totalProveedores = proveedorService.listarTodos().stream()
                    .filter(p -> "Activo".equalsIgnoreCase(p.getEstado())).count();
            request.setAttribute("totalProveedoresActivos", totalProveedores);
        }

        if (usuario.tienePermiso("VER_PRODUCTOS") || usuario.tienePermiso("GESTIONAR_PRODUCTOS")) {
            List<Producto> todosProductos = productoService.listarTodos();
            long totalProductos = todosProductos.stream()
                    .filter(p -> "Activo".equalsIgnoreCase(p.getEstado())).count();
            request.setAttribute("totalProductosActivos", totalProductos);

            List<Producto> criticos = productoService.listarStockCritico();
            request.setAttribute("productosStockCritico", criticos);
            request.setAttribute("totalStockCritico", criticos.size());
        }

        request.getRequestDispatcher("/WEB-INF/views/bienvenida.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
