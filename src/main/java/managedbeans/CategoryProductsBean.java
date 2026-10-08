package managedbeans;

import beans.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import service.ProductService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("categoryProductsBean")
@RequestScoped
public class CategoryProductsBean implements Serializable {

    private final ProductService productService = new ProductService();

    private int categoryId;
    private List<Product> products = Collections.emptyList();

    public void loadProducts() {
        products = productService.getProductsByCategory(categoryId);
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public List<Product> getProducts() {
        if (products == null) {
            loadProducts();
        }
        return products;
    }

}