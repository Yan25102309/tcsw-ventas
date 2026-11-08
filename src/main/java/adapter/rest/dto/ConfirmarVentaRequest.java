package adapter.rest.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ConfirmarVentaRequest {

    @NotEmpty(message = "La venta debe incluir al menos un detalle")
    @Valid
    private List<DetalleVentaRequest> detalles;

    public ConfirmarVentaRequest() {
    }

    public ConfirmarVentaRequest(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleVentaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequest> detalles) { this.detalles = detalles; }
}
