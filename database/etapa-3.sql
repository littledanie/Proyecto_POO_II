-- =============================================================================
-- Etapa 3 — Script SQL para PostgreSQL (Supabase)
-- Proyecto: Gestiona Commerce (POO II)
-- Descripción: Reestructuración en 3FN de control de acceso y seguridad:
--              tablas 'rol', 'permiso', 'rol_permiso' y 'usuario' con
--              contraseñas cifradas en BCrypt y control de bloqueo.
-- Esquema canónico: 13_2_Modulos_basicos_Formas_Normales.md (§2 y §5)
-- =============================================================================

-- 1. Limpieza de tablas previas (en orden inverso de dependencias FK)
DROP TABLE IF EXISTS rol_permiso CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE; -- Elimina la tabla provisional de la Etapa 2
DROP TABLE IF EXISTS permiso CASCADE;
DROP TABLE IF EXISTS rol CASCADE;

-- 2. Creación de tabla 'rol'
CREATE TABLE rol (
    id_rol INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_rol VARCHAR(30) UNIQUE NOT NULL,
    descripcion VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 3. Creación de tabla 'permiso'
CREATE TABLE permiso (
    id_permiso INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_permiso VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(150),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 4. Creación de tabla 'rol_permiso' (Relación M:N con clave primaria compuesta)
CREATE TABLE rol_permiso (
    id_rol INT NOT NULL REFERENCES rol(id_rol) ON DELETE CASCADE,
    id_permiso INT NOT NULL REFERENCES permiso(id_permiso) ON DELETE CASCADE,
    fecha_creacion TIMESTAMP DEFAULT now(),
    PRIMARY KEY (id_rol, id_permiso)
);

-- 5. Creación de tabla 'usuario' (3FN: FK a rol, contrasena_hash BCrypt, bloqueo)
CREATE TABLE usuario (
    id_usuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL REFERENCES rol(id_rol),
    ultimo_acceso TIMESTAMP,
    intentos_fallidos INT DEFAULT 0,
    bloqueado BOOLEAN DEFAULT false,
    token_recuperacion VARCHAR(255) NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- =============================================================================
-- INSERCIÓN DE DATOS DE PRUEBA CANÓNICOS (Sección 5 de 13_2_...md)
-- =============================================================================

-- Inserción de Roles
INSERT INTO rol (nombre_rol, descripcion) VALUES
    ('Administrador', 'Acceso total al sistema'),
    ('Vendedor',      'Gestiona clientes y consulta productos'),
    ('Almacenero',    'Gestiona productos y consulta proveedores'),
    ('Supervisor',    'Supervisa clientes, proveedores y productos (solo lectura)'),
    ('Invitado',      'Solo consulta de productos');

-- Inserción de Permisos
INSERT INTO permiso (nombre_permiso, descripcion) VALUES
    ('VER_DASHBOARD',        'Acceder al panel principal'),
    ('GESTIONAR_USUARIOS',   'CRUD de usuarios'),
    ('GESTIONAR_CLIENTES',   'CRUD de clientes'),
    ('VER_CLIENTES',         'Consultar clientes'),
    ('GESTIONAR_PROVEEDORES','CRUD de proveedores'),
    ('VER_PROVEEDORES',      'Consultar proveedores'),
    ('GESTIONAR_PRODUCTOS',  'CRUD de productos/servicios'),
    ('VER_PRODUCTOS',        'Consultar productos/servicios'),
    ('ASIGNAR_PERMISOS',     'Otorgar o revocar permisos a roles');

-- Inserción de Asignación Rol-Permiso (Matriz canónica)
-- 1: Administrador (todos los permisos 1 al 9)
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9);

-- 2: Vendedor (VER_DASHBOARD, GESTIONAR_CLIENTES, VER_CLIENTES, VER_PRODUCTOS)
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES
    (2, 1), (2, 3), (2, 4), (2, 8);

-- 3: Almacenero (VER_DASHBOARD, VER_PROVEEDORES, GESTIONAR_PRODUCTOS, VER_PRODUCTOS)
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES
    (3, 1), (3, 6), (3, 7), (3, 8);

-- 4: Supervisor (VER_DASHBOARD, VER_CLIENTES, VER_PROVEEDORES, VER_PRODUCTOS)
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES
    (4, 1), (4, 4), (4, 6), (4, 8);

-- 5: Invitado (VER_DASHBOARD, VER_PRODUCTOS)
INSERT INTO rol_permiso (id_rol, id_permiso) VALUES
    (5, 1), (5, 8);

-- Inserción de Usuarios con Hashes BCrypt reales (generados por SeedUsuarios)
INSERT INTO usuario (nombre_usuario, contrasena_hash, id_rol, intentos_fallidos, bloqueado) VALUES
    ('lfernandez', '$2a$12$kR1//xnhvkJLAA4WpEeeiuBJNrr3ihRp1AIu4x46yBbiCVXY87IO6', 1, 0, false),
    ('mtorres',    '$2a$12$XTgMV/DwT9h/zZ0eCrjcFesZvFtVs4uk/U/N8Bm/7e2Hfk1QlzyLC', 2, 0, false),
    ('cgomez',     '$2a$12$ZRbnsn63RjSFn8dEr5FiJOBUI1Wsgj87a3AkgHiQAS6xyy9tprk8y', 3, 0, false),
    ('jaguilar',   '$2a$12$OjKvnxr/1lIwTu/PMQN10ufvsGOT4XCvD4XLBLtDJpyxd.iKmnIhi', 4, 0, false),
    ('rlopez',     '$2a$12$jsV4eisfgz7QtUi03iNzZ.ZvwCr69G/fXDJLu69JTcvdalD9ICDtO', 5, 0, false),
    ('amendoza',   '$2a$12$w1mSLjG6XJdCeDFVo9E/guqn2OpAwTUOxnD68sdVwmy2ZRqGhKM.y', 2, 3, true);

-- Consultas de verificación
SELECT u.id_usuario, u.nombre_usuario, r.nombre_rol, u.intentos_fallidos, u.bloqueado
FROM usuario u
JOIN rol r ON u.id_rol = r.id_rol
ORDER BY u.id_usuario;
