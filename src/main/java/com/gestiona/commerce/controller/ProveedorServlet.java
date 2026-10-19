package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.DatosPersonales;
import com.gestiona.commerce.model.entity.Proveedor;
import com.gestiona.commerce.service.ProveedorService;
import com.gestiona.commerce.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para el CRUD de Proveedores (Etapa 5).
 */
@WebServlet(name = "ProveedorServlet", urlPatterns = {"/proveedores"})
public class ProveedorServlet extends HttpServlet {

    private final ProveedorService proveedorService = new ProveedorService();
    private final UsuarioService usuarioService = new UsuarioService();

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
                request.setAttribute("distritos", usuarioService.listarDistritos());
                request.setAttribute("esEdicion", false);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/proveedores-form.jsp").forward(request, response);
                break;

            case "editar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Proveedor p = proveedorService.buscarPorId(id);
                    if (p != null) {
                        request.setAttribute("distritos", usuarioService.listarDistritos());
                        request.setAttribute("proveedorEditar", p);
                        request.setAttribute("esEdicion", true);
                        request.getRequestDispatcher("/WEB-INF/views/dashboard/proveedores-form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/proveedores");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/proveedores");
                }
                break;

            case "eliminar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    proveedorService.eliminarLogico(id);
                    request.getSession().setAttribute("exitoMensaje", "Proveedor desactivado correctamente.");
                } catch (NumberFormatException ignored) {}
                response.sendRedirect(request.getContextPath() + "/proveedores");
                break;

            case "listar":
            default:
                request.setAttribute("proveedores", proveedorService.listarTodos());
                request.getRequestDispatcher("/WEB-INF/views/dashboard/proveedores-lista.jsp").forward(request, response);
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
            String idStr = request.getParameter("idProveedor");
            boolean esNuevo = (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim()));

            Proveedor p = new Proveedor();
            DatosPersonales dp = new DatosPersonales();

            p.setRuc(request.getParameter("ruc"));
            p.setRazonSocial(request.getParameter("razonSocial"));

            dp.setNombres(request.getParameter("contactoNombre"));
            dp.setApellidos(request.getParameter("contactoApellido"));
            dp.setDni(request.getParameter("contactoDni"));
            dp.setCorreoPrincipal(request.getParameter("correo"));
            dp.setTelefonoPrincipal(request.getParameter("telefono"));
            dp.setDireccionPrincipal(request.getParameter("direccion"));

            String idDistStr = request.getParameter("idDistrito");
            if (idDistStr != null && !idDistStr.trim().isEmpty()) {
                dp.setIdDistrito(Integer.parseInt(idDistStr));
            }

            String error;
            if (esNuevo) {
                error = proveedorService.registrar(p, dp);
            } else {
                p.setIdProveedor(Integer.parseInt(idStr));
                String idDPStr = request.getParameter("idDatosPersonales");
                if (idDPStr != null && !idDPStr.trim().isEmpty()) {
                    p.setIdDatosPersonales(Integer.parseInt(idDPStr));
                }
                error = proveedorService.actualizar(p, dp);
            }

            if (error != null) {
                request.setAttribute("distritos", usuarioService.listarDistritos());
                request.setAttribute("errorMensaje", error);
                request.setAttribute("proveedorEditar", p);
                request.setAttribute("esEdicion", !esNuevo);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/proveedores-form.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("exitoMensaje", esNuevo ? "Proveedor registrado exitosamente." : "Proveedor actualizado.");
                response.sendRedirect(request.getContextPath() + "/proveedores");
            }
        } else {
            doGet(request, response);
        }
    }
}
