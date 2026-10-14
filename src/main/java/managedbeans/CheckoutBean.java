package managedbeans;

import beans.CartItem;
import beans.Product;
import beans.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.CartService;
import service.OrderService;
import service.ProductService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean for the checkout page (checkout.xhtml): shows the cart's
 * contents one more time before confirming, then submits the order.
 */
@Named("checkoutBean")
@RequestScoped
public class CheckoutBean implements Serializable {

    /* Service responsible for cart-related operations */
    private final CartService cartService = new CartService();
    /* Service responsible for creating and processing orders */
    private final OrderService orderService = new OrderService();
    /* Service used to retrieve product information */
    private final ProductService productService = new ProductService();

    /* Products currently being displayed on the checkout page */
    private List<CheckoutLine> items = new ArrayList<>();
    /* Total value of all items in the checkout */
    private double total;
    /* Tracks whether checkout data has already been loaded */
    private boolean loaded = false;

    /* View-model row: a cart item joined with its product, for display on the checkout page */
    public static class CheckoutLine implements Serializable {
        private final Product product;
        private final int quantity;

        /* Creates a checkout line for the specified product and quantity */
        public CheckoutLine(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        /* Returns the product associated with this checkout line */
        public Product getProduct() { return product; }

        /* Returns the selected quantity of the product */
        public int getQuantity() { return quantity; }

        /* Calculates the subtotal price for this checkout line */
        public double getSubtotal() { return product.getPrice() * quantity; }
    }

    /*
        Confirms the order: delegates to OrderService.checkout(), which returns the
        new order's ID on success or -1 on failure (empty cart / insufficient stock).
        On success, navigates to the order-confirmation page with that order's ID.
    */
    public String submitOrder() {
        /* Retrieve the currently logged-in user */
        User user = getLoggedInUser();
        /* Redirect unauthenticated users to the login page */
        if (user == null) {
            return "login?faces-redirect=true";
        }

        /* Retrieve the user's cart and submit it for checkout */
        int cartId = cartService.getOrCreateCartId(user.getUserId());
        int orderId = orderService.checkout(cartId, user.getUserId());

        /* Display an error if checkout could not be completed */
        if (orderId == -1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "לא ניתן להשלים את ההזמנה (ייתכן שהעגלה ריקה או שאין מספיק מלאי)", null));
            return null;
        }

        /* Navigate to the order confirmation page for the newly created order */
        return "orderConfirmation?faces-redirect=true&orderId=" + orderId;
    }

    /* Redirects the user to the specified page */
    private void redirect(String page) {
        try {
            /* Perform a JSF redirect to the requested page */
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Returns the currently logged-in user from the session */
    private User getLoggedInUser() {
        /* Retrieve the user object stored in the session map */
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /* Returns the checkout items, loading them if necessary */
    public List<CheckoutLine> getItems() {
        /* Ensure checkout data has been loaded before returning it */
        ensureLoaded();
        return items;
    }

    /* Returns the checkout total, loading data if necessary */
    public double getTotal() {
        /* Ensure checkout data has been loaded before calculating totals */
        ensureLoaded();
        return total;
    }

    /* Lazily loads the cart contents once per request, if a user is logged in */
    private void ensureLoaded() {
        /* Skip loading if the checkout has already been initialized */
        if (!loaded) {
            /* Retrieve the currently logged-in user */
            User user = getLoggedInUser();
            /* Load the user's checkout items if authenticated */
            if (user != null) {
                loadItems(user);
            }
            /* Mark the checkout data as initialized */
            loaded = true;
        }
    }

    /* Loads the user's cart items and joins each one with its product */
    private void loadItems(User user) {
        /* Reset the checkout items and total before rebuilding them */
        items = new ArrayList<>();
        total = 0;

        /* Retrieve the user's cart and all items it contains */
        int cartId = cartService.getOrCreateCartId(user.getUserId());
        List<CartItem> cartItems = cartService.getCartItems(cartId);

        /* Join each cart item with its associated product */
        for (CartItem ci : cartItems) {
            /* Retrieve the corresponding product information */
            Product product = productService.getProductById(ci.getProductId());
            /* Include only products that still exist */
            if (product != null) {
                /* Create a checkout line for UI display */
                CheckoutLine line = new CheckoutLine(product, ci.getQuantity());
                /* Add the line and update the running order total */
                items.add(line);
                total += line.getSubtotal();
            }
        }
    }

    /* Redirects to login if no user is logged in, otherwise loads the cart for review */
    public void checkLoginAndLoad() {

        /* Retrieve the currently logged-in user */
        User user = getLoggedInUser();

        /* Redirect guests to the login page */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        /* Load the user's cart items for checkout review */
        loadItems(user);

        /* Mark checkout data as already loaded */
        loaded = true;
    }

}
