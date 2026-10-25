package infrastructure.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConfig {

    private static final String URL = System.getenv().getOrDefault(
            "DB_URL", "jdbc:postgresql://localhost:5432/ventas_db"
    );
    private static final String USER = System.getenv().getOrDefault(
            "DB_USER", "ventas_user"
    );
    private static final String PASS = System.getenv().getOrDefault(
            "DB_PASSWORD", "ventas_password"
    );

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
