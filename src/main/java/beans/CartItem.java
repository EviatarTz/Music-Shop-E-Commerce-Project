package beans;

public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private int quantity;

    public CartItem() {}

    public CartItem(int cartItemId, int cartId, int productId, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" + "cartItemId=" + cartItemId + ", cartId=" + cartId + ", productId=" + productId + ", quantity=" + quantity + "}";
    }
}
