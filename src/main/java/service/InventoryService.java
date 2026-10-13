package service;

import beans.Inventory;
import dao.InventoryDAO;

/**
 * Business-logic layer on top of InventoryDAO.
 */
public class InventoryService {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    /* Fetches the inventory row for a given product */
    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.getInventoryByProductId(productId);
    }

    /* Checks whether there is enough stock for the requested quantity */
    public boolean hasEnoughStock(int productId, int quantity) {
        return inventoryDAO.hasEnoughStock(productId, quantity);
    }

    /* Reduces stock for a product (e.g. after a purchase) */
    public boolean reduceStock(int productId, int quantity) {
        return inventoryDAO.reduceStock(productId, quantity);
    }

    /* Overwrites the stock quantity for a product */
    public boolean updateQuantity(int productId, int quantity) {
        return inventoryDAO.updateQuantity(productId, quantity);
    }

}
