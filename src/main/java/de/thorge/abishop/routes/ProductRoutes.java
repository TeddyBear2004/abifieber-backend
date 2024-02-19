package de.thorge.abishop.routes;

import de.thorge.abishop.tables.OrderPosition;
import de.thorge.abishop.tables.Product;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProductRoutes {

    private final SessionFactory sessionFactory;

    public ProductRoutes(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void getById(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try (Session session = sessionFactory.openSession()) {
            Product product = session.get(Product.class, id);
            ctx.json(product);
        }
    }

    public void getAll(Context ctx) {
        try (Session session = sessionFactory.openSession()) {
            List<Product> products = session.createQuery("from Product", Product.class).list();
            ctx.json(products);
        }
    }

    public void create(Context ctx) {
        Product product = ctx.bodyAsClass(Product.class);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
            ctx.status(201);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void delete(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                session.remove(product);
                session.getTransaction().commit();
                ctx.status(204);
            } else {
                ctx.status(404);
            }
        }
    }
}