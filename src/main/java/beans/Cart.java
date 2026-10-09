package beans;

/**
 * A Class for that represents a Cart object.
 * Every cart has an ID (cartId) and a user ID of which the cart belongs to.
 */
public class Cart {
    private int cartId;
    private int userId;

    /* An empty constructor */
    public Cart(){}

    /* A parameterized constructor */
    public Cart(int cartId,int userId){
        this.cartId = cartId;
        this.userId = userId;
    }

    /* Gets the ID of the cart */
    public int getCartId(){
        return cartId;
    }

    /* Gets the user ID the cart belongs to */
    public int getUserId(){
        return userId;
    }

    /* Sets the Cart ID */
    public void setCartId(int cartId){
        this.cartId = cartId;
    }

    /* Sets the user ID for the cart */
    public void setUserId(int userId){
        this.userId = userId;
    }

}
