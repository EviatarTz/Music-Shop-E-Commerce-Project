package dao;

import beans.OrderItem;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access class for the "order_Items" table (the line items of an order).
 */
public class OrderItemDAO {

    /* Inserts a single order line item */
    public boolean addOrderItem(OrderItem item) {

        String sql = """
                INSERT INTO order_Items(order_id, product_id, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """;

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, item.getOrderId());
            statement.setInt(2, item.getProductId());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getUnitPrice());

            /* returns true if the execution yields more than one row */
            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns false if the connection to the database has failed */
        return false;
    }

    /* Fetches every line item that belongs to a given order */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        /* Creates a list of items */
        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_Items WHERE order_id = ?";

        /* Try to connect to the database and create a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            /* Execute the query and save the resulted rows in the Results set */
            ResultSet res = statement.executeQuery();

            /* While there are still rows left */
            while (res.next()) {

                /* Creates an order item */
                OrderItem item = new OrderItem();

                item.setOrderItemId(res.getInt("order_item_id"));
                item.setOrderId(res.getInt("order_id"));
                item.setProductId(res.getInt("product_id"));
                item.setQuantity(res.getInt("quantity"));
                item.setUnitPrice(res.getDouble("unit_price"));

                /* Adds the order item to the list of order items */
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns the order items list */
        return items;
    }

    /* Deletes a single order line item by its own ID */
    public boolean deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM order_Items WHERE order_item_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);

            /* Return true if the query returns one or more rows */
            return stmt.executeUpdate() > 0;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns false  if the connection to the database has failed */
        return false;
    }
}
