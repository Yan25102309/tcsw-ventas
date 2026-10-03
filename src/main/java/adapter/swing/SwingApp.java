package adapter.swing;

import adapter.memory.InMemoryProductoRepository;
import application.ProductoRepository;
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

/**
 * Punto de entrada principal para el Cliente Swing.
 * Actúa como la Raíz de Composición (Composition Root),
 * ejecutándose de forma segura en el EDT.
 */
public final class SwingApp {

    private static final Logger LOGGER =
            Logger.getLogger(SwingApp.class.getName());

    /**
     * Punto de entrada de la aplicación.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingApp::createAndShowGui);
    }

    /**
     * Construye y muestra la interfaz principal de la aplicación.
     */
    public static void createAndShowGui() {
        configureLookAndFeel();

        ProductoRepository repository = new InMemoryProductoRepository();
        RegistrarProductoUseCase useCase = new ProductoService(repository);

        JFrame frame = new JFrame(
                "Sistema de Ventas tcsw-ventas (P07 / M08)"
        );

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(850, 550);
        frame.setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        RegistrarProductoView registroView = new RegistrarProductoView();
        new ProductoController(registroView, useCase);
        tabbedPane.addTab("Registro de Productos", registroView);

        VentaView ventaView = new VentaView();
        new VentaController(ventaView, new Venta());
        tabbedPane.addTab("Procesar Venta", ventaView);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Configura el Look and Feel del sistema operativo.
     */
    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception exception) {
            LOGGER.log(
                    Level.WARNING,
                    "No se pudo configurar el Look and Feel del sistema.",
                    exception
            );
        }
    }

    private SwingApp() {
        // Evita la instanciación de esta clase.
    }
}