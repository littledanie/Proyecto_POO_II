-- =============================================================================
-- Etapa 5 — Script SQL para PostgreSQL (Supabase)
-- Proyecto: Gestiona Commerce (POO II)
-- Descripción: Catálogos de productos (categoría, marca, unidad de medida),
--              productos/servicios/insumos, clientes y proveedores.
-- Esquema canónico: 13_2_Modulos_basicos_Formas_Normales.md (§2 y §5)
-- =============================================================================

-- 1. Limpieza preventiva de tablas de Etapa 5
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS proveedor CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS unidad_medida CASCADE;
DROP TABLE IF EXISTS marca CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;

-- 2. Catálogos de mercadería / productos
CREATE TABLE categoria (
    id_categoria INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_categoria VARCHAR(60) UNIQUE NOT NULL,
    descripcion VARCHAR(150),
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE marca (
    id_marca INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_marca VARCHAR(60) UNIQUE NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

CREATE TABLE unidad_medida (
    id_unidad_medida INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    abreviatura VARCHAR(10) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 3. Entidad 'cliente' (vinculada a datos_personales D2)
CREATE TABLE cliente (
    id_cliente INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_datos_personales INT NOT NULL REFERENCES datos_personales(id_datos_personales),
    tipo_cliente VARCHAR(20) NOT NULL CHECK (tipo_cliente IN ('Natural', 'Empresa')),
    ruc VARCHAR(11) NULL,
    razon_social VARCHAR(120) NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 4. Entidad 'proveedor' (vinculada a datos_personales como persona de contacto)
CREATE TABLE proveedor (
    id_proveedor INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_datos_personales INT NOT NULL REFERENCES datos_personales(id_datos_personales),
    ruc VARCHAR(11) UNIQUE NOT NULL,
    razon_social VARCHAR(120) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- 5. Entidad 'producto' (mercadería, insumo, producto, servicio)
CREATE TABLE producto (
    id_producto INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    tipo VARCHAR(15) NOT NULL CHECK (tipo IN ('MERCADERIA', 'PRODUCTO', 'INSUMO', 'SERVICIO')),
    id_categoria INT NOT NULL REFERENCES categoria(id_categoria),
    id_marca INT REFERENCES marca(id_marca), -- Nulo en servicios
    id_unidad_medida INT NOT NULL REFERENCES unidad_medida(id_unidad_medida),
    precio_compra NUMERIC(10,2) DEFAULT 0.00,
    precio_venta NUMERIC(10,2) NOT NULL,
    stock_actual INT DEFAULT 0, -- 0 en servicios
    stock_minimo INT DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT now(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT 'sistema',
    estado VARCHAR(15) DEFAULT 'Activo'
);

-- =============================================================================
-- INSERCIÓN DE DATOS CANÓNICOS (Sección 5 de 13_2_...md)
-- =============================================================================

-- Categorías
INSERT INTO categoria (nombre_categoria, descripcion) VALUES
    ('Herramientas manuales', 'Herramientas de accionamiento manual'),
    ('Herramientas eléctricas', 'Herramientas de potencia eléctrica y baterías'),
    ('Pinturas', 'Pinturas látex, esmaltes y solventes'),
    ('Tornillería y fijación', 'Tornillos, tarugos y pernos'),
    ('Materiales de construcción', 'Cemento, agregados y afines'),
    ('Gasfitería', 'Tuberías, conexiones y grifería'),
    ('Servicios', 'Servicios profesionales y mano de obra');

-- Marcas
INSERT INTO marca (nombre_marca) VALUES
    ('Stanley'),
    ('Bosch'),
    ('Truper'),
    ('Pavco'),
    ('Genérica');

-- Unidades de Medida
INSERT INTO unidad_medida (nombre, abreviatura) VALUES
    ('Unidad', 'UND'),
    ('Kilogramo', 'KG'),
    ('Metro', 'M'),
    ('Galón', 'GL'),
    ('Caja', 'CJ'),
    ('Servicio', 'SRV');

-- Clientes (vinculados a personas DP07 a DP10 previamente creadas en etapa-4.sql)
INSERT INTO cliente (id_datos_personales, tipo_cliente, ruc, razon_social) VALUES
    (7, 'Natural', NULL, NULL),
    (8, 'Empresa', '20512345678', 'Constructora Rojas SAC'),
    (9, 'Natural', NULL, NULL),
    (10, 'Natural', NULL, NULL);

-- Proveedores (vinculados a contactos DP11 a DP13 previamente creados en etapa-4.sql)
INSERT INTO proveedor (id_datos_personales, ruc, razon_social) VALUES
    (11, '20601234567', 'Aceros y Herramientas Silva SAC'),
    (12, '20456789123', 'Distribuidora Espinoza EIRL'),
    (13, '20387654321', 'Pinturas del Sur SAC');

-- Productos / Mercadería / Insumos / Servicios
INSERT INTO producto (codigo, nombre, descripcion, tipo, id_categoria, id_marca, id_unidad_medida, precio_compra, precio_venta, stock_actual, stock_minimo) VALUES
    ('HM-001', 'Martillo de uña 16 oz', 'Mango de fibra de vidrio', 'MERCADERIA', 1, 1, 1, 18.00, 29.90, 40, 10),
    ('HM-002', 'Destornillador Phillips #2', 'Punta magnética cromada', 'MERCADERIA', 1, 3, 1, 4.50, 9.90, 120, 30),
    ('HE-001', 'Taladro percutor 650 W', 'Velocidad variable reversible', 'MERCADERIA', 2, 2, 1, 165.00, 239.00, 12, 3),
    ('HE-002', 'Amoladora angular 4 1/2"', 'Motor de 850 W con guarda protectora', 'MERCADERIA', 2, 2, 1, 140.00, 205.00, 8, 3),
    ('PI-001', 'Pintura látex blanca', 'Balde lavable antihongos', 'MERCADERIA', 3, 5, 4, 38.00, 59.90, 30, 8),
    ('TO-001', 'Tornillo autorroscante 1" (x100)', 'Caja de 100 unidades fosfatizados', 'MERCADERIA', 4, 5, 5, 6.00, 11.50, 200, 50),
    ('MC-001', 'Cemento 42.5 kg', 'Bolsa tipo I alta resistencia', 'INSUMO', 5, 5, 1, 24.00, 31.50, 150, 40),
    ('GA-001', 'Tubo PVC 1/2" x 5 m', 'Para agua fría clase 10', 'MERCADERIA', 6, 4, 1, 7.50, 13.90, 60, 15),
    ('SV-001', 'Servicio de corte a medida', 'Corte preciso de perfiles o tableros', 'SERVICIO', 7, NULL, 6, 0.00, 15.00, 0, 0),
    ('SV-002', 'Servicio de delivery local', 'Transporte urbano en radio 10 km', 'SERVICIO', 7, NULL, 6, 0.00, 10.00, 0, 0);

-- Consultas de verificación
SELECT c.id_cliente, c.tipo_cliente, dp.nombres, dp.apellidos, c.ruc, c.razon_social, c.estado
FROM cliente c JOIN datos_personales dp ON c.id_datos_personales = dp.id_datos_personales;

SELECT pr.id_proveedor, pr.ruc, pr.razon_social, dp.nombres AS contacto_nombre, dp.apellidos AS contacto_apellido, pr.estado
FROM proveedor pr JOIN datos_personales dp ON pr.id_datos_personales = dp.id_datos_personales;

SELECT p.id_producto, p.codigo, p.nombre, p.tipo, cat.nombre_categoria, COALESCE(m.nombre_marca, 'N/A') AS marca, 
       um.abreviatura, p.precio_venta, p.stock_actual, p.estado
FROM producto p
JOIN categoria cat ON p.id_categoria = cat.id_categoria
LEFT JOIN marca m ON p.id_marca = m.id_marca
JOIN unidad_medida um ON p.id_unidad_medida = um.id_unidad_medida;
