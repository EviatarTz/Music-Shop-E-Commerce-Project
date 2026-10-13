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
        /* return false if an incorrect quantity to update inserted */
        if (quantity <= 0) {
            return false;
        }
        /* else, add the product with the given quantity to the cart in the database */
        return cartDAO.addProductToCart(cartId, productId, quantity);
    }

    /* Returns the user's cart ID, creating a new cart for them if needed */
    public int getOrCreateCartId(int userId) {

        /* returns the cart's id from the database. if a cart does not exist for the given user, create a new one in the database */
        return cartDAO.getOrCreateCartId(userId);
    }

    /* Returns every line item in the given cart */
    public List<CartItem> getCartItems(int cartId) {

        /* Gets a list of the cart's items from the database */
        return cartDAO.getCartItems(cartId);
    }

    /* Removes a single line item from the cart */
    public boolean removeProductFromCart(int cartItemId) {

        /* Remove a single product from the cart in the database */
        return cartDAO.removeProductFromCart(cartItemId);
    }

    /* Empties the entire cart */
    public boolean clearCart(int cartId) {

        /* Clears the cart from it's items in the database */
        return cartDAO.clearCart(cartId);
    }

    /* Updates a line item's quantity; rejects non-positive quantities */
    public boolean updateQuantity(int cartItemId, int quantity) {

        /* If the quantity inserted is not valid, return false (nothing to update) */
        if (quantity <= 0) {
            return false;
        }

        /* Else, update the quantity if that cart's item in the database */
        return cartDAO.updateQuantity(cartItemId, quantity);
    }

}
