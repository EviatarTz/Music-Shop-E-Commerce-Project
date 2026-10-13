package dao;

import beans.Order;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access class for the "orders" table.
 */
public class OrderDAO {

    /* Inserts a new order row and returns its generated order_id (or -1 on failure) */
    public int createOrder(Order order) {

        String sql = """
            INSERT INTO orders
            (user_id, total_price, order_date)
            VALUES (?, ?, ?)
            """;
        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setDouble(2, order.getTotalPrice());
            statement.setTimestamp(3, order.getOrderDate());

            /* Gets the number of the affected rows */
            int affectedRows = statement.executeUpdate();

            /* If the insertion was done successfully */
            if (affectedRows > 0) {

                /* Get the keys generated automatically by the statement call */
                ResultSet keys = statement.getGeneratedKeys();

                /* Gets the id created automatically by the statement call (if there's a new row found in the ResultSet) */
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* if the Connection to the database or the Insertion failed */
        return -1;
    }

    /* Fetches a single order by its ID, or null if not found */
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            /* Saves the rows returned by the executing the statement */
            ResultSet res = statement.executeQuery();

            /* if there is a row in the result(s) set, create an Order object and fill it's fields with the row's data */
            if (res.next()) {

                Order order = new Order();

                order.setOrderId(res.getInt("order_id"));
                order.setUserId(res.getInt("user_id"));
                order.setTotalPrice(res.getDouble("total_price"));
                order.setOrderDate(res.getTimestamp("order_date"));

                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* If the connection failed */
        return null;
    }

    /* Fetches every order placed by a given user */
    public List<Order> getOrdersByUser(int userId) {

        /* Create a list of orders */
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE user_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);

            /* Execute the query and save the resulted rows in the results set */
            ResultSet res = statement.executeQuery();

            /* While there are still rows left - create an Order object for each row */
            while (res.next()) {

                Order order = new Order();

                order.setOrderId(res.getInt("order_id"));
                order.setUserId(res.getInt("user_id"));
                order.setTotalPrice(res.getDouble("total_price"));
                order.setOrderDate(res.getTimestamp("order_date"));

                /* Add the order to the list of orders */
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* If the connection to the database has failed */
        return orders;
    }
}
