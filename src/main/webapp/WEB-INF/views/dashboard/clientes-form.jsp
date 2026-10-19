<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${esEdicion}">Editar Cliente</c:when><c:otherwise>Nuevo Cliente</c:otherwise></c:choose> — Gestiona Commerce</title>

    <!-- Google Fonts: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Tabler UI (Licencia MIT, Bootstrap 5) -->
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
                    <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline-secondary btn-sm">
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
                            <div class="page-pretitle text-muted">CRUD de Clientes &bull; Etapa 5 (Patrón Repository)</div>
                            <h2 class="page-title text-dark">
                                <c:choose>
                                    <c:when test="${esEdicion}">Editar Cliente: <c:out value="${clienteEditar.razonSocial != null && !clienteEditar.razonSocial.isEmpty() ? clienteEditar.razonSocial : clienteEditar.datosPersonales.nombreCompleto}"/></c:when>
                                    <c:otherwise>Registrar Nuevo Cliente</c:otherwise>
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

                    <form action="${pageContext.request.contextPath}/clientes" method="POST" class="card shadow-sm border-0">
                        <input type="hidden" name="accion" value="guardar">
                        <input type="hidden" name="idCliente" value="${clienteEditar.idCliente != null ? clienteEditar.idCliente : 0}">
                        <input type="hidden" name="idDatosPersonales" value="${clienteEditar.datosPersonales.idDatosPersonales}">

                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark">Información del Cliente y Contacto Comercial</h3>
                        </div>

                        <div class="card-body">
                            <!-- Sección 1: Tipo y Datos Fiscales -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">1</span>
                                Clasificación y Datos Fiscales
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Tipo de Cliente</label>
                                    <select name="tipoCliente" id="tipoClienteSelect" class="form-select" required onchange="toggleTipoCliente()">
                                        <option value="Natural" ${clienteEditar.tipoCliente == 'Natural' ? 'selected' : ''}>Persona Natural</option>
                                        <option value="Empresa" ${clienteEditar.tipoCliente == 'Empresa' ? 'selected' : ''}>Empresa / Jurídica</option>
                                    </select>
                                </div>
                                <div class="col-md-4" id="divRuc">
                                    <label class="form-label">RUC (11 dígitos)</label>
                                    <input type="text" name="ruc" class="form-control" placeholder="20123456789"
                                           maxlength="11" value="<c:out value='${clienteEditar.ruc}'/>">
                                </div>
                                <div class="col-md-4" id="divRazonSocial">
                                    <label class="form-label">Razón Social</label>
                                    <input type="text" name="razonSocial" class="form-control" placeholder="Ej. Soluciones SAC"
                                           value="<c:out value='${clienteEditar.razonSocial}'/>">
                                </div>
                            </div>

                            <!-- Sección 2: Datos Personales / Representante -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">2</span>
                                Datos Personales / Contacto
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Nombres</label>
                                    <input type="text" name="nombres" class="form-control" required placeholder="Nombres"
                                           value="<c:out value='${clienteEditar.datosPersonales.nombres}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label required">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control" required placeholder="Apellidos"
                                           value="<c:out value='${clienteEditar.datosPersonales.apellidos}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label required">DNI (8 dígitos)</label>
                                    <input type="text" name="dni" class="form-control" required maxlength="8" placeholder="12345678"
                                           value="<c:out value='${clienteEditar.datosPersonales.dni}'/>">
                                </div>
                            </div>

                            <!-- Sección 3: Contacto y Ubicación -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">3</span>
                                Contacto y Dirección
                            </h4>
                            <div class="row g-3 mb-2">
                                <div class="col-md-6">
                                    <label class="form-label">Correo Electrónico</label>
                                    <input type="email" name="correo" class="form-control" placeholder="cliente@ejemplo.com"
                                           value="<c:out value='${clienteEditar.datosPersonales.correoPrincipal}'/>">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Teléfono / Celular</label>
                                    <input type="text" name="telefono" class="form-control" placeholder="987654321"
                                           value="<c:out value='${clienteEditar.datosPersonales.telefonoPrincipal}'/>">
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label">Dirección Fiscal / Domicilio</label>
                                    <input type="text" name="direccion" class="form-control" placeholder="Av. Principal 123"
                                           value="<c:out value='${clienteEditar.datosPersonales.direccionPrincipal}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Distrito / Ubicación</label>
                                    <select name="idDistrito" class="form-select">
                                        <option value="">-- Seleccionar Distrito --</option>
                                        <c:forEach var="dist" items="${distritos}">
                                            <option value="${dist.idDistrito}"
                                                ${clienteEditar.datosPersonales.idDistrito == dist.idDistrito ? 'selected' : ''}>
                                                <c:out value="${dist.nombreDistrito}"/> (<c:out value="${dist.nombreProvincia}"/>)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-light d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-link link-secondary">Cancelar</a>
                            <button type="submit" class="btn btn-success px-4">
                                <c:choose>
                                    <c:when test="${esEdicion}">Actualizar Cliente</c:when>
                                    <c:otherwise>Guardar Cliente</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>

                </div>
            </div>

            <!-- Footer -->
            <footer class="footer footer-transparent d-print-none py-3 border-top mt-auto">
                <div class="container-xl text-center text-muted small">
                    Gestiona Commerce &bull; Etapa 5 &bull; Arquitectura MVC + Patrón Repository &bull; Tabler UI (MIT)
                </div>
            </footer>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
