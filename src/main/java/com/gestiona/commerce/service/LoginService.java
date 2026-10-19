package com.gestiona.commerce.service;

import com.gestiona.commerce.config.ConexionBD;
import com.gestiona.commerce.model.entity.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servicio encargado de la lógica de autenticación.
 * 
 * Cumple con los lineamientos del curso:
 * - Uso del patrón creacional Singleton para obtener la conexión (ConexionBD.getInstancia()).
 * - Uso estricto de PreparedStatement para evitar inyección SQL (SQLi).
 * - Manejo adecuado de recursos mediante try-with-resources.
 */
public class LoginService {

    /**
     * Valida las credenciales ingresadas contra la base de datos.
     * 
     * @param nombreUsuario Nombre de usuario ingresado en el formulario
     * @param contrasena Contraseña ingresada
     * @return Objeto Usuario si las credenciales son válidas; null si no coinciden
     * @throws SQLException Si ocurre un error al conectar o consultar la base de datos
     */
    public Usuario autenticar(String nombreUsuario, String contrasena) throws SQLException {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            return null;
        }

        // Consulta parametrizada obligatoria (previene inyecciones SQL)
        String sql = "SELECT id_usuario, nombre_usuario, contrasena FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";

        // Aplicación del patrón SINGLETON: se obtiene la conexión desde la instancia única
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario.trim());
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    return usuario;
                }
            }
        }

        // Si no hubo coincidencia en la BD, retorna null
        return null;
    }
}
