# Evidencias — Etapa 5: CRUD de Clientes, Proveedores y Productos con Repository

## Resumen de la Etapa
En esta última etapa correspondiente al **Avance 1 (POO II)**, se completó la persistencia y gestión de las tres entidades comerciales fundamentales del sistema:
1. **Clientes** (Personas naturales con DNI y empresas con RUC vinculadas a `datos_personales`).
2. **Proveedores** (Empresas proveedoras con RUC, razón social y persona de contacto vinculada a `datos_personales`).
3. **Productos, Insumos y Servicios** (Catálogo de artículos comerciales clasificados en `MERCADERIA`, `PRODUCTO`, `INSUMO` y `SERVICIO`, asociados a categorías, marcas y unidades de medida con control de precios y stocks).

Todo el desarrollo se implementó aplicando el patrón arquitectural **Repository** en capas (`Repository`, `Service`, `Servlet`, `JSP`), con soporte de eliminación lógica (`estado = 'Inactivo'`), interfaz moderna en **Tabler UI (MIT / Bootstrap 5)** y datos de prueba canónicos normalizados en 3FN.

---

## Pasos Realizados

### Paso 11 — Estructura de BD y CRUD con Patrón Repository
1. **Base de Datos y Datos Canónicos:**
   - Se diseñó el script `database/etapa-5.sql` con las tablas:
     - Catálogos: `categoria`, `marca`, `unidad_medida`.
     - Entidad comercial de venta: `cliente` (relacionada 1:1 con `datos_personales`).
     - Entidad comercial de compra: `proveedor` (relacionada 1:1 con `datos_personales`).
     - Artículos y existencias: `producto` (con claves foráneas a `categoria`, `marca`, `unidad_medida`).
   - Se insertaron los datos canónicos obligatorios especificados en la sección 5 de `13_2_Modulos_basicos_Formas_Normales.md`.
2. **Entidades del Dominio (POJO):**
   - Se crearon las clases en `com.gestiona.commerce.model.entity`:
     - `Categoria.java`, `Marca.java`, `UnidadMedida.java`.
     - `Cliente.java`, `Proveedor.java`, `Producto.java`.
3. **Capa de Acceso a Datos (Repository):**
   - Se definieron los contratos e implementaciones JDBC puro con `PreparedStatement` y transacciones ACID:
     - `ClienteRepository.java` y `ClienteRepositoryImpl.java`.
     - `ProveedorRepository.java` y `ProveedorRepositoryImpl.java`.
     - `ProductoRepository.java` y `ProductoRepositoryImpl.java`.
4. **Capa de Lógica de Negocio (Service):**
   - Se implementaron las clases de servicio con validaciones de negocio:
     - `ClienteService.java` (validación de DNI 8 dígitos / RUC 11 dígitos, unicidad).
     - `ProveedorService.java` (validación de RUC 11 dígitos y unicidad).
     - `ProductoService.java` (validación de código único, márgenes de precio, stocks).
5. **Capa Web (MVC Servlets y Vistas JSP):**
   - Servlets registrados en `web.xml`: `ClienteServlet` (`/clientes`), `ProveedorServlet` (`/proveedores`), `ProductoServlet` (`/productos`).
   - Vistas con Tabler UI / Bootstrap 5 en `src/main/webapp/WEB-INF/views/dashboard/`:
     - `clientes-lista.jsp` y `clientes-form.jsp`.
     - `proveedores-lista.jsp` y `proveedores-form.jsp`.
     - `productos-lista.jsp` y `productos-form.jsp`.
   - Se habilitaron y enlazaron todos los módulos en la barra de navegación superior de todas las pantallas del sistema.

### Paso 12 — Análisis del Patrón Repository para Entidades Comerciales
- Se redactó el análisis exhaustivo del código fuente en 6 bloques en `docs/analisis/repository-clientes-proveedores-productos.md`, estructurado con explicación conceptual, código, rol en el patrón y justificación técnica para el informe académico.

---

## Capturas solicitadas para el informe

📸 **CAPTURA 5.1** — Script SQL `etapa-5.sql` ejecutado en Supabase SQL Editor mostrando la creación exitosa de las tablas `cliente`, `proveedor`, `producto` y sus catálogos con los datos canónicos insertados  
→ `![[etapa5_paso11_supabase_tablas.png]]`

📸 **CAPTURA 5.2** — Pantalla de bienvenida (`/bienvenida`) con los botones de acceso rápido a los 4 módulos funcionales habilitados (Usuarios, Clientes, Proveedores y Productos)  
→ `![[etapa5_paso11_dashboard_inicio.png]]`

📸 **CAPTURA 5.3** — Módulo de Clientes (`/clientes`) mostrando el listado con clientes tipo Natural y Empresa, RUC/DNI y badges de estado  
→ `![[etapa5_paso11_lista_clientes.png]]`

📸 **CAPTURA 5.4** — Formulario de registro de nuevo cliente (`/clientes?accion=nuevo`) completado con datos de contacto y ubicación distrital  
→ `![[etapa5_paso11_formulario_nuevo_cliente.png]]`

📸 **CAPTURA 5.5** — Módulo de Proveedores (`/proveedores`) mostrando el directorio comercial con RUC, Razón Social y datos de la persona de contacto  
→ `![[etapa5_paso11_lista_proveedores.png]]`

📸 **CAPTURA 5.6** — Formulario de nuevo proveedor (`/proveedores?accion=nuevo`) y confirmación de registro exitoso mediante banner de notificación  
→ `![[etapa5_paso11_proveedor_registrado.png]]`

📸 **CAPTURA 5.7** — Catálogo de Productos y Mercaderías (`/productos`) mostrando la grilla de artículos con categoría, marca, precio de venta formateado en moneda (S/) y badges de alerta de stock  
→ `![[etapa5_paso11_lista_productos.png]]`

📸 **CAPTURA 5.8** — Formulario de nuevo producto (`/productos?accion=nuevo`) mostrando los selectores de tipo (MERCADERIA, PRODUCTO, INSUMO, SERVICIO), categorías, marcas y unidades de medida dinámicas  
→ `![[etapa5_paso11_formulario_nuevo_producto.png]]`

📸 **CAPTURA 5.9** — Eliminación lógica: producto o cliente desactivado mostrando badge `Inactivo` en la tabla sin haber sido borrado físicamente de la base de datos  
→ `![[etapa5_paso11_eliminacion_logica_producto.png]]`

📸 **CAPTURA 5.10** — Acceso en vivo desde el enlace público desplegado en Render (`https://gestiona-commerce.onrender.com`) navegando por el sistema comercial  
→ `![[etapa5_paso11_render_live.png]]`

---

## Resultados esperados
- El usuario puede navegar de forma fluida y responsiva entre todos los módulos del sistema desde la barra superior.
- Los módulos de Clientes, Proveedores y Productos permiten registrar, listar, editar y desactivar registros (eliminación lógica).
- El manejo de clientes y proveedores realiza inserciones atómicas en `datos_personales` y las tablas respectivas mediante transacciones JDBC controladas por el Repositorio.
- La aplicación compila limpiamente para Java 17 y se ejecuta sin advertencias críticas en Apache Tomcat 10.1 (`jakarta.*`).

---

## Problemas encontrados y cómo se resolvieron

| # | Problema | Solución |
|---|----------|----------|
| 1 | Reutilización de `datos_personales` para entidades con propósitos distintos (usuario con credenciales, cliente con RUC/razón social, proveedor con representante). | Se modelaron claves foráneas independientes `id_datos_personales` en `cliente` y `proveedor`, permitiendo que el repositorio comparta la misma estructura de contacto y territorio sin duplicar tablas. |
| 2 | Presentación de precios y existencias en la interfaz JSP. | Se integró la librería de etiquetas de formato de Jakarta (`<%@ taglib prefix="fmt" uri="jakarta.tags.core" %>` / `jakarta.tags.fmt`) para mostrar los precios con dos decimales y el símbolo monetario de forma consistente. |
| 3 | Manejo de marcas opcionales en productos (`id_marca NULL`). | En `ProductoRepositoryImpl`, se utilizó `LEFT JOIN marca` y `ps.setNull(..., Types.INTEGER)` para permitir artículos genéricos sin infringir restricciones de base de datos. |
