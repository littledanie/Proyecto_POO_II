<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${esEdicion}">Editar Producto</c:when><c:otherwise>Nuevo Producto</c:otherwise></c:choose> — Gestiona Commerce</title>

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
                    <a href="${pageContext.request.contextPath}/productos" class="btn btn-outline-secondary btn-sm">
                        &larr; Volver al Catálogo
                    </a>
                </div>
            </div>
        </header>

        <div class="page-wrapper">
            <div class="page-header d-print-none">
                <div class="container-xl">
                    <div class="row g-2 align-items-center">
                        <div class="col">
                            <div class="page-pretitle text-muted">CRUD de Productos &bull; Etapa 5 (Patrón Repository)</div>
                            <h2 class="page-title text-dark">
                                <c:choose>
                                    <c:when test="${esEdicion}">Editar Producto: <c:out value="${productoEditar.nombre}"/></c:when>
                                    <c:otherwise>Registrar Nuevo Producto / Mercadería</c:otherwise>
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

                    <form action="${pageContext.request.contextPath}/productos" method="POST" class="card shadow-sm border-0">
                        <input type="hidden" name="accion" value="guardar">
                        <input type="hidden" name="idProducto" value="${productoEditar.idProducto != null ? productoEditar.idProducto : 0}">

                        <div class="card-header bg-white py-3">
                            <h3 class="card-title fw-bold text-dark">Ficha Técnica y Comercial del Producto</h3>
                        </div>

                        <div class="card-body">
                            <!-- Sección 1: Identificación y Clasificación -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">1</span>
                                Identificación y Clasificación
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-3">
                                    <label class="form-label required">Código / SKU</label>
                                    <input type="text" name="codigo" class="form-control font-monospace" required placeholder="Ej. PROD-001"
                                           value="<c:out value='${productoEditar.codigo}'/>">
                                </div>
                                <div class="col-md-5">
                                    <label class="form-label required">Nombre del Producto</label>
                                    <input type="text" name="nombre" class="form-control" required placeholder="Ej. Laptop Dell Vostro 3510"
                                           value="<c:out value='${productoEditar.nombre}'/>">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label required">Tipo de Artículo</label>
                                    <select name="tipo" class="form-select" required>
                                        <option value="MERCADERIA" ${productoEditar.tipo == 'MERCADERIA' ? 'selected' : ''}>MERCADERIA (Reventa comercial)</option>
                                        <option value="PRODUCTO" ${productoEditar.tipo == 'PRODUCTO' ? 'selected' : ''}>PRODUCTO (Fabricación propia)</option>
                                        <option value="INSUMO" ${productoEditar.tipo == 'INSUMO' ? 'selected' : ''}>INSUMO (Materia prima)</option>
                                        <option value="SERVICIO" ${productoEditar.tipo == 'SERVICIO' ? 'selected' : ''}>SERVICIO (Intangible)</option>
                                    </select>
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Descripción Detallada</label>
                                    <textarea name="descripcion" class="form-control" rows="2" placeholder="Especificaciones técnicas o detalles del producto"><c:out value="${productoEditar.descripcion}"/></textarea>
                                </div>
                            </div>

                            <!-- Sección 2: Categorización y Unidades -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">2</span>
                                Categoría, Marca y Unidad de Medida
                            </h4>
                            <div class="row g-3 mb-4">
                                <div class="col-md-4">
                                    <label class="form-label required">Categoría</label>
                                    <select name="idCategoria" class="form-select" required>
                                        <option value="">-- Seleccionar Categoría --</option>
                                        <c:forEach var="cat" items="${categorias}">
                                            <option value="${cat.idCategoria}" ${productoEditar.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                                <c:out value="${cat.nombre}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Marca (opcional)</label>
                                    <select name="idMarca" class="form-select">
                                        <option value="">-- Sin Marca o Genérico --</option>
                                        <c:forEach var="m" items="${marcas}">
                                            <option value="${m.idMarca}" ${productoEditar.idMarca == m.idMarca ? 'selected' : ''}>
                                                <c:out value="${m.nombre}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label required">Unidad de Medida</label>
                                    <select name="idUnidadMedida" class="form-select" required>
                                        <option value="">-- Seleccionar Unidad --</option>
                                        <c:forEach var="um" items="${unidadesMedida}">
                                            <option value="${um.idUnidadMedida}" ${productoEditar.idUnidadMedida == um.idUnidadMedida ? 'selected' : ''}>
                                                <c:out value="${um.nombre}"/> (<c:out value="${um.abreviatura}"/>)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <!-- Sección 3: Precios y Existencias -->
                            <h4 class="text-success fw-bold mb-3 d-flex align-items-center gap-2">
                                <span class="badge bg-success-subtle text-success">3</span>
                                Precios y Control de Existencias (Stock)
                            </h4>
                            <div class="row g-3 mb-2">
                                <div class="col-md-3">
                                    <label class="form-label required">Precio Compra (S/)</label>
                                    <div class="input-group">
                                        <span class="input-group-text">S/</span>
                                        <input type="number" step="0.01" min="0" name="precioCompra" class="form-control" required
                                               value="${productoEditar.precioCompra != null ? productoEditar.precioCompra : '0.00'}">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label required">Precio Venta (S/)</label>
                                    <div class="input-group">
                                        <span class="input-group-text">S/</span>
                                        <input type="number" step="0.01" min="0" name="precioVenta" class="form-control" required
                                               value="${productoEditar.precioVenta != null ? productoEditar.precioVenta : '0.00'}">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label required">Stock Actual</label>
                                    <input type="number" min="0" name="stockActual" class="form-control" required
                                           value="${productoEditar.stockActual != null ? productoEditar.stockActual : 0}">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label required">Stock Mínimo (Alerta)</label>
                                    <input type="number" min="0" name="stockMinimo" class="form-control" required
                                           value="${productoEditar.stockMinimo != null ? productoEditar.stockMinimo : 5}">
                                </div>
                            </div>
                        </div>

                        <div class="card-footer bg-light d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/productos" class="btn btn-link link-secondary">Cancelar</a>
                            <button type="submit" class="btn btn-success px-4">
                                <c:choose>
                                    <c:when test="${esEdicion}">Actualizar Producto</c:when>
                                    <c:otherwise>Guardar Producto</c:otherwise>
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
