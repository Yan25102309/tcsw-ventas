package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import domain.DescuentoPorVolumen;
import domain.PoliticaDescuento;
import domain.Producto;
import domain.Venta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.math.BigDecimal;

class ArchitectureTest {

    private final JavaClasses clases = new ClassFileImporter().importPackages("domain", "application", "adapter");

    @Test
    @DisplayName("Regla 1: El dominio debe estar totalmente aislado de adaptadores, Swing, AWT y SQL")
    void elDominioDebeEstarAislado() {
        ArchRule regla = noClasses()
                .that().resideInAPackage("..domain..")
                .and().haveNameNotMatching(".*Test")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..adapter..", "..application..", "javax.swing..", "java.awt..", "java.sql..");

        regla.check(clases);
    }

    @Test
    @DisplayName("Regla 2: La capa de aplicación no debe depender de adaptadores concretos de infraestructura")
    void laAplicacionNoDebeDependerDeAdaptadores() {
        ArchRule regla = noClasses()
                .that().resideInAPackage("..application..")
                .and().haveNameNotMatching(".*Test")
                .should().dependOnClassesThat()
                .resideInAPackage("..adapter..");

        regla.check(clases);
    }
    @Test
    @DisplayName("Descuento por volumen retorna cero si la venta es nula")
    void testDescuentoVolumenVentaNula() {
        PoliticaDescuento politica = new DescuentoPorVolumen();
        BigDecimal descuento = politica.calcularDescuento(null);
        assertEquals(new BigDecimal("0.00"), descuento);
    }

    @Test
    @DisplayName("Descuento por volumen retorna cero si el subtotal no alcanza el umbral")
    void testDescuentoVolumenNoAlcanzaUmbral() {
        PoliticaDescuento politica = new DescuentoPorVolumen();
        
       
        Venta venta = new Venta();
        Producto producto = new Producto(1L, "Camisa", new BigDecimal("100.00"), 3);
        venta.agregarDetalle(producto, 3); 

        BigDecimal descuento = politica.calcularDescuento(venta);
        assertEquals(new BigDecimal("0.00"), descuento);
    }
}
