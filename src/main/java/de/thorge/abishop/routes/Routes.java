package de.thorge.abishop.routes;

import io.javalin.Javalin;
import org.hibernate.SessionFactory;

public class Routes {
    private final SessionFactory sessionFactory;

    public Routes(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void registerRoutes(Javalin javalin) {
        UserRoutes userRoutes = new UserRoutes(sessionFactory);
        OrderRoutes orderRoutes = new OrderRoutes(sessionFactory);
        ProductRoutes productRoutes = new ProductRoutes(sessionFactory);

        javalin.get("/user/email/{email}", userRoutes::getByEmail);
        javalin.get("/user/{id}", userRoutes::getById);
        javalin.get("/user", userRoutes::getAll);
        javalin.post("/user", userRoutes::create);

        javalin.get("/order/{id}", orderRoutes::getById);
        javalin.get("/order", orderRoutes::getAll);
        javalin.post("/order", orderRoutes::create);
        javalin.get("/order/{id}/position", orderRoutes::getPositions);
        javalin.post("/order/{id}/position", orderRoutes::addPosition);

        javalin.get("/product/{id}", productRoutes::getById);
        javalin.delete("/product/{id}", productRoutes::delete);
        javalin.get("/product", productRoutes::getAll);
        javalin.post("/product", productRoutes::create);
    }
}