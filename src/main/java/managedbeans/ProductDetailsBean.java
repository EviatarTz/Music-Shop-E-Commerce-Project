package managedbeans;

import beans.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import service.ProductService;

import java.io.Serializable;

/**
 * Backing bean for the product-details page (productDetails.xhtml): loads
 * the single product given by the productId passed in the request.
 */
@Named("productDetailsBean")
@RequestScoped
public class ProductDetailsBean implements Serializable {

    /* Service used to retrieve product information */
    private final ProductService productService = new ProductService();

    /* ID of the product being displayed */
    private int productId;

    /* The loaded product details */
    private Product product;

    /* Loads the product for the current productId */
    public void loadProduct() {

        /* Retrieve the product by its ID */
        product = productService.getProductById(productId);
    }

    /* Returns the current product ID */
    public int getProductId() {
        return productId;
    }

    /* Updates the current product ID */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /* Returns the current product */
    public Product getProduct() {

        /* Load the product if it has not been loaded yet */
        if (product == null && productId > 0) {
            loadProduct();
        }
        return product;
    }

}
