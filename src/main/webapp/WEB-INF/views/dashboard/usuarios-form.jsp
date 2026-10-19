<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${esEdicion}">Editar Usuario</c:when><c:otherwise>Nuevo Usuario</c:otherwise></c:choose> — Gestiona Commerce</title>

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
                    <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline-secondary btn-sm">
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
                            <div class="page-pretitle text-muted">CRUD de Usuarios &bull; Etapa 4</div>
                            <h2 class="page-title text-dark">
                                <c:choose>
                                    <c:when test="${esEdicion}">Editar Usuario: <c:out value="${usuarioEditar.nombreUsuario}"/></c:when>
                                    <c:otherwise>Registrar Nuevo Usuario</c:otherwise>
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

                    <form action="${pageContext.request.contextPath}/usuarios" method="POST" class="card shadow-sm border-0">
                        <input type="hidden" name="accion" value="guardar">
                        <input type="hidden" name="idUsuario" value="${usuarioEditar.idUsuario != null ? usuarioEditar.idUsuario : 0}">
                        <input type="hidden" name="idDatosPersonales" value="${usuarioEditar.datosPersonales.idDatosPersonales}">

                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark">Información de la Cuenta y Persona Asociada</h3>
                        </div>

                        <div class="card-body">
                            <!-- Sección 1: Cuenta de Acceso -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">1</span>
                                Credenciales de Acceso
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Nombre de Usuario</label>
                                    <input type="text" name="nombreUsuario" class="form-control" 
                                           placeholder="Ej. mtorres" required
                                           <c:if test="${esEdicion}">readonly</c:if>
                                           value="<c:out value='${usuarioEditar.nombreUsuario}'/>">
                                    <c:if test="${esEdicion}"><small class="text-muted">El nombre de usuario no se puede cambiar.</small></c:if>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label <c:if test='${!esEdicion}'>required</c:if>">
                                        <c:choose>
                                            <c:when test="${esEdicion}">Nueva Contraseña (dejar en blanco para conservar)</c:when>
                                            <c:otherwise>Contraseña (mínimo 6 caracteres)</c:otherwise>
                                        </c:choose>
                                    </label>
                                    <input type="password" name="contrasena" class="form-control" 
                                           placeholder="••••••••" <c:if test="${!esEdicion}">required minlength="6"</c:if>>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label required">Rol en el Sistema</label>
                                    <select name="idRol" class="form-select" required>
                                        <option value="">-- Seleccionar Rol --</option>
                                        <c:forEach var="rol" items="${roles}">
                                            <option value="${rol.idRol}" <c:if test="${usuarioEditar.idRol == rol.idRol}">selected</c:if>>
                                                <c:out value="${rol.nombreRol}"/> &mdash; <c:out value="${rol.descripcion}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <hr class="my-4">

                            <!-- Sección 2: Datos Personales (Entidad Compartida D2) -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">2</span>
                                Datos Personales (Persona Compartida)
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Nombres</label>
                                    <input type="text" name="nombres" class="form-control" placeholder="Ej. Carlos" required
                                           value="<c:out value='${usuarioEditar.datosPersonales.nombres}'/>">
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label required">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control" placeholder="Ej. Gómez Rivas" required
                                           value="<c:out value='${usuarioEditar.datosPersonales.apellidos}'/>">
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label required">DNI (Documento de Identidad)</label>
                                    <input type="text" name="dni" class="form-control" placeholder="Ej. 50987452" maxlength="15" required
                                           value="<c:out value='${usuarioEditar.datosPersonales.dni}'/>">
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Fecha de Nacimiento</label>
                                    <input type="date" name="fechaNacimiento" class="form-control"
                                           value="${usuarioEditar.datosPersonales.fechaNacimiento}">
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Sexo</label>
                                    <select name="idSexo" class="form-select">
                                        <option value="">-- Seleccionar --</option>
                                        <c:forEach var="s" items="${sexos}">
                                            <option value="${s.idSexo}" <c:if test="${usuarioEditar.datosPersonales.idSexo == s.idSexo}">selected</c:if>>
                                                <c:out value="${s.descripcion}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Estado Civil</label>
                                    <select name="idEstadoCivil" class="form-select">
                                        <option value="">-- Seleccionar --</option>
                                        <c:forEach var="ec" items="${estadosCiviles}">
                                            <option value="${ec.idEstadoCivil}" <c:if test="${usuarioEditar.datosPersonales.idEstadoCivil == ec.idEstadoCivil}">selected</c:if>>
                                                <c:out value="${ec.descripcion}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <hr class="my-4">

                            <!-- Sección 3: Contacto y Ubicación Geográfica -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">3</span>
                                Contacto y Ubicación Geográfica
                            </h4>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Correo Electrónico</label>
                                    <input type="email" name="correo" class="form-control" placeholder="ejemplo@correo.com"
                                           value="<c:out value='${usuarioEditar.datosPersonales.correoPrincipal}'/>">
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Teléfono Móvil</label>
                                    <input type="text" name="telefono" class="form-control" placeholder="Ej. 987654321"
                                           value="<c:out value='${usuarioEditar.datosPersonales.telefonoPrincipal}'/>">
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Distrito</label>
                                    <select name="idDistrito" class="form-select">
                                        <option value="">-- Seleccionar Distrito --</option>
                                        <c:forEach var="dist" items="${distritos}">
                                            <option value="${dist.idDistrito}" <c:if test="${usuarioEditar.datosPersonales.idDistrito == dist.idDistrito}">selected</c:if>>
                                                <c:out value="${dist.nombreDistrito}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label">Dirección / Calle</label>
                                    <input type="text" name="direccion" class="form-control" placeholder="Ej. Av. Larco 345"
                                           value="<c:out value='${usuarioEditar.datosPersonales.direccionPrincipal}'/>">
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-white d-flex justify-content-end gap-2 py-3 border-top">
                            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline-secondary">
                                Cancelar
                            </a>
                            <button type="submit" class="btn btn-success">
                                <c:choose>
                                    <c:when test="${esEdicion}">Guardar Cambios</c:when>
                                    <c:otherwise>Registrar Usuario</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>

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
