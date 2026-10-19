package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.Cliente;
import com.gestiona.commerce.model.entity.DatosPersonales;
import com.gestiona.commerce.service.ClienteService;
import com.gestiona.commerce.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para el CRUD de Clientes (Etapa 5).
 */
@WebServlet(name = "ClienteServlet", urlPatterns = {"/clientes"})
public class ClienteServlet extends HttpServlet {

    private final ClienteService clienteService = new ClienteService();
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
                request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-form.jsp").forward(request, response);
                break;

            case "editar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Cliente c = clienteService.buscarPorId(id);
                    if (c != null) {
                        request.setAttribute("distritos", usuarioService.listarDistritos());
                        request.setAttribute("clienteEditar", c);
                        request.setAttribute("esEdicion", true);
                        request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-form.jsp").forward(request, response);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/clientes");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/clientes");
                }
                break;

            case "eliminar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    clienteService.eliminarLogico(id);
                    request.getSession().setAttribute("exitoMensaje", "Cliente desactivado correctamente.");
                } catch (NumberFormatException ignored) {}
                response.sendRedirect(request.getContextPath() + "/clientes");
                break;

            case "listar":
            default:
                request.setAttribute("clientes", clienteService.listarTodos());
                request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-lista.jsp").forward(request, response);
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
            String idStr = request.getParameter("idCliente");
            boolean esNuevo = (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim()));

            Cliente c = new Cliente();
            DatosPersonales dp = new DatosPersonales();

            c.setTipoCliente(request.getParameter("tipoCliente"));
            c.setRuc(request.getParameter("ruc"));
            c.setRazonSocial(request.getParameter("razonSocial"));

            dp.setNombres(request.getParameter("nombres"));
            dp.setApellidos(request.getParameter("apellidos"));
            dp.setDni(request.getParameter("dni"));
            dp.setCorreoPrincipal(request.getParameter("correo"));
            dp.setTelefonoPrincipal(request.getParameter("telefono"));
            dp.setDireccionPrincipal(request.getParameter("direccion"));

            String idDistStr = request.getParameter("idDistrito");
            if (idDistStr != null && !idDistStr.trim().isEmpty()) {
                dp.setIdDistrito(Integer.parseInt(idDistStr));
            }

            String error;
            if (esNuevo) {
                error = clienteService.registrar(c, dp);
            } else {
                c.setIdCliente(Integer.parseInt(idStr));
                String idDPStr = request.getParameter("idDatosPersonales");
                if (idDPStr != null && !idDPStr.trim().isEmpty()) {
                    c.setIdDatosPersonales(Integer.parseInt(idDPStr));
                }
                error = clienteService.actualizar(c, dp);
            }

            if (error != null) {
                request.setAttribute("distritos", usuarioService.listarDistritos());
                request.setAttribute("errorMensaje", error);
                request.setAttribute("clienteEditar", c);
                request.setAttribute("esEdicion", !esNuevo);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-form.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("exitoMensaje", esNuevo ? "Cliente registrado exitosamente." : "Cliente actualizado.");
                response.sendRedirect(request.getContextPath() + "/clientes");
            }
        } else {
            doGet(request, response);
        }
    }
}
