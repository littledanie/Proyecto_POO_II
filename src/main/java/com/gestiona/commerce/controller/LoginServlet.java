package com.gestiona.commerce.controller;

import com.gestiona.commerce.service.LoginService;
import com.gestiona.commerce.service.ResultadoLogin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controlador de Autenticación (LoginServlet - Etapa 3).
 * 
 * Gestiona el flujo de autenticación contra la base de datos PostgreSQL en Supabase,
 * delegando la validación a LoginService (BCrypt + límite de 3 intentos).
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/bienvenida");
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            // 2. Ejecución de la autenticación mediante LoginService
            ResultadoLogin resultado = loginService.autenticar(usuario.trim(), contrasena);

            if (resultado.isExitoso()) {
                // Autenticación correcta: creación de sesión y redirección PRG
                HttpSession session = request.getSession(true);
                session.setAttribute("usuarioLogueado", resultado.getUsuario());
                response.sendRedirect(request.getContextPath() + "/bienvenida");

            } else if (resultado.isBloqueado()) {
                // Usuario bloqueado por superar 3 intentos erróneos
                request.setAttribute("error", resultado.getMensaje());
                request.setAttribute("bloqueado", true);
                request.getRequestDispatcher("/index.jsp").forward(request, response);

            } else {
                // Credenciales incorrectas
                request.setAttribute("error", resultado.getMensaje());
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            System.err.println("[LoginServlet] Error al procesar autenticación: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("error", "El servicio no está disponible en este momento. Por favor intente más tarde.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
