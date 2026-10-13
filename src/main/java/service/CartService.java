package service;

import beans.CartItem;
import dao.CartDAO;
import java.util.List;

/**
 * Business-logic layer on top of CartDAO: a thin wrapper that adds basic
 * input validation (e.g. rejecting non-positive quantities) before delegating
 * to the DAO.
 */
public class CartService {

    private final CartDAO cartDAO = new CartDAO();

    /* Adds a product to the cart; rejects non-positive quantities */
    public boolean addProductToCart(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartDAO.addProductToCart(cartId, productId, quantity);
    }

    /* Returns the user's cart ID, creating a new cart for them if needed */
    public int getOrCreateCartId(int userId) {
        return cartDAO.getOrCreateCartId(userId);
    }

    /* Returns every line item in the given cart */
    public List<CartItem> getCartItems(int cartId) {
        return cartDAO.getCartItems(cartId);
    }

    /* Removes a single line item from the cart */
    public boolean removeProductFromCart(int cartItemId) {
        return cartDAO.removeProductFromCart(cartItemId);
    }

    /* Empties the entire cart */
    public boolean clearCart(int cartId) {
        return cartDAO.clearCart(cartId);
    }

    /* Updates a line item's quantity; rejects non-positive quantities */
    public boolean updateQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartDAO.updateQuantity(cartItemId, quantity);
    }
}
