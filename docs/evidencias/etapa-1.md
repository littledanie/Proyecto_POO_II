# Evidencias — Etapa 1: Login y publicación

## Paso 1 — Crear la GUI del login

### Acciones realizadas
1. Inicializar proyecto Maven WAR (`pom.xml`) con Java 17, Jakarta Servlet 6.0 y JSTL 3.0 (API + implementación Glassfish).
2. Crear descriptor de despliegue `WEB-INF/web.xml` (Jakarta EE 6.0).
3. Diseñar `index.jsp` con formulario de login (usuario y contraseña).
4. Crear `assets/css/login.css` con diseño profesional:
   - Paleta: Slate industrial (`#334155`) + verde stock (`#059669`) — basada en la skill de diseño (Inventory & Stock Management).
   - Tipografía: Inter (Google Fonts).
   - Layout split-panel (marca a la izquierda, formulario a la derecha).
   - Responsive mobile-first (panel de marca se oculta en móvil).
   - Accesibilidad: labels visibles, `role="alert"`, `autocomplete`, `focus-visible`, `prefers-reduced-motion`.
   - Toggle de visibilidad de contraseña con iconos SVG.
5. Archivos creados:
   - `pom.xml`
   - `src/main/webapp/WEB-INF/web.xml`
   - `src/main/webapp/index.jsp`
   - `src/main/webapp/assets/css/login.css`

### Capturas solicitadas

📸 **CAPTURA 1.1** — Interfaz del login en el navegador (vista escritorio, mostrando ambos paneles)
→ `![[etapa1_paso1_login_desktop.png]]`

📸 **CAPTURA 1.2** — Interfaz del login en vista móvil (DevTools → responsive, ~375px)
→ `![[etapa1_paso1_login_mobile.png]]`

### Resultado esperado
- Se ve un panel izquierdo oscuro (slate) con el logotipo "Gestiona Commerce", el eslogan "Tu negocio, bajo control." y tres tarjetas de estadísticas decorativas.
- A la derecha, un formulario limpio con campos de usuario y contraseña, iconos SVG, y botón "Ingresar".
- En móvil, el panel izquierdo desaparece y se muestra solo el formulario con el logotipo arriba.

---

## Paso 2 — Publicar en hosting

### Acciones realizadas
1. Crear `Dockerfile` multi-stage:
   - Etapa 1 (build): `maven:3.9-eclipse-temurin-17` — compila el WAR.
   - Etapa 2 (run): `tomcat:10.1-jdk17` — sirve el WAR como `ROOT.war`.
2. Crear `.dockerignore` para excluir archivos innecesarios del contexto Docker.
3. Configurar servicio en Render (Web Service con Docker, plan Free, rama `main`).
4. Documentar el proceso en `docs/despliegue.md`.
5. Archivos creados:
   - `Dockerfile`
   - `.dockerignore`
   - `docs/despliegue.md`

### Capturas solicitadas

📸 **CAPTURA 1.3** — Panel de Render mostrando el servicio en estado **Live** (verde)
→ `![[etapa1_paso2_render_live.png]]`

📸 **CAPTURA 1.4** — Login funcionando en el navegador a través de la URL pública de Render
→ `![[etapa1_paso2_login_publico.png]]`

### Resultado esperado
- En el panel de Render se ve el servicio `gestiona-commerce` con estado "Live".
- Al abrir la URL pública, se muestra la interfaz del login idéntica a la vista local.

---

## Problemas encontrados y cómo se resolvieron

| # | Problema | Solución |
|---|----------|----------|
| 1 | Maven no está instalado localmente en la PC de la estudiante. | El Dockerfile usa su propia imagen Maven (`maven:3.9-eclipse-temurin-17`) para compilar. Localmente, NetBeans gestiona la compilación con el Maven embebido. |
| 2 | Git push rechazado por credenciales de otra cuenta (`Mencirge`). | Se configuró la URL remota con el usuario explícito (`littledanie@github.com/...`) o se actualizó el Administrador de Credenciales de Windows. |
