package com.gestiona.commerce.filter;

import com.gestiona.commerce.model.entity.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro de seguridad y autorización granular por permisos.
 * Intercepta las solicitudes a módulos protegidos y valida:
 * 1. Existencia de sesión activa y usuario autenticado.
 * 2. Permisos específicos requeridos para operaciones de consulta y modificación.
 * 3. Registra en el log del servidor cualquier intento de acceso no autorizado.
 */
public class FiltroAutorizacion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro si fuera requerida
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        // 1. Excluir rutas públicas y recursos estáticos
        if (path == null || path.equals("/") || path.equals("/index.jsp") || path.equals("/login")
                || path.equals("/logout") || path.startsWith("/assets") || path.startsWith("/WEB-INF/views/error")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Validar sesión activa
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            // Sin sesión: redirigir al login
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Si la ruta es bienvenida (dashboard), solo requiere autenticación
        if (path.equals("/bienvenida") || path.equals("/bienvenida/")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Determinar módulo y tipo de acción (consulta vs modificación)
        String accion = req.getParameter("accion");
        boolean esModificacion = !"GET".equalsIgnoreCase(req.getMethod())
                || (accion != null && !accion.trim().isEmpty() && !"listar".equalsIgnoreCase(accion.trim()));

        boolean autorizado = false;
        String permisoRequerido = "";

        if (path.startsWith("/usuarios")) {
            permisoRequerido = "GESTIONAR_USUARIOS";
            autorizado = usuario.tienePermiso(permisoRequerido);

        } else if (path.startsWith("/clientes")) {
            if (esModificacion) {
                permisoRequerido = "GESTIONAR_CLIENTES";
                autorizado = usuario.tienePermiso(permisoRequerido);
            } else {
                permisoRequerido = "VER_CLIENTES o GESTIONAR_CLIENTES";
                autorizado = usuario.tienePermiso("VER_CLIENTES") || usuario.tienePermiso("GESTIONAR_CLIENTES");
            }

        } else if (path.startsWith("/proveedores")) {
            if (esModificacion) {
                permisoRequerido = "GESTIONAR_PROVEEDORES";
                autorizado = usuario.tienePermiso(permisoRequerido);
            } else {
                permisoRequerido = "VER_PROVEEDORES o GESTIONAR_PROVEEDORES";
                autorizado = usuario.tienePermiso("VER_PROVEEDORES") || usuario.tienePermiso("GESTIONAR_PROVEEDORES");
            }

        } else if (path.startsWith("/productos")) {
            if (esModificacion) {
                permisoRequerido = "GESTIONAR_PRODUCTOS";
                autorizado = usuario.tienePermiso(permisoRequerido);
            } else {
                permisoRequerido = "VER_PRODUCTOS o GESTIONAR_PRODUCTOS";
                autorizado = usuario.tienePermiso("VER_PRODUCTOS") || usuario.tienePermiso("GESTIONAR_PRODUCTOS");
            }

        } else {
            // Rutas no mapeadas explícitamente se permiten pasar
            autorizado = true;
        }

        // 4. Si no cumple los permisos, registrar en log del servidor y responder 403
        if (!autorizado) {
            String queryString = req.getQueryString();
            String uriCompleta = path + (queryString != null ? "?" + queryString : "");
            String nombreRol = (usuario.getRol() != null) ? usuario.getRol().getNombreRol() : "Sin Rol";

            System.err.println("[FiltroAutorizacion] Acceso DENEGADO - Usuario: '" + usuario.getNombreUsuario()
                    + "' (Rol: " + nombreRol + ") | Ruta: " + uriCompleta
                    + " | Método: " + req.getMethod()
                    + " | Permiso requerido: [" + permisoRequerido + "]");

            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Permiso concedido: continuar la cadena
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza de recursos si fuera requerida
    }
}
