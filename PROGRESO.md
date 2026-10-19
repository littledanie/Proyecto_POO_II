# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12) — **COMPLETADO AL 100%**
- **Fase actual:** Limpieza final de interfaz completada — Producto comercial "Gestiona Commerce" orientado a usuario final, sin rastros académicos ni técnicos. Navegación y KPI basados en permisos, dashboard con stock crítico real, métricas limpias, títulos dinámicos, manejo seguro de errores (404/500 sin trazas), licencias en `docs/LICENSE-THIRD-PARTY.md` y tags `pre-limpieza` y `avance-1-final`.
- **Servidor / Runtime:** Java 17 (`--release 17`) + Apache Tomcat 10.1 (`jakarta.*`) + Maven WAR
- **Base de Datos:** PostgreSQL en Supabase (JDBC puro 42.7.2 con Singleton y Repository)
- **Hosting:** Render (Web Service con Docker multi-stage, plan Free) — auto-deploy conectado a GitHub
- **IDE:** Apache NetBeans con soporte Maven y JDK 17
- **Repositorio:** `https://github.com/littledanie/Proyecto_POO_II`
- **Link público verificado:** `https://gestiona-commerce.onrender.com`

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
- [x] **Paso 8:** Dashboard con plantilla Bootstrap para CRUD de usuarios con datos personales, contacto y ubicación (`database/etapa-4.sql`, vistas `usuarios-lista.jsp` y `usuarios-form.jsp` con Tabler UI / Bootstrap 5, licencia MIT).
- [x] **Paso 9:** Aplicación del patrón Repository en el CRUD de usuarios (`UsuarioRepository` y `UsuarioRepositoryImpl` con transacciones ACID y eliminación lógica).
- [x] **Paso 10:** Análisis en bloques del patrón Repository usuarios (`docs/analisis/repository-usuarios.md`) listo para Google Docs.

### Etapa 5 — CRUD de clientes, proveedores y mercadería
- [x] **Paso 11:** CRUD con Repository para Clientes, Proveedores y Mercadería/Productos/Insumos/Servicios (`database/etapa-5.sql`, entidades POJO, repositorios con JDBC puro y transacciones, servicios con validaciones de negocio y vistas Tabler UI).
- [x] **Paso 12:** Análisis en bloques del patrón Repository para entidades comerciales (`docs/analisis/repository-clientes-proveedores-productos.md`) y guía de evidencias `docs/evidencias/etapa-5.md`.

### Hito Especial — Limpieza comercial y preparación para entrega
- [x] **Tag pre-limpieza:** Creado antes de iniciar las modificaciones de limpieza visual.
- [x] **Permisos en navegación y dashboard:** Menú de opciones y accesos rápidos condicionados estrictamente por los permisos del usuario (`GESTIONAR_USUARIOS`, `VER_CLIENTES`, `VER_PROVEEDORES`, `VER_PRODUCTOS`), no por el nombre del rol.
- [x] **Dashboard con datos reales:** Tarjetas KPI con conteos reales obtenidos de BD y tabla de "Alerta de Stock Crítico" (`Activo`, `tipo <> 'SERVICIO'`, `stock_minimo > 0` y `stock_actual <= stock_minimo`) visible solo con permiso `VER_PRODUCTOS`. Se removieron indicadores sin sustento real.
- [x] **Métricas en Usuarios:** Conteo en memoria de activos, cuentas bloqueadas y roles distintos en `UsuarioServlet`.
- [x] **Títulos dinámicos de formularios:** `<title>` adaptativo ("Nuevo/Editar usuario/cliente/proveedor/producto | Gestiona Commerce").
- [x] **Seguridad en Login:** Mensaje de bloqueo unificado `"Cuenta bloqueada. Contacte al administrador del sistema."` y eliminación de credenciales de prueba o textos técnicos en `index.jsp`.
- [x] **Licencias de terceros:** Atribución visible retirada de los pies de página; creado `docs/LICENSE-THIRD-PARTY.md` documentando Tabler UI, Bootstrap 5 y la fuente Inter bajo sus licencias originales.
- [x] **Páginas de error personalizadas y seguridad de trazas:** Creadas `src/main/webapp/WEB-INF/views/error/404.jsp` y `500.jsp`, registradas en `web.xml`. Cero fugas de excepciones o trazas técnicas (`getMessage()`, `printStackTrace()`) hacia la pantalla de usuario.
- [x] **Tag final de entrega:** `avance-1-final`.

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
| DEC-19 | Plantilla de Dashboard: **Tabler UI (Licencia MIT, basada en Bootstrap 5)**. | Cumple el requisito de AGENTS.md §9 de usar una plantilla Bootstrap de licencia libre sin librerías obsoletas como jQuery. |
| DEC-20 | Entidad compartida `datos_personales` vinculada a `usuario` mediante `id_datos_personales` (Decisión D2). | Normalización en 3FN evitando redundancia de nombres, DNI, correos, teléfonos y direcciones. |
| DEC-21 | Patrón Repository (`UsuarioRepository` y `UsuarioRepositoryImpl`). | Desacopla la lógica comercial de la persistencia JDBC y maneja transacciones atómicas con `commit()` y `rollback()`. |
| DEC-22 | Eliminación lógica (`estado = 'Inactivo'`) transversal en todos los CRUDs. | Preserva la integridad referencial y la trazabilidad de auditoría según AGENTS.md §6.6. |
| DEC-23 | Reutilización de `datos_personales` para `cliente` y `proveedor`. | Coherencia del modelo relacional 3FN; los repositorios insertan persona + contacto + entidad comercial en una transacción atómica. |
| DEC-24 | Manejo de valores monetarios con `BigDecimal` y catálogo con `LEFT JOIN`. | Evita errores de redondeo en precios de compra/venta y soporta productos sin marca asignada. |
| DEC-25 | Enlace unificado entre todos los módulos en la barra superior. | Experiencia de usuario completa y sin enlaces rotos entre Usuarios, Clientes, Proveedores y Productos. |
| DEC-26 | Navegación y KPI basados en `Usuario.tienePermiso(String)`. | Desacopla las vistas del nombre de los roles, respetando el control de acceso granular canónico. |
| DEC-27 | Ocultamiento total de detalles de desarrollo en vistas públicas. | Garantiza aspecto de aplicación de producción para usuario final. |

---

## 4. Archivos clave del proyecto
| Archivo | Propósito |
|---|---|
| `AGENTS.md` | Reglas permanentes y estándares de entrega. |
| `Etapas.md` | Guía de etapas 1 a 5 y checklist de entrega. |
| `13_2_Modulos_basicos_Formas_Normales.md` | Esquema de BD canónico, datos de prueba y arquitectura de clases. |
| `PROGRESO.md` | Estado actual y registro de traspaso entre modelos. |
| `docs/LICENSE-THIRD-PARTY.md` | Licencias de librerías de terceros (Tabler UI, Bootstrap 5, Inter font). |
| `pom.xml` | Proyecto Maven WAR (Java 17, Jakarta Servlet 6.0, JSTL 3.0, PostgreSQL JDBC 42.7.2, BCrypt 0.4). |
| `Dockerfile` | Build multi-stage: Maven compila → Tomcat 10.1 sirve el WAR. |
| `src/main/webapp/WEB-INF/web.xml` | Configuración de servlets y mapeo de errores 404 y 500. |
| `src/main/webapp/WEB-INF/views/error/*.jsp` | Páginas de error 404 y 500 con diseño sobrio y sin detalles del servidor. |
| `src/main/java/com/gestiona/commerce/config/ConexionBD.java` | Patrón creacional **Singleton** para conexión JDBC a Supabase. |
| `src/main/java/com/gestiona/commerce/util/PasswordUtil.java` | Utilidad de hashing y verificación con BCrypt (rounds=12). |
| `src/main/java/com/gestiona/commerce/model/entity/*.java` | Entidades POJO (`Usuario`, `DatosPersonales`, `Rol`, `Cliente`, `Proveedor`, `Producto`, etc.). |
| `src/main/java/com/gestiona/commerce/model/repository/*.java` | Interfaces e implementaciones JDBC del patrón **Repository**. |
| `src/main/java/com/gestiona/commerce/service/*.java` | Servicios de negocio (`LoginService`, `UsuarioService`, `ClienteService`, `ProveedorService`, `ProductoService`). |
| `src/main/java/com/gestiona/commerce/controller/*.java` | Servlets MVC (`LoginServlet`, `BienvenidaServlet`, `UsuarioServlet`, `ClienteServlet`, `ProveedorServlet`, `ProductoServlet`). |
| `src/main/webapp/WEB-INF/views/dashboard/*.jsp` | Vistas de listado y formularios adaptadas con permisos y diseño comercial Tabler UI. |
| `docs/analisis/singleton.md` | Análisis en bloques del patrón Singleton (Paso 7). |
| `docs/analisis/repository-usuarios.md` | Análisis en bloques del patrón Repository usuarios (Paso 10). |
| `docs/analisis/repository-clientes-proveedores-productos.md` | Análisis en bloques del patrón Repository para entidades comerciales (Paso 12). |
| `docs/evidencias/etapa-1.md` … `etapa-5.md` | Guías de evidencias paso a paso y marcadores de capturas. |

---

## 5. Cómo ejecutar el proyecto

### Supabase (Base de Datos)
1. Los scripts `database/etapa-3.sql`, `etapa-4.sql` y `etapa-5.sql` deben estar ejecutados en el SQL Editor de Supabase.

### Local (NetBeans + Tomcat)
1. Abrir el proyecto en Apache NetBeans como proyecto Maven.
2. Contar con `.env` configurado en la raíz con `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. Ejecutar (Run) en Apache Tomcat 10.1.

### Render (Producción)
- Cada push a la rama `main` dispara el despliegue automático en `https://gestiona-commerce.onrender.com`.

---

## 6. Pendientes y decisiones abiertas
- **Hallazgo de Seguridad (Instrucción 3 de limpieza):**
  Actualmente los Servlets (`UsuarioServlet`, `ClienteServlet`, `ProveedorServlet`, `ProductoServlet`) validan únicamente que exista una sesión activa (`session.getAttribute("usuarioLogueado") != null`), pero **no validan si el usuario posee el permiso correspondiente si ingresa la URL directamente en el navegador**. Tampoco existe un `Filter` de autorización registrado.
  *Estado:* Reportado a la estudiante según lo solicitado, a la espera de su confirmación para proceder con la implementación de un filtro o validación por servlet.

---

## 7. Próximo paso exacto
- **¡Avance 1 Completado y Limpio!**
- Realizar `git push origin main --tags` hacia GitHub para activar el redespliegue final en Render.
- Tomar las capturas de pantalla de la interfaz comercial para la carpeta de evidencias y memoria del informe.
- No avanzar a ventas, facturación ni reportes (Etapa 6 en adelante) hasta que el docente evalúe el Avance 1.
