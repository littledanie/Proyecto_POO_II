package com.gestiona.commerce.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador de Bienvenida (BienvenidaServlet).
 * 
 * Protege la vista interna /WEB-INF/views/bienvenida.jsp.
 * Solo permite el acceso si existe una sesión HTTP activa con un usuario autenticado;
 * en caso contrario, redirige al formulario de inicio de sesión.
 */
@WebServlet(name = "BienvenidaServlet", urlPatterns = {"/bienvenida"})
public class BienvenidaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Verificar si existe sesión y si el usuario está autenticado
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            // Usuario no autenticado: redirigir al login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Usuario autenticado: hacer FORWARD a la vista protegida en WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/bienvenida.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
