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

@Named("orderConfirmationBean")
@RequestScoped
public class OrderConfirmationBean implements Serializable {

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    private int orderId;
    private Order order;
    private List<ConfirmationLine> items = new ArrayList<>();

    public static class ConfirmationLine implements Serializable {
        private final Product product;
        private final int quantity;
        private final double unitPrice;

        public ConfirmationLine(Product product, int quantity, double unitPrice) {
            this.product = product;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getSubtotal() { return unitPrice * quantity; }
    }

    public void loadOrder() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }

        order = orderService.getOrderById(orderId);

        if (order == null || order.getUserId() != user.getUserId()) {
            redirect("products.xhtml");
            return;
        }

        items = new ArrayList<>();
        List<OrderItem> orderItems = orderService.getOrderItems(orderId);
        for (OrderItem oi : orderItems) {
            Product product = productService.getProductById(oi.getProductId());
            if (product != null) {
                items.add(new ConfirmationLine(product, oi.getQuantity(), oi.getUnitPrice()));
            }
        }
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

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public Order getOrder() { return order; }
    public List<ConfirmationLine> getItems() { return items; }
}