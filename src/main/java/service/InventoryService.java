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

        /* Gets inventory information of a specific product from the database */
        return inventoryDAO.getInventoryByProductId(productId);
    }

    /* Checks whether there is enough stock for the requested quantity */
    public boolean hasEnoughStock(int productId, int quantity) {

        /* Returns true if there's enough stock for a specific product in the database for the requested quantity */
        return inventoryDAO.hasEnoughStock(productId, quantity);
    }

    /* Reduces stock for a product (e.g. after a purchase) */
    public boolean reduceStock(int productId, int quantity) {

        /* Returns true if a product's quantity was reduced successfully in the database */
        return inventoryDAO.reduceStock(productId, quantity);
    }

    /* Overwrites the stock quantity for a product */
    public boolean updateQuantity(int productId, int quantity) {

        /* Returns true if the quantity was updated successfully in the database, or false otherwise */
        return inventoryDAO.updateQuantity(productId, quantity);
    }

}
