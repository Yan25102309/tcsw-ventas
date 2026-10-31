-- DDL para el agregado de Venta y Detalle
CREATE TABLE IF NOT EXISTS venta (
    id BIGINT PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    total NUMERIC(12, 2) NOT NULL CHECK (total >= 0.00)
);

CREATE TABLE IF NOT EXISTS detalle_venta (
    id BIGINT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12, 2) NOT NULL CHECK (precio_unitario >= 0.00),
    subtotal NUMERIC(12, 2) NOT NULL CHECK (subtotal >= 0.00),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES venta (id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto (id)
);
