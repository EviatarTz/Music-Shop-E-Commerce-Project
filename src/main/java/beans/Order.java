package beans;
import java.sql.Timestamp;

/**
 * Represents a row in the "orders" table: a completed order placed by a user,
 * its total price and the date it was placed.
 */
public class Order {

    private int orderId;
    private int userId;
    private double totalPrice;
    private Timestamp orderDate;

    /* An empty constructor */
    public Order() {}

    /* A parameterized constructor */
    public Order(int orderId, int userId, double totalPrice, Timestamp orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    /* Gets the order's own ID */
    public int getOrderId() {
        return orderId;
    }

    /* Gets the ID of the user who placed the order */
    public int getUserId() {
        return userId;
    }

    /* Gets the total price charged for the order */
    public double getTotalPrice() {
        return totalPrice;
    }

    /* Gets the date/time the order was placed */
    public Timestamp getOrderDate() {
        return orderDate;
    }

    /* Sets the order's own ID */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /* Sets the ID of the user who placed the order */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /* Sets the total price charged for the order */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /* Sets the date/time the order was placed */
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", userId=" + userId + ", totalPrice=" + totalPrice + ", orderDate=" + orderDate + "}";
    }
}
