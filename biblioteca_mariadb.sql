-- =====================================================================
-- POO-2026-Biblioteca -> MariaDB
-- Uso:  sudo mysql < biblioteca_mariadb.sql   (borra y recrea la base POO_2026_Biblioteca)
-- =====================================================================
DROP DATABASE IF EXISTS POO_2026_Biblioteca;
CREATE DATABASE POO_2026_Biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE POO_2026_Biblioteca;

-- ---------------------------------------------------------------------
-- BIBLIOTECA (contenedor central)
-- ---------------------------------------------------------------------
CREATE TABLE biblioteca (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    CONSTRAINT ck_biblioteca_nombre CHECK (TRIM(nombre) <> '')
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- HERENCIA: Persona (abstracta) -> Autor, Socio
-- Estrategia "tabla por subclase": persona guarda lo común y cada
-- subclase guarda sus atributos, compartiendo la misma PK.
-- ---------------------------------------------------------------------
CREATE TABLE persona (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    tipo    ENUM('AUTOR','SOCIO') NOT NULL,
    CONSTRAINT ck_persona_nombre CHECK (TRIM(nombre) <> '')
) ENGINE=InnoDB;

CREATE TABLE autor (
    persona_id       INT PRIMARY KEY,
    nacionalidad     VARCHAR(60) NOT NULL,
    anio_nacimiento  SMALLINT NOT NULL,
    CONSTRAINT fk_autor_persona FOREIGN KEY (persona_id)
        REFERENCES persona(id) ON DELETE CASCADE,
    CONSTRAINT ck_autor_anio CHECK (anio_nacimiento >= 1)
) ENGINE=InnoDB;

CREATE TABLE socio (
    persona_id     INT PRIMARY KEY,
    numero_socio   INT NOT NULL UNIQUE,            -- final en Java
    email          VARCHAR(120) NOT NULL UNIQUE,
    biblioteca_id  INT NOT NULL,                   -- agregación: socio registrado en una biblioteca
    CONSTRAINT fk_socio_persona FOREIGN KEY (persona_id)
        REFERENCES persona(id) ON DELETE CASCADE,
    CONSTRAINT fk_socio_biblioteca FOREIGN KEY (biblioteca_id)
        REFERENCES biblioteca(id),
    CONSTRAINT ck_socio_numero CHECK (numero_socio > 0),
    CONSTRAINT ck_socio_email  CHECK (email LIKE '%@%')
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- COMPOSICIÓN Socio -> Credencial (1 a 1; si se borra el socio, se borra)
-- ---------------------------------------------------------------------
CREATE TABLE credencial (
    socio_id       INT PRIMARY KEY,
    numero         INT NOT NULL UNIQUE,            -- = numero_socio
    fecha_emision  DATE NOT NULL DEFAULT (CURRENT_DATE),
    vigente        BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_credencial_socio FOREIGN KEY (socio_id)
        REFERENCES socio(persona_id) ON DELETE CASCADE,
    CONSTRAINT ck_credencial_numero CHECK (numero > 0)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- LIBRO (asociación N:1 con Autor; agregación con Biblioteca)
-- ---------------------------------------------------------------------
CREATE TABLE libro (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    isbn              VARCHAR(20) NOT NULL UNIQUE, -- final en Java
    titulo            VARCHAR(200) NOT NULL,
    anio_publicacion  SMALLINT NOT NULL,
    disponible        BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id          INT NOT NULL,
    biblioteca_id     INT NOT NULL,
    CONSTRAINT fk_libro_autor FOREIGN KEY (autor_id)
        REFERENCES autor(persona_id),
    CONSTRAINT fk_libro_biblioteca FOREIGN KEY (biblioteca_id)
        REFERENCES biblioteca(id),
    CONSTRAINT ck_libro_titulo CHECK (TRIM(titulo) <> ''),
    CONSTRAINT ck_libro_anio   CHECK (anio_publicacion >= 1)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- PRESTAMO (asocia Libro y Socio; composición con Biblioteca)
-- Regla: plazo de 14 días (CalculadoraMulta.PLAZO_DIAS)
-- ---------------------------------------------------------------------
CREATE TABLE prestamo (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    biblioteca_id     INT NOT NULL,
    libro_id          INT NOT NULL,
    socio_id          INT NOT NULL,
    fecha_prestamo    DATE NOT NULL,
    fecha_limite      DATE AS (DATE_ADD(fecha_prestamo, INTERVAL 14 DAY)) PERSISTENT,
    fecha_devolucion  DATE NULL,
    activo            BOOLEAN NOT NULL DEFAULT TRUE,
    devuelto          BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_prestamo_biblioteca FOREIGN KEY (biblioteca_id)
        REFERENCES biblioteca(id) ON DELETE CASCADE,
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id)
        REFERENCES libro(id),
    CONSTRAINT fk_prestamo_socio FOREIGN KEY (socio_id)
        REFERENCES socio(persona_id),
    CONSTRAINT ck_prestamo_estado CHECK (
        (activo = TRUE  AND devuelto = FALSE AND fecha_devolucion IS NULL) OR
        (activo = FALSE AND devuelto = TRUE  AND fecha_devolucion IS NOT NULL)),
    CONSTRAINT ck_prestamo_fechas CHECK (
        fecha_devolucion IS NULL OR fecha_devolucion >= fecha_prestamo)
) ENGINE=InnoDB;

CREATE INDEX ix_prestamo_libro ON prestamo(libro_id, activo);
CREATE INDEX ix_prestamo_socio ON prestamo(socio_id, activo);

-- ---------------------------------------------------------------------
-- TRIGGERS: reglas de negocio de Libro y Socio
--  - un libro prestado deja de estar disponible (y vuelve al devolverse)
--  - un socio no puede tener más de 3 préstamos activos (MAX_LIBROS)
-- ---------------------------------------------------------------------
DELIMITER //

CREATE TRIGGER trg_prestamo_bi BEFORE INSERT ON prestamo
FOR EACH ROW
BEGIN
    IF NEW.activo = TRUE THEN
        IF (SELECT disponible FROM libro WHERE id = NEW.libro_id) = FALSE THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El libro no está disponible';
        END IF;
        IF (SELECT COUNT(*) FROM prestamo
             WHERE socio_id = NEW.socio_id AND activo = TRUE) >= 3 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El socio alcanzó el máximo de 3 préstamos';
        END IF;
    END IF;
END//

CREATE TRIGGER trg_prestamo_ai AFTER INSERT ON prestamo
FOR EACH ROW
BEGIN
    IF NEW.activo = TRUE THEN
        UPDATE libro SET disponible = FALSE WHERE id = NEW.libro_id;
    END IF;
END//

CREATE TRIGGER trg_prestamo_au AFTER UPDATE ON prestamo
FOR EACH ROW
BEGIN
    IF OLD.activo = TRUE AND NEW.activo = FALSE THEN
        UPDATE libro SET disponible = TRUE WHERE id = NEW.libro_id;
    END IF;
END//

DELIMITER ;

-- ---------------------------------------------------------------------
-- VISTAS: valores derivados (en Java son atributos o métodos)
-- ---------------------------------------------------------------------
-- Socio con nombre y libros prestados actuales
CREATE VIEW v_socios AS
SELECT s.persona_id, s.numero_socio, p.nombre, s.email,
       (SELECT COUNT(*) FROM prestamo pr
         WHERE pr.socio_id = s.persona_id AND pr.activo = TRUE) AS libros_prestados_actuales,
       c.vigente AS credencial_vigente
FROM socio s
JOIN persona p    ON p.id = s.persona_id
JOIN credencial c ON c.socio_id = s.persona_id;

-- Libros con su autor
CREATE VIEW v_catalogo AS
SELECT l.id, l.isbn, l.titulo, l.anio_publicacion, pa.nombre AS autor,
       a.nacionalidad, l.disponible
FROM libro l
JOIN autor a    ON a.persona_id = l.autor_id
JOIN persona pa ON pa.id = a.persona_id;

-- Préstamos con multa ($500 por día de atraso, CalculadoraMulta)
-- Si sigue activo, se calcula contra la fecha de hoy.
CREATE VIEW v_prestamos AS
SELECT pr.id, l.titulo, ps.nombre AS socio, pr.fecha_prestamo, pr.fecha_limite,
       pr.fecha_devolucion,
       IF(pr.activo, 'ACTIVO', 'DEVUELTO') AS estado,
       GREATEST(DATEDIFF(COALESCE(pr.fecha_devolucion, CURRENT_DATE), pr.fecha_limite), 0) AS dias_atraso,
       GREATEST(DATEDIFF(COALESCE(pr.fecha_devolucion, CURRENT_DATE), pr.fecha_limite), 0) * 500 AS multa
FROM prestamo pr
JOIN libro l     ON l.id = pr.libro_id
JOIN persona ps  ON ps.id = pr.socio_id;

-- =====================================================================
-- DATOS DE MUESTRA
-- =====================================================================
INSERT INTO biblioteca (nombre) VALUES ('Biblioteca UMC');

-- Autores (persona + autor)
INSERT INTO persona (id, nombre, tipo) VALUES
 (1, 'Gabriel García Márquez', 'AUTOR'),
 (2, 'Isabel Allende',         'AUTOR'),
 (3, 'Gabriela Mistral',       'AUTOR'),
 (4, 'Pablo Neruda',           'AUTOR'),
 (5, 'Jorge Luis Borges',      'AUTOR'),
 (6, 'Roberto Bolaño',         'AUTOR');

INSERT INTO autor (persona_id, nacionalidad, anio_nacimiento) VALUES
 (1, 'Colombiana', 1927),
 (2, 'Chilena',    1942),
 (3, 'Chilena',    1889),
 (4, 'Chilena',    1904),
 (5, 'Argentina',  1899),
 (6, 'Chilena',    1953);

-- Socios (persona + socio + credencial)
INSERT INTO persona (id, nombre, tipo) VALUES
 (101, 'Felipe Pino',    'SOCIO'),
 (102, 'Matías Araya',   'SOCIO'),
 (103, 'Cristian Díaz',  'SOCIO'),
 (104, 'Camila Rojas',   'SOCIO');

INSERT INTO socio (persona_id, numero_socio, email, biblioteca_id) VALUES
 (101, 1, 'felipe.pino@umc.cl',   1),
 (102, 2, 'matias.araya@umc.cl',  1),
 (103, 3, 'cristian.diaz@umc.cl', 1),
 (104, 4, 'camila.rojas@umc.cl',  1);

INSERT INTO credencial (socio_id, numero, fecha_emision, vigente) VALUES
 (101, 1, '2026-03-02', TRUE),
 (102, 2, '2026-03-02', TRUE),
 (103, 3, '2026-03-05', TRUE),
 (104, 4, '2026-04-10', FALSE);   -- credencial revocada (revocar())

-- Libros (ISBN de ejemplo)
INSERT INTO libro (isbn, titulo, anio_publicacion, autor_id, biblioteca_id) VALUES
 ('978-0-00-000001-1', 'Cien años de soledad',     1967, 1, 1),
 ('978-0-00-000002-2', 'La casa de los espíritus', 1982, 2, 1),
 ('978-0-00-000003-3', 'Desolación',               1922, 3, 1),
 ('978-0-00-000004-4', 'Veinte poemas de amor y una canción desesperada', 1924, 4, 1),
 ('978-0-00-000005-5', 'Ficciones',                1944, 5, 1),
 ('978-0-00-000006-6', 'Los detectives salvajes',  1998, 6, 1),
 ('978-0-00-000007-7', 'El amor en los tiempos del cólera', 1985, 1, 1);

-- Préstamos (los triggers actualizan libro.disponible)
-- 1) Devuelto con atraso: límite 2026-09-15, devuelto 2026-10-10 -> 25 días -> $12.500
INSERT INTO prestamo (biblioteca_id, libro_id, socio_id, fecha_prestamo, fecha_devolucion, activo, devuelto)
VALUES (1, 1, 101, '2026-09-01', '2026-10-10', FALSE, TRUE);

-- 2) Devuelto a tiempo (sin multa)
INSERT INTO prestamo (biblioteca_id, libro_id, socio_id, fecha_prestamo, fecha_devolucion, activo, devuelto)
VALUES (1, 3, 102, '2026-10-01', '2026-10-12', FALSE, TRUE);

-- 3) Activos (libros quedan no disponibles)
INSERT INTO prestamo (biblioteca_id, libro_id, socio_id, fecha_prestamo) VALUES
 (1, 2, 101, '2026-11-10'),
 (1, 5, 101, '2026-11-15'),
 (1, 6, 103, '2026-10-20');   -- vencido: límite 2026-11-03

-- =====================================================================
-- CONSULTAS DE PRUEBA
-- =====================================================================
-- SELECT * FROM v_catalogo;
-- SELECT * FROM v_socios;
-- SELECT * FROM v_prestamos;
-- SELECT * FROM v_prestamos WHERE multa > 0;
-- SELECT * FROM v_catalogo WHERE disponible = TRUE;
