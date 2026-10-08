package dao;

import beans.OrderItem;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class OrderItemDAO {

    public boolean addOrderItem(OrderItem item) {

        String sql = """
                INSERT INTO order_Items(order_id, product_id, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, item.getOrderId());
            statement.setInt(2, item.getProductId());
            statement.setInt(3, item.getQuantity());
            statement.setDouble(4, item.getUnitPrice());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = "SELECT * FROM order_Items WHERE order_id = ?";

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, orderId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {

                OrderItem item = new OrderItem();

                item.setOrderItemId(res.getInt("order_item_id"));
                item.setOrderId(res.getInt("order_id"));
                item.setProductId(res.getInt("product_id"));
                item.setQuantity(res.getInt("quantity"));
                item.setUnitPrice(res.getDouble("unit_price"));

                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    public boolean deleteOrderItem(int orderItemId) {

        String sql = "DELETE FROM order_Items WHERE order_item_id = ?";

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
