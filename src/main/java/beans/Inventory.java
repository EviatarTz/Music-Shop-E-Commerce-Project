package beans;

import java.sql.Timestamp;

public class Inventory {

    private int inventoryId;
    private int productId;
    private int quantity;
    private Timestamp lastUpdate;

    public Inventory() {}

    public Inventory(int inventoryId, int productId, int quantity, Timestamp lastUpdate) {

        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdate = lastUpdate;

    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Inventory{" + "inventoryId=" + inventoryId + ", productId=" + productId + ", quantity=" + quantity + ", lastUpdate=" + lastUpdate + "}";
    }
}
