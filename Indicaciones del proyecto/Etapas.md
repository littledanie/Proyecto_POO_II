# Etapas del desarrollo de la solución informática — Gestiona Commerce

> **Alcance del Avance 1:** Etapa 1 hasta Etapa 5 (termina en el paso 12, *Análisis Repository*).
> Las etapas 6 en adelante (ventas, reportería) **NO** forman parte de este avance: no construirlas ni diseñarlas.
>
> Archivos relacionados:
> - Esquema de BD, datos de ejemplo, clases y patrones: [[13_2_Modulos_basicos_Formas_Normales]] (`13_2_Modulos_basicos_Formas_Normales.md`)
> - Reglas permanentes para el agente: `AGENTS.md`
> - Descripción general del proyecto: [[INDICACIONES_DEL_PROYECTO_DETALLADOS]]

**Modo de trabajo:** cada etapa se construye de forma incremental y deja evidencia paso a paso en `docs/evidencias/etapa-N.md` (el estudiante toma las capturas). Una etapa a la vez; al terminar, el agente se detiene.

---

## Etapa 1 — Login y publicación

### Paso 1 — Crear la GUI del login
- Crear la GUI del login con **JSP y HTML** (CSS propio o Bootstrap, siguiendo la skill de diseño).
- Formulario con usuario y contraseña; mensajes de error visibles.

### Paso 2 — Publicar en hosting
- Publicar el login en un **hosting gratuito** y obtener el **link público** (es el entregable final del avance).
- Prueba temprana: validar cuanto antes que el proveedor elegido ejecuta JSP/Tomcat (ver `AGENTS.md`, sección Despliegue).

---

## Etapa 2 — Verificación de la conexión con la BD

### Paso 3 — Verificación de la conexión con la BD
1. Crear la base de datos (Supabase / PostgreSQL).
2. Crear una tabla mínima `usuarios` (id, nombre de usuario, contraseña).
3. Registrar un usuario y su contraseña (en esta etapa la contraseña puede ir sin cifrar; se cifra en la Etapa 3).
4. Elaborar la conexión entre el login creado y la BD (login que valida contra la tabla).

---

## Etapa 3 — Singleton, reestructuración de usuarios y validaciones

### Paso 4 — Aplicar Singleton
- Modificar el código fuente para usar el patrón creacional **Singleton** en la conexión a la BD.

### Paso 5 — Reestructurar la tabla usuarios
- Reestructurar `usuarios` con los **campos básicos obligatorios** según el esquema de [[13_2_Modulos_basicos_Formas_Normales]]: tablas `rol`, `permiso`, `rol_permiso` y `usuario` (Etapa 3).
- Registrar datos de ejemplo (roles, permisos, usuarios) del archivo de esquema.

### Paso 6 — Validaciones
- **3 intentos erróneos** → el usuario se bloquea (queda registrado en BD) y se cierra la ventana.
- La contraseña debe estar **encriptada** (hash BCrypt).

### Paso 7 — Análisis Singleton
- Elaborar el análisis del código fuente **en bloques** del patrón Singleton (`docs/analisis/singleton.md`).

---

## Etapa 4 — Dashboard y CRUD de usuarios con Repository

### Paso 8 — Dashboard: CRUD del usuario
- Usar una **plantilla de dashboard (Bootstrap)** para el CRUD de usuarios.
- Las tablas de la Etapa 4 (datos personales, contacto, ubicación) están en [[13_2_Modulos_basicos_Formas_Normales]].

### Paso 9 — Aplicación de Repository en el CRUD del usuario
- Reestructurar el código fuente usando el patrón **Repository**.

### Paso 10 — Análisis Repository
- Elaborar el análisis del código fuente **en bloques** del patrón Repository (`docs/analisis/repository-usuarios.md`).

---

## Etapa 5 — CRUD de clientes, proveedores y mercadería

### Paso 11 — CRUD: Clientes, proveedores, mercadería/productos/insumos/servicios
1. Reestructurar o crear las tablas: clientes, proveedores, mercadería/productos/insumos/servicios (y sus catálogos).
2. Registrar datos en las tablas anteriormente creadas.
3. Elaborar el CRUD de: clientes, proveedores, mercadería/productos/insumos/servicios (con el mismo dashboard y con Repository).

### Paso 12 — Análisis Repository
- Elaborar el análisis del código fuente **en bloques** del patrón Repository para las tablas anteriores (`docs/analisis/repository-clientes-proveedores-productos.md`).

---

## Checklist de entrega del Avance 1
- [ ] Link público del sistema funcionando (login → dashboard).
- [ ] Evidencias paso a paso en `docs/evidencias/etapa-1.md` … `etapa-5.md` (capturas tomadas por el estudiante).
- [ ] Análisis por bloques: Singleton (paso 7), Repository usuarios (paso 10), Repository clientes/proveedores/productos (paso 12).
- [ ] Cada semana: pegar la evidencia correspondiente en un Google Docs.
