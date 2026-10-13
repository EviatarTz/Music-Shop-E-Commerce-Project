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

        /* Creates a list of cart Items from a specific user's cart */
        List<CartItem> cartItems = cartDAO.getCartItems(cartId);

        /* If the cart is empty */
        if (cartItems.isEmpty()) {
            return -1;
        }

        /* If not, create a variable for holding the total price for the cart items */
        double totalPrice = 0;

        /* For each item in the cart, check if it has enough in stock after reducing the quantity and if so,
        *  Add the price of that item multiplied by it's quantity to the total sum of the cart.
        *  If there's not enough in the stock, return -1: the purchase cannot be done (out of stock item) */
        for (CartItem item : cartItems) {
            if (!inventoryDAO.hasEnoughStock(item.getProductId(), item.getQuantity())) {
                return -1;
            }
            Product product = productDAO.getProductById(item.getProductId());
            totalPrice += product.getPrice() * item.getQuantity();
        }

        /* If the calculation phase completed, create an order and add to it  the user who made the purchase, the total price and the date of order */
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));

        /* Add the order to the database.
        *  If the order was created successfully in the database, the id of that order is then returned. */
        int orderId = orderDAO.createOrder(order);

        /* If the order cannot be created, return -1 */
        if (orderId == -1) {
            return -1;
        }

        /* Create an order item for each item in the cart. every item is a product */
        for (CartItem item : cartItems) {

            /* Get the product that is the item in the cart */
            Product product = productDAO.getProductById(item.getProductId());

            /* Create an empty order item */
            OrderItem orderItem = new OrderItem();

            /* Set the order item's information */
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            /* Insert the order item to the database as the created order's item */
            orderItemDAO.addOrderItem(orderItem);

            /* Update the quantity of the item in the database */
            inventoryDAO.reduceStock(item.getProductId(), item.getQuantity());

        }

        /* Clear the cart of all it's items */
        cartDAO.clearCart(cartId);

        /* Return the order id created */
        return orderId;

    }

    /* Fetches a single order by its ID */
    public Order getOrderById(int orderId) {

        /* Gets the order by it's ID from the database (if exists) */
        return orderDAO.getOrderById(orderId);
    }

    /* Fetches every order placed by a given user */
    public List<Order> getOrdersByUser(int userId) {

        /* Returns a list of all the orders of a specific user from the database */
        return orderDAO.getOrdersByUser(userId);
    }

    /* Fetches every line item that belongs to a given order */
    public List<OrderItem> getOrderItems(int orderId) {

        /* Returns a list of all the order items of a specific order */
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }

}
