package dao;

import beans.Inventory;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Data-access class for the "inventory" table (per-product stock tracking).
 */
public class InventoryDAO {

    /* Fetches the inventory row for a given product, or null if none exists */
    public Inventory getInventoryByProductId(int productId) {

        String sql = "SELECT * FROM inventory WHERE product_id = ?";

        /* Tries to open a database connection and create a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, productId);

            /* Executes the query and retrieve the rows returned */
            ResultSet res = statement.executeQuery();

            /* Sets the inventory row. the information are retrieved from the query */
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

        /* If the connection to the database failed */
        return null;
    }

    /* Checks whether there is at least 'requestedQuantity' units of stock available */
    public boolean hasEnoughStock(int productId, int requestedQuantity) {
        Inventory inventory = getInventoryByProductId(productId);
        return inventory != null && inventory.getQuantity() >= requestedQuantity;
    }

    /* Overwrites the stock quantity for a product and refreshes its last-update timestamp */
    public boolean updateQuantity(int productId, int newQuantity) {

        String sql = """
            UPDATE inventory
            SET quantity = ?,last_update = ?
            WHERE product_id = ?
            """;

        /* Tries to open a database connection and create a prepared statement */
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

    /*
        Reduces the stock for a product by the given amount (used after a successful
        checkout). Fails (returns false) if there isn't enough stock to reduce, or if
        no inventory row exists for the product.
    */
    public boolean reduceStock(int productId, int quantityToReduce) {

        /* Gets the inventory product by it's ID */
        Inventory inventory = getInventoryByProductId(productId);

        /* If an inventory item doesn't exist, there's nothing to reduce */
        if (inventory == null) {
            return false;
        }

        /* Calculate the updated quantity */
        int updatedQuantity = inventory.getQuantity() - quantityToReduce;

        /* If the inventory for that product is empty */
        if (updatedQuantity < 0) {
            return false;
        }

        /* Update the product's quantity and return true if the update was made successfully */
        return updateQuantity(productId, updatedQuantity);
    }

}
