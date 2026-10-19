# Análisis del Código Fuente: Patrón Repository en Clientes, Proveedores y Productos

> **Curso:** Programación Orientada a Objetos II (POO II)  
> **Proyecto:** Gestiona Commerce  
> **Entregable:** Avance 1 — Etapa 5 (Paso 12: Análisis de Patrones)  
> **Autor/a:** Daniela  
> *Documento preparado para su presentación en informe académico (Google Docs).*

---

## 1. Introducción y Justificación del Patrón en las Entidades Comerciales

### 1.1 Definición y Propósito
El patrón de diseño estructural/dominio **Repository** proporciona una abstracción orientada a objetos sobre el almacenamiento persistente. Al aplicarlo a las entidades comerciales del sistema (**Clientes**, **Proveedores** y **Productos/Mercaderías**), se logra que la lógica de negocio trate los datos como una colección en memoria, independizando las reglas comerciales de la sintaxis SQL de PostgreSQL.

### 1.2 Justificación de la Elección
1. **Modelos con relaciones compartidas:** Tanto `Cliente` como `Proveedor` reutilizan la entidad normalizada `DatosPersonales` (3FN), lo que exige operaciones compuestas de persistencia. Sin el patrón Repository, la lógica de inserción y actualización transaccional estaría duplicada y dispersa en múltiples controladores.
2. **Catálogos y llaves foráneas múltiples:** La entidad `Producto` depende de tres catálogos simultáneos (`Categoria`, `Marca`, `UnidadMedida`). El patrón Repository encapsula las sentencias `JOIN` complejas y la reconstrucción de grafos de objetos dentro de un único componente cohesivo.
3. **Mantenibilidad y principio de sustitución:** Si se requiere incorporar almacenamiento en caché, migrar a otro motor de persistencia o realizar pruebas unitarias con repositorios simulados (*Mocks*), las capas superiores (Servicios y Servlets) no sufren ningún impacto.

---

## 2. Diagrama Arquitectural del Módulo Comercial

```
┌────────────────────────────────────────────────────────┐
│   Capa Web / Presentación (JSP + Servlets MVC)         │
│   ClienteServlet  |  ProveedorServlet  | ProductoServlet│
└──────────────────────────┬─────────────────────────────┘
                           │ (invoca)
                           ▼
┌────────────────────────────────────────────────────────┐
│   Capa de Lógica de Negocio (Services)                 │
│   ClienteService  |  ProveedorService  | ProductoService│
└──────────────────────────┬─────────────────────────────┘
                           │ (depende de abstracciones)
                           ▼
┌────────────────────────────────────────────────────────┐
│   Capa de Dominio / Contratos (Interfaces Repository)   │
│   ClienteRepository | ProveedorRepository | ProductoRepo│  ◄── Bloque 1
└──────────────────────────▲─────────────────────────────┘
                           │ (implementa con JDBC puro)
┌──────────────────────────┴─────────────────────────────┐
│   Capa de Persistencia (Implementaciones Repository)   │  ◄── Bloques 2, 3 y 4
│   ClienteRepoImpl   | ProveedorRepoImpl | ProductoRepoImpl│
└──────────────────────────┬─────────────────────────────┘
                           │ (consume)
                           ▼
┌────────────────────────────────────────────────────────┐
│   Patrón Singleton: ConexionBD (Instancia Única)       │
└────────────────────────────────────────────────────────┘
```

---

## 3. Análisis del Código Fuente en Bloques

### Bloque 1: Contratos de Abstracción de Repositorio

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 1: Interfaz abstracta del Repositorio de Clientes
// =========================================================================
package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.Cliente;
import com.gestiona.commerce.model.entity.DatosPersonales;
import java.util.List;

public interface ClienteRepository {
    List<Cliente> listarTodos();
    Cliente buscarPorId(int id);
    Cliente buscarPorDni(String dni);
    Cliente buscarPorRuc(String ruc);
    boolean registrar(Cliente cliente, DatosPersonales dp);
    boolean actualizar(Cliente cliente, DatosPersonales dp);
    boolean eliminarLogico(int id);
}
```

#### ¿Qué hace este bloque?
Declara el contrato formal de operaciones disponibles para la gestión de clientes. Expresa métodos con semántica de colección (`listarTodos`, `buscarPorId`, `registrar`, `actualizar`, `eliminarLogico`) recibiendo y devolviendo entidades de dominio puras (`Cliente`, `DatosPersonales`), sin ninguna mención a SQL, cursores de base de datos ni conexiones JDBC.

#### Rol en el patrón Repository
Representa la **Interfaz del Repositorio** (*Repository Interface*). Desacopla la especificación de lo que el sistema necesita persistir de los detalles técnicos de cómo se almacena en PostgreSQL.

#### ¿Por qué se eligió este diseño?
Cumple con el **Principio de Inversión de Dependencias (DIP)**: el servicio `ClienteService` interactúa exclusivamente con esta interfaz abstracta, garantizando bajo acoplamiento y facilitando la extensibilidad y pruebas del software.

---

### Bloque 2: Persistencia Transaccional Compuesta (Transacciones ACID)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 2: Transacción atómica en ClienteRepositoryImpl.registrar
// =========================================================================
@Override
public boolean registrar(Cliente cliente, DatosPersonales dp) {
    String sqlDP = "INSERT INTO datos_personales (nombres, apellidos, dni, id_distrito) "
                 + "VALUES (?, ?, ?, ?) RETURNING id_datos_personales";
    String sqlCli = "INSERT INTO cliente (tipo_cliente, ruc, razon_social, id_datos_personales, estado) "
                  + "VALUES (?, ?, ?, ?, 'Activo')";

    Connection conn = null;
    try {
        conn = getConexion();
        conn.setAutoCommit(false); // Inicio de transacción atómica

        int idDP;
        try (PreparedStatement psDP = conn.prepareStatement(sqlDP)) {
            psDP.setString(1, dp.getNombres());
            psDP.setString(2, dp.getApellidos());
            psDP.setString(3, dp.getDni());
            if (dp.getIdDistrito() != null && dp.getIdDistrito() > 0) {
                psDP.setInt(4, dp.getIdDistrito());
            } else {
                psDP.setNull(4, Types.INTEGER);
            }
            try (ResultSet rs = psDP.executeQuery()) {
                if (rs.next()) {
                    idDP = rs.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }
        }

        // Registrar canales de contacto (correo, teléfono, dirección)
        insertarContacto(conn, idDP, dp);

        // Registrar entidad cliente vinculada
        try (PreparedStatement psCli = conn.prepareStatement(sqlCli)) {
            psCli.setString(1, cliente.getTipoCliente());
            psCli.setString(2, cliente.getRuc());
            psCli.setString(3, cliente.getRazonSocial());
            psCli.setInt(4, idDP);
            psCli.executeUpdate();
        }

        conn.commit(); // Confirmación de todos los cambios
        return true;
    } catch (SQLException e) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) {}
        }
        return false;
    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }
}
```

#### ¿Qué hace este bloque?
Inserta un registro de persona en `datos_personales`, sus canales de contacto asociados (`correo_electronico`, `telefono`, `direccion`) y finalmente el registro en `cliente` vinculado mediante la clave foránea `id_datos_personales`. Toda la operación se ejecuta bajo un único contexto transaccional con control explícito de `commit()` y `rollback()`.

#### Rol en el patrón Repository
Representa la **Gestión de Agregados Transaccionales**. El repositorio no es un simple ejecutor de consultas aisladas; se responsabiliza de mantener la integridad y consistencia de los agregados del modelo de dominio.

#### ¿Por qué se eligió este diseño?
Garantiza la propiedad **ACID (Atomicidad)**: si ocurre un fallo durante la creación del cliente (por ejemplo, violación de unicidad de RUC), el `rollback()` previene la creación de registros huérfanos en `datos_personales`, evitando inconsistencias en la base de datos.

---

### Bloque 3: Mapeo Relacional de Múltiples Catálogos en Productos

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 3: Reconstrucción de entidad Producto con joins relacionales
// =========================================================================
@Override
public List<Producto> listarTodos() {
    List<Producto> lista = new ArrayList<>();
    String sql = "SELECT p.id_producto, p.codigo, p.nombre, p.descripcion, p.tipo, "
               + "       p.id_categoria, c.nombre AS categoria_nombre, "
               + "       p.id_marca, m.nombre AS marca_nombre, "
               + "       p.id_unidad_medida, u.nombre AS unidad_nombre, u.abreviatura AS unidad_abrev, "
               + "       p.precio_compra, p.precio_venta, p.stock_actual, p.stock_minimo, "
               + "       p.estado, p.fecha_registro "
               + "FROM producto p "
               + "INNER JOIN categoria c ON p.id_categoria = c.id_categoria "
               + "LEFT JOIN marca m ON p.id_marca = m.id_marca "
               + "INNER JOIN unidad_medida u ON p.id_unidad_medida = u.id_unidad_medida "
               + "ORDER BY p.id_producto ASC";

    try (Connection conn = getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            lista.add(mapearProducto(rs));
        }
    } catch (SQLException e) {
        System.err.println("Error al listar productos: " + e.getMessage());
    }
    return lista;
}
```

#### ¿Qué hace este bloque?
Ejecuta una consulta SQL combinada que cruza la tabla principal `producto` con sus catálogos normalizados (`categoria`, `marca` mediante `LEFT JOIN` por si es opcional, y `unidad_medida`). Posteriormente delega la instanciación y poblado del POJO `Producto` a un método privado reutilizable `mapearProducto(rs)`.

#### Rol en el patrón Repository
Cumple con la función de **Mapeo Objeto-Relacional (Data Mapper)** integrado dentro del Repositorio. Traduce filas tabulares y columnas de múltiples tablas foráneas a atributos fuertemente tipados de un objeto de dominio Java.

#### ¿Por qué se eligió este diseño?
Evita el problema de las consultas *N+1*: en lugar de consultar la base de datos para obtener el nombre de categoría por cada producto, se obtiene toda la información contextual en una sola consulta optimizada, mejorando el rendimiento en Tomcat y Render.

---

### Bloque 4: Eliminación Lógica (*Soft Delete*)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 4: Eliminación lógica para preservar integridad referencial
// =========================================================================
@Override
public boolean eliminarLogico(int id) {
    String sql = "UPDATE producto SET estado = 'Inactivo' WHERE id_producto = ?";
    try (Connection conn = getConexion();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error en eliminación lógica de producto: " + e.getMessage());
        return false;
    }
}
```

#### ¿Qué hace este bloque?
En lugar de invocar un comando destructivo `DELETE FROM producto`, actualiza el campo de control `estado` estableciendo el valor `'Inactivo'` para el identificador seleccionado.

#### Rol en el patrón Repository
Implementa la regla de negocio de **Persistencia de Auditoría** requerida por las directrices del proyecto (`AGENTS.md` §6).

#### ¿Por qué se eligió este diseño?
En un sistema comercial (ERP/CRM), eliminar físicamente un producto o cliente provocaría fallos de clave foránea en ventas o compras futuras, o la pérdida irreversible de registros históricos. La eliminación lógica permite ocultar registros inactivos de la operación cotidiana manteniendo intacta la integridad histórica y referencial.

---

### Bloque 5: Capa de Servicio y Validación de Negocio (`ClienteService`)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 5: Reglas comerciales y desacoplamiento en ClienteService
// =========================================================================
public class ClienteService {

    private final ClienteRepository clienteRepo;

    public ClienteService() {
        this.clienteRepo = new ClienteRepositoryImpl();
    }

    public String registrar(Cliente c, DatosPersonales dp) {
        if ("Natural".equalsIgnoreCase(c.getTipoCliente())) {
            if (dp.getDni() == null || dp.getDni().trim().length() != 8) {
                return "Para personas naturales el DNI debe tener exactamente 8 dígitos.";
            }
            if (clienteRepo.buscarPorDni(dp.getDni()) != null) {
                return "El DNI ingresado ya se encuentra registrado con otro cliente.";
            }
        } else if ("Empresa".equalsIgnoreCase(c.getTipoCliente())) {
            if (c.getRuc() == null || c.getRuc().trim().length() != 11) {
                return "Para empresas el RUC debe tener exactamente 11 dígitos.";
            }
            if (clienteRepo.buscarPorRuc(c.getRuc()) != null) {
                return "El RUC ingresado ya pertenece a un cliente registrado.";
            }
        }
        boolean ok = clienteRepo.registrar(c, dp);
        return ok ? null : "Ocurrió un error al persistir el cliente en la base de datos.";
    }
}
```

#### ¿Qué hace este bloque?
Contiene las reglas de validación específicas del negocio peruano (longitud de DNI de 8 dígitos para personas naturales, RUC de 11 dígitos para personas jurídicas, y control de unicidad). Si los datos son válidos, invoca al repositorio; si no, retorna mensajes claros en español.

#### Rol en el patrón Repository
Representa el **Consumidor Directo del Repositorio**. La capa de servicio contiene el "qué se debe cumplir", mientras que el repositorio se limita al "cómo se guarda".

#### ¿Por qué se eligió este diseño?
Aplica el **Principio de Responsabilidad Única (SRP)**: el repositorio no valida reglas comerciales, y el servicio no escribe sentencias SQL. Esta clara separación hace que el código sea comprensible, reutilizable y fácil de depurar.

---

### Bloque 6: Despacho y Presentación Web (`ClienteServlet`)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 6: Controlador HTTP que despacha a la vista Tabler UI
// =========================================================================
@WebServlet(name = "ClienteServlet", urlPatterns = {"/clientes"})
public class ClienteServlet extends HttpServlet {

    private final ClienteService clienteService = new ClienteService();
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                request.setAttribute("distritos", usuarioService.listarDistritos());
                request.setAttribute("esEdicion", false);
                request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-form.jsp").forward(request, response);
                break;
            case "listar":
            default:
                request.setAttribute("clientes", clienteService.listarTodos());
                request.getRequestDispatcher("/WEB-INF/views/dashboard/clientes-lista.jsp").forward(request, response);
                break;
        }
    }
}
```

#### ¿Qué hace este bloque?
Filtra solicitudes HTTP no autenticadas redirigiendo a `/login`, despacha las peticiones según el parámetro `accion`, prepara los atributos requeridos en el `HttpServletRequest` y realiza un `forward` seguro hacia las vistas JSP ubicadas bajo `WEB-INF/views/dashboard/`.

#### Rol en la arquitectura general
Actúa como el **Controlador en la arquitectura MVC**. Coordina el flujo de navegación entre las peticiones del navegador del usuario y los servicios del dominio.

#### ¿Por qué se eligió este diseño?
Mantiene las vistas JSP seguras contra accesos directos desde la URL pública (`WEB-INF/` bloquea navegación directa no autorizada) y separa radicalmente la lógica de interacción HTTP de la lógica de negocio y persistencia.

---

## 4. Conclusiones del Análisis de Patrones en el Avance 1

1. **Evolución arquitectural sólida:** A lo largo de las 5 etapas del Avance 1, el proyecto evolucionó desde una conexión JDBC elemental con contraseñas planas (Etapa 2), pasando por la centralización con el patrón **Singleton** y seguridad **BCrypt** (Etapa 3), hasta la arquitectura empresarial desacoplada con el patrón **Repository** y vistas **Tabler UI** (Etapas 4 y 5).
2. **Cumplimiento estricto de principios SOLID:**
   - **SRP (Single Responsibility):** Entidades representan datos, Repositorios gestionan persistencia, Servicios validan reglas y Servlets controlan navegación.
   - **OCP (Open/Closed):** Nuevos métodos de consulta se pueden incorporar extendiendo las interfaces sin alterar el código existente.
   - **DIP (Dependency Inversion):** Los servicios dependen de contratos abstractos (`ClienteRepository`), no de implementaciones directas acopladas a PostgreSQL.
3. **Calidad de software universitario:** El sistema cumple cabalmente con todos los requerimientos académicos del curso POO II: JDBC puro sin ORM externo (demostrando dominio manual de patrones), arquitectura limpia en capas, eliminación lógica, transacciones atómicas y protección contra inyecciones SQL mediante `PreparedStatement`.
