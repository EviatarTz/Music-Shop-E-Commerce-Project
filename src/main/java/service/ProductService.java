package service;

import dao.ProductDAO;
import beans.Product;
import java.util.List;

/**
 * Business-logic layer on top of ProductDAO.
 */
public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    /* Fetches every product in the catalog */
    public List<Product> getAllProducts(){
        return productDAO.getAllProducts();
    }

    /* Fetches a single product by its ID */
    public Product getProductById(int id){
        return productDAO.getProductById(id);
    }

    /* Fetches every product in a given (sub)category */
    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    /* Adds a new product */
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    /* Updates an existing product */
    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    /* Deletes a product by its ID */
    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }

    /* Fetches every product whose ID falls within the given range */
    public List<Product> getProductsByIdRange(int minId, int maxId) {
        return productDAO.getProductsByIdRange(minId, maxId);
    }

    /* Searches products by a free-text keyword */
    public List<Product> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }
}
