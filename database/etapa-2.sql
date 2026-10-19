-- =============================================================================
-- Etapa 2 — Script SQL para PostgreSQL (Supabase)
-- Proyecto: Gestiona Commerce (POO II)
-- Descripción: Tabla provisional de usuarios con contraseñas en texto plano.
--              Esta tabla se reestructurará en la Etapa 3 con roles y BCrypt.
-- =============================================================================

-- 1. Eliminar la tabla provisional si ya existiera de pruebas previas
DROP TABLE IF EXISTS usuarios CASCADE;

-- 2. Creación de la tabla provisional 'usuarios'
--    Nota: en PostgreSQL usamos GENERATED ALWAYS AS IDENTITY para la PK autoincremental
CREATE TABLE usuarios (
    id_usuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL
);

-- 3. Inserción de credenciales de prueba canónicas
--    (según 13_2_Modulos_basicos_Formas_Normales.md, Sección 5)
--    En esta Etapa 2 las contraseñas se almacenan en texto plano para evidenciar
--    el antes y después pedagógico. En la Etapa 3 se migrarán a BCrypt con salt.
INSERT INTO usuarios (nombre_usuario, contrasena) VALUES
    ('lfernandez', 'Admin#2026'),
    ('mtorres',    'Ventas#2026'),
    ('cgomez',     'Almacen#2026'),
    ('jaguilar',   'Super#2026'),
    ('rlopez',     'Invitado#2026'),
    ('amendoza',   'Bloqueo#2026');

-- 4. Consulta de verificación
SELECT id_usuario, nombre_usuario, contrasena FROM usuarios ORDER BY id_usuario;
