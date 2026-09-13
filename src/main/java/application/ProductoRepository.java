package application;

import domain.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de la capa de aplicación para abstraer la gestión y acceso
 * a los datos de la entidad Producto.
 */
public interface ProductoRepository {

    /**
     * Guarda o actualiza un producto en el sistema de almacenamiento.
     *
     * @param producto Entidad Producto a almacenar.
     * @return Producto guardado.
     */
    Producto guardar(Producto producto);

    /**
     * Busca un producto por su identificador único.
     *
     * @param id Identificador único del producto.
     * @return Optional con el producto si fue encontrado, u Optional.empty() si no existe.
     */
    Optional<Producto> buscarPorId(long id);

    /**
     * Recupera la lista completa de productos almacenados.
     *
     * @return Lista de productos registrados.
     */
    List<Producto> obtenerTodos();
}
