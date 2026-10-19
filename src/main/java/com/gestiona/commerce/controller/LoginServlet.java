package com.gestiona.commerce.controller;

import com.gestiona.commerce.model.entity.Usuario;
import com.gestiona.commerce.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controlador de autenticación (LoginServlet).
 * 
 * Gestiona las solicitudes de inicio de sesión, validando credenciales
 * contra la base de datos PostgreSQL en Supabase.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si el usuario ya tiene sesión activa, redirigir a bienvenida
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/bienvenida");
            return;
        }

        // Si no tiene sesión, mostrar el formulario de login
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Configurar codificación de caracteres
        request.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        // 1. Validación de campos obligatorios
        if (usuario == null || usuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        try {
            // 2. Consulta de autenticación mediante el servicio
            Usuario usuarioValido = loginService.autenticar(usuario.trim(), contrasena);

            if (usuarioValido != null) {
                // Autenticación exitosa: crear sesión HTTP
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", usuarioValido);

                // Redirección PRG (Post-Redirect-Get) hacia la ruta controlada de bienvenida
                response.sendRedirect(request.getContextPath() + "/bienvenida");
            } else {
                // Credenciales incorrectas: mensaje genérico por seguridad (AGENTS.md §7)
                request.setAttribute("error", "Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            // Error de conexión a la BD: se registra el detalle en el log del servidor
            System.err.println("[LoginServlet] Error al conectar o consultar la base de datos: " + e.getMessage());
            e.printStackTrace();

            // Mensaje amigable y seguro hacia el usuario final
            request.setAttribute("error", "No se pudo conectar con la base de datos. Verifique la conexión o intente nuevamente.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
