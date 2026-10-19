package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.Producto;
import com.gestiona.commerce.service.ProductoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Controlador para el CRUD de Productos, Insumos y Servicios (Etapa 5).
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private final ProductoService productoService = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                cargarCatalogos(request);
                request.setAttribute("esEdicion", false);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/productos-form.jsp").forward(request, response);
                break;

            case "editar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Producto p = productoService.buscarPorId(id);
                    if (p != null) {
                        cargarCatalogos(request);
                        request.setAttribute("productoEditar", p);
                        request.setAttribute("esEdicion", true);
                        request.getRequestDispatcher("/WEB-INF/views/dashboard/productos-form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/productos");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/productos");
                }
                break;

            case "eliminar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    productoService.eliminarLogico(id);
                    request.getSession().setAttribute("exitoMensaje", "Producto desactivado lógicamente (Inactivo).");
                } catch (NumberFormatException ignored) {}
                response.sendRedirect(request.getContextPath() + "/productos");
                break;

            case "listar":
            default:
                request.setAttribute("productos", productoService.listarTodos());
                request.getRequestDispatcher("/WEB-INF/views/dashboard/productos-lista.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if ("guardar".equals(accion)) {
            String idStr = request.getParameter("idProducto");
            boolean esNuevo = (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim()));

            Producto p = new Producto();
            p.setCodigo(request.getParameter("codigo"));
            p.setNombre(request.getParameter("nombre"));
            p.setDescripcion(request.getParameter("descripcion"));
            p.setTipo(request.getParameter("tipo"));
            p.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));

            String idMarcaStr = request.getParameter("idMarca");
            if (idMarcaStr != null && !idMarcaStr.trim().isEmpty()) {
                p.setIdMarca(Integer.parseInt(idMarcaStr));
            }

            p.setIdUnidadMedida(Integer.parseInt(request.getParameter("idUnidadMedida")));

            String pCompraStr = request.getParameter("precioCompra");
            p.setPrecioCompra(pCompraStr != null && !pCompraStr.trim().isEmpty() ? new BigDecimal(pCompraStr.trim()) : BigDecimal.ZERO);

            String pVentaStr = request.getParameter("precioVenta");
            p.setPrecioVenta(pVentaStr != null && !pVentaStr.trim().isEmpty() ? new BigDecimal(pVentaStr.trim()) : BigDecimal.ZERO);

            String stockActStr = request.getParameter("stockActual");
            p.setStockActual(stockActStr != null && !stockActStr.trim().isEmpty() ? Integer.parseInt(stockActStr.trim()) : 0);

            String stockMinStr = request.getParameter("stockMinimo");
            p.setStockMinimo(stockMinStr != null && !stockMinStr.trim().isEmpty() ? Integer.parseInt(stockMinStr.trim()) : 0);

            String error;
            if (esNuevo) {
                error = productoService.registrar(p);
            } else {
                p.setIdProducto(Integer.parseInt(idStr));
                error = productoService.actualizar(p);
            }

            if (error != null) {
                cargarCatalogos(request);
                request.setAttribute("errorMensaje", error);
                request.setAttribute("productoEditar", p);
                request.setAttribute("esEdicion", !esNuevo);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/productos-form.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("exitoMensaje", esNuevo ? "Producto registrado exitosamente." : "Producto actualizado.");
                response.sendRedirect(request.getContextPath() + "/productos");
            }
        } else {
            doGet(request, response);
        }
    }

    private void cargarCatalogos(HttpServletRequest request) {
        request.setAttribute("categorias", productoService.listarCategorias());
        request.setAttribute("marcas", productoService.listarMarcas());
        request.setAttribute("unidadesMedida", productoService.listarUnidadesMedida());
    }
}
