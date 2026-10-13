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

    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    private List<CheckoutLine> items = new ArrayList<>();
    private double total;
    private boolean loaded = false;

    /* View-model row: a cart item joined with its product, for display on the checkout page */
    public static class CheckoutLine implements Serializable {
        private final Product product;
        private final int quantity;

        public CheckoutLine(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return product.getPrice() * quantity; }
    }
    /*
    // Superseded by loadItems() below - kept here for reference only.
    public void checkLoginAndLoad() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        items = new ArrayList<>();
        total = 0;

        int cartId = cartService.getOrCreateCartId(user.getUserId());
        List<CartItem> cartItems = cartService.getCartItems(cartId);

        for (CartItem ci : cartItems) {
            Product product = productService.getProductById(ci.getProductId());
            if (product != null) {
                CheckoutLine line = new CheckoutLine(product, ci.getQuantity());
                items.add(line);
                total += line.getSubtotal();
            }
        }
    }

     */

    /*
        Confirms the order: delegates to OrderService.checkout(), which returns the
        new order's ID on success or -1 on failure (empty cart / insufficient stock).
        On success, navigates to the order-confirmation page with that order's ID.
    */
    public String submitOrder() {
        User user = getLoggedInUser();
        if (user == null) {
            return "login?faces-redirect=true";
        }

        int cartId = cartService.getOrCreateCartId(user.getUserId());
        int orderId = orderService.checkout(cartId, user.getUserId());

        if (orderId == -1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "לא ניתן להשלים את ההזמנה (ייתכן שהעגלה ריקה או שאין מספיק מלאי)", null));
            return null;
        }

        return "orderConfirmation?faces-redirect=true&orderId=" + orderId;
    }

    private void redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    public List<CheckoutLine> getItems() {
        ensureLoaded();
        return items;
    }

    public double getTotal() {
        ensureLoaded();
        return total;
    }

    /* Lazily loads the cart contents once per request, if a user is logged in */
    private void ensureLoaded() {
        if (!loaded) {
            User user = getLoggedInUser();
            if (user != null) {
                loadItems(user);
            }
            loaded = true;
        }
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
                CheckoutLine line = new CheckoutLine(product, ci.getQuantity());
                items.add(line);
                total += line.getSubtotal();
            }
        }
    }

    /* Redirects to login if no user is logged in, otherwise loads the cart for review */
    public void checkLoginAndLoad() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }
        loadItems(user);
        loaded = true;
    }


}
