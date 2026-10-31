package application; 
import domain.Venta; 
import java.util.List; 
import java.util.Optional; 
public interface VentaRepository {
     Venta guardar(Venta venta); 
     Optional<Venta> buscarPorId(long id);
     List<Venta> obtenerTodos();
}