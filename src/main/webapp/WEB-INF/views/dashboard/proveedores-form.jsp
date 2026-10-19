<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${esEdicion}">Editar proveedor</c:when><c:otherwise>Nuevo proveedor</c:otherwise></c:choose> | Gestiona Commerce</title>

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
                    <a href="${pageContext.request.contextPath}/proveedores" class="btn btn-outline-secondary btn-sm">
                        &larr; Volver al Listado
                    </a>
                </div>
            </div>
        </header>

        <div class="page-wrapper">
            <div class="page-header d-print-none">
                <div class="container-xl">
                    <div class="row g-2 align-items-center">
                        <div class="col">
                            <div class="page-pretitle text-muted">Compras y Abastecimiento</div>
                            <h2 class="page-title text-dark">
                                <c:choose>
                                    <c:when test="${esEdicion}">Editar Proveedor: <c:out value="${proveedorEditar.razonSocial}"/></c:when>
                                    <c:otherwise>Registrar Nuevo Proveedor</c:otherwise>
                                </c:choose>
                            </h2>
                        </div>
                    </div>
                </div>
            </div>

            <div class="page-body">
                <div class="container-xl">

                    <c:if test="${not empty errorMensaje}">
                        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
                            <strong>Atención:</strong> <c:out value="${errorMensaje}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/proveedores" method="POST" class="card shadow-sm border-0">
                        <input type="hidden" name="accion" value="guardar">
                        <input type="hidden" name="idProveedor" value="${proveedorEditar.idProveedor != null ? proveedorEditar.idProveedor : 0}">
                        <input type="hidden" name="idDatosPersonales" value="${proveedorEditar.datosPersonales.idDatosPersonales}">

                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark">Información de la Empresa y Contacto</h3>
                        </div>

                        <div class="card-body">
                            <!-- Sección 1: Datos de la Empresa -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">1</span>
                                Datos Fiscales de la Empresa Proveedora
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">RUC (11 dígitos)</label>
                                    <input type="text" name="ruc" class="form-control" required placeholder="20123456789"
                                           maxlength="11" value="<c:out value='${proveedorEditar.ruc}'/>">
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label required">Razón Social</label>
                                    <input type="text" name="razonSocial" class="form-control" required placeholder="Ej. Distribuidora del Norte SAC"
                                           value="<c:out value='${proveedorEditar.razonSocial}'/>">
                                </div>
                            </div>

                            <!-- Sección 2: Persona de Contacto / Representante -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">2</span>
                                Persona de Contacto / Representante
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Nombres del Contacto</label>
                                    <input type="text" name="contactoNombre" class="form-control" required placeholder="Nombres"
                                           value="<c:out value='${proveedorEditar.datosPersonales.nombres}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label required">Apellidos del Contacto</label>
                                    <input type="text" name="contactoApellido" class="form-control" required placeholder="Apellidos"
                                           value="<c:out value='${proveedorEditar.datosPersonales.apellidos}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">DNI de Contacto</label>
                                    <input type="text" name="contactoDni" class="form-control" maxlength="8" placeholder="12345678"
                                           value="<c:out value='${proveedorEditar.datosPersonales.dni}'/>">
                                </div>
                            </div>

                            <!-- Sección 3: Teléfono, Correo y Dirección -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">3</span>
                                Canales de Contacto y Ubicación
                            </h4>
                            <div class="row g-3 mb-2">
                                <div class="col-md-6">
                                    <label class="form-label">Correo Electrónico Comercial</label>
                                    <input type="email" name="correo" class="form-control" placeholder="ventas@proveedor.com"
                                           value="<c:out value='${proveedorEditar.datosPersonales.correoPrincipal}'/>">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Teléfono / Central</label>
                                    <input type="text" name="telefono" class="form-control" placeholder="(01) 456-7890 / 999888777"
                                           value="<c:out value='${proveedorEditar.datosPersonales.telefonoPrincipal}'/>">
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label">Dirección / Sede</label>
                                    <input type="text" name="direccion" class="form-control" placeholder="Av. Industrial 450"
                                           value="<c:out value='${proveedorEditar.datosPersonales.direccionPrincipal}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Distrito / Ubicación</label>
                                    <select name="idDistrito" class="form-select">
                                        <option value="">-- Seleccionar Distrito --</option>
                                        <c:forEach var="dist" items="${distritos}">
                                            <option value="${dist.idDistrito}"
                                                ${proveedorEditar.datosPersonales.idDistrito == dist.idDistrito ? 'selected' : ''}>
                                                <c:out value="${dist.nombreDistrito}"/> (<c:out value="${dist.nombreProvincia}"/>)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-light d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/proveedores" class="btn btn-link link-secondary">Cancelar</a>
                            <button type="submit" class="btn btn-success px-4">
                                <c:choose>
                                    <c:when test="${esEdicion}">Actualizar Proveedor</c:when>
                                    <c:otherwise>Guardar Proveedor</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>

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
