package managedbeans;

import beans.Product;
import beans.User;
import service.ProductService;
import service.CartService;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 * Backing bean for the home page (home.xhtml): shows a short list of
 * featured products and handles the "add to cart" action from the home page.
 */
@Named("homeBean")
@RequestScoped
public class HomeBean implements Serializable {

    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();

    private List<Product> featuredProducts;

    /* Picks the first 6 products (or fewer, if the catalog is smaller) to feature */
    @PostConstruct
    public void init() {
        List<Product> all = productService.getAllProducts();
        featuredProducts = (all.size() > 6) ? all.subList(0, 6) : all;
    }

    public List<Product> getFeaturedProducts() {
        return featuredProducts;
    }

    /* Adds a product to the logged-in user's cart; shows an error if no one is logged in */
    public String addToCart(int productId) {
        if (productId <= 0) {
            return null;
        }

        User user = (User) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("user");

        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "יש להתחבר",
                            "עליך להתחבר כדי להוסיף מוצרים לעגלה"));
            return null;
        }

        int cartId = cartService.getOrCreateCartId(user.getUserId());
        cartService.addProductToCart(cartId, productId, 1);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart", null));
        return null;
    }

    /* Invalidates the session and navigates back to the login page */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }
}
