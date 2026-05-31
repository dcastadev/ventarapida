-- ============================================================
-- VentaRapidaDB — Script completo SQL Server
-- Ejecutar en SQL Server Management Studio
-- ============================================================

-- 1. Crear base de datos
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'VentaRapidaDB')
BEGIN
    CREATE DATABASE VentaRapidaDB;
END
GO

USE VentaRapidaDB;
GO

-- ============================================================
-- 2. TABLA ROLES
-- ============================================================
IF OBJECT_ID('ROLES','U') IS NULL
CREATE TABLE ROLES (
    id_rol        CHAR(8)      NOT NULL,
    nombre_rol    VARCHAR(100) NOT NULL,
    descripcion   VARCHAR(200) NOT NULL,
    estado        CHAR(1)      NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)  NOT NULL,
    create_date   DATETIME     NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)  NULL,
    updated_date  DATETIME     NULL,
    deleted_date  DATETIME     NULL,
    deleted_user  VARCHAR(50)  NULL,
    CONSTRAINT PK_ROLES PRIMARY KEY (id_rol)
);
GO

-- ============================================================
-- 3. TABLA PERSONAS
-- ============================================================
IF OBJECT_ID('PERSONAS','U') IS NULL
CREATE TABLE PERSONAS (
    id_persona    CHAR(8)      NOT NULL,
    apellidos     VARCHAR(200) NOT NULL,
    nombres       VARCHAR(200) NOT NULL,
    dni           CHAR(8)      NOT NULL,
    sexo          CHAR(1)      NOT NULL,
    fecha_nac     DATE         NOT NULL,
    celular       CHAR(9)      NOT NULL,
    email         VARCHAR(100) NOT NULL,
    estado        CHAR(1)      NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)  NOT NULL,
    create_date   DATETIME     NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)  NULL,
    updated_date  DATETIME     NULL,
    deleted_date  DATETIME     NULL,
    deleted_user  VARCHAR(50)  NULL,
    CONSTRAINT PK_PERSONAS PRIMARY KEY (id_persona)
);
GO

-- ============================================================
-- 4. TABLA USUARIOS
-- ============================================================
IF OBJECT_ID('USUARIOS','U') IS NULL
CREATE TABLE USUARIOS (
    id_user       CHAR(8)      NOT NULL,
    id_usPersona  CHAR(8)      NOT NULL,
    id_usRol      CHAR(8)      NOT NULL,
    username      VARCHAR(100) NOT NULL,
    password_hash VARCHAR(300) NOT NULL,
    estado        CHAR(1)      NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)  NOT NULL,
    create_date   DATETIME     NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)  NULL,
    updated_date  DATETIME     NULL,
    deleted_date  DATETIME     NULL,
    deleted_user  VARCHAR(50)  NULL,
    CONSTRAINT PK_USUARIOS       PRIMARY KEY (id_user),
    CONSTRAINT UQ_USUARIOS_USER  UNIQUE (username),
    CONSTRAINT FK_US_PERSONA     FOREIGN KEY (id_usPersona) REFERENCES PERSONAS(id_persona),
    CONSTRAINT FK_US_ROL         FOREIGN KEY (id_usRol)     REFERENCES ROLES(id_rol)
);
GO

-- ============================================================
-- 5. TABLA CATEGORIAS
-- ============================================================
IF OBJECT_ID('CATEGORIAS','U') IS NULL
CREATE TABLE CATEGORIAS (
    id_categoria  CHAR(8)      NOT NULL,
    nombre        VARCHAR(100) NOT NULL,
    descripcion   VARCHAR(200) NULL,
    estado        CHAR(1)      NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)  NOT NULL,
    create_date   DATETIME     NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)  NULL,
    updated_date  DATETIME     NULL,
    deleted_date  DATETIME     NULL,
    deleted_user  VARCHAR(50)  NULL,
    CONSTRAINT PK_CATEGORIAS PRIMARY KEY (id_categoria)
);
GO

-- ============================================================
-- 6. TABLA PRODUCTOS
-- ============================================================
IF OBJECT_ID('PRODUCTOS','U') IS NULL
CREATE TABLE PRODUCTOS (
    id_producto   CHAR(8)        NOT NULL,
    id_categoria  CHAR(8)        NOT NULL,
    nombre        VARCHAR(200)   NOT NULL,
    descripcion   VARCHAR(500)   NULL,
    marca         VARCHAR(100)   NOT NULL,
    modelo        VARCHAR(100)   NOT NULL,
    precio_venta  DECIMAL(10,2)  NOT NULL,
    stock         INT            NOT NULL DEFAULT 0,
    stock_minimo  INT            NOT NULL DEFAULT 5,
    imagen_url    VARCHAR(300)   NULL,
    estado        CHAR(1)        NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)    NOT NULL,
    create_date   DATETIME       NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)    NULL,
    updated_date  DATETIME       NULL,
    deleted_date  DATETIME       NULL,
    deleted_user  VARCHAR(50)    NULL,
    CONSTRAINT PK_PRODUCTOS    PRIMARY KEY (id_producto),
    CONSTRAINT FK_PROD_CATEG   FOREIGN KEY (id_categoria) REFERENCES CATEGORIAS(id_categoria)
);
GO

-- ============================================================
-- 7. TABLA CLIENTES
-- ============================================================
IF OBJECT_ID('CLIENTES','U') IS NULL
CREATE TABLE CLIENTES (
    id_cliente    CHAR(8)      NOT NULL,
    apellidos     VARCHAR(200) NOT NULL,
    nombres       VARCHAR(200) NOT NULL,
    tipo_doc      CHAR(3)      NOT NULL,
    nro_doc       VARCHAR(12)  NOT NULL,
    email         VARCHAR(100) NULL,
    celular       CHAR(9)      NULL,
    direccion     VARCHAR(300) NULL,
    estado        CHAR(1)      NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)  NOT NULL,
    create_date   DATETIME     NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)  NULL,
    updated_date  DATETIME     NULL,
    deleted_date  DATETIME     NULL,
    deleted_user  VARCHAR(50)  NULL,
    CONSTRAINT PK_CLIENTES     PRIMARY KEY (id_cliente),
    CONSTRAINT UQ_CLI_DOC      UNIQUE (nro_doc)
);
GO

-- ============================================================
-- 8. TABLA VENTAS
-- ============================================================
IF OBJECT_ID('VENTAS','U') IS NULL
CREATE TABLE VENTAS (
    id_venta      CHAR(8)        NOT NULL,
    id_cliente    CHAR(8)        NOT NULL,
    id_user       CHAR(8)        NOT NULL,
    fecha_venta   DATETIME       NOT NULL DEFAULT GETDATE(),
    tipo_compro   VARCHAR(20)    NOT NULL,
    serie         VARCHAR(10)    NOT NULL,
    correlativo   INT            NOT NULL,
    subtotal      DECIMAL(10,2)  NOT NULL DEFAULT 0,
    igv           DECIMAL(10,2)  NOT NULL DEFAULT 0,
    total         DECIMAL(10,2)  NOT NULL DEFAULT 0,
    observacion   VARCHAR(300)   NULL,
    estado        CHAR(1)        NOT NULL DEFAULT '1',
    create_user   VARCHAR(50)    NOT NULL,
    create_date   DATETIME       NOT NULL DEFAULT GETDATE(),
    updated_user  VARCHAR(50)    NULL,
    updated_date  DATETIME       NULL,
    deleted_date  DATETIME       NULL,
    deleted_user  VARCHAR(50)    NULL,
    CONSTRAINT PK_VENTAS        PRIMARY KEY (id_venta),
    CONSTRAINT FK_VTA_CLIENTE   FOREIGN KEY (id_cliente) REFERENCES CLIENTES(id_cliente),
    CONSTRAINT FK_VTA_USUARIO   FOREIGN KEY (id_user)    REFERENCES USUARIOS(id_user)
);
GO

-- ============================================================
-- 9. TABLA DETALLE_VENTA
-- ============================================================
IF OBJECT_ID('DETALLE_VENTA','U') IS NULL
CREATE TABLE DETALLE_VENTA (
    id_detalle    CHAR(8)        NOT NULL,
    id_venta      CHAR(8)        NOT NULL,
    id_producto   CHAR(8)        NOT NULL,
    cantidad      INT            NOT NULL,
    precio_unit   DECIMAL(10,2)  NOT NULL,
    descuento     DECIMAL(10,2)  NOT NULL DEFAULT 0,
    subtotal      DECIMAL(10,2)  NOT NULL,
    estado        CHAR(1)        NOT NULL DEFAULT '1',
    CONSTRAINT PK_DETALLE_VENTA   PRIMARY KEY (id_detalle),
    CONSTRAINT FK_DET_VENTA       FOREIGN KEY (id_venta)    REFERENCES VENTAS(id_venta),
    CONSTRAINT FK_DET_PRODUCTO    FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto)
);
GO

-- ============================================================
-- 10. DATOS DE PRUEBA
-- ============================================================

-- Roles
INSERT INTO ROLES VALUES ('ROL00001','ADMIN',   'Administrador del sistema','1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);
INSERT INTO ROLES VALUES ('ROL00002','VENDEDOR','Vendedor de tienda',       '1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- Categorías
INSERT INTO CATEGORIAS VALUES ('CAT00001','Laptops',                 'Computadoras portátiles',          '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
INSERT INTO CATEGORIAS VALUES ('CAT00002','Computadoras de escritorio','PC Desktop y All-in-One',        '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
INSERT INTO CATEGORIAS VALUES ('CAT00003','Componentes',             'CPU, RAM, SSD, GPU',               '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
INSERT INTO CATEGORIAS VALUES ('CAT00004','Monitores',               'Monitores LED y curvo',            '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
INSERT INTO CATEGORIAS VALUES ('CAT00005','Periféricos',             'Teclados, mouse, auriculares',     '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- Persona administrador
INSERT INTO PERSONAS VALUES
('PER00001','Administrador','Sistema','00000000','M','1990-01-01','999999999',
 'admin@ventarapida.pe','1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);

-- Persona vendedor
INSERT INTO PERSONAS VALUES
('PER00002','Ramirez Torres','Luis Alberto','45678901','M','1998-05-15','987654321',
 'luis.ramirez@ventarapida.pe','1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- ⚠️ IMPORTANTE: Estos son los hashes BCrypt de:
--   admin   → password: admin123
--   vendedor → password: vendedor123
-- Si cambias las contraseñas, genera nuevos hashes BCrypt con fortaleza 12.
INSERT INTO USUARIOS VALUES
('USR00001','PER00001','ROL00001','admin',
 '$2a$12$GkBFLRtf7eE1eB/MHqTR0OhWe5VTn8B7bx3PuTqJLN4D1EaBP9d2y',
 '1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO USUARIOS VALUES
('USR00002','PER00002','ROL00002','vendedor',
 '$2a$12$WxKA2mHfE3nQ8rL9pXvZ4.c1JR5TuMb6GyDsOhP0kN7Ea4VqFt8Ia',
 '1','SYSTEM',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- Productos de prueba
INSERT INTO PRODUCTOS VALUES
('PRD00001','CAT00001','Laptop HP Pavilion 15',
 'Intel Core i5-12va gen, 8GB RAM, 512GB SSD','HP','Pavilion 15-eh3027la',
 2899.00,15,3,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00002','CAT00001','Laptop ASUS VivoBook 15',
 'Ryzen 5 5600H, 8GB RAM, 256GB SSD','ASUS','VivoBook 15 M515',
 2499.00,10,3,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00003','CAT00002','PC Escritorio Core i7',
 'Intel Core i7-12va, 16GB RAM, 1TB HDD + 256GB SSD','Generic','Custom i7',
 3200.00,8,2,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00004','CAT00003','RAM DDR4 8GB 3200MHz',
 'Memoria RAM DDR4 8GB 3200MHz','Kingston','KVR32N22S8/8',
 159.00,30,10,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00005','CAT00004','Monitor LG 24 Full HD',
 '24 pulgadas, IPS, 75Hz, HDMI+VGA','LG','24MK430H-B',
 649.00,12,3,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00006','CAT00005','Mouse Inalámbrico Logitech',
 'Óptico, 1000DPI, receptor USB nano','Logitech','M185',
 59.00,50,10,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO PRODUCTOS VALUES
('PRD00007','CAT00005','Teclado Mecánico Redragon',
 'Switch azul, retroiluminado RGB, USB','Redragon','K552',
 189.00,20,5,NULL,'1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- Cliente de prueba
INSERT INTO CLIENTES VALUES
('CLI00001','Gonzales Ruiz','Maria Elena','DNI','12345678',
 'maria.gonzales@gmail.com','987123456','Av. La Marina 1250, San Miguel',
 '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);

INSERT INTO CLIENTES VALUES
('CLI00002','Empresa Tech SAC','Empresa Tech SAC','RUC','20601234567',
 'compras@empresatech.pe','014561234','Jr. Paruro 890, Lima Centro',
 '1','ADMIN',GETDATE(),NULL,NULL,NULL,NULL);
GO

-- ============================================================
-- 11. VERIFICAR TABLAS CREADAS
-- ============================================================
SELECT
    t.name AS tabla,
    p.rows AS registros
FROM sys.tables t
JOIN sys.partitions p ON t.object_id = p.object_id
WHERE p.index_id IN (0,1)
ORDER BY t.name;
GO

PRINT '✅ VentaRapidaDB creada correctamente con todos los datos de prueba';
GO
