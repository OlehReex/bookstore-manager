package com.example.bookstore.integration.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

public class CustomPostgresContainer extends PostgreSQLContainer<CustomPostgresContainer> {
    private static final String DB_IMAGE = "postgres:latest";
    private static final String INIT_SCRIPT_PATH = "resources/init-database.sql";

    private static CustomPostgresContainer postgresContainer;

    private CustomPostgresContainer() {
        super(DB_IMAGE);
        withInitScript();
    }

    public static synchronized CustomPostgresContainer getInstance() {
        if (postgresContainer == null) {
            postgresContainer = new CustomPostgresContainer();
        }
        return postgresContainer;
    }

    private void withInitScript() {
        this.withCopyFileToContainer(MountableFile.forClasspathResource(INIT_SCRIPT_PATH), "/docker-entrypoint-initdb.d/init.sql");
        System.out.println("DB INITIALIZED");
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", postgresContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", postgresContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", postgresContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
