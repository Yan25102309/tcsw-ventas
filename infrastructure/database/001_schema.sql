-- DDL de la tabla producto con restricciones relacionales y de consistencia
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT PRIMARY KEY,
    nombre TEXT NOT NULL CHECK (length(trim(nombre)) > 0),
    precio NUMERIC(12,2) NOT NULL CHECK (precio >= 0.00),
    existencia INTEGER NOT NULL CHECK (existencia >= 0)
);

-- Fixtures de datos sintéticos iniciales
INSERT INTO producto (id, nombre, precio, existencia)
VALUES (1, 'Teclado Mecanico', 750.00, 15)
ON CONFLICT (id) DO NOTHING;

INSERT INTO producto (id, nombre, precio, existencia)
VALUES (2, 'Mouse Optico', 350.50, 20)
ON CONFLICT (id) DO NOTHING;
