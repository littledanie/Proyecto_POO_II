<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Gestiona Commerce — Sistema web de gestión comercial. Inicia sesión para acceder al panel de administración.">
    <title>Iniciar Sesión — Gestiona Commerce</title>

    <!-- Tipografía: Inter de Google Fonts (profesional, limpia, ideal para sistemas de gestión) -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>

    <div class="login-layout">

        <!-- Panel izquierdo: branding e ilustración -->
        <aside class="login-brand" aria-hidden="true">
            <div class="brand-content">
                <div class="brand-logo">
                    <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                        <polyline points="9 22 9 12 15 12 15 22"/>
                    </svg>
                    <span class="brand-name">Gestiona Commerce</span>
                </div>
                <h2 class="brand-tagline">Tu negocio,<br>bajo control.</h2>
                <p class="brand-description">
                    Administra inventario, clientes y proveedores
                    desde un solo lugar. Simple, seguro y eficiente.
                </p>

                <!-- Indicadores decorativos de métricas -->
                <div class="brand-stats">
                    <div class="stat-item">
                        <span class="stat-value">+500</span>
                        <span class="stat-label">Productos</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value">120</span>
                        <span class="stat-label">Clientes</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-value">35</span>
                        <span class="stat-label">Proveedores</span>
                    </div>
                </div>
            </div>
        </aside>

        <!-- Panel derecho: formulario de login -->
        <main class="login-main">
            <div class="login-card">
                <!-- Logo para vista móvil (oculto en escritorio) -->
                <div class="mobile-logo">
                    <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                         stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                        <polyline points="9 22 9 12 15 12 15 22"/>
                    </svg>
                    <span class="brand-name">Gestiona Commerce</span>
                </div>

                <h1 class="login-title">Iniciar Sesión</h1>
                <p class="login-subtitle">Ingresa tus credenciales para acceder al sistema</p>

                <%-- Zona de mensajes de error (role="alert" para accesibilidad) --%>
                <c:if test="${not empty error}">
                    <div id="login-error" class="alert alert-error" role="alert" tabindex="-1">
                        <svg class="alert-icon" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                  d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0
                                     012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
                                  clip-rule="evenodd"/>
                        </svg>
                        <span><c:out value="${error}"/></span>
                    </div>
                </c:if>

                <c:if test="${bloqueado}">
                    <div class="alert alert-error" style="background-color: #fee2e2; border-color: #f87171; color: #991b1b; margin-bottom: 1.25rem;">
                        <strong>Cuenta Bloqueada:</strong> Se han agotado los 3 intentos permitidos. Por seguridad, la sesión y el formulario han sido deshabilitados.
                    </div>
                </c:if>

                <%-- Formulario de login --%>
                <form id="loginForm" action="${pageContext.request.contextPath}/login" method="POST"
                      novalidate autocomplete="on">

                    <div class="form-group">
                        <label for="usuario" class="form-label">Usuario</label>
                        <div class="input-wrapper">
                            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                      d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0
                                         0114 0H3z" clip-rule="evenodd"/>
                            </svg>
                            <input type="text"
                                   id="usuario"
                                   name="usuario"
                                   class="form-input"
                                   placeholder="Ej. lfernandez"
                                   autocomplete="username"
                                   required
                                   autofocus
                                   <c:if test="${bloqueado}">disabled</c:if>
                                   value="<c:out value='${param.usuario}'/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <div class="input-wrapper">
                            <svg class="input-icon" viewBox="0 0 20 20" fill="currentColor">
                                <path fill-rule="evenodd"
                                      d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0
                                         01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3
                                         3 0 016 0z" clip-rule="evenodd"/>
                            </svg>
                            <input type="password"
                                   id="contrasena"
                                   name="contrasena"
                                   class="form-input"
                                   placeholder="Ingresa tu contraseña"
                                   autocomplete="current-password"
                                   <c:if test="${bloqueado}">disabled</c:if>
                                   required>
                            <button type="button"
                                    id="togglePassword"
                                    class="toggle-password"
                                    aria-label="Mostrar contraseña"
                                    <c:if test="${bloqueado}">disabled style="display:none;"</c:if>
                                    title="Mostrar contraseña">
                                <svg id="iconEyeOpen" class="eye-icon" viewBox="0 0 20 20" fill="currentColor">
                                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                                    <path fill-rule="evenodd"
                                          d="M.458 10C1.732 5.943 5.522 3 10 3s8.268
                                             2.943 9.542 7c-1.274 4.057-5.064 7-9.542
                                             7S1.732 14.057.458 10zM14 10a4 4 0 11-8
                                             0 4 4 0 018 0z" clip-rule="evenodd"/>
                                </svg>
                                <svg id="iconEyeClosed" class="eye-icon" viewBox="0 0 20 20"
                                     fill="currentColor" style="display:none;">
                                    <path fill-rule="evenodd"
                                          d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0
                                             001.414-1.414l-1.473-1.473A10.014 10.014 0
                                             0019.542 10C18.268 5.943 14.478 3 10 3a9.958
                                             9.958 0 00-4.512 1.074l-1.78-1.781zm4.261
                                             4.26l1.514 1.515a2.003 2.003 0 012.45
                                             2.45l1.514 1.514a4 4 0 00-5.478-5.478z"
                                          clip-rule="evenodd"/>
                                    <path d="M12.454 16.697L9.75 13.992a4 4 0
                                             01-3.742-3.741L2.335 6.578A9.98 9.98 0
                                             00.458 10c1.274 4.057 5.065 7 9.542
                                             7 .847 0 1.669-.105 2.454-.303z"/>
                                </svg>
                            </button>
                        </div>
                    </div>

                    <button type="submit" id="btnLogin" class="btn-login"
                            <c:if test="${bloqueado}">disabled style="opacity: 0.5; cursor: not-allowed;"</c:if>>
                        <c:choose>
                            <c:when test="${bloqueado}">Cuenta Bloqueada</c:when>
                            <c:otherwise>Ingresar</c:otherwise>
                        </c:choose>
                    </button>
                </form>

                <footer class="login-footer">
                    <p>&copy; 2026 Gestiona Commerce &mdash; Proyecto POO II</p>
                </footer>
            </div>
        </main>
    </div>

    <script>
        // Mostrar/ocultar contraseña
        document.getElementById('togglePassword').addEventListener('click', function () {
            const input = document.getElementById('contrasena');
            const iconOpen = document.getElementById('iconEyeOpen');
            const iconClosed = document.getElementById('iconEyeClosed');
            const isPassword = input.type === 'password';

            input.type = isPassword ? 'text' : 'password';
            iconOpen.style.display = isPassword ? 'none' : 'block';
            iconClosed.style.display = isPassword ? 'block' : 'none';
            this.setAttribute('aria-label', isPassword ? 'Ocultar contraseña' : 'Mostrar contraseña');
            this.setAttribute('title', isPassword ? 'Ocultar contraseña' : 'Mostrar contraseña');
        });

        // Foco automático en la alerta de error si existe
        (function () {
            var alertEl = document.getElementById('login-error');
            if (alertEl) alertEl.focus();
        })();
    </script>
</body>
</html>
