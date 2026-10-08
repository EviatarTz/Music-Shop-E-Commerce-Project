package util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static final String URL = "jdbc:postgresql://localhost:5432/music_shop_db";

    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws Exception{
        Class.forName("org.postgresql.Driver");
        System.out.println("POSTGRES DRIVER LOADED");
        return DriverManager.getConnection(URL, USER , PASSWORD);
    }

}
