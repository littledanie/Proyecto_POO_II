# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12)
- **Fase actual:** Etapa 2 completada — código y scripts listos, pendiente de ejecución del SQL en Supabase, configuración de variables en Render y toma de capturas
- **Servidor / Runtime:** Java 17 + Apache Tomcat 10.1 (`jakarta.*`) + Maven WAR
- **Base de Datos:** PostgreSQL en Supabase (acceso JDBC puro 42.7) — script `database/etapa-2.sql` creado
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
- [ ] **Paso 4:** Aplicar patrón creacional Singleton en la conexión a BD (`ConexionBD`).
- [ ] **Paso 5:** Reestructurar tabla `usuario` en 3FN (`rol`, `permiso`, `rol_permiso`, `usuario`) con datos canónicos.
- [ ] **Paso 6:** Validaciones de seguridad (bloqueo tras 3 intentos erróneos, contraseñas con hash BCrypt mediante `SeedUsuarios`).
- [ ] **Paso 7:** Análisis en bloques del patrón Singleton (`docs/analisis/singleton.md`).

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
| DEC-12 | Conexión directa en `ConexionDirectaBD` con lectura dual (`System.getenv` + fallback `.env`). | Facilita ejecución tanto en contenedor (Render) como en entorno local sin comprometer credenciales en Git. Conexión directa obligatoria en Etapa 2 antes de migrar a Singleton en Etapa 3. |
| DEC-13 | Redirección PRG (Post-Redirect-Get) tras login hacia `/bienvenida`. | Evita el reenvío del formulario de autenticación si el usuario refresca con F5. |
| DEC-14 | Mensaje de error genérico en el login ("Usuario o contraseña incorrectos"). | Cumplimiento del estándar de seguridad (AGENTS.md §7) para no revelar si falló el usuario o la clave. |

---

## 4. Archivos clave del proyecto
| Archivo | Propósito |
|---|---|
| `AGENTS.md` | Reglas permanentes y estándares de entrega. |
| `Etapas.md` | Guía de etapas 1 a 5 y checklist de entrega. |
| `13_2_Modulos_basicos_Formas_Normales.md` | Esquema de BD canónico, datos de prueba y arquitectura de clases. |
| `PROGRESO.md` | Estado actual y registro de traspaso entre modelos. |
| `pom.xml` | Proyecto Maven WAR (Java 17, Jakarta Servlet 6.0, JSTL 3.0, PostgreSQL JDBC 42.7.2). |
| `Dockerfile` | Build multi-stage: Maven compila → Tomcat 10.1 sirve el WAR. |
| `database/etapa-2.sql` | DDL provisional de `usuarios` y 6 usuarios de prueba con contraseña texto plano. |
| `src/main/java/com/gestiona/commerce/config/ConexionDirectaBD.java` | Conexión JDBC directa a Supabase (sin Singleton aún). |
| `src/main/java/com/gestiona/commerce/model/entity/Usuario.java` | Entidad POJO de usuario para la Etapa 2. |
| `src/main/java/com/gestiona/commerce/service/LoginService.java` | Servicio de autenticación con `PreparedStatement`. |
| `src/main/java/com/gestiona/commerce/controller/LoginServlet.java` | Controlador de autenticación (`/login`). |
| `src/main/java/com/gestiona/commerce/controller/BienvenidaServlet.java` | Controlador que protege `/WEB-INF/views/bienvenida.jsp`. |
| `src/main/java/com/gestiona/commerce/controller/LogoutServlet.java` | Controlador para invalidar sesión (`/logout`). |
| `src/main/webapp/index.jsp` | Formulario de login. |
| `src/main/webapp/assets/css/login.css` | Estilos del login. |
| `src/main/webapp/WEB-INF/views/bienvenida.jsp` | Vista de bienvenida protegida tras login exitoso. |
| `src/main/webapp/assets/css/bienvenida.css` | Estilos de la vista de bienvenida. |
| `src/main/webapp/WEB-INF/web.xml` | Descriptor de despliegue con servlets mapeados. |
| `docs/evidencias/etapa-1.md` | Evidencias de la Etapa 1. |
| `docs/evidencias/etapa-2.md` | Evidencias y marcadores de captura de la Etapa 2. |

---

## 5. Cómo ejecutar el proyecto

### Local (NetBeans + Tomcat)
1. Abrir el proyecto en Apache NetBeans como proyecto Maven.
2. Asegurar que las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` estén configuradas (o el archivo `.env` en la raíz).
3. Ejecutar (Run) en Apache Tomcat 10.1.
4. Acceder a `http://localhost:8080/`.

### Render (producción)
1. En el panel de Render, ir al servicio `gestiona-commerce` -> **Environment**.
2. Agregar las variables:
   - `DB_URL`: `jdbc:postgresql://<HOST_POOLER>:5432/postgres?sslmode=require`
   - `DB_USER`: `postgres.<PROJECT_REF>`
   - `DB_PASSWORD`: `<PASSWORD_DE_SUPABASE>`
3. Guardar cambios (Render re-desplegará automáticamente).
4. Acceder al link público.

---

## 6. Pendientes y bloqueos actuales
- **Acción requerida de la estudiante en Supabase:**
  1. Abrir Supabase SQL Editor y ejecutar el script `database/etapa-2.sql`.
  2. Si no recuerda la contraseña de la base de datos o sale error de autenticación, restablecerla en **Project Settings -> Database -> Reset database password**, y actualizarla en el `.env` local y en las variables de entorno de Render.
- **Acción requerida de la estudiante en Render:**
  Configurar las 3 variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`) en la sección **Environment** del Web Service.
- **Acción requerida de evidencias:**
  Tomar las 5 capturas indicadas en `docs/evidencias/etapa-2.md`.

---

## 7. Próximo paso exacto
- Tras la validación de la estudiante de las capturas y pruebas de la Etapa 2, iniciar la **Etapa 3**:
  1. **Paso 4:** Implementar el patrón creacional **Singleton** en `com.gestiona.commerce.config.ConexionBD`.
  2. **Paso 5:** Reestructurar la base de datos a 3FN (`database/etapa-3.sql`) con las tablas `rol`, `permiso`, `rol_permiso` y `usuario`.
  3. **Paso 6:** Migrar contraseñas a BCrypt con sal (clase `SeedUsuarios` y `PasswordUtil`) e implementar bloqueo automático tras 3 intentos fallidos.
  4. **Paso 7:** Redactar el análisis en bloques del patrón Singleton en `docs/analisis/singleton.md`.
