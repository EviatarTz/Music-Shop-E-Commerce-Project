package managedbeans;

import beans.CartItem;
import beans.Product;
import beans.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.CartService;
import service.ProductService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean for the cart page (cart.xhtml): loads the logged-in user's
 * cart as a list of CartLine view-model rows, and lets them update
 * quantities or remove items.
 */
@Named("cartBean")
@RequestScoped
public class CartBean implements Serializable {

    private final CartService cartService = new CartService();
    private final ProductService productService = new ProductService();

    private List<CartLine> items = new ArrayList<>();
    private double total;

    private boolean loaded = false;

    /* View-model row: a cart item joined with its product, for display in the UI */
    public static class CartLine implements Serializable {
        private final int cartItemId;
        private final Product product;
        private int quantity;

        public CartLine(int cartItemId, Product product, int quantity) {
            this.cartItemId = cartItemId;
            this.product = product;
            this.quantity = quantity;
        }

        public int getCartItemId() { return cartItemId; }
        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getSubtotal() { return product.getPrice() * quantity; }
    }

    /* Redirects to login if no user is logged in, otherwise loads the cart */
    public void checkLoginAndLoad() {
        User user = getLoggedInUser();
        if (user == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        loadItems(user);
    }

    /* Loads the user's cart items and joins each one with its product */
    private void loadItems(User user) {
        items = new ArrayList<>();
        total = 0;

        int cartId = cartService.getOrCreateCartId(user.getUserId());
        List<CartItem> cartItems = cartService.getCartItems(cartId);

        for (CartItem ci : cartItems) {
            Product product = productService.getProductById(ci.getProductId());
            if (product != null) {
                CartLine line = new CartLine(ci.getCartItemId(), product, ci.getQuantity());
                items.add(line);
                total += line.getSubtotal();
            }
        }
    }

    /* Removes a line item and reloads the cart */
    public void removeItem(int cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        User user = getLoggedInUser();
        if (user != null) {
            loadItems(user);
        }
    }

    /* Updates a line item's quantity (clamped to a minimum of 1) and reloads the cart */
    public void updateQuantity(CartLine line) {
        if (line.getQuantity() < 1) {
            line.setQuantity(1);
        }
        cartService.updateQuantity(line.getCartItemId(), line.getQuantity());
        User user = getLoggedInUser();
        if (user != null) {
            loadItems(user);
        }
    }

    /* Lazily loads the cart once per request, if a user is logged in */
    private void ensureLoaded() {
        if (!loaded) {
            User user = getLoggedInUser();
            if (user != null) {
                loadItems(user);
            }
            loaded = true;
        }
    }

    private User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    public List<CartLine> getItems() {
        ensureLoaded();
        return items;
    }

    public double getTotal() {
        ensureLoaded();
        return total;
    }
}
