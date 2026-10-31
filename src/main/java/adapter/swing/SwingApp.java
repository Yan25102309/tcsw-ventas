package adapter.swing;

import adapter.hibernate.ProductoHibernateRepository;
import adapter.hibernate.VentaHibernateRepository;
import application.ConfirmarVentaService;
import application.ProductoRepository;
import application.VentaRepository;
import application.port.in.RegistrarProductoUseCase;
import application.service.ProductoService;
import domain.Venta;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SwingApp {

    private static final Logger LOGGER = Logger.getLogger(SwingApp.class.getName());

    private SwingApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApp::createAndShowGui);
    }

    public static void createAndShowGui() {
        configureLookAndFeel();

        ProductoRepository productoRepository = new ProductoHibernateRepository();
        VentaRepository ventaRepository = new VentaHibernateRepository();

        RegistrarProductoUseCase useCase = new ProductoService(productoRepository);
        ConfirmarVentaService ventaService = new ConfirmarVentaService(ventaRepository, productoRepository);

        JFrame frame = new JFrame("Sistema de Ventas tcsw-ventas (C02 - Swing + Hibernate)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(850, 550);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        RegistrarProductoView registroView = new RegistrarProductoView();
        new ProductoController(registroView, useCase);
        tabbedPane.addTab("Registro de Productos", registroView);

        VentaView ventaView = new VentaView();
        new VentaController(ventaView, new Venta(), ventaService);
        tabbedPane.addTab("Procesar Venta", ventaView);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "No se pudo configurar el Look and Feel del sistema.", exception);
        }
    }
}
