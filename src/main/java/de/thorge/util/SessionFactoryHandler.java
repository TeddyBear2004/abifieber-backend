package de.thorge.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.function.Consumer;

public class SessionFactoryHandler {

    private final File baseConfig;

    public SessionFactoryHandler(File baseConfig) {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null) {
            System.setProperty("db.url", databaseUrl);
        }

        String username = System.getenv("DATABASE_USER");
        if (username != null) {
            System.setProperty("db.username", username);
        }

        String password = System.getenv("DATABASE_PASSWORD");
        if (password != null) {
            System.setProperty("db.password", password);
        }
        this.baseConfig = baseConfig;
    }

    public SessionFactory buildSessionFactory(Consumer<Configuration> configurationConsumer) {
        Configuration configuration = new Configuration().configure(this.baseConfig);
        configurationConsumer.accept(configuration);
        return configuration.buildSessionFactory();
    }

    public SessionFactory buildSessionFactory() {
        return new Configuration().configure(this.baseConfig).buildSessionFactory();
    }

}