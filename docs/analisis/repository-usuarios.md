# Análisis del Código Fuente: Patrón Repository en el CRUD de Usuarios

> **Curso:** Programación Orientada a Objetos II (POO II)  
> **Proyecto:** Gestiona Commerce  
> **Entregable:** Avance 1 — Etapa 4 (Paso 10: Análisis de Patrones)  
> **Autor/a:** Daniela  
> *Documento preparado para su presentación en informe académico (Google Docs).*

---

## 1. Introducción y Justificación del Patrón Repository

### 1.1 Definición formal
El patrón **Repository** (introducido por Martin Fowler y formalizado en el Diseño Guiado por el Dominio - DDD) media entre la capa de lógica de negocio del dominio y la capa de acceso a datos utilizando una interfaz similar a una colección en memoria de objetos de negocio.

### 1.2 Problema que resuelve en Gestiona Commerce
En un sistema comercial universitario con entidades complejas como `Usuario`, `DatosPersonales`, `Rol` y ubicaciones geográficas:
1. Sin Repository, las consultas SQL compuestas y la manipulación de `ResultSet` invaden los Servlets y Servicios, generando código espagueti con alto acoplamiento.
2. Si cambia el motor de base de datos o la estructura física de las tablas, se rompería toda la lógica de negocio.
3. El patrón **Repository** aísla completamente la persistencia física (SQL, JDBC, conexiones, transacciones) detrás de una interfaz limpia de alto nivel (`UsuarioRepository`), permitiendo que el servicio (`UsuarioService`) trabaje únicamente con entidades POJO como si estuviesen en una lista en memoria.

---

## 2. Estructura Arquitectural del Patrón

```
┌────────────────────────────────────────────────────────┐
│                   UsuarioService                       │  ◄── Capa de Negocio
│         (Depende solo de la interfaz abstracta)        │
└──────────────────────────┬─────────────────────────────┘
                           │ (usa)
                           ▼
┌────────────────────────────────────────────────────────┐
│             <<interface>> UsuarioRepository            │  ◄── Bloque 1
├────────────────────────────────────────────────────────┤
│ + listarTodos(): List<Usuario>                         │
│ + buscarPorId(id: int): Usuario                        │
│ + registrar(u: Usuario, dp: DatosPersonales): boolean   │
│ + actualizar(u: Usuario, dp: DatosPersonales): boolean │
│ + eliminarLogico(id: int): boolean                     │
│ + cambiarEstadoBloqueo(id: int, b: boolean): boolean   │
└──────────────────────────▲─────────────────────────────┘
                           │ (implementa)
┌──────────────────────────┴─────────────────────────────┐
│                 UsuarioRepositoryImpl                  │  ◄── Bloques 2 al 5
├────────────────────────────────────────────────────────┤
│ - getConexion(): Connection (Consume Singleton)        │
│ + listarTodos(), registrar(), actualizar(), ...        │
└────────────────────────────────────────────────────────┘
```

---

## 3. Análisis del Código Fuente por Bloques

### Bloque 1: Definición de la Interfaz del Repositorio (`UsuarioRepository`)

#### Fragmento de código
```java
package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.*;
import java.util.List;

public interface UsuarioRepository {
    List<Usuario> listarTodos();
    Usuario buscarPorId(int id);
    Usuario buscarPorUsername(String username);
    boolean registrar(Usuario usuario, DatosPersonales dp);
    boolean actualizar(Usuario usuario, DatosPersonales dp);
    boolean eliminarLogico(int id);
    boolean cambiarEstadoBloqueo(int id, boolean bloqueado);

    List<Rol> listarRoles();
    List<Sexo> listarSexos();
    List<EstadoCivil> listarEstadosCiviles();
    List<Distrito> listarDistritos();
}
```

#### ¿Qué hace?
Declara el contrato público con las operaciones de lectura, búsqueda, persistencia transaccional y catálogos para los usuarios y sus datos personales, sin contener ningún detalle de implementación de PostgreSQL ni JDBC.

#### Rol en el patrón Repository
Representa el **Contrato de Abstracción**. Permite la inversión de dependencias (Principio D de SOLID): los módulos de alto nivel (como `UsuarioService`) dependen de esta abstracción y no de una implementación concreta.

#### ¿Por qué se diseñó de esta manera?
Garantiza que la lógica de negocio permanezca pura e independiente. Si en el futuro se migrase a otra base de datos o se realizaran pruebas unitarias con un repositorio en memoria (Mock), el código del servicio no requeriría ninguna modificación.

---

### Bloque 2: Integración con el Patrón Creacional Singleton

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 1: Obtención de conexión a través del patrón creacional Singleton
// =========================================================================
private Connection getConexion() throws SQLException {
    return ConexionBD.getInstancia().getConexion();
}
```

#### ¿Qué hace?
El repositorio no gestiona cadenas de conexión ni drivers, sino que delega la obtención de cada conexión física JDBC a la instancia única global provista por `ConexionBD.getInstancia()`.

#### Rol en la arquitectura
Muestra la **sinergia entre patrones de diseño**: el patrón estructural/arquitectural *Repository* consume directamente el patrón creacional *Singleton* implementado en la Etapa 3.

#### ¿Por qué se diseñó de esta manera?
Evita que el repositorio duplique lógica de configuración y lectura de variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`), manteniendo el principio de responsabilidad única (SRP).

---

### Bloque 3: Mapeo Relacional de Consulta Compleja Multitabla (`listarTodos`)

#### Fragmento de código
```java
String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, u.id_rol, ... " +
             "       r.nombre_rol, dp.nombres, dp.apellidos, dp.dni, ... " +
             "       ce.correo, t.numero_telefono, dir.calle_avenida, d.nombre_distrito " +
             "FROM usuario u " +
             "JOIN rol r ON u.id_rol = r.id_rol " +
             "LEFT JOIN datos_personales dp ON u.id_datos_personales = dp.id_datos_personales " +
             "LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales " +
             "LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales " +
             "LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales " +
             "LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito " +
             "ORDER BY u.id_usuario ASC";

try (Connection con = getConexion();
     PreparedStatement ps = con.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    while (rs.next()) {
        lista.add(mapearUsuarioCompleto(rs));
    }
}
```

#### ¿Qué hace?
Ejecuta una consulta SQL con múltiples `JOIN` entre las tablas normalizadas en 3FN y recompone el grafo de objetos Java (`Usuario` conteniendo a `Rol` y a `DatosPersonales`, el cual a su vez contiene `Distrito`, correo y teléfono).

#### Rol en el patrón Repository
Cumple el rol de **Mapeador Relacional a Objetos (ORM a mano)**. Traduce registros tabulares planos de la base de datos en un modelo de objetos enriquecido para la capa de presentación.

#### ¿Por qué se diseñó de esta manera?
Oculta la complejidad relacional de la base de datos a los controladores JSP/Servlets. Para la vista, un usuario es simplemente un objeto `usuario.getDatosPersonales().getNombreCompleto()`.

---

### Bloque 4: Operación de Persistencia Transaccional Atómica (`registrar`)

#### Fragmento de código
```java
Connection con = null;
try {
    con = getConexion();
    con.setAutoCommit(false); // Inicio de transacción atómica

    // 1. Insertar datos personales y recuperar la clave primaria generada (RETURNING)
    int idGeneradoDP = 0;
    try (PreparedStatement psDP = con.prepareStatement(sqlDP)) {
        // ... seteo de parámetros
        try (ResultSet rs = psDP.executeQuery()) {
            if (rs.next()) idGeneradoDP = rs.getInt(1);
        }
    }

    // 2. Insertar correo electrónico
    // 3. Insertar teléfono
    // 4. Insertar dirección
    // 5. Insertar cuenta de usuario vinculada con idGeneradoDP
    try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
        psU.setInt(4, idGeneradoDP);
        psU.executeUpdate();
    }

    con.commit(); // Confirmación de todos los cambios
    return true;
} catch (SQLException e) {
    if (con != null) con.rollback(); // Rollback ante cualquier falla
    return false;
}
```

#### ¿Qué hace?
Registra de forma coordinada los datos de la persona en `datos_personales`, sus canales de contacto (`correo_electronico`, `telefono`, `direccion`) y finalmente la cuenta en `usuario`. Todo el proceso está protegido por una transacción JDBC con `setAutoCommit(false)`, `commit()` y `rollback()`.

#### Rol en el patrón Repository
Asegura la **Integridad y Coherencia de Datos (ACID)**. Garantiza que no queden datos personales huérfanos sin usuario o usuarios sin datos personales.

#### ¿Por qué se diseñó de esta manera?
Si fallara la inserción de la dirección o de la cuenta, la transacción revierte todos los cambios automáticamente (`rollback()`), impidiendo la corrupción de la base de datos.

---

### Bloque 5: Eliminación Lógica de Entidades (`eliminarLogico`)

#### Fragmento de código
```java
@Override
public boolean eliminarLogico(int id) {
    String sql = "UPDATE usuario SET estado = 'Inactivo', fecha_actualizacion = now() WHERE id_usuario = ?";
    try (Connection con = getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("[UsuarioRepositoryImpl] Error en eliminación lógica: " + e.getMessage());
        return false;
    }
}
```

#### ¿Qué hace?
En lugar de ejecutar un `DELETE FROM usuario` físico que destruiría registros históricos y violaría claves foráneas de auditoría, ejecuta una actualización lógica (`estado = 'Inactivo'`).

#### Rol en el patrón Repository
Aplica la **política de preservación de datos y auditoría** exigida por los estándares del curso (AGENTS.md §6.6).

#### ¿Por qué se diseñó de esta manera?
La eliminación física de usuarios rompería la trazabilidad de auditoría (`usuario_registro`) y generaría errores de restricción de clave foránea en ventas o movimientos futuros.

---

### Bloque 6: Inyección y Consumo en la Capa de Negocio (`UsuarioService`)

#### Fragmento de código
```java
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    public String registrarUsuario(Usuario usuario, DatosPersonales dp, String contrasenaPlana) {
        // 1. Validaciones de negocio (campos obligatorios, formato de DNI, etc.)
        // 2. Verificación de unicidad de username mediante el repositorio
        if (usuarioRepository.buscarPorUsername(usuario.getNombreUsuario().trim()) != null) {
            return "El nombre de usuario ya está registrado.";
        }
        // 3. Hasheo seguro de la contraseña con BCrypt (costo 12)
        usuario.setContrasenaHash(PasswordUtil.hashear(contrasenaPlana));

        // 4. Delegación de la persistencia al Repository
        boolean exito = usuarioRepository.registrar(usuario, dp);
        return exito ? null : "Error en base de datos.";
    }
}
```

#### ¿Qué hace?
El servicio valida las reglas comerciales (campos obligatorios, unicidad del usuario) y aplica el hashing BCrypt antes de solicitar la persistencia al repositorio.

#### Rol en la arquitectura
Demuestra la **separación limpia de responsabilidades**:
- `UsuarioService`: Sabe *qué* reglas de negocio validar y *cómo* procesar la seguridad.
- `UsuarioRepository`: Sabe *cómo* persistir y recuperar los datos de la base de datos.

#### ¿Por qué se diseñó de esta manera?
Evita que la lógica de hashing o de validación de negocio se mezcle con el código SQL. Si mañana cambia la regla de longitud de contraseña o se añade un nuevo rol, el repositorio permanece inalterado.

---

## 4. Comparación: Enfoque DAO Tradicional vs. Patrón Repository

| Aspecto | Enfoque DAO Tradicional | Patrón Repository |
|---|---|---|
| **Nivel de Abstracción** | Orientado a la base de datos (operaciones CRUD directas por tabla: `UsuarioDAO`, `DatosPersonalesDAO` separados). | Orientado al Dominio / Negocio (trata la agregación `Usuario + DatosPersonales` como una unidad lógica). |
| **Manejo de Transacciones** | La transacción queda expuesta en el servicio o requiere pasar la `Connection` entre múltiples DAOs. | El repositorio encapsula la transacción internamente, entregando operaciones atómicas (`registrar(u, dp)`). |
| **Simulación de Colección** | Métodos con firma SQL (`insertRecord`, `updateRow`). | Métodos con semántica de colección (`listarTodos`, `buscarPorId`, `registrar`). |
| **Cohesión y Acoplamiento** | Alto acoplamiento a las tablas individuales. | Bajo acoplamiento; el servicio solo conoce objetos del dominio. |

---

## 5. Conclusiones

1. **Alineación con POO II:** La implementación del patrón Repository demuestra el dominio de polimorfismo mediante interfaces (`UsuarioRepository`), encapsulamiento de JDBC y manejo de transacciones con `PreparedStatement`.
2. **Auditoría e Integridad:** El uso de transacciones con `rollback()` y la eliminación lógica garantizan la estabilidad y la preservación de los datos en PostgreSQL/Supabase.
3. **Escalabilidad para la Etapa 5:** Esta misma arquitectura de repositorio servirá como plantilla para implementar los repositorios de Clientes, Proveedores y Productos/Mercadería.
