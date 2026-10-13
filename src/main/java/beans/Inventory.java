package beans;

import java.sql.Timestamp;

/**
 * Represents a row in the "inventory" table: the stock quantity available
 * for a given product, and when that quantity was last updated.
 */
public class Inventory {

    private int inventoryId;
    private int productId;
    private int quantity;
    private Timestamp lastUpdate;

    /* An empty constructor */
    public Inventory() {}

    /* A parameterized constructor */
    public Inventory(int inventoryId, int productId, int quantity, Timestamp lastUpdate) {

        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdate = lastUpdate;

    }

    /* Gets the inventory row's own ID */
    public int getInventoryId() {
        return inventoryId;
    }

    /* Gets the ID of the product this inventory row tracks */
    public int getProductId() {
        return productId;
    }

    /* Gets the current stock quantity */
    public int getQuantity() {
        return quantity;
    }

    /* Gets the timestamp of the last stock update */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /* Sets the inventory row's own ID */
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    /* Sets the ID of the product this inventory row tracks */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /* Sets the current stock quantity */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* Sets the timestamp of the last stock update */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Inventory{" + "inventoryId=" + inventoryId + ", productId=" + productId + ", quantity=" + quantity + ", lastUpdate=" + lastUpdate + "}";
    }
}
