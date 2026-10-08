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

@Named("ordersBean")
@RequestScoped
public class OrdersBean implements Serializable {

    private final OrderService orderService = new OrderService();

    private List<Order> orders = new ArrayList<>();

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