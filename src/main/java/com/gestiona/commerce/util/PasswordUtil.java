package com.gestiona.commerce.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para el hashing y verificación segura de contraseñas mediante BCrypt.
 * 
 * Cumple con los requerimientos de seguridad obligatorios (AGENTS.md §7):
 * - Hashing unidireccional con sal aleatoria generada por BCrypt.
 * - Factor de costo de trabajo (work factor / rounds): 12.
 * - Jamás se almacena ni compara texto plano en BD o logs.
 */
public class PasswordUtil {

    // Factor de costo de BCrypt (2^12 iteraciones, equilibrio ideal entre seguridad y latencia)
    private static final int BCRYPT_WORK_FACTOR = 12;

    /**
     * Genera un hash BCrypt seguro a partir de una contraseña en texto plano.
     * 
     * @param contrasenaPlana Contraseña enviada por el usuario
     * @return Cadena de 60 caracteres que incluye el algoritmo, costo, sal y hash
     */
    public static String hashear(String contrasenaPlana) {
        if (contrasenaPlana == null || contrasenaPlana.isEmpty()) {
            throw new IllegalArgumentException("La contraseña a hashear no puede estar vacía.");
        }
        // Genera sal criptográfica aleatoria y computa el hash
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt(BCRYPT_WORK_FACTOR));
    }

    /**
     * Compara una contraseña en texto plano contra un hash BCrypt existente.
     * 
     * @param contrasenaPlana Contraseña ingresada en el login
     * @param hashBD Hash almacenado previamente en la base de datos
     * @return true si la contraseña coincide con el hash, false en caso contrario
     */
    public static boolean verificar(String contrasenaPlana, String hashBD) {
        if (contrasenaPlana == null || hashBD == null || hashBD.trim().isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(contrasenaPlana, hashBD);
        } catch (IllegalArgumentException e) {
            // Si el hash almacenado tiene un formato inválido
            System.err.println("[PasswordUtil] Formato de hash BCrypt inválido: " + e.getMessage());
            return false;
        }
    }
}
