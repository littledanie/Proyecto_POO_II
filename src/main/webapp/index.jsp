<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestiona Commerce — Prueba de despliegue</title>
    <style>
        body {
            font-family: system-ui, -apple-system, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background: #0f172a;
            color: #e2e8f0;
        }
        .card {
            text-align: center;
            padding: 2rem 3rem;
            border: 1px solid #334155;
            border-radius: 12px;
            background: #1e293b;
        }
        h1 { font-size: 1.5rem; margin-bottom: .5rem; }
        p  { color: #94a3b8; }
    </style>
</head>
<body>
    <div class="card">
        <h1>Gestiona Commerce</h1>
        <p>Despliegue exitoso en Tomcat 10.1</p>
        <p><small>JSP funcionando correctamente &mdash; <%= new java.util.Date() %></small></p>
    </div>
</body>
</html>
