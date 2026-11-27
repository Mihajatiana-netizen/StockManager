
package com.stockmanager.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_DIRECTORY = "data";
    private static final String DB_NAME = "stockmanager.db";

    private static final String URL =
            "jdbc:sqlite:" + DB_DIRECTORY + "/" + DB_NAME;

    // Ouvrir une connexion SQLite
    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(URL);

        // Activer les clés étrangères pour cette connexion
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    // Initialiser la base de données et créer les tables
    public static void initialize() throws SQLException, IOException {

        // Créer le dossier data s'il n'existe pas
        Files.createDirectories(Path.of(DB_DIRECTORY));

        // Lire le fichier schema.sql
        try (InputStream input = Database.class.getResourceAsStream(
                "/database/schema.sql")) {

            if (input == null) {
                throw new IOException("Fichier schema.sql introuvable !");
            }

            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Exécuter les instructions SQL
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {

                for (String command : sql.split(";")) {

                    String instruction = command.trim();

                    if (!instruction.isEmpty()) {
                        statement.execute(instruction);
                    }
                }
            }
        }

        System.out.println("Base de données initialisée avec succès !");
    }
}
