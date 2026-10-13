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

    private final ProductService productService = new ProductService();

    private int productId;
    private Product product;

    /* Loads the product for the current productId */
    public void loadProduct() {
        product = productService.getProductById(productId);
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public Product getProduct() {
        if (product == null && productId > 0) {
            loadProduct();
        }
        return product;
    }

}
