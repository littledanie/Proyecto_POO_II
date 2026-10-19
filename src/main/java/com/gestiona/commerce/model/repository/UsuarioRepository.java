package com.gestiona.commerce.model.repository;

import com.gestiona.commerce.model.entity.*;

import java.util.List;

/**
 * Interfaz del Patrón de Diseño REPOSITORY para la gestión de Usuarios y sus Datos Personales.
 * 
 * PROPÓSITO DEL PATRÓN REPOSITORY:
 * Desacopla la lógica de negocio de la infraestructura de persistencia (acceso a BD),
 * proporcionando una abstracción de colección en memoria para las operaciones CRUD.
 */
public interface UsuarioRepository {

    /**
     * Obtiene la lista completa de usuarios con sus roles y datos personales asociados.
     * @return Lista de objetos Usuario
     */
    List<Usuario> listarTodos();

    /**
     * Busca un usuario por su identificador primario.
     * @param id Identificador de usuario
     * @return Objeto Usuario con sus relaciones cargadas o null si no existe
     */
    Usuario buscarPorId(int id);

    /**
     * Busca un usuario por su nombre de usuario único.
     * @param username Nombre de usuario para login
     * @return Objeto Usuario o null
     */
    Usuario buscarPorUsername(String username);

    /**
     * Registra un nuevo usuario y su información personal, de contacto y ubicación de forma transaccional.
     * @param usuario Datos de la cuenta de usuario
     * @param dp Datos personales, correo, teléfono y dirección
     * @return true si se registró exitosamente, false en caso contrario
     */
    boolean registrar(Usuario usuario, DatosPersonales dp);

    /**
     * Actualiza la información de cuenta y datos personales de un usuario existente.
     * @param usuario Datos actualizados de la cuenta
     * @param dp Datos personales y de contacto actualizados
     * @return true si se actualizó con éxito
     */
    boolean actualizar(Usuario usuario, DatosPersonales dp);

    /**
     * Realiza la eliminación LÓGICA de un usuario (cambio de estado a 'Inactivo', según regla AGENTS.md §6.6).
     * @param id Identificador del usuario a desactivar
     * @return true si se actualizó el estado a 'Inactivo'
     */
    boolean eliminarLogico(int id);

    /**
     * Modifica el estado de bloqueo de un usuario (para desbloquear cuentas tras 3 intentos fallidos).
     * @param id Identificador del usuario
     * @param bloqueado true para bloquear, false para desbloquear
     * @return true si la operación fue exitosa
     */
    boolean cambiarEstadoBloqueo(int id, boolean bloqueado);

    // Métodos para carga de catálogos necesarios en formularios
    List<Rol> listarRoles();
    List<Sexo> listarSexos();
    List<EstadoCivil> listarEstadosCiviles();
    List<Distrito> listarDistritos();
}
