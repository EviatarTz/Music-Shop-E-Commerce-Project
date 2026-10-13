package service;

import beans.CartItem;
import beans.Order;
import beans.OrderItem;
import beans.Product;
import dao.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Business logic for turning a cart into an order: validates stock,
 * computes the total price, creates the order and its line items,
 * reduces inventory, and clears the cart.
 */
public class OrderService {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /*
      Performs the full checkout flow for the given cart/user:
      1. Loads the cart's line items (fails if the cart is empty).
      2. Verifies there's enough stock for every item and computes the total price.
      3. Creates the order row.
      4. Creates each order line item and reduces the corresponding stock.
      5. Clears the cart.
      Returns the new order's ID on success, or -1 on any failure (empty cart,
      insufficient stock, or the order row failing to be created).

      Note: the stock check (step 2) and the stock reduction (step 4) are not
      wrapped in a single database transaction, so under concurrent checkouts
      for the same product there's a theoretical race condition between the
      check and the reduction. Not an issue for normal single-user testing.
    */
    public int checkout(int cartId, int userId) {

        List<CartItem> cartItems = cartDAO.getCartItems(cartId);

        if (cartItems.isEmpty()) {
            return -1;
        }

        double totalPrice = 0;


        for (CartItem item : cartItems) {
            if (!inventoryDAO.hasEnoughStock(item.getProductId(), item.getQuantity())) {
                return -1;
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
            return -1;
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

        return orderId;

    }

    /* Fetches a single order by its ID */
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    /* Fetches every order placed by a given user */
    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    /* Fetches every line item that belongs to a given order */
    public List<OrderItem> getOrderItems(int orderId) {
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }

}
