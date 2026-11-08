package infrastructure;

import adapter.hibernate.ProductoHibernateRepository;
import adapter.hibernate.VentaHibernateRepository;
import application.ConfirmarVentaService;
import application.ProductoRepository;
import application.VentaRepository;
import application.port.in.RegistrarProductoUseCase;
import application.service.ProductoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"adapter.rest", "infrastructure"})
public class RestApp {

    public static void main(String[] args) {
        SpringApplication.run(RestApp.class, args);
    }

    @Bean
    public ProductoRepository productoRepository() {
        return new ProductoHibernateRepository();
    }

    @Bean
    public VentaRepository ventaRepository() {
        return new VentaHibernateRepository();
    }

    @Bean
    public RegistrarProductoUseCase registrarProductoUseCase(ProductoRepository productoRepository) {
        return new ProductoService(productoRepository);
    }

    @Bean
    public ConfirmarVentaService confirmarVentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        return new ConfirmarVentaService(ventaRepository, productoRepository);
    }
}
