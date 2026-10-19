# Análisis del Código Fuente: Patrón Creacional Singleton (ConexionBD)

> **Curso:** Programación Orientada a Objetos II (POO II)  
> **Proyecto:** Gestiona Commerce  
> **Entregable:** Avance 1 — Etapa 3 (Paso 7: Análisis de Patrones)  
> **Autor/a:** Daniela  
> *Documento preparado para su presentación en informe académico (Google Docs).*

---

## 1. Introducción y Justificación del Patrón Singleton

### 1.1 Definición formal
El patrón **Singleton** (GoF - Gang of Four) es un patrón de diseño creacional cuyo propósito principal es garantizar que una clase posea **una única instancia** en toda la ejecución de la aplicación (JVM) y proporcionar un **punto de acceso global** a dicha instancia.

### 1.2 Problema que resuelve en Gestiona Commerce
En aplicaciones web basadas en servlets (como Apache Tomcat), múltiples peticiones concurrentes de usuarios son atendidas por diferentes hilos de ejecución. Si cada componente (servlets, servicios o DAOs) creara su propio gestor de conexión o volviera a leer los archivos de configuración y cargar los drivers en cada llamada:
1. Se consumiría memoria innecesaria al duplicar objetos de configuración.
2. Se sobrecargaría el *ClassLoader* al intentar registrar el driver JDBC reiteradamente.
3. Existiría descontrol sobre las credenciales y parámetros de conexión.

Al implementar **Singleton** en `ConexionBD`, aseguramos una única fuente de verdad para la configuración de la base de datos PostgreSQL (Supabase), optimizando los recursos y aislando la infraestructura técnica del resto de la arquitectura MVC.

---

## 2. Análisis del Código Fuente por Bloques

A continuación se desglosa el código fuente de la clase `ConexionBD` y su consumo en `LoginService`, organizándolo en bloques funcionales con su correspondiente justificación técnica y pedagógica.

```
┌─────────────────────────────────────────────────────────────┐
│                 ConexionBD (Singleton)                      │
├─────────────────────────────────────────────────────────────┤
│ - instancia: ConexionBD                                     │  ◄── Bloque 1
│ - DRIVER_CLASS: String                                      │
│ - envCache: Map<String, String>                             │
├─────────────────────────────────────────────────────────────┤
│ - ConexionBD()                                              │  ◄── Bloque 2
│ + getInstancia(): ConexionBD                                │  ◄── Bloque 3
│ + getConexion(): Connection                                 │  ◄── Bloque 4
│ + getEnvVariable(clave: String): String                     │  ◄── Bloque 5
└─────────────────────────────────────────────────────────────┘
```

---

### Bloque 1: Declaración del Atributo Estático de la Instancia Única

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 1: Atributo estático que almacena la única instancia de la clase
// =========================================================================
private static ConexionBD instancia;

// Constante con el nombre del driver oficial JDBC de PostgreSQL
private static final String DRIVER_CLASS = "org.postgresql.Driver";

// Mapa en memoria para almacenar las variables de entorno leídas de .env
private final Map<String, String> envCache;
```

#### ¿Qué hace?
Declara la variable de referencia `instancia` como `private static`. Al ser estática (`static`), pertenece a la clase y no a un objeto individual, existiendo una sola copia en toda la memoria de la JVM. Al ser privada (`private`), se impide que clases externas modifiquen o reasignen directamente esta referencia.

#### Rol en el patrón Singleton
Constituye el **contenedor de la instancia única**. Es el estado global controlado que almacena el objeto compartido por todos los hilos del sistema.

#### ¿Por qué se diseñó de esta manera?
Garantiza encapsulamiento y unicidad. Si fuera pública o no estática, cualquier clase podría sobreescribir la referencia o requerir una instancia previa para existir, violando el principio del patrón.

---

### Bloque 2: Constructor Privado

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 2: Constructor PRIVADO que impide la instanciación directa con 'new'
// =========================================================================
private ConexionBD() {
    try {
        // Registro y carga del driver JDBC en el ClassLoader
        Class.forName(DRIVER_CLASS);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException("Error: No se encontró el driver JDBC de PostgreSQL (" + DRIVER_CLASS + ")", e);
    }
    // Carga de variables de entorno locales como respaldo
    this.envCache = cargarArchivoEnv();
}
```

#### ¿Qué hace?
Define el constructor con visibilidad privada (`private`). Dentro de él, se ejecuta una sola vez la carga reflexiva del driver de PostgreSQL (`org.postgresql.Driver`) y la lectura en caché del archivo `.env`.

#### Rol en el patrón Singleton
Es el **mecanismo de restricción de instanciación**. Al anular el constructor público por defecto, el compilador prohíbe que cualquier otra clase ejecute `new ConexionBD()`.

#### ¿Por qué se diseñó de esta manera?
Si el constructor fuese público, los desarrolladores podrían instanciar múltiples gestores de conexión por error. La privacidad del constructor fuerza a toda la aplicación a pasar obligatoriamente por el punto de acceso controlado.

---

### Bloque 3: Punto de Acceso Global Sincronizado (`getInstancia`)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 3: Método público y estático de acceso global (Punto de acceso Singleton)
// =========================================================================
/**
 * Retorna la única instancia de ConexionBD (Lazy Initialization / Inicialización perezosa).
 * El modificador synchronized garantiza seguridad en entornos multihilo (Thread-Safe)
 * propios de un contenedor de servlets como Tomcat.
 */
public static synchronized ConexionBD getInstancia() {
    if (instancia == null) {
        instancia = new ConexionBD();
    }
    return instancia;
}
```

#### ¿Qué hace?
Proporciona el método estático para obtener la instancia. Utiliza **inicialización perezosa (Lazy Initialization)**: el objeto no se crea al arrancar la aplicación, sino la primera vez que un servlet o servicio lo necesita (`if (instancia == null)`). La palabra clave `synchronized` asegura que si dos solicitudes concurrentes intentan llamar al método al mismo milisegundo, la segunda esperará, evitando la creación de instancias duplicadas.

#### Rol en el patrón Singleton
Es el **punto de acceso global** exigido por la definición formal del patrón.

#### ¿Por qué se diseñó de esta manera?
- **Lazy Initialization:** Ahorra memoria y tiempo de arranque al no inicializar recursos hasta que sean indispensables.
- **Thread-Safety (`synchronized`):** En un servidor web como Apache Tomcat, múltiples peticiones HTTP se procesan en hilos concurrentes. Sin sincronización, existiría una condición de carrera (*race condition*) donde dos hilos podrían crear dos instancias distintas.

---

### Bloque 4: Método de Conexión Física (`getConexion`)

#### Fragmento de código
```java
// =========================================================================
// BLOQUE 4: Método de negocio para obtener conexiones físicas a la base de datos
// =========================================================================
public Connection getConexion() throws SQLException {
    String url = getEnvVariable("DB_URL");
    String user = getEnvVariable("DB_USER");
    String password = getEnvVariable("DB_PASSWORD");

    if (url == null || url.trim().isEmpty()) {
        throw new SQLException("Falta la variable de entorno DB_URL para conectar a la base de datos.");
    }
    if (user == null || user.trim().isEmpty()) {
        throw new SQLException("Falta la variable de entorno DB_USER para conectar a la base de datos.");
    }
    if (password == null) {
        throw new SQLException("Falta la variable de entorno DB_PASSWORD para conectar a la base de datos.");
    }

    return DriverManager.getConnection(url, user, password);
}
```

#### ¿Qué hace?
Consulta las variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`), valida que no estén vacías y solicita una conexión física activa al `DriverManager` de PostgreSQL.

#### Rol en el patrón Singleton
Representa el **servicio de negocio centralizado** que ofrece la instancia Singleton al resto de la aplicación.

#### ¿Por qué se diseñó de esta manera?
Separa la responsabilidad de configurar las credenciales y el driver respecto de la lógica comercial. Si el host de Supabase o los parámetros de SSL cambian, únicamente se modifica esta capa sin afectar a los servicios o controladores.

---

### Bloque 5: Consumo del Singleton desde la Capa de Negocio (`LoginService`)

#### Fragmento de código
```java
// Consulta parametrizada obligatoria (previene inyecciones SQL)
String sql = "SELECT u.id_usuario, u.nombre_usuario, u.contrasena_hash, ... FROM usuario u ...";

// Aplicación del patrón SINGLETON: se obtiene la conexión desde la instancia única
try (Connection con = ConexionBD.getInstancia().getConexion();
     PreparedStatement ps = con.prepareStatement(sql)) {
    
    ps.setString(1, nombreUsuario.trim());
    // ... ejecución segura
}
```

#### ¿Qué hace?
En el método `autenticar()` de `LoginService`, se invoca `ConexionBD.getInstancia().getConexion()` dentro de una estructura `try-with-resources`.

#### Rol en la arquitectura
Demuestra la **interacción cliente-patrón**. La capa de servicio desconoce cómo se configuran o de dónde provienen las credenciales; simplemente solicita una conexión a la instancia global.

#### ¿Por qué se diseñó de esta manera?
Cumple con el principio de bajo acoplamiento y alta cohesión. Al usar `try-with-resources`, la conexión física se cierra automáticamente al finalizar la consulta, evitando fugas de memoria o saturación del pool de conexiones de Supabase, mientras que la instancia Singleton permanece viva en memoria para la siguiente solicitud.

---

## 3. Comparativa: Etapa 2 (Conexión Directa) vs. Etapa 3 (Conexión Singleton)

| Criterio | Etapa 2 (Conexión Directa) | Etapa 3 (Patrón Singleton) |
|---|---|---|
| **Punto de creación** | En cada llamada (`DriverManager.getConnection(...)`) de forma dispersa. | Centralizado en `ConexionBD.getInstancia()`. |
| **Control de instancias** | Ilimitado (`new` permitido o múltiples métodos estáticos). | Controlado estrictamente: 1 sola instancia en la JVM. |
| **Carga del Driver JDBC** | Podía dispararse repetidamente. | Se realiza una sola vez en el constructor privado. |
| **Manejo en concurrencia** | Sin protección ante condiciones de carrera de configuración. | Protegido con `synchronized` (Thread-Safe). |
| **Mantenibilidad** | Baja: cambios de configuración requerían tocar múltiples clases. | Alta: cambios centralizados en un único archivo (`ConexionBD`). |

---

## 4. Conclusiones

1. **Alineación con POO II:** La implementación del patrón Singleton demuestra el dominio de modificadores de acceso (`private`), atributos estáticos (`static`), inicialización diferida (*Lazy Loading*) y sincronización de hilos (*Thread-Safety*).
2. **Eficiencia en la nube:** En entornos con límites de recursos (como los contenedores Free de Render y el Session Pooler de Supabase), la centralización provista por Singleton reduce la latencia de inicialización y previene errores por sobrecarga de drivers.
3. **Escalabilidad para las siguientes etapas:** Esta misma instancia de `ConexionBD` servirá como base para los Repositorios de Usuario (Etapa 4), Clientes, Proveedores y Productos (Etapa 5).
