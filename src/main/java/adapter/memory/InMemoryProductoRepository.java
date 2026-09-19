package adapter.memory; 

import application.ProductoRepository; 
import domain.Producto; 

import java.util.ArrayList; 
import java.util.List; 
import java.util.Map; 
import java.util.Objects; 
import java.util.Optional; 
import java.util.concurrent.ConcurrentHashMap;

/** 
 * Adaptador de infraestructura en memoria que implementa el puerto de salida ProductoRepository. 
 */

public final class InMemoryProductoRepository implements ProductoRepository {

    private final Map<Long, Producto> tabla = new ConcurrentHashMap<>();

    @Override
    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto o su ID no pueden ser nulos");
        }
        tabla.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public Optional<Producto> buscarPorId(long id) {
        return Optional.ofNullable(tabla.get(id));
    }

    @Override
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(tabla.values());
    }

}