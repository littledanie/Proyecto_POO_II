# PROGRESO.md — Registro de avance y memoria compartida (Gestiona Commerce)

> Este archivo es la memoria compartida entre modelos de IA y la estudiante.
> Se actualiza obligatoriamente al terminar cada paso. Manda sobre la memoria de cualquier sesión.

---

## 1. Estado general del proyecto
- **Estudiante:** Daniela
- **Curso:** Programación Orientada a Objetos II (POO II)
- **Alcance actual:** Avance 1 (Etapas 1 a 5 de `Etapas.md`, Pasos 1 al 12)
- **Fase actual:** Sesión 1 — Planificación inicial (Etapa 1 pendiente de aprobación)
- **Servidor / Runtime:** Java 17 + Apache Tomcat 10.1 (`jakarta.*`) + Maven WAR
- **Base de Datos:** PostgreSQL en Supabase (acceso JDBC puro)
- **Repositorio / Hosting:** Pendiente de configuración inicial

---

## 2. Checklist detallado de pasos (Avance 1)

### Etapa 1 — Login y publicación
- [ ] **Paso 1:** Crear la GUI del login (JSP + HTML + CSS moderno basado en skill de diseño, responsive y accesible).
- [ ] **Paso 2:** Publicar en hosting gratuito con `Dockerfile` (Tomcat 10.1) y obtener link público funcional.

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

---

## 4. Archivos clave del proyecto
- `Indicaciones del proyecto/AGENTS.md`: Reglas permanentes y estándares de entrega.
- `Indicaciones del proyecto/Etapas.md`: Guía de etapas 1 a 5 y checklist de entrega.
- `Indicaciones del proyecto/13_2_Modulos_basicos_Formas_Normales.md`: Esquema de BD canónico, datos de prueba y arquitectura de clases.
- `PROGRESO.md`: Estado actual y registro de traspaso.

*(Se irán incorporando los archivos Java, JSP, SQL, scripts y Dockerfile conforme se ejecuten los pasos).*

---

## 5. Cómo ejecutar el proyecto y pruebas
- **Local:** (Se definirá en el Paso 1: compilar con `mvn clean package` y desplegar el WAR resultante en Tomcat 10.1 desde NetBeans o vía terminal).
- **Docker:** `docker build -t gestiona-commerce .` y `docker run -p 8080:8080 gestiona-commerce`.
- **Variables de entorno requeridas:** `DB_URL`, `DB_USER`, `DB_PASSWORD` (especificadas en `.env.example`).

---

## 6. Pendientes y bloqueos actuales
- **Bloqueo:** Esperando aprobación de la estudiante para el plan de trabajo de las Etapas 1 y 2.
- **Preparativos por la estudiante:**
  1. Proyecto creado en Supabase y obtención de credenciales JDBC (Session Pooler).
  2. Elección y registro en hosting gratuito (ej. Render, Koyeb).
  3. Confirmación de versión instalada de NetBeans, JDK 17 y Apache Tomcat 10.1.

---

## 7. Próximo paso exacto
- **Acción:** Tras recibir aprobación de la estudiante y confirmar sus preparativos, iniciar el **Paso 1 de la Etapa 1**: inicializar el proyecto Maven WAR con su `pom.xml`, estructurar carpetas MVC y diseñar la GUI del login (`index.jsp` + CSS).
