package beans;

/**
 * A class for representing a single Cart Item attached to a specific cart.
 * Every Cart Item has an ID (internal id inside the cart), a cart ID (the cart that contains the item),
 * A product ID (that specifies the Item) and the quantity of that item inside the cart.
 */
public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private int quantity;

    /* An empty constructor */
    public CartItem() {}

    /* A parameterized constructor */
    public CartItem(int cartItemId, int cartId, int productId, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /* Gets the Item ID inside the cart */
    public int getCartItemId() {
        return cartItemId;
    }

    /* Gets the cart ID */
    public int getCartId() {
        return cartId;
    }

    /* Gets the product id the item represents */
    public int getProductId() {
        return productId;
    }

    /* Gets the quantity of the item inside the cart */
    public int getQuantity() {
        return quantity;
    }

    /* Sets the item id */
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    /* Sets the cart id */
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    /* Sets the product id  of the item inside the cart */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /* Sets the quantity of the item inside the Cart */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* A String representation for the cart ID */
    @Override
    public String toString() {
        return "CartItem{" + "cartItemId=" + cartItemId + ", cartId=" + cartId + ", productId=" + productId + ", quantity=" + quantity + "}";
    }
}
