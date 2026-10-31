package adapter.hibernate;

import application.VentaRepository;
import domain.DetalleVenta;
import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaHibernateRepository implements VentaRepository {

    private final SessionFactory sessionFactory;

    public VentaHibernateRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    @Override
    public Venta guardar(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            // 1. Crear VentaEntity asignando un ID de transacción y usando tu venta.calcularTotal()
            long ventaId = System.currentTimeMillis();
            VentaEntity ventaEntity = new VentaEntity(ventaId, LocalDateTime.now(), venta.calcularTotal());

            // 2. Mapear cada DetalleVenta a DetalleVentaEntity
            long detalleCounter = 1;
            for (DetalleVenta detalle : venta.getDetalles()) {
                ProductoEntity productoEntity = session.get(ProductoEntity.class, detalle.getProducto().getId());
                if (productoEntity == null) {
                    throw new PersistenceException("El producto con ID " + detalle.getProducto().getId() + " no existe en la base de datos", null);
                }

                long detalleId = ventaId + (detalleCounter++);
                DetalleVentaEntity detalleEntity = new DetalleVentaEntity(
                        detalleId,
                        ventaEntity,
                        productoEntity,
                        detalle.getCantidad(),
                        detalle.getPrecioCapturado(),
                        detalle.getSubtotal()
                );
                ventaEntity.agregarDetalle(detalleEntity);
            }

            // 3. Persistir el agregado completo en PostgreSQL
            session.saveOrUpdate(ventaEntity);
            tx.commit();
            return venta;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new PersistenceException("Error al guardar la Venta en Hibernate", e);
        }
    }

    @Override
    public Optional<Venta> buscarPorId(long id) {
        try (Session session = sessionFactory.openSession()) {
            VentaEntity entity = session.get(VentaEntity.class, id);
            if (entity != null) {
                return Optional.of(mapearADominio(entity));
            }
        } catch (Exception e) {
            throw new PersistenceException("Error al buscar Venta por ID en Hibernate", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Venta> obtenerTodos() {
        try (Session session = sessionFactory.openSession()) {
            List<VentaEntity> entities = session.createQuery("FROM VentaEntity", VentaEntity.class).list();
            List<Venta> ventas = new ArrayList<>();
            for (VentaEntity entity : entities) {
                ventas.add(mapearADominio(entity));
            }
            return ventas;
        } catch (Exception e) {
            throw new PersistenceException("Error al consultar todas las ventas en Hibernate", e);
        }
    }

    private Venta mapearADominio(VentaEntity entity) {
        Venta venta = new Venta();
        for (DetalleVentaEntity d : entity.getDetalles()) {
            Producto producto = new Producto(
                    d.getProducto().getId(),
                    d.getProducto().getNombre(),
                    d.getProducto().getPrecio(),
                    d.getProducto().getExistencia()
            );
            // Llama a tu método exacto agregarDetalle(Producto, int) de Venta.java
            venta.agregarDetalle(producto, d.getCantidad());
        }
        return venta;
    }
}
