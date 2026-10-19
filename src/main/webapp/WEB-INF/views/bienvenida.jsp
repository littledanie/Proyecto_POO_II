<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Gestiona Commerce — Panel principal de gestión comercial.">
    <title>Inicio | Gestiona Commerce</title>

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
        .hero-card {
            background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
            color: #ffffff;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 1.5rem;
        }
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
                        <li class="nav-item active">
                            <a class="nav-link active fw-bold text-success" href="${pageContext.request.contextPath}/bienvenida">
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
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_CLIENTES') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_CLIENTES')}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/clientes">
                                    <span class="nav-link-title">Clientes</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PROVEEDORES') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PROVEEDORES')}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/proveedores">
                                    <span class="nav-link-title">Proveedores</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PRODUCTOS') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PRODUCTOS')}">
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
            <div class="page-body">
                <div class="container-xl">

                    <!-- Banner de Bienvenida -->
                    <div class="hero-card shadow-sm">
                        <h1 class="h2 mb-2 text-white fw-bold">¡Bienvenido(a), <c:out value="${sessionScope.usuarioLogueado.nombreUsuario}"/>!</h1>
                        <p class="text-white-50 mb-0" style="max-width: 650px;">
                            Panel de control comercial. Consulta las existencias del almacén, el directorio comercial y administra las operaciones de tu área según tu rol de <strong><c:out value="${sessionScope.usuarioLogueado.rol.nombreRol}"/></strong>.
                        </p>
                    </div>

                    <!-- Métricas KPI Reales según permisos -->
                    <div class="row row-cards mb-4">
                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PRODUCTOS') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PRODUCTOS')}">
                            <div class="col-sm-6 col-lg-3">
                                <div class="card card-sm shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <span class="bg-primary text-white avatar">
                                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5"/><path d="M12 12l8 -4.5"/><path d="M12 12l0 9"/><path d="M12 12l-8 -4.5"/></svg>
                                                </span>
                                            </div>
                                            <div class="col">
                                                <div class="font-weight-medium">Productos Activos</div>
                                                <div class="text-muted small">${totalProductosActivos != null ? totalProductosActivos : 0} artículos</div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/productos" class="card-footer card-footer-transparent py-2 px-3 text-muted small d-flex justify-content-between">
                                        <span>Ir al catálogo</span>
                                        <span>&rarr;</span>
                                    </a>
                                </div>
                            </div>

                            <div class="col-sm-6 col-lg-3">
                                <div class="card card-sm shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <span class="${totalStockCritico > 0 ? 'bg-danger text-white' : 'bg-success text-white'} avatar">
                                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 9v4"/><path d="M12 16h.01"/><path d="M5 19h14a2 2 0 0 0 1.84 -2.75l-7.1 -12.25a2 2 0 0 0 -3.5 0l-7.1 12.25a2 2 0 0 0 1.75 2.75"/></svg>
                                                </span>
                                            </div>
                                            <div class="col">
                                                <div class="font-weight-medium">Alertas de Stock</div>
                                                <div class="${totalStockCritico > 0 ? 'text-danger fw-bold' : 'text-muted'} small">
                                                    ${totalStockCritico} bajo mínimo
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/productos" class="card-footer card-footer-transparent py-2 px-3 text-muted small d-flex justify-content-between">
                                        <span>Revisar inventario</span>
                                        <span>&rarr;</span>
                                    </a>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_CLIENTES') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_CLIENTES')}">
                            <div class="col-sm-6 col-lg-3">
                                <div class="card card-sm shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <span class="bg-azure text-white avatar">
                                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M8 7a4 4 0 1 0 8 0a4 4 0 0 0 -8 0"/><path d="M6 21v-2a4 4 0 0 1 4 -4h4a4 4 0 0 1 4 4v2"/></svg>
                                                </span>
                                            </div>
                                            <div class="col">
                                                <div class="font-weight-medium">Clientes Activos</div>
                                                <div class="text-muted small">${totalClientesActivos != null ? totalClientesActivos : 0} registrados</div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/clientes" class="card-footer card-footer-transparent py-2 px-3 text-muted small d-flex justify-content-between">
                                        <span>Ver directorio</span>
                                        <span>&rarr;</span>
                                    </a>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PROVEEDORES') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PROVEEDORES')}">
                            <div class="col-sm-6 col-lg-3">
                                <div class="card card-sm shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <span class="bg-purple text-white avatar">
                                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 21l18 0"/><path d="M9 8l1 0"/><path d="M9 12l1 0"/><path d="M9 16l1 0"/><path d="M14 8l1 0"/><path d="M14 12l1 0"/><path d="M14 16l1 0"/><path d="M5 21v-16a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v16"/></svg>
                                                </span>
                                            </div>
                                            <div class="col">
                                                <div class="font-weight-medium">Proveedores Activos</div>
                                                <div class="text-muted small">${totalProveedoresActivos != null ? totalProveedoresActivos : 0} empresas</div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/proveedores" class="card-footer card-footer-transparent py-2 px-3 text-muted small d-flex justify-content-between">
                                        <span>Ver proveedores</span>
                                        <span>&rarr;</span>
                                    </a>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_USUARIOS')}">
                            <div class="col-sm-6 col-lg-3">
                                <div class="card card-sm shadow-sm border-0">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col-auto">
                                                <span class="bg-teal text-white avatar">
                                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0"/><path d="M12 10m-3 0a3 3 0 1 0 6 0a3 3 0 1 0 -6 0"/><path d="M6.168 18.849a4 4 0 0 1 3.832 -2.849h4a4 4 0 0 1 3.834 2.855"/></svg>
                                                </span>
                                            </div>
                                            <div class="col">
                                                <div class="font-weight-medium">Usuarios Activos</div>
                                                <div class="text-muted small">${totalUsuariosActivos != null ? totalUsuariosActivos : 0} cuentas</div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/usuarios" class="card-footer card-footer-transparent py-2 px-3 text-muted small d-flex justify-content-between">
                                        <span>Administrar usuarios</span>
                                        <span>&rarr;</span>
                                    </a>
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Tabla de Stock Crítico (visible solo si tiene VER_PRODUCTOS) -->
                    <c:if test="${sessionScope.usuarioLogueado.tienePermiso('VER_PRODUCTOS') or sessionScope.usuarioLogueado.tienePermiso('GESTIONAR_PRODUCTOS')}">
                        <div class="card shadow-sm border-0">
                            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                                <div>
                                    <h3 class="card-title fw-bold text-dark mb-0">Artículos con Stock por Debajo del Mínimo</h3>
                                    <small class="text-muted">Productos que requieren reposición inmediata en inventario</small>
                                </div>
                                <span class="badge ${totalStockCritico > 0 ? 'bg-danger-subtle text-danger' : 'bg-success-subtle text-success'}">
                                    ${totalStockCritico > 0 ? totalStockCritico.toString().concat(' productos en alerta') : 'Sin alertas'}
                                </span>
                            </div>

                            <c:choose>
                                <c:when test="${empty productosStockCritico}">
                                    <div class="card-body text-center py-5">
                                        <div class="text-success mb-2">
                                            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                <circle cx="12" cy="12" r="9"/>
                                                <path d="M9 12l2 2l4 -4"/>
                                            </svg>
                                        </div>
                                        <h4 class="fw-bold text-dark mb-1">Inventario en niveles óptimos</h4>
                                        <p class="text-muted mb-0 small">Todos los artículos comerciales cuentan con existencias por encima del stock mínimo establecido.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-responsive">
                                        <table class="table table-vcenter card-table table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Código</th>
                                                    <th>Artículo</th>
                                                    <th>Categoría</th>
                                                    <th class="text-center">Stock Actual</th>
                                                    <th class="text-center">Stock Mínimo</th>
                                                    <th class="text-center">Diferencia</th>
                                                    <th class="text-center">Acción</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="p" items="${productosStockCritico}">
                                                    <tr>
                                                        <td>
                                                            <span class="badge bg-blue-subtle text-blue font-monospace"><c:out value="${p.codigo}"/></span>
                                                        </td>
                                                        <td class="fw-semibold text-dark"><c:out value="${p.nombre}"/></td>
                                                        <td><c:out value="${p.nombreCategoria != null ? p.nombreCategoria : '-'}"/></td>
                                                        <td class="text-center">
                                                            <span class="badge bg-danger text-white"><c:out value="${p.stockActual}"/> <c:out value="${p.abreviaturaUnidad}"/></span>
                                                        </td>
                                                        <td class="text-center text-muted"><c:out value="${p.stockMinimo}"/> <c:out value="${p.abreviaturaUnidad}"/></td>
                                                        <td class="text-center text-danger font-monospace fw-bold">
                                                            <c:out value="${p.stockActual - p.stockMinimo}"/> <c:out value="${p.abreviaturaUnidad}"/>
                                                        </td>
                                                        <td class="text-center">
                                                            <a href="${pageContext.request.contextPath}/productos?accion=editar&id=${p.idProducto}" class="btn btn-sm btn-outline-secondary">
                                                                Actualizar
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>

                </div>
            </div>

            <footer class="footer footer-transparent d-print-none py-3 mt-auto border-top">
                <div class="container-xl text-center text-muted small">
                    &copy; 2026 Gestiona Commerce
                </div>
            </footer>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
