package service;


import beans.CartItem;
import beans.Order;
import beans.OrderItem;
import beans.Product;
import dao.*;
import java.sql.Timestamp;
import java.util.List;

public class OrderService {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public boolean checkout(int cartId, int userId) {

        List<CartItem> cartItems = cartDAO.getCartItems(cartId);

        if (cartItems.isEmpty()) {
            return false;
        }

        double totalPrice = 0;


        for (CartItem item : cartItems) {
            if (!inventoryDAO.hasEnoughStock(item.getProductId(), item.getQuantity())) {
                return false;
            }
            Product product = productDAO.getProductById(item.getProductId());
            totalPrice += product.getPrice() * item.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));

        int orderId = orderDAO.createOrder(order);

        if (orderId == -1) {
            return false;
        }

        for (CartItem item : cartItems) {
            Product product = productDAO.getProductById(item.getProductId());


            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItemDAO.addOrderItem(orderItem);
            inventoryDAO.reduceStock(item.getProductId(), item.getQuantity());

        }

        cartDAO.clearCart(cartId);

        return true;

    }

    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }

}
