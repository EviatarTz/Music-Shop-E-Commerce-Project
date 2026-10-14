package managedbeans;

import beans.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import service.ProductService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Backing bean for the subcategory product-list page (categoryProducts.xhtml):
 * loads every product belonging to the subcategory given in the request.
 */
@Named("categoryProductsBean")
@RequestScoped
public class CategoryProductsBean implements Serializable {

    private final ProductService productService = new ProductService();

    private int categoryId;
    private List<Product> products = Collections.emptyList();

    /* Loads the products for the current categoryId */
    public void loadProducts() {
        /* Retrieve all products that match the selected category ID */
        products = productService.getProductsByCategory(categoryId);
    }

    /* Returns the currently selected category ID */
    public int getCategoryId() {
        /* Return the active category identifier */
        return categoryId;
    }
    /* Updates the currently selected category ID */
    public void setCategoryId(int categoryId) {
        /* Store the new category identifier */
        this.categoryId = categoryId;
    }

    /* Returns the products for the current category, loading them if needed */
    public List<Product> getProducts() {

        /* Lazily load products if they have not yet been initialized */
        if (products == null) {
            loadProducts();
        }

        /* Return the cached product list */
        return products;
    }

}
