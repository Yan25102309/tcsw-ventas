package adapter.hibernate;

import application.ProductoRepository;
import domain.Producto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoHibernateRepository implements ProductoRepository {

    private final SessionFactory sessionFactory;

    public ProductoHibernateRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            ProductoEntity entity = new ProductoEntity(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getExistencia()
            );
            session.saveOrUpdate(entity);
            tx.commit();
            return producto;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new IllegalStateException("Error en transaccion Hibernate al guardar Producto", e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(long id) {
        try (Session session = sessionFactory.openSession()) {
            ProductoEntity entity = session.get(ProductoEntity.class, id);
            if (entity != null) {
                Producto producto = new Producto(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getPrecio(),
                        entity.getExistencia()
                );
                return Optional.of(producto);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error en consulta Hibernate al buscar Producto por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> obtenerTodos() {
        try (Session session = sessionFactory.openSession()) {
            List<ProductoEntity> entities = session.createQuery("FROM ProductoEntity", ProductoEntity.class).list();
            List<Producto> productos = new ArrayList<>();
            for (ProductoEntity entity : entities) {
                productos.add(new Producto(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getPrecio(),
                        entity.getExistencia()
                ));
            }
            return productos;
        } catch (Exception e) {
            throw new IllegalStateException("Error en consulta Hibernate al obtener todos los productos", e);
        }
    }
}
