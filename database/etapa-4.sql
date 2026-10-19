-- =============================================================================
-- Etapa 4 — Script SQL para PostgreSQL (Supabase)
-- Proyecto: Gestiona Commerce (POO II)
-- Descripción: Datos personales, contacto, ubicación geográfica y vinculación
--              con la tabla 'usuario' (ALTER TABLE).
-- Esquema canónico: 13_2_Modulos_basicos_Formas_Normales.md (§2 y §5)
-- =============================================================================

-- 1. Limpieza preventiva de tablas de Etapa 4 (si ya existieran)
DROP TABLE IF EXISTS direccion CASCADE;
DROP TABLE IF EXISTS telefono CASCADE;
DROP TABLE IF EXISTS correo_electronico CASCADE;
-- Desvincular id_datos_personales de usuario temporalmente si ya existiera
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'usuario' AND column_name = 'id_datos_personales'
    ) THEN
        ALTER TABLE usuario DROP COLUMN id_datos_personales CASCADE;
    END IF;
END $$;
DROP TABLE IF EXISTS datos_personales CASCADE;
DROP TABLE IF EXISTS distrito CASCADE;
DROP TABLE IF EXISTS provincia CASCADE;
DROP TABLE IF EXISTS departamento CASCADE;
DROP TABLE IF EXISTS pais CASCADE;
DROP TABLE IF EXISTS estado_civil CASCADE;
DROP TABLE IF EXISTS sexo CASCADE;

-- 2. Catálogos básicos de personas
CREATE TABLE sexo (
    id_sexo INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    descripcion VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE estado_civil (
    id_estado_civil INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    descripcion VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 3. Jerarquía geográfica (3FN territorial)
CREATE TABLE pais (
    id_pais INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_pais VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE departamento (
    id_departamento INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_departamento VARCHAR(100) NOT NULL,
    id_pais INT NOT NULL REFERENCES pais(id_pais),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE provincia (
    id_provincia INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_provincia VARCHAR(100) NOT NULL,
    id_departamento INT NOT NULL REFERENCES departamento(id_departamento),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE distrito (
    id_distrito INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_distrito VARCHAR(100) NOT NULL,
    id_provincia INT NOT NULL REFERENCES provincia(id_provincia),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 4. Entidad 'datos_personales' (Persona compartida)
CREATE TABLE datos_personales (
    id_datos_personales INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombres VARCHAR(80) NOT NULL,
    apellidos VARCHAR(80) NOT NULL,
    dni VARCHAR(15) UNIQUE,
    fecha_nacimiento DATE,
    id_sexo INT REFERENCES sexo(id_sexo),
    id_estado_civil INT REFERENCES estado_civil(id_estado_civil),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 5. Canales de contacto y ubicación
CREATE TABLE correo_electronico (
    id_correo INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_datos_personales INT NOT NULL REFERENCES datos_personales(id_datos_personales) ON DELETE CASCADE,
    correo VARCHAR(80) NOT NULL,
    tipo_correo VARCHAR(20) DEFAULT 'personal', -- personal, laboral, institucional
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE telefono (
    id_telefono INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_datos_personales INT NOT NULL REFERENCES datos_personales(id_datos_personales) ON DELETE CASCADE,
    numero_telefono VARCHAR(20) NOT NULL,
    tipo_telefono VARCHAR(20) DEFAULT 'móvil', -- móvil, fijo, oficina
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE direccion (
    id_direccion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_datos_personales INT NOT NULL REFERENCES datos_personales(id_datos_personales) ON DELETE CASCADE,
    id_distrito INT NOT NULL REFERENCES distrito(id_distrito),
    calle_avenida VARCHAR(150) NOT NULL,
    numero VARCHAR(10),
    piso VARCHAR(10),
    tipo_direccion VARCHAR(20) DEFAULT 'domicilio', -- domicilio, trabajo, fiscal
    referencia VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 6. ALTER TABLE en usuario para vincular a datos_personales (según D3 de 13_2_...md)
ALTER TABLE usuario 
    ADD COLUMN id_datos_personales INT UNIQUE REFERENCES datos_personales(id_datos_personales);

-- =============================================================================
-- INSERCIÓN DE DATOS CANÓNICOS (Sección 5 de 13_2_...md)
-- =============================================================================

-- Catálogo: Sexo
INSERT INTO sexo (descripcion) VALUES
    ('Masculino'),
    ('Femenino'),
    ('No binario'),
    ('Prefiere no decirlo');

-- Catálogo: Estado Civil
INSERT INTO estado_civil (descripcion) VALUES
    ('Soltero'),
    ('Casado'),
    ('Divorciado'),
    ('Viudo'),
    ('Conviviente');

-- Catálogo Geográfico: Países
INSERT INTO pais (nombre_pais) VALUES
    ('Perú'),
    ('Chile'),
    ('Ecuador'),
    ('Colombia'),
    ('Argentina'),
    ('Bolivia');

-- Departamentos (Perú, id_pais = 1)
INSERT INTO departamento (nombre_departamento, id_pais) VALUES
    ('Lima', 1),
    ('Cusco', 1),
    ('Arequipa', 1),
    ('Piura', 1),
    ('Callao', 1);

-- Provincias
INSERT INTO provincia (nombre_provincia, id_departamento) VALUES
    ('Lima', 1),
    ('Cusco', 2),
    ('Urubamba', 2),
    ('Arequipa', 3),
    ('Camaná', 3),
    ('Piura', 4),
    ('Paita', 4),
    ('Callao', 5);

-- Distritos
INSERT INTO distrito (nombre_distrito, id_provincia) VALUES
    ('Miraflores', 1),
    ('San Isidro', 1),
    ('Santiago de Surco', 1),
    ('Jesús María', 1),
    ('Cusco', 2),
    ('San Blas', 2),
    ('Urubamba', 3),
    ('Ollantaytambo', 3),
    ('Yanahuara', 4),
    ('Cayma', 4),
    ('Camaná', 5),
    ('Piura', 6),
    ('Castilla', 6),
    ('Paita', 7),
    ('Bellavista', 8),
    ('La Perla', 8);

-- Datos Personales (DP01 a DP13)
INSERT INTO datos_personales (nombres, apellidos, dni, fecha_nacimiento, id_sexo, id_estado_civil) VALUES
    ('Luis',    'Fernández Paredes', '45896321', '1990-05-21', 1, 2), -- DP01 (Usuario 1: lfernandez)
    ('María',   'Torres Vargas',     '48963214', '1989-08-11', 2, 2), -- DP02 (Usuario 2: mtorres)
    ('Carla',   'Gómez Rivas',       '50987452', '1995-01-09', 2, 1), -- DP03 (Usuario 3: cgomez)
    ('José',    'Aguilar Ramos',     '47256314', '1987-12-15', 1, 2), -- DP04 (Usuario 4: jaguilar)
    ('Ricardo', 'López Medina',      '45789632', '1992-04-18', 1, 1), -- DP05 (Usuario 5: rlopez)
    ('Andrea',  'Mendoza Castillo',  '51896325', '1996-02-25', 2, 1), -- DP06 (Usuario 6: amendoza)
    ('Sofía',   'Ramírez León',      '50897415', '1997-06-22', 2, 1), -- DP07 (Cliente 1)
    ('Diego',   'Rojas Gutiérrez',   '47859632', '1991-09-12', 1, 3), -- DP08 (Cliente 2)
    ('Camila',  'Vargas Soto',       '52478963', '1998-11-28', 2, 1), -- DP09 (Cliente 3)
    ('Felipe',  'Castro Arias',      '46321587', '1994-10-05', 1, 2), -- DP10 (Cliente 4)
    ('Arturo',  'Silva Díaz',        '49563124', '1985-03-03', 1, 2), -- DP11 (Contacto Proveedor 1)
    ('Natalia', 'Espinoza Campos',   '52987415', '1999-01-30', 2, 1), -- DP12 (Contacto Proveedor 2)
    ('Hugo',    'Pacheco Salazar',   '46987123', '1992-08-14', 1, 5); -- DP13 (Contacto Proveedor 3)

-- Correos Electrónicos
INSERT INTO correo_electronico (id_datos_personales, correo, tipo_correo) VALUES
    (1, 'lfernandez@gmail.com', 'personal'),
    (2, 'mariatorres@gmail.com', 'personal'),
    (3, 'carlagomez@hotmail.com', 'personal'),
    (4, 'jaguilar@yahoo.com', 'personal'),
    (5, 'rlopez@gmail.com', 'personal'),
    (6, 'andrea.mendoza@hotmail.com', 'personal'),
    (7, 'sofia.ramirez@gmail.com', 'personal'),
    (8, 'diego.rojas@outlook.com', 'laboral'),
    (9, 'camila.vargas@gmail.com', 'personal'),
    (10, 'felipe.castro@hotmail.com', 'personal'),
    (11, 'arturo.silva@silvaherramientas.pe', 'laboral'),
    (12, 'natalia.espinoza@espinoza.pe', 'laboral'),
    (13, 'hugo.pacheco@pinturassur.pe', 'laboral');

-- Teléfonos
INSERT INTO telefono (id_datos_personales, numero_telefono, tipo_telefono) VALUES
    (1, '999456123', 'móvil'),
    (2, '987654321', 'móvil'),
    (3, '951357456', 'móvil'),
    (4, '942753951', 'móvil'),
    (5, '934256789', 'móvil'),
    (6, '945123687', 'móvil'),
    (7, '941236587', 'móvil'),
    (8, '999874512', 'móvil'),
    (9, '956874123', 'móvil'),
    (10, '953214879', 'móvil'),
    (11, '972458963', 'móvil'),
    (12, '987412365', 'móvil'),
    (13, '987654987', 'móvil');

-- Direcciones
INSERT INTO direccion (id_datos_personales, id_distrito, calle_avenida, numero, tipo_direccion) VALUES
    (1, 1, 'Av. Larco', '345', 'domicilio'),
    (2, 2, 'Calle Los Sauces', '112', 'domicilio'),
    (3, 6, 'Jr. San Blas', '51', 'domicilio'),
    (4, 9, 'Av. Ejército', '200', 'domicilio'),
    (5, 15, 'Av. Guardia Chalaca', '765', 'domicilio'),
    (6, 3, 'Calle Los Pinos', '234', 'domicilio'),
    (7, 16, 'Av. Universitaria', '1789', 'domicilio'),
    (8, 4, 'Av. Angamos Oeste', '950', 'fiscal'),
    (9, 12, 'Jr. Amazonas', '412', 'domicilio'),
    (10, 13, 'Av. Prolongación Grau', '540', 'domicilio'),
    (11, 15, 'Av. Argentina', '2100', 'fiscal'),
    (12, 5, 'Jr. Huallaga', '675', 'fiscal'),
    (13, 10, 'Av. Los Incas', '890', 'fiscal');

-- 7. Vinculación canónica de los 6 usuarios de prueba con sus datos personales (1→1, 2→2, ..., 6→6)
UPDATE usuario SET id_datos_personales = 1 WHERE id_usuario = 1;
UPDATE usuario SET id_datos_personales = 2 WHERE id_usuario = 2;
UPDATE usuario SET id_datos_personales = 3 WHERE id_usuario = 3;
UPDATE usuario SET id_datos_personales = 4 WHERE id_usuario = 4;
UPDATE usuario SET id_datos_personales = 5 WHERE id_usuario = 5;
UPDATE usuario SET id_datos_personales = 6 WHERE id_usuario = 6;

-- Consulta de verificación integrada
SELECT u.id_usuario, u.nombre_usuario, r.nombre_rol, dp.nombres, dp.apellidos, dp.dni,
       ce.correo, t.numero_telefono, d.nombre_distrito, u.estado
FROM usuario u
JOIN rol r ON u.id_rol = r.id_rol
LEFT JOIN datos_personales dp ON u.id_datos_personales = dp.id_datos_personales
LEFT JOIN correo_electronico ce ON dp.id_datos_personales = ce.id_datos_personales
LEFT JOIN telefono t ON dp.id_datos_personales = t.id_datos_personales
LEFT JOIN direccion dir ON dp.id_datos_personales = dir.id_datos_personales
LEFT JOIN distrito d ON dir.id_distrito = d.id_distrito
ORDER BY u.id_usuario;
