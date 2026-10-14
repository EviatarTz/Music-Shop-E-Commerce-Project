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

        /* Creates a cart line from a cart item and its associated product */
        public CartLine(int cartItemId, Product product, int quantity) {
            this.cartItemId = cartItemId;
            this.product = product;
            this.quantity = quantity;
        }

        /* Returns the cart item identifier */
        public int getCartItemId() { return cartItemId; }

        /* Returns the product associated with this cart line */
        public Product getProduct() { return product; }

        /* Returns the selected quantity for this product */
        public int getQuantity() { return quantity; }

        /* Updates the quantity for this cart line */
        public void setQuantity(int quantity) { this.quantity = quantity; }

        /* Calculates the subtotal for this line item */
        public double getSubtotal() { return product.getPrice() * quantity; }
    }

    /* Redirects to login if no user is logged in, otherwise loads the cart */
    public void checkLoginAndLoad() {

        /* Retrieve the currently logged-in user from the session */
        User user = getLoggedInUser();

        /* edirect to the login page if no user is logged in */
        if (user == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        /* Load the user's cart contents */
        loadItems(user);
    }

    /* Loads the user's cart items and joins each one with its product */
    private void loadItems(User user) {
        items = new ArrayList<>();
        total = 0;

        /* Retrieve the user's cart ID, creating a cart if necessary */
        int cartId = cartService.getOrCreateCartId(user.getUserId());
        /* Load all cart items belonging to the cart*/
        List<CartItem> cartItems = cartService.getCartItems(cartId);

        /* Join each cart item with its product details */
        for (CartItem ci : cartItems) {
            /* Retrieve the product associated with the current cart item */
            Product product = productService.getProductById(ci.getProductId());
            /* Only add lines for products that still exist */
            if (product != null) {

                /* Create a view-model object for UI display and Add the line to the cart and update the running total */
                CartLine line = new CartLine(ci.getCartItemId(), product, ci.getQuantity());
                items.add(line);
                total += line.getSubtotal();
            }
        }
    }

    /* Removes a line item and reloads the cart */
    public void removeItem(int cartItemId) {

        /* Delete the selected cart item */
        cartService.removeProductFromCart(cartItemId);

        /* Reload the cart if the user is still logged in */
        User user = getLoggedInUser();
        if (user != null) {
            loadItems(user);
        }
    }

    /* Updates a line item's quantity (clamped to a minimum of 1) and reloads the cart */
    public void updateQuantity(CartLine line) {

        /* Prevent invalid quantities by enforcing a minimum value of one */
        if (line.getQuantity() < 1) {
            line.setQuantity(1);
        }

        /* Persist the updated quantity in the database */
        cartService.updateQuantity(line.getCartItemId(), line.getQuantity());

        /* Reload the cart to reflect the updated values and totals */
        User user = getLoggedInUser();
        if (user != null) {
            loadItems(user);
        }
    }

    /* Lazily loads the cart once per request, if a user is logged in */
    private void ensureLoaded() {
        /* Load cart contents only if they have not already been loaded */
        if (!loaded) {
            /* Retrieve the current user from the session */
            User user = getLoggedInUser();
            /* Load cart data for authenticated users */
            if (user != null) {
                loadItems(user);
            }
            /* Mark the cart as initialized */
            loaded = true;
        }
    }

    /* Returns the currently logged-in user from the session */
    private User getLoggedInUser() {
        /* Read the user object stored in the HTTP session map */
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /* Returns the cart items, loading them if necessary */
    public List<CartLine> getItems() {

        /* Ensure the cart data has been initialized */
        ensureLoaded();
        return items;
    }

    /* Returns the current cart total, loading cart data if necessary */
    public double getTotal() {
        /* Ensure the cart data has been initialized */
        ensureLoaded();
        return total;
    }
}
