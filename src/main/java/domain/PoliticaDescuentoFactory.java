package domain;

/**
 * Encapsula la logica de creacion e instanciacias de estrategias de descuento
 */
public final class PoliticaDescuentoFactory {

    private PoliticaDescuentoFactory() {
    }

    public static PoliticaDescuento crearPolitica(String tipo) {
        if (tipo == null) {
            return new SinDescuento();
        }
        switch (tipo.toUpperCase().trim()) {
            case "VOLUMEN":
                return new DescuentoPorVolumen();
            case "CLIENTE_FRECUENTE":
                return new DescuentoClienteFrecuente();
            default:
                return new SinDescuento();
        }
    }
}
