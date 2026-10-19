<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Gestiona Commerce — Panel de bienvenida tras autenticación exitosa contra base de datos PostgreSQL.">
    <title>Bienvenida — Gestiona Commerce</title>

    <!-- Tipografía: Inter de Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bienvenida.css">
</head>
<body>

    <!-- Barra de navegación superior -->
    <header class="navbar">
        <a href="${pageContext.request.contextPath}/bienvenida" class="brand-link">
            <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                 stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
            <span>Gestiona Commerce</span>
        </a>

        <div class="user-nav">
            <div class="user-badge">
                <span class="user-avatar">
                    <c:out value="${sessionScope.usuarioLogueado.nombreUsuario.substring(0, 1).toUpperCase()}"/>
                </span>
                <span><c:out value="${sessionScope.usuarioLogueado.nombreUsuario}"/></span>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout" title="Cerrar sesión">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                    <polyline points="16 17 21 12 16 7"/>
                    <line x1="21" y1="12" x2="9" y2="12"/>
                </svg>
                <span>Salir</span>
            </a>
        </div>
    </header>

    <!-- Contenido principal -->
    <main class="main-container">

        <!-- Banner de éxito de conexión -->
        <section class="hero-banner">
            <div class="hero-tag">
                <span class="pulse-dot"></span>
                <span>Etapa 3: Singleton + BCrypt + 3FN Activo</span>
            </div>
            <h1 class="hero-title">¡Bienvenido(a), <c:out value="${sessionScope.usuarioLogueado.nombreUsuario}"/>!</h1>
            <p class="hero-subtitle">
                Has iniciado sesión exitosamente como <strong><c:out value="${sessionScope.usuarioLogueado.rol.nombreRol}"/></strong>.
                La autenticación verificó tu contraseña contra el hash <strong>BCrypt</strong> almacenado en Supabase,
                utilizando una única instancia global mediante el patrón creacional <strong>Singleton</strong>.
            </p>
            <div style="margin-top: 1.25rem;">
                <a href="${pageContext.request.contextPath}/usuarios" 
                   style="display: inline-flex; align-items: center; gap: 0.5rem; background-color: #059669; color: #ffffff; padding: 0.625rem 1.25rem; border-radius: 8px; font-weight: 600; text-decoration: none; font-size: 0.9375rem; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.15);">
                    <span>Ir al Dashboard de Usuarios (Etapa 4)</span>
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>
                </a>
            </div>
        </section>

        <!-- Grilla con el estado técnico de la etapa -->
        <section class="info-grid">
            <!-- Tarjeta 1: Patrón Singleton -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <ellipse cx="12" cy="5" rx="9" ry="3"/>
                        <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
                        <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
                    </svg>
                </div>
                <h2 class="card-title">Patrón Singleton</h2>
                <p class="card-desc">
                    La clase <code>ConexionBD</code> implementa Singleton con constructor privado y método
                    estático sincronizado <code>getInstancia()</code>, centralizando la configuración JDBC.
                </p>
                <div class="card-data">
                    <span>Instancia:</span>
                    <span class="card-data-value">Única (Thread-Safe)</span>
                </div>
            </div>

            <!-- Tarjeta 2: Seguridad y Cifrado -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                    </svg>
                </div>
                <h2 class="card-title">Seguridad BCrypt</h2>
                <p class="card-desc">
                    Contraseñas con hash y sal aleatoria (costo 12). Control automático: tras 3 intentos erróneos
                    consecutivos la cuenta se bloquea en BD.
                </p>
                <div class="card-data">
                    <span>Rol asignado:</span>
                    <span class="card-data-value"><c:out value="${sessionScope.usuarioLogueado.rol.nombreRol}"/></span>
                </div>
            </div>

            <!-- Tarjeta 3: Esquema 3FN -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>
                    </svg>
                </div>
                <h2 class="card-title">Esquema 3FN (RBAC)</h2>
                <p class="card-desc">
                    Estructura normalizada en 3FN con tablas <code>rol</code>, <code>permiso</code>,
                    <code>rol_permiso</code> y <code>usuario</code>.
                </p>
                <div class="card-data">
                    <span>Próximo hito:</span>
                    <span class="card-data-value">Etapa 4 (Dashboard)</span>
                </div>
            </div>
        </section>

    </main>

    <!-- Pie de página -->
    <footer class="app-footer">
        <p>&copy; 2026 Gestiona Commerce &mdash; Sistema de Gestión Comercial (POO II)</p>
    </footer>

</body>
</html>
