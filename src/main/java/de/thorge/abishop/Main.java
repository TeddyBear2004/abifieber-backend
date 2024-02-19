package de.thorge.abishop;

import de.thorge.abishop.routes.Routes;
import de.thorge.util.SessionFactoryHandler;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(7000);

        SessionFactoryHandler sessionFactoryHandler
                = new SessionFactoryHandler(
                        new File("hibernate.cfg.xml"));

        new Routes(sessionFactoryHandler.buildSessionFactory())
                .registerRoutes(server.getApp());

    }
}
