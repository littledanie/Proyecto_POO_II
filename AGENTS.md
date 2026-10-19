# AGENTS.md — Reglas permanentes del proyecto Gestiona Commerce (POO II)

Este archivo se lee **al inicio de cada sesión**, sin importar qué modelo esté trabajando.

## 1. Contexto
Proyecto universitario (POO II): sistema web de gestión comercial en **Java (JSP + Servlets)**. La estudiante entrega el **Avance 1** con un link público funcional y evidencias paso a paso.
Descripción general: `INDICACIONES_DEL_PROYECTO_DETALLADOS.md`.

## 2. Alcance del Avance 1
- **Dentro:** Etapas 1 a 5 de `Etapas.md` (hasta el paso 12, *Análisis Repository*).
- **Fuera:** ventas, facturación, reportería, ECharts, Excel, realidad virtual, IA, IoT. **No construir ni anticipar nada de eso.**
- No pasar a una etapa nueva sin que la estudiante lo pida explícitamente.

## 3. Orden de lectura al iniciar
1. `AGENTS.md` (este archivo)
2. `PROGRESO.md` (si existe: manda sobre tu memoria; si no existe, créalo)
3. `Etapas.md` (la etapa en curso)
4. `13_2_Modulos_basicos_Formas_Normales.md` (**esquema canónico y datos de ejemplo; manda sobre cualquier otro esquema**)
5. La skill de diseño, **antes de tocar cualquier JSP/CSS**

Los `[[wikilinks]]` son de Obsidian: no son rutas; los archivos están en la raíz del proyecto.

## 4. Stack técnico (no cambiar sin avisar)
| Componente | Decisión |
| --- | --- |
| Lenguaje | Java 17 |
| Build | **Maven**, proyecto web con empaquetado **WAR** (abre en Apache NetBeans como proyecto Maven) |
| Servidor | **Apache Tomcat 10.1** (usa `jakarta.*`, **no** `javax.*`) |
| Vista | JSP + JSTL (versión Jakarta) + Bootstrap 5 |
| BD | **PostgreSQL en Supabase** (driver `org.postgresql:postgresql` 42.x) |
| Cifrado | BCrypt (`org.mindrot:jbcrypt`) |
| Acceso a datos | **JDBC puro** con `PreparedStatement` (sin Hibernate/JPA/Spring: el curso evalúa patrones a mano) |

Si la estudiante reporta que su NetBeans no reconoce Tomcat 10.1, proponer Tomcat 9 + `javax.*` + JSTL 1.2 y **avisar antes de cambiar**.

## 5. Base de datos y secretos
- Credenciales **solo** en variables de entorno: `DB_URL`, `DB_USER`, `DB_PASSWORD`. Prohibido hardcodearlas o subirlas al repositorio. Crear `.env.example` (sin valores reales) y `.gitignore` con `.env`.
- Supabase gratuito: la conexión directa es IPv6; usar el **Session pooler** si hay problemas de red (ver sección 9 de `13_2_...md`). Los proyectos gratuitos se **pausan por inactividad**: si falla la conexión, avisar a la estudiante antes de depurar código.
- Scripts SQL en `database/` por etapa: `etapa-2.sql`, `etapa-3.sql`, `etapa-4.sql`, `etapa-5.sql` (DDL + datos de ejemplo). Cada script debe poder ejecutarse en el SQL Editor de Supabase.
- Nombres de BD en `snake_case`; en Java, `camelCase` / `PascalCase`.

## 6. Reglas de trabajo
1. **Una etapa a la vez, de forma incremental.** La estudiante necesita el paso a paso: no saltar a la versión final.
   - Etapa 2: contraseña sin cifrar. Etapa 3: se cifra. Deben quedar **commits separados**.
2. Un **commit por paso** con mensaje en español (`paso 3: conexión login-BD`) y un **tag por etapa** (`etapa-1` … `etapa-5`).
3. Al terminar una etapa: generar evidencias y análisis, actualizar `PROGRESO.md` y **detenerse** esperando confirmación.
4. Si hay una contradicción o duda, **preguntar**; no inventar. No modificar el esquema canónico sin aprobación.
5. Datos de ejemplo: usar los de `13_2_...md` (sección 5). Las contraseñas se generan con una clase `SeedUsuarios` + BCrypt a partir de las credenciales de prueba; **nunca** pegar hashes inventados.
6. Eliminar en el CRUD = **eliminación lógica** (`estado = 'Inactivo'`).
7. No instalar dependencias ni servicios fuera de lo listado sin avisar.

## 7. Seguridad (obligatorio)
- Contraseñas con **BCrypt** (hash con sal); jamás texto plano en BD ni en logs.
- **PreparedStatement** en todas las consultas (sin concatenar SQL).
- 3 intentos fallidos → `bloqueado = true`, mensaje claro y cierre de la ventana/sesión.
- Validar y escapar la salida en JSP (`<c:out>`), proteger rutas con un filtro de sesión.
- Mensajes de error genéricos hacia el usuario ("Usuario o contraseña incorrectos"); detalle solo en logs.

## 8. Arquitectura y patrones
- Arquitectura **MVC** con la estructura de paquetes de la sección 8 de `13_2_...md` (`controller`, `model/entity`, `model/repository`, `service`, `config`, `util`).
- **Obligatorios:** Singleton (conexión a BD, Etapa 3) y Repository (usuarios en Etapa 4; clientes, proveedores y productos en Etapa 5).
- Los demás patrones de la sección 7 de `13_2_...md` son **planificados, no implementar** salvo petición expresa.
- Código con **comentarios en español** que expliquen el *porqué* de cada bloque clave (se usan en el análisis).

## 9. Diseño de la interfaz
- Leer y aplicar la skill de diseño antes de escribir interfaces. Objetivo: que **no parezca generado por IA** (sin degradados morados genéricos, sin tarjetas idénticas repetidas, tipografía y paleta con intención).
- Etapa 4 en adelante: partir de una **plantilla de dashboard Bootstrap** de licencia libre (por ejemplo AdminLTE o Tabler); indicar cuál se usó y su licencia.
- Interfaces en español, responsivas, con estados vacíos y mensajes de error claros.

## 10. Despliegue
- Incluir un `Dockerfile` basado en `tomcat:10.1-jdk17` que copie el WAR como `ROOT.war`, para no depender de un proveedor concreto.
- El hosting gratuito para JSP es escaso y cambia seguido: **hacer una prueba temprana en la Etapa 1** (despliegue de un JSP mínimo) y, si el proveedor no sirve, **proponer alternativas a la estudiante** antes de continuar.
- Documentar en `docs/despliegue.md` los pasos exactos para reproducir el despliegue y las variables de entorno necesarias.

## 11. Evidencias y análisis (formato Obsidian, archivos `.md`)
Por cada etapa crear `docs/evidencias/etapa-N.md` con:
- Título de la etapa y **pasos numerados** con comandos/acciones exactas para reproducirlos.
- Por cada paso, un marcador de captura con nombre de archivo sugerido, para que la estudiante la tome y la inserte:
  `📸 CAPTURA 2.3 — Login mostrando error de contraseña → ![[etapa2_paso3_login_error.png]]`
- Resultado esperado de cada paso (qué se debería ver).
- Una breve sección "Problemas encontrados y cómo se resolvieron" si los hubo.

Análisis de patrones en `docs/analisis/*.md` (pasos 7, 10 y 12): dividir el código **en bloques** numerados; para cada bloque: el fragmento, qué hace, qué rol cumple en el patrón y por qué se eligió ese patrón. Redactar para que se pueda pegar en Google Docs.

## 12. Protocolo de traspaso entre modelos (`PROGRESO.md`)
La estudiante alterna entre modelos. `PROGRESO.md` es la memoria compartida. Debe contener:
- **Etapa/paso actual** y estado: `[x]` hecho, `[ ]` pendiente por cada paso 1–12.
- **Decisiones tomadas** (y su motivo) y desviaciones respecto al esquema.
- **Archivos clave** creados y su propósito.
- **Cómo ejecutar** el proyecto y las pruebas realizadas.
- **Pendientes y bloqueos** (por ejemplo, "esperando capturas de la estudiante").
- **Próximo paso exacto** que debe hacer el siguiente modelo.

Actualizarlo **al terminar cada paso**, no solo al final de la etapa. Al empezar una sesión, leerlo antes de actuar.

## 13. Definición de "terminado" por etapa
- El código compila y se ejecuta con Tomcat 10.1.
- Los pasos de la etapa funcionan de punta a punta con los datos de ejemplo.
- Existen evidencias, commits y tag de la etapa; `PROGRESO.md` actualizado.
- Se informa a la estudiante qué capturas tomar y se **detiene** el trabajo.

## 14. Comunicación
- Responder en español, claro y breve. Al cerrar cada tarea: qué hiciste, qué falta y qué debe revisar la estudiante.
