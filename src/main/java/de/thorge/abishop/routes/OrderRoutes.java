package de.thorge.abishop.routes;

import de.thorge.abishop.tables.Order;
import de.thorge.abishop.tables.OrderPosition;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OrderRoutes {

    private final SessionFactory sessionFactory;

    public OrderRoutes(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void getById(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try (Session session = sessionFactory.openSession()) {
            Order order = session.get(Order.class, id);
            ctx.json(order);
        }
    }

    public void getAll(Context ctx) {
        try (Session session = sessionFactory.openSession()) {
            List<Order> orders = session.createQuery("from Order", Order.class).list();
            ctx.json(orders);
        }
    }

    public void create(Context ctx) {
        Order order = ctx.bodyAsClass(Order.class);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(order);
            transaction.commit();
            ctx.status(201).json(order);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void addPosition(Context ctx) {
        Long orderId = Long.valueOf(ctx.pathParam("id"));

        sessionFactory.inSession(session -> {
            Order order = session.get(Order.class, orderId);
            if (order == null) {
                ctx.status(404).result("Order not found");
                return;
            }

            OrderPosition position = ctx.bodyAsClass(OrderPosition.class);
            position.setOrder(order);
            order.getOrderPositions().add(position);
            Transaction transaction = session.beginTransaction();
            session.persist(position);
            transaction.commit();
            ctx.status(201);
        });
    }

    public void getPositions(Context context) {
        Long orderId = Long.valueOf(context.pathParam("id"));
        sessionFactory.inSession(session -> {
            Order order = session.get(Order.class, orderId);
            if (order == null) {
                context.status(404).result("Order not found");
                return;
            }
            List<OrderPosition> positions = order.getOrderPositions();
            context.json(positions);
        });
    }
}