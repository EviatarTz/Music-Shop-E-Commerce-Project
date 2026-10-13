package dao;

import beans.Product;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access class for the "products" table.
 */
public class ProductDAO {

    /* Fetches every product in the catalog */
    public List<Product> getAllProducts(){

        /* Creates a list of Products */
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql); ResultSet res = statement.executeQuery()){

            /* While there are still rows left (the results from the query) */
            while(res.next()){

                /* Creates a new product object */
                Product product = new Product();

                product.setProductId(res.getInt("product_id"));
                product.setProductName(res.getString("product_name"));
                product.setDescription(res.getString("description"));
                product.setPrice(res.getDouble("price"));
                product.setCategoryId(res.getInt("category_id"));

                /* add the prepared product object to the products list */
                products.add(product);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        /* Returns the products list */
        return products;
    }

    /* Fetches a single product by its ID, or null if not found */
    public Product getProductById(int id){

        String sql = "SELECT * FROM products WHERE product_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1,id);

            /* Saves the rows returned by the executing the statement */
            ResultSet res = statement.executeQuery();

            /* If a resulted row exists (if the id found in the database) */
            if (res.next()){

                /* Creates a new product object */
                Product product = new Product();

                product.setProductId(res.getInt("product_id"));
                product.setProductName(res.getString("product_name"));
                product.setDescription(res.getString("description"));
                product.setPrice(res.getDouble("price"));
                product.setCategoryId(res.getInt("category_id"));

                /* Returns the product created with the information from the resulted row */
                return product;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        /* If the connection to the database has failed */
        return null;
    }

    /* Inserts a new product */
    public boolean addProduct(Product product) {

        String sql = """
            INSERT INTO products(product_name, description, price, category_id)
            VALUES (?, ?, ?, ?)
            """;

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getCategoryId());

            /* Returns true if the row was created in the database */
            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns false if the connection to the database has failed */
        return false;
    }

    /* Updates every field of an existing product, matched by its product ID */
    public boolean updateProduct(Product product) {

        String sql = """
            UPDATE products
            SET product_name = ?, description = ?, price = ?, category_id = ?
            WHERE product_id = ?
            """;

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getCategoryId());
            statement.setInt(5, product.getProductId());

            /* Returns true if the row was created in the database */
            return statement.executeUpdate() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /* Returns false if the connection to the database has failed */
        return false;
    }

    /* Deletes a product by its ID */
    public boolean deleteProduct(int id) {

        String sql = "DELETE FROM products WHERE product_id = ?";

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection();

             /* Creates a prepared statement that contains the query along with the id of the product to delete */
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            /* Returns true if the product was deleted in the database */
            return statement.executeUpdate() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /* Returns false if the connection to the database has failed */
        return false;
    }

    /* Fetches every product that belongs to a given (sub)category */
    public List<Product> getProductsByCategory(int categoryId) {

        /* Creates a products list */
        List<Product> products = new ArrayList<>();

        String sql = """
        SELECT *
        FROM products
        WHERE category_id = ?
        """;

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            /* Execute the query and save the resulted rows in the Results set */
            ResultSet rs = stmt.executeQuery();

            /* While there are rows (products) left */
            while (rs.next()) {

                /* Creates a new product */
                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));

                /* Adds the product to the products list */
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Returns the products list */
        return products;
    }

    /* Fetches every product whose ID falls within the given range (inclusive), ordered by ID */
    public List<Product> getProductsByIdRange(int minId, int maxId) {

        /* Creates a products list */
        List<Product> products = new ArrayList<>();

        String sql = """
        SELECT *
        FROM products
        WHERE product_id BETWEEN ? AND ?
        ORDER BY product_id
        """;

        /* Tries to connect to the database and creates a prepared statement */
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);

            /* Execute the query and save the resulted rows in the Results set */
            ResultSet rs = stmt.executeQuery();

            /* While there are rows (products) left */
            while (rs.next()) {

                /* Creates a new product */
                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));

                /* Adds the product to the products list */
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Returns the products list */
        return products;
    }

    /* Searches products whose name or description contains the given keyword (case-insensitive) */
    public List<Product> searchProducts(String keyword) {

        /* Creates a products list */
        List<Product> products = new ArrayList<>();

        String sql = """
        SELECT *
        FROM products
        WHERE product_name ILIKE ? OR description ILIKE ?
        ORDER BY product_name
        """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            /* Creates a string pattern for the query. the pattern in SQL starts and ends with '%' character */
            String pattern = "%" + keyword + "%";

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            /* Execute the query and save the resulted rows in the Results set */
            ResultSet rs = stmt.executeQuery();

            /* While there are rows (products) left */
            while (rs.next()) {

                /* Creates a new product */
                Product product = new Product();

                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));

                /* Adds the product to the products list */
                products.add(product);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /* Returns the products list */
        return products;
    }
}
