package com.gestiona.commerce.service;

import com.gestiona.commerce.model.entity.Usuario;

/**
 * Encapsula el resultado de un intento de autenticación en el sistema.
 */
public class ResultadoLogin {

    public enum Estado {
        EXITO,
        CREDENCIALES_INVALIDAS,
        CUENTA_BLOQUEADA
    }

    private final Estado estado;
    private final String mensaje;
    private final Usuario usuario;
    private final int intentosRestantes;

    public ResultadoLogin(Estado estado, String mensaje, Usuario usuario, int intentosRestantes) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.intentosRestantes = intentosRestantes;
    }

    public static ResultadoLogin exito(Usuario usuario) {
        return new ResultadoLogin(Estado.EXITO, "Autenticación exitosa", usuario, 3);
    }

    public static ResultadoLogin credencialesInvalidas(int intentosRestantes) {
        String msg = "Usuario o contraseña incorrectos.";
        if (intentosRestantes > 0 && intentosRestantes < 3) {
            msg += " Te quedan " + intentosRestantes + " intento(s) antes del bloqueo de cuenta.";
        }
        return new ResultadoLogin(Estado.CREDENCIALES_INVALIDAS, msg, null, intentosRestantes);
    }

    public static ResultadoLogin cuentaBloqueada(String motivo) {
        return new ResultadoLogin(Estado.CUENTA_BLOQUEADA, motivo, null, 0);
    }

    public Estado getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public boolean isExitoso() {
        return estado == Estado.EXITO;
    }

    public boolean isBloqueado() {
        return estado == Estado.CUENTA_BLOQUEADA;
    }
}
