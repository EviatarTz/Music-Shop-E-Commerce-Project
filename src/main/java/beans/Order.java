package beans;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private int userId;
    private double totalPrice;
    private Timestamp orderDate;

    public Order() {
    }

    public Order(int orderId, int userId, double totalPrice, Timestamp orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    @Override
    public String toString() {
        return "Order{" + "orderId=" + orderId + ", userId=" + userId + ", totalPrice=" + totalPrice + ", orderDate=" + orderDate + "}";
    }
}
