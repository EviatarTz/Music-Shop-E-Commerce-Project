package beans;

public class Product {

    private int productId;
    private String productName;
    private String description;
    private double price;
    private int categoryId;

    public Product(){}

    public Product(int productId,String productName,String description,double price,int categoryId){
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    /* Getters */
    public int getProductId(){ return this.productId; }
    public String getProductName(){ return this.productName; }
    public String getDescription(){ return this.description; }
    public double getPrice(){ return this.price; }
    public int getCategoryId(){ return this.categoryId; }

    /* Setters */
    public void setProductId(int prodId){ this.productId = prodId; }
    public void setProductName(String prodName){ this.productName = prodName; }
    public void setDescription(String desc){ this.description = desc; }
    public void setPrice(double prodPrice){ this.price = prodPrice; }
    public void setCategoryId(int catId){ this.categoryId = catId; }


    @Override
    public String toString(){
        return "Product = " + this.productId + "Product Name = " + this.productName + "Product Price = " + this.price;
    }
}
