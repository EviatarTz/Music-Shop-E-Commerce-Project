package managedbeans;

import beans.Order;
import beans.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import service.OrderService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Backing bean for the order-history page (orders.xhtml): lists the
 * logged-in user's past orders, newest first.
 */
@Named("ordersBean")
@RequestScoped
public class OrdersBean implements Serializable {

    private final OrderService orderService = new OrderService();

    private List<Order> orders = new ArrayList<>();

    /* Redirects to login if no user is logged in, otherwise loads their orders (newest first) */
    public void checkLoginAndLoad() {
        User user = getLoggedInUser();
        if (user == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        orders = new ArrayList<>(orderService.getOrdersByUser(user.getUserId()));
        orders.sort(Comparator.comparingInt(Order::getOrderId).reversed());
    }

    private User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    public List<Order> getOrders() { return orders; }
}
