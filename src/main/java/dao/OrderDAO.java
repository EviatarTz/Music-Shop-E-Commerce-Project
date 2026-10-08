package dao;

import beans.Order;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public int createOrder(Order order) {
        String sql = """
            INSERT INTO orders
            (user_id, total_price, order_date)
            VALUES (?, ?, ?)
            """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setDouble(2, order.getTotalPrice());
            statement.setTimestamp(3, order.getOrderDate());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {

                ResultSet keys = statement.getGeneratedKeys();

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Order getOrderById(int orderId) {

        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            ResultSet res = statement.executeQuery();

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
        return null;
    }
    public List<Order> getOrdersByUser(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet res = statement.executeQuery();

            while (res.next()) {

                Order order = new Order();

                order.setOrderId(res.getInt("order_id"));
                order.setUserId(res.getInt("user_id"));
                order.setTotalPrice(res.getDouble("total_price"));
                order.setOrderDate(res.getTimestamp("order_date"));

                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
