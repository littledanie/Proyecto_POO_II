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
                <span>Etapa 2: Conexión BD Verificada</span>
            </div>
            <h1 class="hero-title">¡Bienvenido(a), <c:out value="${sessionScope.usuarioLogueado.nombreUsuario}"/>!</h1>
            <p class="hero-subtitle">
                Has iniciado sesión exitosamente. La aplicación se comunicó mediante JDBC puro con la
                base de datos PostgreSQL en <strong>Supabase</strong>, validando tus credenciales contra la tabla
                provisional de usuarios.
            </p>
        </section>

        <!-- Grilla con el estado técnico de la etapa -->
        <section class="info-grid">
            <!-- Tarjeta 1: Base de Datos -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <ellipse cx="12" cy="5" rx="9" ry="3"/>
                        <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/>
                        <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/>
                    </svg>
                </div>
                <h2 class="card-title">Base de Datos PostgreSQL</h2>
                <p class="card-desc">
                    Conexión establecida con Supabase utilizando el driver oficial JDBC (42.7) mediante
                    variables de entorno seguras (<code>DB_URL</code>, <code>DB_USER</code>, <code>DB_PASSWORD</code>).
                </p>
                <div class="card-data">
                    <span>Estado:</span>
                    <span class="card-data-value">Conectado (Activo)</span>
                </div>
            </div>

            <!-- Tarjeta 2: Autenticación -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                    </svg>
                </div>
                <h2 class="card-title">Autenticación Etapa 2</h2>
                <p class="card-desc">
                    Consulta ejecutada mediante <code>PreparedStatement</code> sobre la tabla provisional
                    <code>usuarios</code>. En la siguiente etapa se migrará a hash BCrypt.
                </p>
                <div class="card-data">
                    <span>Usuario ID:</span>
                    <span class="card-data-value">#<c:out value="${sessionScope.usuarioLogueado.idUsuario}"/></span>
                </div>
            </div>

            <!-- Tarjeta 3: Próximo Hito -->
            <div class="info-card">
                <div class="card-header-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/>
                    </svg>
                </div>
                <h2 class="card-title">Evolución: Etapa 3</h2>
                <p class="card-desc">
                    Implementación del patrón creacional <strong>Singleton</strong> en la conexión,
                    reestructuración a 3FN (roles y permisos) y bloqueo tras 3 intentos fallidos.
                </p>
                <div class="card-data">
                    <span>Próximo paso:</span>
                    <span class="card-data-value">Paso 4 (Singleton)</span>
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
