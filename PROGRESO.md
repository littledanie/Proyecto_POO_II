# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12)
- **Fase actual:** Etapa 3 completada — Singleton, esquema 3FN (RBAC), BCrypt, bloqueo automático tras 3 intentos y análisis en bloques listos. Pendiente de ejecución de `database/etapa-3.sql` en Supabase y toma de capturas.
- **Servidor / Runtime:** Java 17 + Apache Tomcat 10.1 (`jakarta.*`) + Maven WAR
- **Base de Datos:** PostgreSQL en Supabase (acceso JDBC puro 42.7 con Singleton)
- **Hosting:** Render (Web Service con Docker multi-stage, plan Free) — auto-deploy conectado a GitHub
- **IDE:** Apache NetBeans con soporte Maven y JDK 17
- **Repositorio:** `https://github.com/littledanie/Proyecto_POO_II`

---

## 2. Checklist detallado de pasos (Avance 1)

### Etapa 1 — Login y publicación
- [x] **Paso 1:** GUI del login (`index.jsp` + `login.css`) — diseño split-panel profesional con paleta slate/verde, Inter, SVG icons, responsive, accesible.
- [x] **Paso 2:** Publicación en hosting — `Dockerfile` multi-stage (Maven + Tomcat 10.1), `.dockerignore`, servicio en Render verificado con link público funcional.

### Etapa 2 — Verificación de la conexión con la BD
- [x] **Paso 3:** Verificación de la conexión con la BD (script `database/etapa-2.sql`, driver PostgreSQL en `pom.xml`, conexión directa `ConexionDirectaBD`, validación con `PreparedStatement`, `LoginServlet`, vista protegida `WEB-INF/views/bienvenida.jsp` y `BienvenidaServlet`).

### Etapa 3 — Singleton, reestructuración de usuarios y validaciones
- [x] **Paso 4:** Aplicar patrón creacional Singleton en la conexión a BD (`ConexionBD.java` con constructor privado, instancia única y `getInstancia()` sincronizado).
- [x] **Paso 5:** Reestructurar tabla `usuario` en 3FN (`rol`, `permiso`, `rol_permiso`, `usuario`) con datos canónicos en `database/etapa-3.sql` y entidades POJO.
- [x] **Paso 6:** Validaciones de seguridad (bloqueo tras 3 intentos erróneos persistido en BD, contraseñas con hash BCrypt mediante `SeedUsuarios` y `PasswordUtil`).
- [x] **Paso 7:** Análisis en bloques del patrón Singleton (`docs/analisis/singleton.md`) listo para copiar en Google Docs.

### Etapa 4 — Dashboard y CRUD de usuarios con Repository
- [ ] **Paso 8:** Dashboard con plantilla Bootstrap para CRUD de usuarios (incorporando datos personales, contacto y ubicación).
- [ ] **Paso 9:** Aplicación del patrón Repository en el CRUD de usuarios.
- [ ] **Paso 10:** Análisis en bloques del patrón Repository usuarios (`docs/analisis/repository-usuarios.md`).

### Etapa 5 — CRUD de clientes, proveedores y mercadería
- [ ] **Paso 11:** CRUD con Repository para Clientes, Proveedores y Mercadería/Productos/Insumos/Servicios.
- [ ] **Paso 12:** Análisis en bloques del patrón Repository para entidades comerciales (`docs/analisis/repository-clientes-proveedores-productos.md`).

---

## 3. Decisiones tomadas
| # | Decisión | Motivo / Justificación |
|---|---|---|
| DEC-01 | Seguir estrictamente el orden de lectura y reglas de `AGENTS.md`. | Garantizar consistencia entre sesiones y modelos de IA. |
| DEC-02 | Avance 1 restringido únicamente a Etapas 1–5 (hasta Paso 12). | Excluir ventas, reportes y tecnologías no solicitadas según requerimientos. |
| DEC-03 | Trabajo incremental estricto: Paso a paso con commit individual en español y tag por etapa. | La estudiante requiere evidencias pedagógicas paso a paso para su evaluación. |
| DEC-04 | Archivos de indicaciones (`AGENTS.md`, `Etapas.md`, `13_2_...md`) en la raíz del proyecto. | La raíz del workspace es el repositorio clonado; simplifica rutas y acceso. |
| DEC-05 | Paleta slate (#334155) + verde stock (#059669) con tipografía Inter. | Basada en la skill de diseño (perfil "Inventory & Stock Management"); evita degradados morados genéricos. |
| DEC-06 | Login con CSS propio (sin Bootstrap) para Etapa 1; Bootstrap se usará desde Etapa 4 (dashboard). | Máximo control visual en el login; Bootstrap con plantilla dashboard llega en la etapa que corresponde. |
| DEC-07 | JSTL 3.0: API (`jakarta.servlet.jsp.jstl-api:3.0.0`) + implementación Glassfish (`jakarta.servlet.jsp.jstl:3.0.1`) en pom.xml. | Tomcat 10.1 no incluye JSTL; se necesitan ambas dependencias para que `<c:...>` funcione. |
| DEC-08 | `maven.compiler.release=17` en pom.xml; Dockerfile usa `jdk17`. | El proyecto debe compilar con API de Java 17 por requisito del curso. |
| DEC-09 | `bienvenida.jsp` dentro de `WEB-INF/views/`; acceso solo vía forward desde Servlet, nunca sendRedirect directo. | Proteger las vistas internas; patrón MVC estricto. |
| DEC-10 | Primer deploy en Render con JSP mínimo antes de diseñar el login. | Validar que el hosting ejecuta JSP/Tomcat antes de invertir tiempo en diseño (AGENTS.md §10). |
| DEC-11 | Driver PostgreSQL JDBC `42.7.2` agregado en `pom.xml`. | Requerido para conectar Tomcat 10.1 con Supabase mediante JDBC estándar. |
| DEC-12 | Conexión directa en `ConexionDirectaBD` con lectura dual (`System.getenv` + fallback `.env`). | Facilita ejecución tanto en contenedor (Render) como en entorno local sin comprometer credenciales en Git. |
| DEC-13 | Redirección PRG (Post-Redirect-Get) tras login hacia `/bienvenida`. | Evita el reenvío del formulario de autenticación si el usuario refresca con F5. |
| DEC-14 | Mensaje de error genérico en el login ("Usuario o contraseña incorrectos"). | Cumplimiento del estándar de seguridad (AGENTS.md §7) para no revelar si falló el usuario o la clave. |
| DEC-15 | Patrón Singleton en `ConexionBD` con constructor privado e inicialización perezosa sincronizada (`synchronized`). | Garantiza una sola instancia en la JVM para Tomcat (Thread-Safe), centralizando la configuración JDBC. |
| DEC-16 | BCrypt (`jbcrypt:0.4`) con factor de costo 12 para todas las contraseñas en BD. | Cumplimiento estricto de seguridad: sal criptográfica aleatoria, jamás texto plano. |
| DEC-17 | Generación de hashes reales mediante `SeedUsuarios` a partir de datos canónicos. | Se prohíbe pegar hashes simulados; los 6 usuarios de prueba se generaron computacionalmente con BCrypt. |
| DEC-18 | Bloqueo persistente de cuenta en BD tras 3 intentos fallidos consecutivos. | Cumplimiento de regla de negocio evaluada: la cuenta se marca `bloqueado = true` en PostgreSQL y se inhabilita el login. |

---

## 4. Archivos clave del proyecto
| Archivo | Propósito |
|---|---|
| `AGENTS.md` | Reglas permanentes y estándares de entrega. |
| `Etapas.md` | Guía de etapas 1 a 5 y checklist de entrega. |
| `13_2_Modulos_basicos_Formas_Normales.md` | Esquema de BD canónico, datos de prueba y arquitectura de clases. |
| `PROGRESO.md` | Estado actual y registro de traspaso entre modelos. |
| `pom.xml` | Proyecto Maven WAR (Java 17, Jakarta Servlet 6.0, JSTL 3.0, PostgreSQL JDBC 42.7.2, BCrypt 0.4). |
| `Dockerfile` | Build multi-stage: Maven compila → Tomcat 10.1 sirve el WAR. |
| `database/etapa-2.sql` | DDL provisional Etapa 2 (texto plano para evidenciar evolución pedagógica). |
| `database/etapa-3.sql` | DDL 3FN (`rol`, `permiso`, `rol_permiso`, `usuario`) con hashes BCrypt reales y control de bloqueo. |
| `src/main/java/com/gestiona/commerce/config/ConexionBD.java` | Patrón creacional **Singleton** para conexión JDBC a Supabase. |
| `src/main/java/com/gestiona/commerce/util/PasswordUtil.java` | Utilidad de hashing y verificación con BCrypt (rounds=12). |
| `src/main/java/com/gestiona/commerce/util/SeedUsuarios.java` | Generador canónico de hashes para inserción en BD. |
| `src/main/java/com/gestiona/commerce/model/entity/Rol.java` | Entidad POJO Rol (RBAC). |
| `src/main/java/com/gestiona/commerce/model/entity/Permiso.java` | Entidad POJO Permiso (RBAC). |
| `src/main/java/com/gestiona/commerce/model/entity/Usuario.java` | Entidad POJO Usuario con soporte 3FN, rol y bloqueo. |
| `src/main/java/com/gestiona/commerce/service/ResultadoLogin.java` | Encapsulador de estados de autenticación (éxito, inválido, bloqueado). |
| `src/main/java/com/gestiona/commerce/service/LoginService.java` | Lógica de autenticación con BCrypt, contador y bloqueo tras 3 intentos. |
| `src/main/java/com/gestiona/commerce/controller/LoginServlet.java` | Controlador de autenticación con manejo de bloqueo y PRG. |
| `src/main/java/com/gestiona/commerce/controller/BienvenidaServlet.java` | Controlador que protege `/WEB-INF/views/bienvenida.jsp`. |
| `src/main/java/com/gestiona/commerce/controller/LogoutServlet.java` | Controlador para invalidar sesión (`/logout`). |
| `src/main/webapp/index.jsp` | Formulario de login con soporte para bloqueo visual y deshabilitación. |
| `src/main/webapp/assets/css/login.css` | Estilos del login. |
| `src/main/webapp/WEB-INF/views/bienvenida.jsp` | Vista de bienvenida mostrando el rol y estado Singleton + BCrypt. |
| `src/main/webapp/assets/css/bienvenida.css` | Estilos de la vista de bienvenida. |
| `src/main/webapp/WEB-INF/web.xml` | Descriptor de despliegue con servlets mapeados. |
| `docs/analisis/singleton.md` | Análisis formal en 5 bloques del patrón Singleton para informe. |
| `docs/evidencias/etapa-1.md` | Evidencias y capturas de la Etapa 1. |
| `docs/evidencias/etapa-2.md` | Evidencias y capturas de la Etapa 2. |
| `docs/evidencias/etapa-3.md` | Evidencias y capturas de la Etapa 3. |

---

## 5. Cómo ejecutar el proyecto

### Supabase
- Ejecutar el script `database/etapa-3.sql` en Supabase SQL Editor para crear las tablas 3FN e insertar los datos con hashes BCrypt.

### Local (NetBeans + Tomcat)
1. Abrir el proyecto en Apache NetBeans como proyecto Maven.
2. Contar con `.env` configurado en la raíz con `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. Ejecutar (Run) en Apache Tomcat 10.1.

### Render (producción)
- Con las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` en **Environment**, Render auto-despliega cada push a `main`.

---

## 6. Pendientes y bloqueos actuales
- **Acción requerida de la estudiante en Supabase:**
  Ejecutar el script `database/etapa-3.sql` en el SQL Editor de Supabase.
- **Acción requerida de evidencias:**
  Tomar las 6 capturas indicadas en `docs/evidencias/etapa-3.md`.

---

## 7. Próximo paso exacto
- Tras la confirmación de la estudiante, iniciar la **Etapa 4 (Dashboard y CRUD de usuarios con Repository)**:
  1. **Paso 8:** Seleccionar e integrar una plantilla de dashboard Bootstrap de licencia libre (AdminLTE o Tabler) para el CRUD de usuarios con datos personales, contacto y ubicación.
  2. **Paso 9:** Aplicar el patrón creacional/estructural **Repository** (`UsuarioRepository` y `UsuarioRepositoryImpl`).
  3. **Paso 10:** Elaborar el análisis en bloques de código del patrón Repository de usuarios en `docs/analisis/repository-usuarios.md`.
