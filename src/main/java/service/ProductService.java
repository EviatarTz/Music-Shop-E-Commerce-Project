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

        /* Returns a list of all the products */
        return productDAO.getAllProducts();
    }

    /* Fetches a single product by its ID */
    public Product getProductById(int id){

        /* Gets a product by searching for it's ID in the database */
        return productDAO.getProductById(id);
    }

    /* Fetches every product in a given (sub)category */
    public List<Product> getProductsByCategory(int categoryId) {

        /* Gets a list of all products of a specific category */
        return productDAO.getProductsByCategory(categoryId);
    }

    /* Adds a new product */
    public boolean addProduct(Product product) {

        /* adds the product to the database */
        return productDAO.addProduct(product);
    }

    /* Updates an existing product */
    public boolean updateProduct(Product product) {

        /* Updates the product's data in the database */
        return productDAO.updateProduct(product);
    }

    /* Deletes a product by its ID */
    public boolean deleteProduct(int id) {

        /* Return True after a user deleted successfully from the database */
        return productDAO.deleteProduct(id);
    }

    /* Fetches every product whose ID falls within the given range */
    public List<Product> getProductsByIdRange(int minId, int maxId) {
        /* Returns a list of products whose ID falls between a given product IDs range in the database */
        return productDAO.getProductsByIdRange(minId, maxId);
    }

    /* Searches products by a free-text keyword */
    public List<Product> searchProducts(String keyword) {

        /* Returns a list of all products that have the same keyword as a part of their name in the database */
        return productDAO.searchProducts(keyword);
    }
}
