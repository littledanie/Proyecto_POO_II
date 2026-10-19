<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso no autorizado | Gestiona Commerce</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/core@1.0.0-beta20/dist/css/tabler.min.css">
    <style>
        :root {
            --tblr-font-sans-serif: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            --tblr-primary: #059669;
        }
    </style>
</head>
<body class="border-top-wide border-warning d-flex flex-column bg-light min-vh-100 justify-content-center">
    <div class="page page-center">
        <div class="container-tight py-4">
            <div class="empty">
                <div class="empty-header display-1 fw-bold text-warning">403</div>
                <p class="empty-title fs-2 fw-bold text-dark">Acceso restringido</p>
                <p class="empty-subtitle text-muted">
                    No dispones de los permisos suficientes para acceder a este recurso o ejecutar esta operación.
                </p>
                <div class="empty-action mt-4">
                    <a href="${pageContext.request.contextPath}/bienvenida" class="btn btn-success d-inline-flex align-items-center gap-2">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <line x1="19" y1="12" x2="5" y2="12"></line>
                            <polyline points="12 19 5 12 12 5"></polyline>
                        </svg>
                        <span>Volver al inicio</span>
                    </a>
                </div>
            </div>
            <div class="text-center text-muted small mt-5">
                &copy; 2026 Gestiona Commerce
            </div>
        </div>
    </div>
</body>
</html>
