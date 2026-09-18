package application.service;

import adapter.memory.InMemoryProductoRepository;
import domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService(new InMemoryProductoRepository());
    }

    @Test
    void testRegistrarProductoExitoso() {
        Producto creado = productoService.registrarProducto(1L, "Café Veracruz", new BigDecimal("120.50"), 10);
        assertNotNull(creado);
        assertEquals("Café Veracruz", creado.getNombre());
    }
}
