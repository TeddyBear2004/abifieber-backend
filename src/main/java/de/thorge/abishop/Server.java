package de.thorge.abishop;

import io.javalin.Javalin;

public class Server {
    private final Javalin app;

    public Server(int port) {
        this.app = Javalin.create().start(port);
    }

    public Javalin getApp() {
        return app;
    }
}
