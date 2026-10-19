package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.*;
import com.gestiona.commerce.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Controlador de Gestión de Usuarios (UsuarioServlet - CRUD Etapa 4).
 * 
 * Atiende las operaciones del dashboard administrativo para listar, registrar,
 * editar, desactivar (lógicamente) y desbloquear cuentas de usuarios.
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Verificación de sesión activa
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "nuevo":
                cargarCatalogos(request);
                request.setAttribute("esEdicion", false);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/usuarios-form.jsp").forward(request, response);
                break;

            case "editar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Usuario u = usuarioService.buscarPorId(id);
                    if (u != null) {
                        cargarCatalogos(request);
                        request.setAttribute("usuarioEditar", u);
                        request.setAttribute("esEdicion", true);
                        request.getRequestDispatcher("/WEB-INF/views/dashboard/usuarios-form.jsp").forward(request, response);
                    } else {
                        request.getSession().setAttribute("errorMensaje", "El usuario solicitado no existe.");
                        response.sendRedirect(request.getContextPath() + "/usuarios");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/usuarios");
                }
                break;

            case "desbloquear":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    usuarioService.cambiarBloqueo(id, false);
                    request.getSession().setAttribute("exitoMensaje", "Cuenta desbloqueada exitosamente.");
                } catch (NumberFormatException ignored) {}
                response.sendRedirect(request.getContextPath() + "/usuarios");
                break;

            case "eliminar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    usuarioService.eliminarLogico(id);
                    request.getSession().setAttribute("exitoMensaje", "Usuario desactivado correctamente.");
                } catch (NumberFormatException ignored) {}
                response.sendRedirect(request.getContextPath() + "/usuarios");
                break;

            case "listar":
            default:
                List<Usuario> lista = usuarioService.listarTodos();
                request.setAttribute("usuarios", lista);
                long totalActivos = lista.stream().filter(u -> "Activo".equalsIgnoreCase(u.getEstado())).count();
                long totalBloqueadas = lista.stream().filter(Usuario::isBloqueado).count();
                int totalRoles = usuarioService.listarRoles().size();
                request.setAttribute("totalActivos", totalActivos);
                request.setAttribute("totalBloqueadas", totalBloqueadas);
                request.setAttribute("totalRoles", totalRoles);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/usuarios-lista.jsp").forward(request, response);
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
            procesarGuardado(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void procesarGuardado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("idUsuario");
        boolean esNuevo = (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim()));

        Usuario u = new Usuario();
        DatosPersonales dp = new DatosPersonales();

        try {
            // Datos de la cuenta
            u.setNombreUsuario(request.getParameter("nombreUsuario"));
            u.setIdRol(Integer.parseInt(request.getParameter("idRol")));
            String contrasena = request.getParameter("contrasena");

            // Datos Personales
            dp.setNombres(request.getParameter("nombres"));
            dp.setApellidos(request.getParameter("apellidos"));
            dp.setDni(request.getParameter("dni"));

            String fechaNacStr = request.getParameter("fechaNacimiento");
            if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
                dp.setFechaNacimiento(Date.valueOf(fechaNacStr.trim()));
            }

            String idSexoStr = request.getParameter("idSexo");
            if (idSexoStr != null && !idSexoStr.trim().isEmpty()) {
                dp.setIdSexo(Integer.parseInt(idSexoStr));
            }

            String idEstadoCivilStr = request.getParameter("idEstadoCivil");
            if (idEstadoCivilStr != null && !idEstadoCivilStr.trim().isEmpty()) {
                dp.setIdEstadoCivil(Integer.parseInt(idEstadoCivilStr));
            }

            // Contacto y ubicación
            dp.setCorreoPrincipal(request.getParameter("correo"));
            dp.setTelefonoPrincipal(request.getParameter("telefono"));
            dp.setDireccionPrincipal(request.getParameter("direccion"));

            String idDistritoStr = request.getParameter("idDistrito");
            if (idDistritoStr != null && !idDistritoStr.trim().isEmpty()) {
                dp.setIdDistrito(Integer.parseInt(idDistritoStr));
            }

            String errorMsg = null;
            if (esNuevo) {
                errorMsg = usuarioService.registrarUsuario(u, dp, contrasena);
            } else {
                u.setIdUsuario(Integer.parseInt(idStr));
                String idDPStr = request.getParameter("idDatosPersonales");
                if (idDPStr != null && !idDPStr.trim().isEmpty()) {
                    u.setIdDatosPersonales(Integer.parseInt(idDPStr));
                }
                errorMsg = usuarioService.actualizarUsuario(u, dp, contrasena);
            }

            if (errorMsg != null) {
                // Reenviar al formulario con mensaje de error
                cargarCatalogos(request);
                request.setAttribute("errorMensaje", errorMsg);
                request.setAttribute("usuarioEditar", u);
                request.setAttribute("esEdicion", !esNuevo);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/usuarios-form.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("exitoMensaje", esNuevo ? 
                        "Usuario registrado con éxito en el sistema." : "Usuario actualizado correctamente.");
                response.sendRedirect(request.getContextPath() + "/usuarios");
            }

        } catch (Exception e) {
            System.err.println("[UsuarioServlet] Error al procesar formulario de usuario: " + e.getMessage());
            cargarCatalogos(request);
            request.setAttribute("errorMensaje", "Error al procesar la solicitud. Por favor verifique los datos ingresados.");
            request.setAttribute("esEdicion", !esNuevo);
            request.getRequestDispatcher("/WEB-INF/views/dashboard/usuarios-form.jsp").forward(request, response);
        }
    }

    private void cargarCatalogos(HttpServletRequest request) {
        request.setAttribute("roles", usuarioService.listarRoles());
        request.setAttribute("sexos", usuarioService.listarSexos());
        request.setAttribute("estadosCiviles", usuarioService.listarEstadosCiviles());
        request.setAttribute("distritos", usuarioService.listarDistritos());
    }
}
