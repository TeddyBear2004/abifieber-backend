package de.thorge.abishop.routes;

import de.thorge.abishop.tables.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserRoutes {

    private final SessionFactory sessionFactory;

    public UserRoutes(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void getById(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            User user = entityManager.find(User.class, id);
            ctx.json(user);
        }
    }

    public void getByEmail(Context ctx) {
        String email = ctx.pathParam("email");
        try (var session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            List<User> users = query.list();
            if (users.isEmpty()) {
                ctx.status(404).result("User not found");
                return;
            }
            ctx.json(users.getFirst());
        }
    }

    public void getAll(Context ctx) {
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            List<User> users = entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
            ctx.json(users);
        }
    }

    public void create(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        sessionFactory.inSession(session -> {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", user.getEmail());
            List<User> users = query.list();
            if (!users.isEmpty()) {
                ctx.status(400).result("Email already exists");
                return;
            }
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            ctx.status(201);
        });
    }
}