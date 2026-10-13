package beans;

/**
 * Represents a single line item inside an order: which product was bought,
 * how many units, and the unit price at the time of the order (so later
 * price changes to the product don't affect historical orders).
 */
public class OrderItem {

    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;
    private double unitPrice;

    /* An empty constructor */
    public OrderItem() {}

    /* A parameterized constructor */
    public OrderItem(int orderItemId, int orderId, int productId, int quantity, double unitPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /* Gets this line item's own ID */
    public int getOrderItemId() {
        return orderItemId;
    }

    /* Gets the ID of the order this line item belongs to */
    public int getOrderId() {
        return orderId;
    }

    /* Gets the ID of the purchased product */
    public int getProductId() {
        return productId;
    }

    /* Gets the purchased quantity */
    public int getQuantity() {
        return quantity;
    }

    /* Gets the unit price charged at the time of the order */
    public double getUnitPrice() {
        return unitPrice;
    }

    /* Sets this line item's own ID */
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    /* Sets the ID of the order this line item belongs to */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /* Sets the ID of the purchased product */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /* Sets the purchased quantity */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* Sets the unit price charged at the time of the order */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" + "orderItemId=" + orderItemId + ", orderId=" + orderId + ", productId=" + productId + ", quantity=" + quantity + ", unitPrice=" + unitPrice + "}";
    }

}
