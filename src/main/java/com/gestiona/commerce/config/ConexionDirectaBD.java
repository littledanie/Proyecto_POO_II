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
 * Clase de conexión directa a la base de datos PostgreSQL (Supabase).
 * 
 * NOTA DE ARQUITECTURA (Etapa 2):
 * En esta etapa se implementa una conexión directa sin patrón creacional.
 * Cada invocación a getConexion() solicita una nueva conexión física.
 * En la Etapa 3, este diseño evolucionará al patrón Singleton para optimizar
 * el uso de recursos y asegurar una única instancia global de configuración.
 */
public class ConexionDirectaBD {

    // Nombre de la clase del driver JDBC oficial de PostgreSQL
    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    // Caché en memoria para las variables leídas del archivo .env local
    private static Map<String, String> envCache = null;

    static {
        // Cargar el driver en memoria al inicializar la clase
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se encontró el driver JDBC de PostgreSQL (" + DRIVER_CLASS + ")", e);
        }
    }

    /**
     * Obtiene el valor de una variable de entorno, con fallback al archivo .env local.
     * Esto permite que el proyecto funcione de forma transparente tanto en producción
     * (Render con variables de entorno del contenedor) como en el entorno local (NetBeans/Tomcat).
     * 
     * @param clave Nombre de la variable (ej. DB_URL, DB_USER, DB_PASSWORD)
     * @return El valor de la variable o null si no se encuentra
     */
    public static String getEnvVariable(String clave) {
        // 1. Prioridad: Variable de entorno del sistema o contenedor
        String valor = System.getenv(clave);
        if (valor != null && !valor.trim().isEmpty()) {
            return valor.trim();
        }

        // 2. Fallback: Leer archivo .env local si existe
        if (envCache == null) {
            envCache = cargarArchivoEnv();
        }
        return envCache.get(clave);
    }

    /**
     * Lee un archivo .env si se encuentra en la ruta del proyecto o directorio actual.
     */
    private static Map<String, String> cargarArchivoEnv() {
        Map<String, String> mapa = new HashMap<>();
        // Posibles ubicaciones del archivo .env (directorio raíz o ruta de ejecución)
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
                        // Ignorar líneas vacías y comentarios
                        if (linea.isEmpty() || linea.startsWith("#")) {
                            continue;
                        }
                        int separador = linea.indexOf('=');
                        if (separador > 0) {
                            String k = linea.substring(0, separador).trim();
                            String v = linea.substring(separador + 1).trim();
                            // Quitar comillas si las tuviera
                            if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
                                v = v.substring(1, v.length() - 1);
                            }
                            mapa.put(k, v);
                        }
                    }
                    break; // Archivo encontrado y procesado
                } catch (IOException e) {
                    System.err.println("[ConexionDirectaBD] Advertencia: No se pudo leer el archivo .env: " + e.getMessage());
                }
            }
        }
        return mapa;
    }

    /**
     * Establece y retorna una nueva conexión JDBC con la base de datos Supabase.
     * 
     * @return Objeto Connection listo para ejecutar consultas
     * @throws SQLException Si falla la autenticación o la conexión de red
     */
    public static Connection getConexion() throws SQLException {
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

        // Conexión directa mediante DriverManager (Etapa 2: sin Singleton todavía)
        return DriverManager.getConnection(url, user, password);
    }
}
