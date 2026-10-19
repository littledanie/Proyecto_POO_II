<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Proveedores | Gestiona Commerce</title>

    <!-- Google Fonts: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Tabler UI (Bootstrap 5) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta20/dist/css/tabler.min.css">

    <style>
        :root {
            --tblr-font-sans-serif: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            --tblr-primary: #059669;
        }
        .navbar-brand-autodark {
            font-weight: 700;
            color: #0f172a;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .brand-icon-svg { width: 24px; height: 24px; color: #059669; }
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
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_USUARIOS')}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">
                                    <span class="nav-link-title">Usuarios</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_CLIENTES') || sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_CLIENTES')}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/clientes">
                                    <span class="nav-link-title">Clientes</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PROVEEDORES') || sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PROVEEDORES')}">
                            <li class="nav-item active">
                                <a class="nav-link active fw-bold text-success" href="${pageContext.request.contextPath}/proveedores">
                                    <span class="nav-link-title">Proveedores</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PRODUCTOS') || sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PRODUCTOS')}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/productos">
                                    <span class="nav-link-title">Productos</span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </header>

        <div class="page-wrapper">
            <div class="page-header d-print-none">
                <div class="container-xl">
                    <div class="row g-2 align-items-center">
                        <div class="col">
                            <div class="page-pretitle text-muted">Compras y Abastecimiento</div>
                            <h2 class="page-title text-dark">Gestión de Proveedores</h2>
                        </div>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PROVEEDORES')}">
                            <div class="col-auto ms-auto d-print-none">
                                <a href="${pageContext.request.contextPath}/proveedores?accion=nuevo" class="btn btn-success d-inline-flex align-items-center gap-2">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <line x1="12" y1="5" x2="12" y2="19"></line>
                                        <line x1="5" y1="12" x2="19" y2="12"></line>
                                    </svg>
                                    <span>Nuevo Proveedor</span>
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="page-body">
                <div class="container-xl">

                    <c:if test="${not empty sessionScope.exitoMensaje}">
                        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
                            <strong>Operación exitosa:</strong> <c:out value="${sessionScope.exitoMensaje}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="exitoMensaje" scope="session"/>
                    </c:if>

                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark">Directorio de Proveedores y Distribuidores</h3>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-vcenter card-table table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>RUC</th>
                                        <th>Razón Social</th>
                                        <th>Persona de Contacto</th>
                                        <th>Teléfono</th>
                                        <th>Correo</th>
                                        <th>Ubicación</th>
                                        <th>Estado</th>
                                        <th class="w-1 text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty proveedores}">
                                            <tr>
                                                <td colspan="8" class="text-center py-5 text-muted">
                                                    <p class="mb-1 fw-semibold">No se encontraron proveedores registrados.</p>
                                                    <small>Presiona "Nuevo Proveedor" para incorporar tu primer proveedor.</small>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="p" items="${proveedores}">
                                                <tr>
                                                    <td>
                                                        <span class="badge bg-purple-subtle text-purple fw-bold font-monospace">
                                                            <c:out value="${p.ruc}"/>
                                                        </span>
                                                    </td>
                                                    <td class="fw-semibold">
                                                        <c:out value="${p.razonSocial}"/>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty p.datosPersonales}">
                                                                <c:out value="${p.datosPersonales.nombreCompleto}"/>
                                                                <c:if test="${not empty p.datosPersonales.dni}">
                                                                    <span class="text-muted small d-block font-monospace">DNI: <c:out value="${p.datosPersonales.dni}"/></span>
                                                                </c:if>
                                                            </c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:out value="${not empty p.datosPersonales && not empty p.datosPersonales.telefonoPrincipal ? p.datosPersonales.telefonoPrincipal : '-'}"/>
                                                    </td>
                                                    <td>
                                                        <c:out value="${not empty p.datosPersonales && not empty p.datosPersonales.correoPrincipal ? p.datosPersonales.correoPrincipal : '-'}"/>
                                                    </td>
                                                    <td>
                                                        <span class="small text-muted">
                                                            <c:out value="${not empty p.datosPersonales && not empty p.datosPersonales.distrito ? p.datosPersonales.distrito.nombreDistrito : '-'}"/>
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${p.estado == 'Activo'}">
                                                                <span class="badge bg-success-subtle text-success">Activo</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger-subtle text-danger">Inactivo</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="text-center text-nowrap">
                                                        <a href="${pageContext.request.contextPath}/proveedores?accion=editar&id=${p.idProveedor}" class="btn btn-sm btn-outline-secondary me-1" title="Editar">
                                                            Editar
                                                        </a>
                                                        <c:if test="${p.estado == 'Activo'}">
                                                            <a href="${pageContext.request.contextPath}/proveedores?accion=eliminar&id=${p.idProveedor}" 
                                                               class="btn btn-sm btn-outline-danger" 
                                                               title="Desactivar"
                                                               onclick="return confirm('¿Seguro que deseas desactivar este proveedor?');">
                                                                Desactivar
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>

            <!-- Footer -->
            <footer class="footer footer-transparent d-print-none py-3 border-top mt-auto">
                <div class="container-xl text-center text-muted small">
                    &copy; 2026 Gestiona Commerce &mdash; Sistema de Gestión Comercial
                </div>
            </footer>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
