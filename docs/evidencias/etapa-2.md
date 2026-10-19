# Evidencias — Etapa 2: Verificación de la conexión con la BD

## Paso 3 — Verificación de la conexión con la BD (Supabase + Tomcat)

### Acciones realizadas
1. **Actualización de dependencias (`pom.xml`)**:
   - Se añadió el driver oficial JDBC de PostgreSQL: `org.postgresql:postgresql:42.7.2`.
2. **Creación del script SQL (`database/etapa-2.sql`)**:
   - DDL para la tabla provisional `usuarios` con campos `id_usuario`, `nombre_usuario` y `contrasena` (en texto plano para esta etapa, como requiere la progresión pedagógica del curso).
   - Inserción de 6 usuarios de prueba canónicos (según la Sección 5 de `13_2_Modulos_basicos_Formas_Normales.md`).
3. **Desarrollo de la arquitectura MVC (Java)**:
   - `ConexionDirectaBD.java`: Conexión JDBC directa mediante `DriverManager.getConnection()`, leyendo las credenciales de variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`) con soporte fallback al archivo local `.env`.
   - `Usuario.java`: Entidad POJO representativa del usuario.
   - `LoginService.java`: Servicio de validación de credenciales con `PreparedStatement` para prevenir inyección SQL.
   - `LoginServlet.java`: Controlador que atiende solicitudes `POST /login`, valida los campos, procesa el inicio de sesión y aplica redirección PRG hacia `/bienvenida`.
   - `BienvenidaServlet.java`: Controlador que protege el acceso a la vista interna `/WEB-INF/views/bienvenida.jsp`, permitiendo el acceso solo a usuarios con sesión activa.
   - `LogoutServlet.java`: Controlador que invalida la sesión HTTP y redirige al login.
4. **Desarrollo de la interfaz de bienvenida**:
   - `src/main/webapp/WEB-INF/views/bienvenida.jsp`: Vista protegida accesible únicamente mediante forward desde el servlet, con diseño coherente en tonos slate y verde stock.
   - `src/main/webapp/assets/css/bienvenida.css`: Estilos visuales con tipografía Inter y layout responsivo.
5. **Configuración del descriptor de despliegue (`web.xml`)**:
   - Registro y mapeo formal de los tres servlets (`/login`, `/bienvenida`, `/logout`).

---

### Capturas solicitadas para el informe

📸 **CAPTURA 2.1** — Script SQL ejecutado en Supabase SQL Editor mostrando la tabla `usuarios` y los datos insertados
→ `![[etapa2_paso3_supabase_tabla.png]]`

📸 **CAPTURA 2.2** — Formulario de login en el navegador con credenciales válidas ingresadas (ej. usuario: `lfernandez`, contraseña: `Admin#2026`)
→ `![[etapa2_paso3_login_credenciales.png]]`

📸 **CAPTURA 2.3** — Login mostrando mensaje de error ante contraseña incorrecta ("Usuario o contraseña incorrectos.")
→ `![[etapa2_paso3_login_error.png]]`

📸 **CAPTURA 2.4** — Pantalla de bienvenida cargada tras autenticación exitosa (`/bienvenida`), mostrando el nombre del usuario logueado
→ `![[etapa2_paso3_bienvenida_exito.png]]`

📸 **CAPTURA 2.5** — Login exitoso y pantalla de bienvenida funcionando desde la URL pública de Render contra la base de datos Supabase
→ `![[etapa2_paso3_render_supabase_publico.png]]`

---

### Resultados esperados
- Al ejecutar `database/etapa-2.sql` en Supabase, la tabla `usuarios` queda creada con 6 registros de prueba.
- Al ingresar credenciales erróneas en el login, se muestra la alerta en rojo con texto genérico seguro sin filtrar detalles del sistema.
- Al ingresar credenciales correctas, el sistema crea la sesión y redirige a `/bienvenida`, donde se visualiza el saludo personalizado y el estado de la conexión.
- Al hacer clic en "Salir", la sesión se invalida y el navegador vuelve al formulario de login. Si se intenta entrar directamente a `/bienvenida` sin autenticarse, el sistema redirige automáticamente al login.

---

### Problemas encontrados y cómo se resolvieron

| # | Problema | Solución |
|---|----------|----------|
| 1 | Protección de vistas internas (`bienvenida.jsp`). | Se ubicó la vista dentro de `/WEB-INF/views/`, impidiendo el acceso directo por URL pública, y se creó el controlador `BienvenidaServlet` que realiza un `forward` solo si existe `session.getAttribute("usuarioLogueado")`. |
| 2 | Reenvío accidental del formulario de login al actualizar página (F5). | Se implementó el patrón PRG (Post-Redirect-Get): tras validar el login en el `POST`, el servlet realiza un `sendRedirect` hacia `/bienvenida`. |
| 3 | Manejo seguro de credenciales de base de datos sin hardcodear. | Se implementó lectura de variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`) en `ConexionDirectaBD.java`, manteniendo `.env` protegido por `.gitignore`. |
