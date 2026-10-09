package dao;

import beans.Product;
import util.sql.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql); ResultSet res = statement.executeQuery()){
            while(res.next()){
                Product product = new Product();
                product.setProductId(res.getInt("product_id"));
                product.setProductName(res.getString("product_name"));
                product.setDescription(res.getString("description"));
                product.setPrice(res.getDouble("price"));
                product.setCategoryId(res.getInt("category_id"));

                products.add(product);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int id){
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet res = statement.executeQuery();
            if (res.next()){
                Product product = new Product();
                product.setProductId(res.getInt("product_id"));
                product.setProductName(res.getString("product_name"));
                product.setDescription(res.getString("description"));
                product.setPrice(res.getDouble("price"));
                product.setCategoryId(res.getInt("category_id"));
                return product;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean addProduct(Product product) {
        String sql = """
            INSERT INTO products(product_name, description, price, category_id)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getCategoryId());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        String sql = """
            UPDATE products
            SET product_name = ?, description = ?, price = ?, category_id = ?
            WHERE product_id = ?
            """;
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getCategoryId());
            statement.setInt(5, product.getProductId());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public List<Product> getProductsByCategory(int categoryId) {

        List<Product> products = new ArrayList<>();

        String sql = """
        SELECT *
        FROM products
        WHERE category_id = ?
        """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> getProductsByIdRange(int minId, int maxId) {

        List<Product> products = new ArrayList<>();

        String sql = """
        SELECT *
        FROM products
        WHERE product_id BETWEEN ? AND ?
        ORDER BY product_id
        """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minId);
            stmt.setInt(2, maxId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = """
        SELECT *
        FROM products
        WHERE product_name ILIKE ? OR description ILIKE ?
        ORDER BY product_name
        """;

        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("category_id"));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
