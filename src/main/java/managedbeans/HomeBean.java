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

    /* Service used to retrieve product information */
    private final ProductService productService = new ProductService();

    /* Service used for cart operations */
    private final CartService cartService = new CartService();

    /* Products displayed in the featured products section */
    private List<Product> featuredProducts;

    /* Picks the first 6 products (or fewer, if the catalog is smaller) to feature */
    @PostConstruct
    public void init() {

        /* Retrieve all available products */
        List<Product> all = productService.getAllProducts();

        /* Keep only the first six products, or all if fewer exist */
        featuredProducts = (all.size() > 6) ? all.subList(0, 6) : all;
    }

    /* Returns the featured products */
    public List<Product> getFeaturedProducts() {
        /* Return the featured product list */
        return featuredProducts;
    }

    /* Adds a product to the logged-in user's cart; shows an error if no one is logged in */
    public String addToCart(int productId) {

        /* Ignore invalid product IDs */
        if (productId <= 0) {
            return null;
        }

        /* Get the logged-in user from the session */
        User user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

        /* Require login before adding products to the cart */
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "יש להתחבר",
                            "עליך להתחבר כדי להוסיף מוצרים לעגלה"));
            return null;
        }

        /* Get or create the user's cart */
        int cartId = cartService.getOrCreateCartId(user.getUserId());

        /* Add one unit of the selected product */
        cartService.addProductToCart(cartId, productId, 1);

        /* Notify the user of success */
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart", null));
        return null;
    }

    /* Invalidates the session and navigates back to the login page  (logouts the user) */
    public String logout() {

        /* Clear the current session */
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        /* Redirect to the login page */
        return "login?faces-redirect=true";
    }
}
