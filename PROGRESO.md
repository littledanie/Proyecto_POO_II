# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12)
- **Fase actual:** Etapa 1 completada — pendiente de capturas, commits y tag por parte de la estudiante
- **Servidor / Runtime:** Java 17 + Apache Tomcat 10.1 (`jakarta.*`) + Maven WAR
- **Base de Datos:** PostgreSQL en Supabase (acceso JDBC puro) — proyecto creado, aún sin conectar al código
- **Hosting:** Render (Web Service con Docker, plan Free) — **link público verificado y funcional**
- **IDE:** Apache NetBeans 31 con JDK 26 instalado (el proyecto compila con `maven.compiler.release=17`; JDK 17 se instalará también)
- **Repositorio:** `https://github.com/littledanie/Proyecto_POO_II` — conectado a Render para auto-deploy

---

## 2. Checklist detallado de pasos (Avance 1)

### Etapa 1 — Login y publicación
- [x] **Paso 1:** GUI del login (`index.jsp` + `login.css`) — diseño split-panel profesional con paleta slate/verde, Inter, SVG icons, responsive, accesible.
- [x] **Paso 2:** Publicación en hosting — `Dockerfile` multi-stage (Maven + Tomcat 10.1), `.dockerignore`, servicio en Render verificado con link público funcional.

### Etapa 2 — Verificación de la conexión con la BD
- [ ] **Paso 3:** Verificación de la conexión con la BD (Supabase, tabla provisional `usuarios`, contraseña texto plano, validación desde Servlet).

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
| DEC-05 | Paleta slate (#334155) + verde stock (#059669) con tipografía Inter. | Basada en búsqueda de la skill de diseño (perfil "Inventory & Stock Management"); evita degradados morados genéricos. |
| DEC-06 | Login con CSS propio (sin Bootstrap) para Etapa 1; Bootstrap se usará desde Etapa 4 (dashboard). | Máximo control visual en el login; Bootstrap con plantilla dashboard llega en la etapa que corresponde. |
| DEC-07 | JSTL 3.0: API (`jakarta.servlet.jsp.jstl-api:3.0.0`) + implementación Glassfish (`jakarta.servlet.jsp.jstl:3.0.1`) en pom.xml. | Tomcat 10.1 no incluye JSTL; se necesitan ambas dependencias para que `<c:...>` funcione. |
| DEC-08 | `maven.compiler.release=17` en pom.xml; Dockerfile usa `jdk17`. | La PC tiene JDK 26 pero el proyecto debe compilar con API de Java 17 por requisito del curso. |
| DEC-09 | `bienvenida.jsp` dentro de `WEB-INF/views/`; acceso solo vía forward desde Servlet, nunca sendRedirect directo. | Proteger las vistas internas; patrón MVC estricto. |
| DEC-10 | Primer deploy en Render con JSP mínimo antes de diseñar el login. | Validar que el hosting ejecuta JSP/Tomcat antes de invertir tiempo en diseño (AGENTS.md §10). |

---

## 4. Archivos clave del proyecto
| Archivo | Propósito |
|---|---|
| `AGENTS.md` | Reglas permanentes y estándares de entrega. |
| `Etapas.md` | Guía de etapas 1 a 5 y checklist de entrega. |
| `13_2_Modulos_basicos_Formas_Normales.md` | Esquema de BD canónico, datos de prueba y arquitectura de clases. |
| `PROGRESO.md` | Estado actual y registro de traspaso entre modelos. |
| `pom.xml` | Proyecto Maven WAR (Java 17, Jakarta Servlet 6.0, JSTL 3.0). |
| `Dockerfile` | Build multi-stage: Maven compila → Tomcat 10.1 sirve el WAR. |
| `.dockerignore` | Excluye secretos, IDE y docs del contexto Docker. |
| `.gitignore` | Protege `.env`, `target/`, archivos de IDE y Obsidian. |
| `.env.example` | Plantilla de variables de entorno (sin valores reales). |
| `src/main/webapp/index.jsp` | Página de login (formulario de autenticación). |
| `src/main/webapp/assets/css/login.css` | Estilos del login (paleta slate/verde, Inter, responsive). |
| `src/main/webapp/WEB-INF/web.xml` | Descriptor de despliegue Jakarta EE 6.0. |
| `docs/despliegue.md` | Instrucciones de despliegue en Render con Docker. |
| `docs/evidencias/etapa-1.md` | Evidencias paso a paso con marcadores de captura. |

---

## 5. Cómo ejecutar el proyecto

### Local (NetBeans + Tomcat)
1. Abrir el proyecto en Apache NetBeans como proyecto Maven.
2. Configurar Tomcat 10.1 como servidor en NetBeans.
3. Ejecutar (Run) — NetBeans compila con Maven embebido y despliega el WAR en Tomcat.
4. Acceder a `http://localhost:8080/`.

### Docker local
```bash
docker build -t gestiona-commerce .
docker run -p 8080:8080 gestiona-commerce
```

### Render (producción)
- Push a `main` → Render auto-deploy → URL pública asignada.

### Variables de entorno (necesarias a partir de Etapa 2)
- `DB_URL`, `DB_USER`, `DB_PASSWORD` — ver `.env.example`.

---

## 6. Pendientes y bloqueos actuales
- **Pendiente:** La estudiante debe tomar las 4 capturas de la Etapa 1 para el informe/Obsidian (detalladas en `docs/evidencias/etapa-1.md`).
- **Completado:** Commits de Paso 1 y Paso 2 realizados y tag `etapa-1` pusheado a GitHub (`main`). Render desplegará automáticamente la nueva interfaz.
- **Pendiente:** Instalar JDK 17 localmente si se desea ejecutar en NetBeans local (aunque compila con release 17; JDK 26 funciona).
- **Nota:** El archivo `.env` ya existe en la raíz con las credenciales de Supabase (Session Pooler). Se usará en la Etapa 2.

---

## 7. Próximo paso exacto
- **Acción:** Tras los commits y tag de la Etapa 1, iniciar **Etapa 2, Paso 3**: crear script SQL `database/etapa-2.sql` con tabla provisional `usuarios`, agregar dependencia del driver PostgreSQL al `pom.xml`, crear clase de conexión directa (sin Singleton aún), crear `LoginServlet.java` que valide contra la BD, y crear `bienvenida.jsp` dentro de `WEB-INF/views/` (acceso solo por forward).
- **Sobre variables de entorno:** Indicar a la estudiante paso a paso cómo cargarlas en NetBeans/Tomcat local (Windows) y cómo configurarlas en Render.
