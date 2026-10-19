package com.gestiona.commerce.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que gestiona la conexión a la base de datos PostgreSQL en Supabase
 * aplicando el patrón creacional SINGLETON.
 * 
 * PROPÓSITO DEL PATRÓN SINGLETON:
 * Garantiza que exista una única instancia de la clase de configuración de
 * conexión en toda la máquina virtual de Java (JVM), proveyendo un punto de acceso
 * global a la misma. Esto evita la duplicación de configuraciones, centraliza
 * la lectura de credenciales y optimiza el consumo de memoria en el servidor.
 */
public class ConexionBD {

    // =========================================================================
    // BLOQUE 1: Atributo estático que almacena la única instancia de la clase
    // =========================================================================
    private static ConexionBD instancia;

    // Constante con el nombre del driver oficial JDBC de PostgreSQL
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    // Mapa en memoria para almacenar las variables de entorno leídas de .env
    private final Map<String, String> envCache;

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

    // =========================================================================
    // BLOQUE 4: Método de negocio para obtener conexiones físicas a la base de datos
    // =========================================================================
    /**
     * Establece y retorna una conexión activa con Supabase mediante JDBC estándar.
     * 
     * @return Connection lista para ejecutar PreparedStatement
     * @throws SQLException Si falla la conexión de red o las credenciales
     */
    public Connection getConexion() throws SQLException {
        String url = getEnvVariable("DB_URL");
        if (url == null || url.trim().isEmpty()) {
            // Soporte fallback para alias común DATABASE_URL
            url = getEnvVariable("DATABASE_URL");
        }

        // Si la URL viene en formato URI postgresql:// convertir a formato JDBC jdbc:postgresql://
        if (url != null && !url.startsWith("jdbc:")) {
            if (url.startsWith("postgres://")) {
                url = "jdbc:postgresql://" + url.substring("postgres://".length());
            } else if (url.startsWith("postgresql://")) {
                url = "jdbc:postgresql://" + url.substring("postgresql://".length());
            }
        }

        String user = getEnvVariable("DB_USER");
        String password = getEnvVariable("DB_PASSWORD");

        // Diagnóstico seguro en logs del servidor (sin exponer la contraseña)
        System.out.println("[ConexionBD] Estado de credenciales: " +
                "URL=" + (url != null ? "PRESENTE" : "FALTA") + ", " +
                "USER=" + (user != null ? "PRESENTE" : "FALTA") + ", " +
                "PASSWORD=" + (password != null ? "PRESENTE" : "FALTA"));

        if (url == null || url.trim().isEmpty()) {
            throw new SQLException("Falta la variable de entorno DB_URL (o DATABASE_URL) para conectar a la base de datos.");
        }
        if (user == null || user.trim().isEmpty()) {
            throw new SQLException("Falta la variable de entorno DB_USER para conectar a la base de datos.");
        }
        if (password == null) {
            throw new SQLException("Falta la variable de entorno DB_PASSWORD para conectar a la base de datos.");
        }

        return DriverManager.getConnection(url, user, password);
    }

    // =========================================================================
    // BLOQUE 5: Métodos auxiliares para resolución segura de credenciales
    // =========================================================================
    /**
     * Obtiene el valor de una variable de entorno con fallback al archivo .env local.
     */
    public String getEnvVariable(String clave) {
        // 1. Prioridad: Variables de entorno del sistema operativo o contenedor Docker
        String valor = System.getenv(clave);
        if (valor != null && !valor.trim().isEmpty()) {
            valor = valor.trim();
            // Limpieza de posibles comillas accidentales
            if ((valor.startsWith("\"") && valor.endsWith("\"")) || (valor.startsWith("'") && valor.endsWith("'"))) {
                valor = valor.substring(1, valor.length() - 1);
            }
            return valor;
        }

        // 2. Fallback: Variables locales de .env (para desarrollo en NetBeans)
        if (envCache != null && envCache.containsKey(clave)) {
            String v = envCache.get(clave);
            if (v != null) {
                v = v.trim();
                if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
                    v = v.substring(1, v.length() - 1);
                }
                return v;
            }
        }
        return null;
    }

    /**
     * Lee el archivo .env si se encuentra en la raíz del proyecto.
     */
    private Map<String, String> cargarArchivoEnv() {
        Map<String, String> mapa = new HashMap<>();
        String[] posiblesRutas = {
            ".env",
            "../.env",
            System.getProperty("user.dir") + File.separator + ".env"
        };

        for (String ruta : posiblesRutas) {
            File archivo = new File(ruta);
            if (archivo.exists() && archivo.isFile()) {
                try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = lector.readLine()) != null) {
                        linea = linea.trim();
                        if (linea.isEmpty() || linea.startsWith("#")) {
                            continue;
                        }
                        int separador = linea.indexOf('=');
                        if (separador > 0) {
                            String k = linea.substring(0, separador).trim();
                            String v = linea.substring(separador + 1).trim();
                            if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
                                v = v.substring(1, v.length() - 1);
                            }
                            mapa.put(k, v);
                        }
                    }
                    break;
                } catch (IOException e) {
                    System.err.println("[ConexionBD] Advertencia al leer .env: " + e.getMessage());
                }
            }
        }
        return mapa;
    }
}
