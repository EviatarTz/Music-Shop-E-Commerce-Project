package util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a plain JDBC connection to the "music_shop_db" PostgreSQL database.
 * Credentials are hardcoded for simplicity (not a best practice for an industrial product).
 */
public class ConnectionManager {

    public static final String URL = "jdbc:postgresql://localhost:5432/music_shop_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    /* Opens and returns a new JDBC connection; the caller is responsible for closing it */
    public static Connection getConnection() throws Exception{
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER , PASSWORD);
    }

}
