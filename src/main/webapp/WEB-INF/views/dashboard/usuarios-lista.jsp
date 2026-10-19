<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios — Gestiona Commerce</title>

    <!-- Google Fonts: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Plantilla de Dashboard: Tabler UI (Licencia MIT, basada en Bootstrap 5) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta20/dist/css/tabler.min.css">

    <style>
        :root {
            --tblr-font-sans-serif: 'Inter', -apple-system, BlinkMacSystemFont, San Francisco, Segoe UI, Roboto, Helvetica Neue, sans-serif;
            --tblr-primary: #059669;
            --tblr-primary-rgb: 5, 150, 105;
        }
        .navbar-brand-autodark {
            font-weight: 700;
            color: #0f172a;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .brand-icon-svg {
            width: 24px;
            height: 24px;
            color: #059669;
        }
        .badge-role-admin { background-color: #059669; color: #fff; }
        .badge-role-vendedor { background-color: #0284c7; color: #fff; }
        .badge-role-almacen { background-color: #d97706; color: #fff; }
        .badge-role-super { background-color: #7c3aed; color: #fff; }
        .badge-role-invitado { background-color: #64748b; color: #fff; }
    </style>
</head>
<body class="layout-boxed">
    <div class="page">
        <!-- Barra de navegación superior -->
        <header class="navbar navbar-expand-md navbar-light d-print-none border-bottom">
            <div class="container-xl">
                <a href="${pageContext.request.contextPath}/bienvenida" class="navbar-brand navbar-brand-autodark">
                    <svg class="brand-icon-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
                        <polyline points="9 22 9 12 15 12 15 22"/>
                    </svg>
                    <span>Gestiona Commerce</span>
                </a>

                <div class="navbar-nav flex-row order-md-last align-items-center gap-3">
                    <div class="d-none d-md-flex align-items-center gap-2">
                        <span class="avatar avatar-sm rounded-circle bg-emerald text-white">
                            <c:out value="${sessionScope.usuarioLogueado.nombreUsuario.substring(0, 1).toUpperCase()}"/>
                        </span>
                        <div class="d-flex flex-column text-start">
                            <span class="fw-semibold small"><c:out value="${sessionScope.usuarioLogueado.nombreUsuario}"/></span>
                            <span class="text-muted small" style="font-size: 0.75rem;"><c:out value="${sessionScope.usuarioLogueado.rol.nombreRol}"/></span>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                        Cerrar Sesión
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="navbar-menu">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/bienvenida">
                                <span class="nav-link-title">Inicio</span>
                            </a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link active fw-bold text-success" href="${pageContext.request.contextPath}/usuarios">
                                <span class="nav-link-title">Usuarios</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link disabled text-muted" title="Disponible en Etapa 5">
                                Clientes <small class="badge bg-secondary-subtle text-secondary ms-1">Etapa 5</small>
                            </span>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link disabled text-muted" title="Disponible en Etapa 5">
                                Proveedores <small class="badge bg-secondary-subtle text-secondary ms-1">Etapa 5</small>
                            </span>
                        </li>
                        <li class="nav-item">
                            <span class="nav-link disabled text-muted" title="Disponible en Etapa 5">
                                Productos <small class="badge bg-secondary-subtle text-secondary ms-1">Etapa 5</small>
                            </span>
                        </li>
                    </ul>
                </div>
            </div>
        </header>

        <!-- Contenido de la página -->
        <div class="page-wrapper">
            <div class="page-header d-print-none">
                <div class="container-xl">
                    <div class="row g-2 align-items-center">
                        <div class="col">
                            <div class="page-pretitle text-muted">Módulo 3.0 &bull; Etapa 4 (Patrón Repository)</div>
                            <h2 class="page-title text-dark">Gestión de Cuentas y Datos Personales</h2>
                        </div>
                        <div class="col-auto ms-auto d-print-none">
                            <a href="${pageContext.request.contextPath}/usuarios?accion=nuevo" class="btn btn-success d-inline-flex align-items-center gap-1">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <line x1="12" y1="5" x2="12" y2="19"></line>
                                    <line x1="5" y1="12" x2="19" y2="12"></line>
                                </svg>
                                Nuevo Usuario
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="page-body">
                <div class="container-xl">

                    <%-- Mensajes Flash de Éxito / Error --%>
                    <c:if test="${not empty sessionScope.exitoMensaje}">
                        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                            <strong>¡Operación exitosa!</strong> <c:out value="${sessionScope.exitoMensaje}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="exitoMensaje" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.errorMensaje}">
                        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                            <strong>Error:</strong> <c:out value="${sessionScope.errorMensaje}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="errorMensaje" scope="session"/>
                    </c:if>

                    <!-- Tarjetas de métricas rápidas -->
                    <div class="row row-cards mb-3">
                        <div class="col-sm-6 col-lg-3">
                            <div class="card card-sm shadow-sm border-0">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col-auto">
                                            <span class="bg-primary text-white avatar">
                                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 7m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0"/><path d="M6 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2"/></svg>
                                            </span>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-medium">Total Registros</div>
                                            <div class="text-muted small">${usuarios.size()} cuentas</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6 col-lg-3">
                            <div class="card card-sm shadow-sm border-0">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col-auto">
                                            <span class="bg-success text-white avatar">
                                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M5 12l5 5l10 -10"/></svg>
                                            </span>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-medium">Patrón Aplicado</div>
                                            <div class="text-muted small">Repository + Singleton</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6 col-lg-3">
                            <div class="card card-sm shadow-sm border-0">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col-auto">
                                            <span class="bg-warning text-white avatar">
                                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
                                            </span>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-medium">Seguridad Activa</div>
                                            <div class="text-muted small">BCrypt + 3 intentos</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6 col-lg-3">
                            <div class="card card-sm shadow-sm border-0">
                                <div class="card-body">
                                    <div class="row align-items-center">
                                        <div class="col-auto">
                                            <span class="bg-info text-white avatar">
                                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/></svg>
                                            </span>
                                        </div>
                                        <div class="col">
                                            <div class="font-weight-medium">Base de Datos</div>
                                            <div class="text-muted small">PostgreSQL 3FN</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Tabla de Usuarios -->
                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark mb-0">Cuentas Registradas en el Sistema</h3>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-vcenter card-table table-hover">
                                <thead>
                                    <tr>
                                        <th>Usuario</th>
                                        <th>Datos Personales</th>
                                        <th>Rol</th>
                                        <th>Contacto</th>
                                        <th>Ubicación</th>
                                        <th>Seguridad / Intentos</th>
                                        <th>Estado</th>
                                        <th class="w-1 text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="u" items="${usuarios}">
                                        <tr class="${u.estado == 'Inactivo' ? 'table-light opacity-75' : ''}">
                                            <td>
                                                <div class="d-flex align-items-center gap-2">
                                                    <span class="avatar avatar-sm rounded-circle ${u.bloqueado ? 'bg-danger text-white' : 'bg-primary-subtle text-primary fw-bold'}">
                                                        <c:out value="${u.nombreUsuario.substring(0, 1).toUpperCase()}"/>
                                                    </span>
                                                    <div>
                                                        <div class="font-weight-medium text-dark"><c:out value="${u.nombreUsuario}"/></div>
                                                        <div class="text-muted small">ID: #<c:out value="${u.idUsuario}"/></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty u.datosPersonales}">
                                                        <div class="fw-medium text-dark"><c:out value="${u.datosPersonales.nombreCompleto}"/></div>
                                                        <div class="text-muted small">DNI: <c:out value="${u.datosPersonales.dni}"/></div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted fst-italic">Sin vincular</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge ${u.rol.nombreRol == 'Administrador' ? 'badge-role-admin' : (u.rol.nombreRol == 'Vendedor' ? 'badge-role-vendedor' : 'badge-role-almacen')}">
                                                    <c:out value="${u.rol.nombreRol}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <div class="small"><c:out value="${u.datosPersonales.correoPrincipal}"/></div>
                                                <div class="text-muted small"><c:out value="${u.datosPersonales.telefonoPrincipal}"/></div>
                                            </td>
                                            <td>
                                                <span class="small text-muted"><c:out value="${u.datosPersonales.distrito.nombreDistrito}"/></span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${u.bloqueado}">
                                                        <span class="badge bg-danger text-white">Bloqueado (3/3)</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success-subtle text-success">
                                                            <c:out value="${u.intentosFallidos}"/>/3 fallos
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge ${u.estado == 'Activo' ? 'bg-success text-white' : 'bg-secondary text-white'}">
                                                    <c:out value="${u.estado}"/>
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-list flex-nowrap justify-content-center">
                                                    <a href="${pageContext.request.contextPath}/usuarios?accion=editar&id=${u.idUsuario}" 
                                                       class="btn btn-sm btn-outline-primary" title="Editar usuario">
                                                        Editar
                                                    </a>

                                                    <c:if test="${u.bloqueado}">
                                                        <a href="${pageContext.request.contextPath}/usuarios?accion=desbloquear&id=${u.idUsuario}"
                                                           class="btn btn-sm btn-outline-warning" title="Desbloquear cuenta">
                                                            Desbloquear
                                                        </a>
                                                    </c:if>

                                                    <c:if test="${u.estado == 'Activo'}">
                                                        <a href="${pageContext.request.contextPath}/usuarios?accion=eliminar&id=${u.idUsuario}" 
                                                           class="btn btn-sm btn-outline-danger" 
                                                           onclick="return confirm('¿Seguro que deseas desactivar lógicamente a este usuario?');"
                                                           title="Eliminación lógica">
                                                            Desactivar
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>

            <!-- Footer con atribución de licencia Tabler UI (AGENTS.md §9) -->
            <footer class="footer footer-transparent d-print-none py-3 mt-auto border-top">
                <div class="container-xl">
                    <div class="row text-center align-items-center flex-row-reverse">
                        <div class="col-lg-auto ms-lg-auto">
                            <span class="text-muted small">Plantilla base: <strong>Tabler UI</strong> (Licencia MIT, basada en Bootstrap 5)</span>
                        </div>
                        <div class="col-12 col-lg-auto mt-3 mt-lg-0">
                            <span class="text-muted small">&copy; 2026 Gestiona Commerce &mdash; Sistema de Gestión Comercial (POO II)</span>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    </div>

    <!-- Tabler Core JS (Bootstrap 5 bundle) -->
    <script src="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta20/dist/js/tabler.min.js"></script>
</body>
</html>
