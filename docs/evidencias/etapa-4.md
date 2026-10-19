# Evidencias — Etapa 4: Dashboard y CRUD de usuarios con Repository

## Resumen de la Etapa
En esta etapa se diseñó e integró un **Dashboard administrativo profesional** basado en la plantilla de código abierto **Tabler UI (Licencia MIT, basada en Bootstrap 5)**, se implementó el patrón de diseño **Repository** (`UsuarioRepository` y `UsuarioRepositoryImpl`), se amplió el modelo de datos en PostgreSQL para incorporar **datos personales, contacto y ubicación geográfica**, y se construyó el CRUD completo con validaciones y eliminación lógica (`estado = 'Inactivo'`).

---

## Pasos Realizados

### Paso 8 — Dashboard: CRUD del Usuario y Entidades de Datos Personales
- Se elaboró el script `database/etapa-4.sql` que crea las tablas:
  - Catálogos: `sexo`, `estado_civil`.
  - Jerarquía territorial en 3FN: `pais`, `departamento`, `provincia`, `distrito`.
  - Persona compartida: `datos_personales`.
  - Canales de contacto: `correo_electronico`, `telefono`, `direccion`.
  - `ALTER TABLE usuario ADD COLUMN id_datos_personales INT UNIQUE REFERENCES datos_personales(id_datos_personales)`.
- Se crearon las entidades POJO correspondientes (`DatosPersonales`, `Sexo`, `EstadoCivil`, `Distrito`) y se actualizó `Usuario`.
- Se diseñaron las interfaces JSP con la plantilla **Tabler UI (Licencia MIT)**:
  - `usuarios-lista.jsp`: Vista tabular moderna con métricas, badges por rol y estado, y acciones contextuales.
  - `usuarios-form.jsp`: Formulario estructurado en 3 secciones (Cuenta, Persona, Contacto/Ubicación).

### Paso 9 — Aplicación del Patrón Repository en el CRUD del Usuario
- Se definió la interfaz `UsuarioRepository` con operaciones de colección (`listarTodos`, `buscarPorId`, `registrar`, `actualizar`, `eliminarLogico`, `cambiarEstadoBloqueo`).
- Se implementó `UsuarioRepositoryImpl` con JDBC puro y `PreparedStatement`, utilizando transacciones atómicas (`setAutoCommit(false)`, `commit()`, `rollback()`) para operaciones compuestas entre usuario y persona.
- Se implementó el servicio `UsuarioService` que aplica validaciones de negocio, unicidad de nombre de usuario y hashing de contraseñas con BCrypt.
- Se desarrolló el controlador `UsuarioServlet` (mapeado a `/usuarios`) con manejo de mensajes flash en sesión.

### Paso 10 — Análisis del Patrón Repository
- Se redactó el análisis detallado en 6 bloques de código en `docs/analisis/repository-usuarios.md`, listo para ser incluido en el informe académico.

---

## Capturas solicitadas para el informe

📸 **CAPTURA 4.1** — Script SQL `etapa-4.sql` ejecutado en Supabase SQL Editor mostrando las tablas creadas y la consulta de verificación con `id_datos_personales`
→ `![[etapa4_paso8_supabase_etapa4.png]]`

📸 **CAPTURA 4.2** — Vista principal del Dashboard de Usuarios (`/usuarios`) mostrando la lista con avatares, roles, datos personales y badges de estado
→ `![[etapa4_paso8_dashboard_lista_usuarios.png]]`

📸 **CAPTURA 4.3** — Formulario de registro (`/usuarios?accion=nuevo`) con los campos de cuenta, datos personales, contacto y distrito completados
→ `![[etapa4_paso9_formulario_nuevo_usuario.png]]`

📸 **CAPTURA 4.4** — Alerta de éxito tras registrar el usuario y visualización del nuevo registro incorporado en la tabla
→ `![[etapa4_paso9_usuario_registrado_exito.png]]`

📸 **CAPTURA 4.5** — Formulario de edición (`/usuarios?accion=editar&id=X`) con los datos cargados y confirmación de actualización exitosa
→ `![[etapa4_paso9_usuario_editado.png]]`

📸 **CAPTURA 4.6** — Eliminación lógica: fila del usuario mostrando badge `Inactivo` tras desactivarlo, sin haber sido borrado físicamente de la base de datos
→ `![[etapa4_paso9_eliminacion_logica.png]]`

---

## Resultados esperados
- La vista de usuarios se renderiza con estilo dashboard profesional usando Tabler UI / Bootstrap 5, responsiva y en idioma español.
- El CRUD permite registrar usuarios junto con sus datos personales, correo, teléfono y dirección en una sola transacción atómica protegida por el Repository.
- Las contraseñas de los nuevos usuarios se cifran automáticamente con BCrypt antes de ingresar a la base de datos.
- Al desactivar un usuario, su estado pasa a `Inactivo` (eliminación lógica), manteniéndose accesible para auditoría.
- La cuenta bloqueada tras 3 intentos erróneos puede ser desbloqueada mediante un clic desde el botón "Desbloquear" de la tabla.

---

## Problemas encontrados y cómo se resolvieron

| # | Problema | Solución |
|---|----------|----------|
| 1 | Inserción compuesta en múltiples tablas (`datos_personales` + `usuario` + `contacto`). | Se encapsuló la operación dentro de una transacción JDBC con `setAutoCommit(false)` en `UsuarioRepositoryImpl`, asegurando que si una parte falla, se ejecuta `rollback()`. |
| 2 | Elección de plantilla dashboard con licencia libre requerida (AGENTS.md §9). | Se seleccionó **Tabler UI**, plantilla moderna de código abierto bajo licencia **MIT** basada en **Bootstrap 5**, que no requiere dependencias pesadas como jQuery y mantiene la coherencia visual con la paleta slate y esmeralda del proyecto. |
| 3 | Preservación de contraseña al editar datos personales. | En `actualizarUsuario`, si el campo de contraseña se deja vacío, el repositorio conserva el hash BCrypt existente sin sobreescribirlo. |
