package integration;

import adapter.memory.InMemoryProductoRepository;
import application.ProductoRepository;
import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import application.service.ProductoService;
import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class Corte1IntegracionTest {

    private ProductoRepository productoRepository;
    private RegistrarProductoUseCase registrarProductoUseCase;

    @BeforeEach
    void setUp() {
        // Inyección explícita por constructor (Puerto / Adaptador)
        this.productoRepository = new InMemoryProductoRepository();
        this.registrarProductoUseCase = new ProductoService(productoRepository);
    }

    @Test
    @DisplayName("Flujo E2E Corte 1: Registrar producto mediante Caso de Uso, agregar a Venta y aplicar Strategy/Factory")
    void testFlujoCompletoCorte1() {
        // 1. Ejecutar Caso de Uso (Puerto de Entrada + Servicio)
        RegistrarProductoCommand command = new RegistrarProductoCommand(
            100L, "Monitor Gamer 27", new BigDecimal("6000.00"), 10
        );
        Producto producto = registrarProductoUseCase.registrarProducto(command);
        assertNotNull(producto);

        // 2. Procesar Dominio (Venta + DetalleVenta)
        Venta venta = new Venta();
        venta.agregarDetalle(producto, 1); // Subtotal = $6000.00

        // 3. Crear Estrategia con Factory Method
        PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica("CLIENTE_FRECUENTE");
        assertTrue(politica instanceof DescuentoClienteFrecuente);

        // 4. Calcular Total con Strategy (15% de $6000.00 = $900.00 de descuento -> Total = $5100.00)
        BigDecimal totalCalculado = venta.calcularTotal(politica);
        assertEquals(new BigDecimal("5100.00"), totalCalculado);

        // 5. Verificar Persistencia en Adaptador de Salida
        assertTrue(productoRepository.buscarPorId(100L).isPresent());
    }
}
