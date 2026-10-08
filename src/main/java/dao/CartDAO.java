
package dao;

import beans.CartItem;
import java.sql.Connection;
import util.sql.ConnectionManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


public class CartDAO {

    public boolean addProductToCart(int cartId,int productId,int quantity){
        String sql = """
                    INSERT INTO cart_items(cart_id,product_id,quantity)
                    VALUES (?, ?, ?)
                """;
        try(Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, cartId);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeProductFromCart(int cartItemId){
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1,cartItemId);
            return statement.executeUpdate() > 0;

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<CartItem> getCartItems(int cartId){
        List<CartItem> items = new ArrayList<>();

        String sql = "SELECT * FROM cart_items WHERE cart_id = ?";

        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, cartId);
            ResultSet res = statement.executeQuery();
            while(res.next()){
                CartItem item = new CartItem();
                item.setCartItemId(res.getInt("cart_item_id"));
                item.setCartId(res.getInt("cart_id"));
                item.setProductId(res.getInt("product_id"));
                item.setQuantity(res.getInt("quantity"));
                items.add(item);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return items;
    }

    public int getOrCreateCartId(int userId) {

        String selectSql = "SELECT cart_id FROM cart WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection()) {

            try (PreparedStatement select = conn.prepareStatement(selectSql)) {
                select.setInt(1, userId);
                ResultSet res = select.executeQuery();
                if (res.next()) {
                    return res.getInt("cart_id");
                }
            }

            String insertSql = "INSERT INTO cart(user_id) VALUES (?)";
            try (PreparedStatement insert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insert.setInt(1, userId);
                insert.executeUpdate();
                ResultSet keys = insert.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean clearCart(int cartId){
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try(Connection conn = ConnectionManager.getConnection();PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, cartId);
            return statement.executeUpdate() >= 0;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean updateQuantity(int cartItemId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, quantity);
            statement.setInt(2, cartItemId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
