package beans;

/**
 * Represents a row in the "products" table.
 */
public class Product {

    private int productId;
    private String productName;
    private String description;
    private double price;
    private int categoryId;

    /* An empty constructor */
    public Product(){}

    /* A parameterized constructor */
    public Product(int productId,String productName,String description,double price,int categoryId){
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    /* Gets the ID of the product */
    public int getProductId(){
        return this.productId;
    }

    /* Gets the name of the product */
    public String getProductName(){
        return this.productName;
    }

    /* Gets the description of the product */
    public String getDescription(){
        return this.description;
    }

    /* Gets the price of the product */
    public double getPrice(){
        return this.price;
    }

    /* Gets the category of the product */
    public int getCategoryId(){
        return this.categoryId;
    }

    /* Sets the ID of the product */
    public void setProductId(int prodId){
        this.productId = prodId;
    }

    /* Sets the name of the product */
    public void setProductName(String prodName){
        this.productName = prodName;
    }

    /* Sets the Description of the product */
    public void setDescription(String desc){
        this.description = desc;
    }

    /* Sets the Price of the product */
    public void setPrice(double prodPrice){
        this.price = prodPrice;
    }

    /* Sets the Category of the product */
    public void setCategoryId(int catId){
        this.categoryId = catId;
    }

    @Override
    public String toString(){
        return "Product = " + this.productId + "Product Name = " + this.productName + "Product Price = " + this.price;
    }
}
