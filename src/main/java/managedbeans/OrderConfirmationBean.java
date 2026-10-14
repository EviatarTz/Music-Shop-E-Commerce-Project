package managedbeans;

import beans.Order;
import beans.OrderItem;
import beans.Product;
import beans.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.OrderService;
import service.ProductService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean for the order-confirmation page (orderConfirmation.xhtml):
 * loads a single order (by the ID passed in the request) and its line items,
 * for display after a successful checkout.
 */
@Named("orderConfirmationBean")
@RequestScoped
public class OrderConfirmationBean implements Serializable {

    /* Service used to retrieve order information */
    private final OrderService orderService = new OrderService();
    /* Service used to retrieve product information */
    private final ProductService productService = new ProductService();

    /* ID of the order being displayed */
    private int orderId;

    /* The loaded order details */
    private Order order;

    /* Order items prepared for display */
    private List<ConfirmationLine> items = new ArrayList<>();

    /* View-model row: an order line item joined with its product, for display */
    public static class ConfirmationLine implements Serializable {
        private final Product product;
        private final int quantity;
        private final double unitPrice;

        /* Creates a confirmation line */
        public ConfirmationLine(Product product, int quantity, double unitPrice) {
            this.product = product;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        /* Returns the product */
        public Product getProduct() {
            return product;
        }

        /* Returns the quantity ordered */
        public int getQuantity() {
            return quantity;
        }

        /* Returns the price per unit */
        public double getUnitPrice() {
            return unitPrice;
        }

        /* Returns the line subtotal */
        public double getSubtotal() {
            return unitPrice * quantity;
        }
    }

    /*
        Loads the order for the current orderId, after verifying the user is logged
        in and that the order actually belongs to them (otherwise redirects away).
    */
    public void loadOrder() {

        /* Get the current user */
        User user = getLoggedInUser();

        /* Redirect guests to the login page */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        /* Load the requested order */
        order = orderService.getOrderById(orderId);

        /* Ensure the order exists and belongs to the current user */
        if (order == null || order.getUserId() != user.getUserId()) {
            redirect("products.xhtml");
            return;
        }

        /* Reset the displayed order items */
        items = new ArrayList<>();

        /* Load all items belonging to the order */
        List<OrderItem> orderItems = orderService.getOrderItems(orderId);

        /* Join each order item with its product */
        for (OrderItem oi : orderItems) {

            /* Retrieve the associated product */
            Product product = productService.getProductById(oi.getProductId());

            /* Add valid products to the display list */
            if (product != null) {
                items.add(new ConfirmationLine(product, oi.getQuantity(), oi.getUnitPrice()));
            }
        }
    }

    /* Redirects to the specified page */
    private void redirect(String page) {
        try {
            /* Perform the redirect */
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Returns the logged-in user */
    private User getLoggedInUser() {
        /* Read the user from the session */
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    /* Returns the current order ID */
    public int getOrderId() {
        return orderId;
    }

    /* Returns the loaded order */
    public Order getOrder() {
        return order;
    }

    /* Returns the order items */
    public List<ConfirmationLine> getItems() {
        return items;
    }

    /* Updates the current order ID */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
