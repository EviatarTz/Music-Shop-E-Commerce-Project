package dao;

import beans.Inventory;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class InventoryDAO {

    public Inventory getInventoryByProductId(int productId) {

        String sql = "SELECT * FROM inventory WHERE product_id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, productId);
            ResultSet res = statement.executeQuery();

            if (res.next()) {

                Inventory inventory = new Inventory();
                inventory.setInventoryId(res.getInt("inventory_id"));
                inventory.setProductId(res.getInt("product_id"));
                inventory.setQuantity(res.getInt("quantity"));
                inventory.setLastUpdate(res.getTimestamp("last_update"));

                return inventory;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean hasEnoughStock(int productId, int requestedQuantity) {
        Inventory inventory = getInventoryByProductId(productId);
        return inventory != null && inventory.getQuantity() >= requestedQuantity;
    }

    public boolean updateQuantity(int productId, int newQuantity) {

        String sql = """
            UPDATE inventory
            SET quantity = ?,last_update = ?
            WHERE product_id = ?
            """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, newQuantity);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, productId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reduceStock(int productId, int quantityToReduce) {

        Inventory inventory = getInventoryByProductId(productId);

        if (inventory == null) {
            return false;
        }

        int updatedQuantity = inventory.getQuantity() - quantityToReduce;

        if (updatedQuantity < 0) {
            return false;
        }

        return updateQuantity(productId, updatedQuantity);
    }

}
