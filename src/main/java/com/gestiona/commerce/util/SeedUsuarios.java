package com.gestiona.commerce.util;

/**
 * Clase utilitaria para generar los hashes BCrypt reales de los usuarios
 * de prueba canónicos (según 13_2_Modulos_basicos_Formas_Normales.md, Sección 5).
 * 
 * REGLA PERMANENTE (AGENTS.md §6.5):
 * Las contraseñas se generan mediante esta clase con BCrypt a partir de las
 * credenciales de prueba; jamás se deben pegar hashes inventados.
 */
public class SeedUsuarios {

    public static void main(String[] args) {
        System.out.println("-- =====================================================");
        System.out.println("-- Hashes generados por SeedUsuarios con BCrypt (cost=12)");
        System.out.println("-- =====================================================");

        String[][] usuariosPrueba = {
            {"lfernandez", "1", "Admin#2026", "0", "false"},
            {"mtorres",    "2", "Ventas#2026", "0", "false"},
            {"cgomez",     "3", "Almacen#2026", "0", "false"},
            {"jaguilar",   "4", "Super#2026", "0", "false"},
            {"rlopez",     "5", "Invitado#2026", "0", "false"},
            {"amendoza",   "2", "Bloqueo#2026", "3", "true"}
        };

        for (String[] u : usuariosPrueba) {
            String usuario = u[0];
            String idRol = u[1];
            String passwordPlano = u[2];
            String intentos = u[3];
            String bloqueado = u[4];

            String hash = PasswordUtil.hashear(passwordPlano);
            System.out.printf("INSERT INTO usuario (nombre_usuario, contrasena_hash, id_rol, intentos_fallidos, bloqueado) VALUES ('%s', '%s', %s, %s, %s);%n",
                    usuario, hash, idRol, intentos, bloqueado);
        }
    }
}
