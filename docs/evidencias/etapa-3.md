# Evidencias — Etapa 3: Singleton, reestructuración de usuarios y validaciones

## Resumen de la Etapa
En esta etapa se implementó el patrón creacional **Singleton** para la gestión de conexiones a la base de datos, se migró el modelo de acceso al esquema normalizado en **3FN** (`rol`, `permiso`, `rol_permiso`, `usuario`) con roles RBAC, se integró el algoritmo de hash criptográfico **BCrypt** para la protección de contraseñas y se programó la regla de negocio de **bloqueo automático de cuenta** al acumular 3 intentos fallidos consecutivos.

---

## Pasos Realizados

### Paso 4 — Aplicar Singleton en la Conexión a BD
- Se creó `com.gestiona.commerce.config.ConexionBD` con constructor privado, atributo estático privado y método estático sincronizado `getInstancia()` (Thread-Safe con inicialización perezosa).
- Se refactorizó `LoginService` para solicitar conexiones físicas a través de `ConexionBD.getInstancia().getConexion()`.

### Paso 5 — Reestructuración de la Base de Datos a 3FN
- Se diseñó el script `database/etapa-3.sql` con las tablas:
  - `rol`: Catálogo de roles del sistema (5 roles canónicos).
  - `permiso`: Catálogo de permisos del sistema (9 permisos canónicos).
  - `rol_permiso`: Tabla de relación M:N con clave primaria compuesta.
  - `usuario`: Tabla de cuentas con clave foránea a `rol`, campo `contrasena_hash` (VARCHAR 255), contador `intentos_fallidos` y bandera booleana `bloqueado`.
- Se crearon las entidades POJO `Rol`, `Permiso` y se actualizó `Usuario`.

### Paso 6 — Validaciones de Seguridad (BCrypt y Bloqueo por 3 Intentos)
- Se añadió la dependencia `jbcrypt:0.4` en `pom.xml`.
- Se construyó `PasswordUtil` con métodos `hashear()` y `verificar()` utilizando factor de costo 12.
- Se implementó la clase `SeedUsuarios` para generar los hashes BCrypt reales a partir de las credenciales de prueba canónicas, eliminando cualquier hash simulado.
- Se programó la lógica en `LoginService` y `LoginServlet` para:
  1. Verificar si la cuenta ya está bloqueada antes de evaluar la contraseña.
  2. Incrementar el contador `intentos_fallidos` en BD con cada contraseña errónea.
  3. Cambiar `bloqueado = true` en BD al alcanzar 3 intentos fallidos.
  4. Restablecer `intentos_fallidos = 0` y actualizar `ultimo_acceso = now()` tras un login exitoso.
  5. Deshabilitar los campos del formulario en `index.jsp` cuando una cuenta queda bloqueada.

### Paso 7 — Análisis del Patrón Singleton
- Se elaboró el documento formal de análisis en bloques de código en `docs/analisis/singleton.md`, listo para ser incluido en el informe académico.

---

## Capturas solicitadas para el informe

📸 **CAPTURA 3.1** — Script SQL `etapa-3.sql` ejecutado en Supabase SQL Editor mostrando las tablas normalizadas (`rol`, `permiso`, `rol_permiso`, `usuario`) y los datos con hashes BCrypt
→ `![[etapa3_paso5_supabase_3fn.png]]`

📸 **CAPTURA 3.2** — Login exitoso con credenciales canónicas verificadas por BCrypt (ej. `lfernandez` con `Admin#2026`)
→ `![[etapa3_paso6_login_bcrypt_exito.png]]`

📸 **CAPTURA 3.3** — Pantalla de bienvenida (`/bienvenida`) mostrando el rol asociado (`Administrador`) obtenido desde el esquema 3FN
→ `![[etapa3_paso6_bienvenida_rol.png]]`

📸 **CAPTURA 3.4** — Intento fallido de login mostrando el mensaje informativo con contador de intentos restantes
→ `![[etapa3_paso6_intento_fallido.png]]`

📸 **CAPTURA 3.5** — Alerta y bloqueo del formulario en `index.jsp` tras fallar 3 veces consecutivas
→ `![[etapa3_paso6_cuenta_bloqueada.png]]`

📸 **CAPTURA 3.6** — Consulta en Supabase SQL Editor (`SELECT nombre_usuario, intentos_fallidos, bloqueado FROM usuario;`) confirmando que el usuario quedó con `intentos_fallidos = 3` y `bloqueado = true` en la base de datos
→ `![[etapa3_paso6_bd_bloqueo.png]]`

---

## Resultados esperados
- Ninguna contraseña en texto plano existe en la base de datos de Supabase; todas residen como cadenas seguras de 60 caracteres que inician con `$2a$12$`.
- El patrón Singleton garantiza una sola instancia de configuración en el servidor Tomcat.
- Al ingresar una contraseña errónea, el sistema descuenta intentos (`Te quedan X intento(s) antes del bloqueo de cuenta`).
- Al tercer intento fallido, el usuario queda marcado como `bloqueado = true` en PostgreSQL y la interfaz deshabilita nuevos envíos.
- Un usuario bloqueado (como el usuario de prueba `amendoza`) no puede iniciar sesión aunque ingrese su contraseña correcta hasta que el administrador lo desbloquee.

---

## Problemas encontrados y cómo se resolvieron

| # | Problema | Solución |
|---|----------|----------|
| 1 | Concurrencia multihilo en Tomcat al acceder a la conexión Singleton. | Se añadió la cláusula `synchronized` al método `ConexionBD.getInstancia()`, asegurando que el acceso sea Thread-Safe bajo múltiples peticiones concurrentes. |
| 2 | Requisito estricto de no usar hashes inventados (AGENTS.md §6.5). | Se ejecutó la utilidad `SeedUsuarios` con `PasswordUtil` en Java para computar los hashes BCrypt canónicos con costo 12 que fueron grabados en `database/etapa-3.sql`. |
| 3 | Persistencia del estado de bloqueo entre reinicios del servidor. | El contador de intentos fallidos y la bandera de bloqueo se persisten directamente en la tabla `usuario` de PostgreSQL, impidiendo que el bloqueo se pierda al reiniciar la aplicación. |
