# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12) — **COMPLETADO AL 100%**
- **Fase actual:** Etapa 5 completada — CRUD de Clientes, Proveedores y Productos/Mercaderías implementado con el patrón **Repository**, arquitectura MVC en capas, persistencia JDBC pura en PostgreSQL/Supabase, eliminación lógica y vistas dashboard con **Tabler UI (MIT / Bootstrap 5)**. Análisis en bloques completado en `docs/analisis/` y evidencias en `docs/evidencias/`.
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
| `database/etapa-3.sql` | DDL 3FN (`rol`, `permiso`, `rol_permiso`, `usuario`) con hashes BCrypt y bloqueo. |
| `database/etapa-4.sql` | DDL personas, contacto, ubicación y vinculación a `usuario` (`id_datos_personales`). |
| `database/etapa-5.sql` | DDL catálogos (`categoria`, `marca`, `unidad_medida`), `producto`, `cliente`, `proveedor` y datos canónicos. |
| `src/main/java/com/gestiona/commerce/config/ConexionBD.java` | Patrón creacional **Singleton** para conexión JDBC a Supabase. |
| `src/main/java/com/gestiona/commerce/util/PasswordUtil.java` | Utilidad de hashing y verificación con BCrypt (rounds=12). |
| `src/main/java/com/gestiona/commerce/util/SeedUsuarios.java` | Generador canónico de hashes para inserción en BD. |
| `src/main/java/com/gestiona/commerce/model/entity/*.java` | Entidades POJO del dominio (`Usuario`, `DatosPersonales`, `Rol`, `Cliente`, `Proveedor`, `Producto`, etc.). |
| `src/main/java/com/gestiona/commerce/model/repository/UsuarioRepository.java` | Interfaz del patrón **Repository** para Usuario. |
| `src/main/java/com/gestiona/commerce/model/repository/UsuarioRepositoryImpl.java` | Implementación JDBC pura de Repository para Usuario. |
| `src/main/java/com/gestiona/commerce/model/repository/ClienteRepository.java` | Interfaz de Repository para Cliente. |
| `src/main/java/com/gestiona/commerce/model/repository/ClienteRepositoryImpl.java` | Implementación JDBC pura con transacciones atómicas para Cliente. |
| `src/main/java/com/gestiona/commerce/model/repository/ProveedorRepository.java` | Interfaz de Repository para Proveedor. |
| `src/main/java/com/gestiona/commerce/model/repository/ProveedorRepositoryImpl.java` | Implementación JDBC pura con transacciones atómicas para Proveedor. |
| `src/main/java/com/gestiona/commerce/model/repository/ProductoRepository.java` | Interfaz de Repository para Producto. |
| `src/main/java/com/gestiona/commerce/model/repository/ProductoRepositoryImpl.java` | Implementación JDBC pura con mapeo relacional multi-tabla para Producto. |
| `src/main/java/com/gestiona/commerce/service/UsuarioService.java` | Servicio de negocio para CRUD de usuarios y validaciones. |
| `src/main/java/com/gestiona/commerce/service/ClienteService.java` | Servicio de negocio para CRUD de clientes y validaciones DNI/RUC. |
| `src/main/java/com/gestiona/commerce/service/ProveedorService.java` | Servicio de negocio para CRUD de proveedores y validaciones RUC. |
| `src/main/java/com/gestiona/commerce/service/ProductoService.java` | Servicio de negocio para CRUD de productos, categorías y unidades. |
| `src/main/java/com/gestiona/commerce/controller/*.java` | Controladores Servlets (`LoginServlet`, `BienvenidaServlet`, `UsuarioServlet`, `ClienteServlet`, `ProveedorServlet`, `ProductoServlet`). |
| `src/main/webapp/WEB-INF/views/dashboard/*.jsp` | Vistas de listado y formularios con plantilla Tabler UI / Bootstrap 5. |
| `docs/analisis/singleton.md` | Análisis en bloques del patrón Singleton (Paso 7). |
| `docs/analisis/repository-usuarios.md` | Análisis en bloques del patrón Repository usuarios (Paso 10). |
| `docs/analisis/repository-clientes-proveedores-productos.md` | Análisis en bloques del patrón Repository para entidades comerciales (Paso 12). |
| `docs/evidencias/etapa-1.md` … `etapa-5.md` | Guías de evidencias paso a paso y marcadores de capturas. |

---

## 5. Cómo ejecutar el proyecto

### Supabase (Base de Datos)
1. Ejecutar `database/etapa-4.sql` si aún no se había corrido.
2. Ejecutar `database/etapa-5.sql` en el SQL Editor de Supabase para crear las tablas de `categoria`, `marca`, `unidad_medida`, `producto`, `cliente` y `proveedor`, cargando los datos canónicos obligatorios.

### Local (NetBeans + Tomcat)
1. Abrir el proyecto en Apache NetBeans como proyecto Maven.
2. Contar con `.env` configurado en la raíz con `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. Ejecutar (Run) en Apache Tomcat 10.1.

### Render (Producción)
- Cada push a la rama `main` dispara el despliegue automático en `https://gestiona-commerce.onrender.com`.

---

## 6. Pendientes y bloqueos actuales
- **Acción requerida de la estudiante en Supabase:**
  Ejecutar el script `database/etapa-5.sql` en el SQL Editor de Supabase (y `database/etapa-4.sql` si faltaba).
- **Acción requerida de evidencias para entrega académica:**
  Tomar las capturas indicadas en `docs/evidencias/etapa-4.md` y `docs/evidencias/etapa-5.md` para armar el documento de entrega del **Avance 1**.

---

## 7. Próximo paso exacto
- **¡Avance 1 Completado con Éxito!**
- El alcance acordado en `AGENTS.md` (Etapas 1 a 5, Pasos 1 al 12) ha finalizado satisfactoriamente.
- No construir ventas ni reportes (Etapa 6 en adelante) hasta que el docente evalúe el Avance 1 y la estudiante lo indique expresamente.
