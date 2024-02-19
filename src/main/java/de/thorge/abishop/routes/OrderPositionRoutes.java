package de.thorge.abishop.routes;

import de.thorge.abishop.tables.OrderPosition;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OrderPositionRoutes {

    private final SessionFactory sessionFactory;

    public OrderPositionRoutes(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void getById(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try (Session session = sessionFactory.openSession()) {
            OrderPosition orderPosition = session.get(OrderPosition.class, id);
            ctx.json(orderPosition);
        }
    }

    public void getAll(Context ctx) {
        try (Session session = sessionFactory.openSession()) {
            List<OrderPosition> orderPositions = session.createQuery("from OrderPosition", OrderPosition.class).list();
            ctx.json(orderPositions);
        }
    }

    public void create(Context ctx) {
        OrderPosition orderPosition = ctx.bodyAsClass(OrderPosition.class);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(orderPosition);
            transaction.commit();
            ctx.status(201);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}