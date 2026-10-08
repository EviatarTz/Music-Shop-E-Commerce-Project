package service;

import beans.CartItem;
import dao.CartDAO;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();

    public boolean addProductToCart(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartDAO.addProductToCart(cartId, productId, quantity);
    }

    public int getOrCreateCartId(int userId) {
        return cartDAO.getOrCreateCartId(userId);
    }

    public List<CartItem> getCartItems(int cartId) {
        return cartDAO.getCartItems(cartId);
    }

    public boolean removeProductFromCart(int cartItemId) {
        return cartDAO.removeProductFromCart(cartItemId);
    }

    public boolean clearCart(int cartId) {
        return cartDAO.clearCart(cartId);
    }

    public boolean updateQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        return cartDAO.updateQuantity(cartItemId, quantity);
    }
}
