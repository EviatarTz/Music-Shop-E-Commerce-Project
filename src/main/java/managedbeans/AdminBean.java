package managedbeans;

import beans.Product;
import beans.User;
import service.ProductService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Backing bean for the admin panel (admin.xhtml): lists products and
 * handles adding, editing and deleting them, including saving an uploaded
 * product image to disk.
 */
@Named("adminBean")
@RequestScoped
public class AdminBean implements Serializable {

    /*
        Base path for product images. Defaults to the project's source directory (not the deploy
        folder), so uploaded images survive mvn clean package / redeploy.
        This path can be overridden without recompiling, by passing a JVM argument to Tomcat:
        -Dmusic.shop.images.path=/path/to/other/images/folder
        (e.g. in Tomcat's bin/setenv.sh / bin/setenv.bat). If the argument isn't provided, the
        default path configured here is used (matches the current development machine).
     */
    private static final String IMAGES_BASE_PATH =
            System.getProperty("music.shop.images.path",
                    "/Users/eviatar_tzabari/IdeaProjects/music-shop/src/main/webapp/images");


    private final ProductService productService = new ProductService();

    @Inject
    private CategoryBean categoryBean;

    private List<Product> products;
    private boolean loaded = false;

    /* Fields for the "add new product" form */
    private String newProductName;
    private String newDescription;
    private double newPrice;
    private int newCategoryId;
    private UploadedFile newImage;

    /* Working copy used by the edit dialog - we don't mutate the object from the list directly */
    private Product editProduct = new Product();
    private UploadedFile editImage;

    /* Redirects away if the current session user is not a logged-in Administrator */
    public void checkAdminAccess() {

        User user = getLoggedInUser();
        /*If the current user's roe is guest (not specified as a role so the user is null) */
        if (user == null) {
            redirect("login.xhtml");
            return;
        }
        /* If the logged user is not an administrator */
        if (!"Administrator".equals(user.getRole())) {
            redirect("products.xhtml");
        }
    }

    /* Lazily loads the product list once per request */
    private void ensureLoaded() {
        /* If the products list was not loaded yet, load it and change the state of the boolean flag to true
        *  next time the boolean flag checked, the value inside the if statement will be false (already loaded)   */
        if (!loaded) {
            products = productService.getAllProducts();
            loaded = true;
        }
    }

    /* Gets all the products as a list of products if they weren't loaded yet */
    public List<Product> getProducts() {
        ensureLoaded();
        return products;
    }

    /* Handles the "add new product" form submission (only as an administrator) */
    public void addProduct() {
        /* only an administrator can add products to the inventory */
        if (!isAdmin()) {
            return;
        }
        /* if an invalid category number was entered - alert an error back to the user */
        if (newCategoryId <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש לבחור תת-קטגוריה", null));
            return;
        }

        /* create a new product */
        Product product = new Product();

        product.setProductName(newProductName);
        product.setDescription(newDescription);
        product.setPrice(newPrice);
        product.setCategoryId(newCategoryId);

        boolean success = productService.addProduct(product);

        /* if the product was added successfully */
        if (success) {
            /* save the image of that product along with the name and the category */
            saveImage(newImage, newProductName, newCategoryId);

            /* alert a message to the user */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר נוסף בהצלחה", null));

            /* reset all the variables */
            newProductName = null;
            newDescription = null;
            newPrice = 0;
            newCategoryId = 0;
            newImage = null;
            loaded = false;
        }
        /* In case of a failure with the addition of the product */
        else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "הוספת המוצר נכשלה", null));
        }
    }

    /* Populates the edit dialog with a working copy of the selected product */
    public void openEditDialog(Product product) {

        Product copy = new Product();

        copy.setProductId(product.getProductId());
        copy.setProductName(product.getProductName());
        copy.setDescription(product.getDescription());
        copy.setPrice(product.getPrice());
        copy.setCategoryId(product.getCategoryId());

        editProduct = copy;
        editImage = null;
    }

    /* Saves the changes made in the edit dialog */
    public void saveEdit() {
        /* only an admin or an edited product can save the new product and be saved, respectively */
        if (!isAdmin() || editProduct == null) {
            return;
        }
        /* if the category is invalid for the for that product - alert a corresponding message */
        if (editProduct.getCategoryId() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש לבחור תת-קטגוריה", null));
            return;
        }

        boolean success = productService.updateProduct(editProduct);

        if (success) {
            /* save the image of the product along with the edited product's name and category */
            saveImage(editImage, editProduct.getProductName(), editProduct.getCategoryId());
            editImage = null;

            /* display a message to the user */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר עודכן בהצלחה", null));
            loaded = false;
        } else {
            /* failure in the addition of the product - alert a message to the user */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "עדכון המוצר נכשל", null));
        }
    }

    /* Deletes a product by its ID */
    public void deleteProduct(int productId) {

        /* only an admin can delete products */
        if (!isAdmin()) {
            return;
        }

        boolean success = productService.deleteProduct(productId);

        if (success) {
            /* display a corresponding message to the user */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר נמחק", null));
            loaded = false;
        } else {
            /* alert a corresponding message to the user */
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "מחיקת המוצר נכשלה", null));
        }
    }

    /*
        Saves the uploaded image file directly to the source tree (src/main/webapp/images),
        under the matching subcategory folder, named after the product (with a .webp extension).
        Requires the admin to upload a .webp file, since the whole site expects images with
        that extension.
    */
    private void saveImage(UploadedFile file, String productName, int categoryId) {

        /* Exit immediately if no file was uploaded or the uploaded file is empty */
        if (file == null || file.getSize() <= 0) {
            return;
        }
        /* Resolve the image subfolder that corresponds to the selected category */
        String folder = categoryBean.getImageFolder(categoryId);
        /* If no folder mapping exists for this category, notify the user and stop */
        if (folder.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "המוצר נשמר, אך לא נמצאה תיקיית תמונות מתאימה לקטגוריה שנבחרה", null));
            return;
        }

        /* Sanitize the product name so it can be safely used as a file name */
        String safeName = productName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");

        /* Build the target directory and final .webp file path */
        Path targetDir = Paths.get(IMAGES_BASE_PATH, folder.split("/"));
        Path targetFile = targetDir.resolve(safeName + ".webp");


        try {
            /* Create any missing directories in the target path */
            Files.createDirectories(targetDir);

            try (InputStream in = file.getInputStream()) {
                /* Copy the uploaded image to the destination, replacing any existing file */
                Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            /* Log the error and inform the user that image saving failed */
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "המוצר נשמר, אך שמירת התמונה נכשלה", null));
        }
    }

    /* Returns whether the current session user is a logged-in Administrator */
    private boolean isAdmin() {
        /* Retrieve the currently logged-in user and verify administrator */
        User user = getLoggedInUser();
        return user != null && "Administrator".equals(user.getRole());
    }

    private User getLoggedInUser() {
        /* Return the user object stored in the current HTTP session */
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    private void redirect(String page) {
        /* Perform a JSF redirect to the specified page */
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Returns the name entered for the new product */
    public String getNewProductName() {
        return newProductName;
    }

    /* Returns the description entered for the new product */
    public String getNewDescription() {
        return newDescription;
    }

    /* Returns the price entered for the new product */
    public double getNewPrice() {
        return newPrice;
    }

    /* Returns the category entered for the new product */
    public int getNewCategoryId() {
        return newCategoryId;
    }

    /* Returns the Image entered for the product */
    public UploadedFile getNewImage() {
        return newImage;
    }

    /* Returns the edited product */
    public Product getEditProduct() {
        return editProduct;
    }

    /* Returns the edited image for the product */
    public UploadedFile getEditImage() {
        return editImage;
    }

    /* Updates the name entered for the new product */
    public void setNewProductName(String newProductName) {
        this.newProductName = newProductName;
    }

    /* Updates the description entered for the new product */
    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    /* Updates the price entered for the new product */
    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    /* Updates the category entered for the new product */
    public void setNewCategoryId(int newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    /* Updates the image entered for the product */
    public void setNewImage(UploadedFile newImage) {
        this.newImage = newImage;
    }

    /* Updates the edited product */
    public void setEditProduct(Product editProduct) {
        this.editProduct = editProduct;
    }
    /* Updates the edited image for the product */
    public void setEditImage(UploadedFile editImage) {
        this.editImage = editImage;
    }

}