package service;

import beans.Inventory;
import dao.InventoryDAO;

public class InventoryService {

    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.getInventoryByProductId(productId);
    }

    public boolean hasEnoughStock(int productId, int quantity) {
        return inventoryDAO.hasEnoughStock(productId, quantity);
    }

    public boolean reduceStock(int productId, int quantity) {
        return inventoryDAO.reduceStock(productId, quantity);
    }

    public boolean updateQuantity(int productId, int quantity) {
        return inventoryDAO.updateQuantity(productId, quantity);
    }

}
