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

@Named("adminBean")
@RequestScoped
public class AdminBean implements Serializable {

    // תיקיית המקור של הפרויקט - כדי שהתמונות ישרדו rebuild, לא כותבים לתיקיית ה-deploy
    private static final String IMAGES_BASE_PATH =
            System.getProperty("music.shop.images.path",
                    "/Users/eviatar_tzabari/IdeaProjects/music-shop/src/main/webapp/images");

    private final ProductService productService = new ProductService();

    @Inject
    private CategoryBean categoryBean;

    private List<Product> products;
    private boolean loaded = false;

    // שדות טופס "הוספת מוצר חדש"
    private String newProductName;
    private String newDescription;
    private double newPrice;
    private int newCategoryId;
    private UploadedFile newImage;

    // עותק עבודה לדיאלוג העריכה - לא נוגעים ישירות באובייקט מתוך הרשימה
    private Product editProduct = new Product();
    private UploadedFile editImage;

    public void checkAdminAccess() {
        User user = getLoggedInUser();
        if (user == null) {
            redirect("login.xhtml");
            return;
        }
        if (!"Administrator".equals(user.getRole())) {
            redirect("products.xhtml");
        }
    }

    private void ensureLoaded() {
        if (!loaded) {
            products = productService.getAllProducts();
            loaded = true;
        }
    }

    public List<Product> getProducts() {
        ensureLoaded();
        return products;
    }

    public void addProduct() {
        if (!isAdmin()) {
            return;
        }
        if (newCategoryId <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש לבחור תת-קטגוריה", null));
            return;
        }

        Product product = new Product();
        product.setProductName(newProductName);
        product.setDescription(newDescription);
        product.setPrice(newPrice);
        product.setCategoryId(newCategoryId);

        boolean success = productService.addProduct(product);

        if (success) {
            saveImage(newImage, newProductName, newCategoryId);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר נוסף בהצלחה", null));
            newProductName = null;
            newDescription = null;
            newPrice = 0;
            newCategoryId = 0;
            newImage = null;
            loaded = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "הוספת המוצר נכשלה", null));
        }
    }

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

    public void saveEdit() {
        if (!isAdmin() || editProduct == null) {
            return;
        }
        if (editProduct.getCategoryId() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "יש לבחור תת-קטגוריה", null));
            return;
        }

        boolean success = productService.updateProduct(editProduct);

        if (success) {
            saveImage(editImage, editProduct.getProductName(), editProduct.getCategoryId());
            editImage = null;

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר עודכן בהצלחה", null));
            loaded = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "עדכון המוצר נכשל", null));
        }
    }

    public void deleteProduct(int productId) {
        if (!isAdmin()) {
            return;
        }

        boolean success = productService.deleteProduct(productId);

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "המוצר נמחק", null));
            loaded = false;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "מחיקת המוצר נכשלה", null));
        }
    }

    /**
     * שומר את קובץ התמונה שהועלה ישירות לתיקיית המקור (src/main/webapp/images),
     * בתיקיית תת-הקטגוריה המתאימה, ותחת שם הקובץ = שם המוצר (עם סיומת .webp).
     * דורש שהאדמין יעלה קובץ בפורמט webp, כי כל האתר מצפה לתמונות עם הסיומת הזו.
     */
    private void saveImage(UploadedFile file, String productName, int categoryId) {
        if (file == null || file.getSize() <= 0) {
            return;
        }

        String folder = categoryBean.getImageFolder(categoryId);
        if (folder.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "המוצר נשמר, אך לא נמצאה תיקיית תמונות מתאימה לקטגוריה שנבחרה", null));
            return;
        }

        String safeName = productName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
        Path targetDir = Paths.get(IMAGES_BASE_PATH, folder.split("/"));
        Path targetFile = targetDir.resolve(safeName + ".webp");

        try {
            Files.createDirectories(targetDir);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "המוצר נשמר, אך שמירת התמונה נכשלה", null));
        }
    }

    private boolean isAdmin() {
        User user = getLoggedInUser();
        return user != null && "Administrator".equals(user.getRole());
    }

    private User getLoggedInUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    private void redirect(String page) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNewProductName() { return newProductName; }
    public void setNewProductName(String newProductName) { this.newProductName = newProductName; }
    public String getNewDescription() { return newDescription; }
    public void setNewDescription(String newDescription) { this.newDescription = newDescription; }
    public double getNewPrice() { return newPrice; }
    public void setNewPrice(double newPrice) { this.newPrice = newPrice; }
    public int getNewCategoryId() { return newCategoryId; }
    public void setNewCategoryId(int newCategoryId) { this.newCategoryId = newCategoryId; }
    public UploadedFile getNewImage() { return newImage; }
    public void setNewImage(UploadedFile newImage) { this.newImage = newImage; }
    public Product getEditProduct() { return editProduct; }
    public void setEditProduct(Product editProduct) { this.editProduct = editProduct; }
    public UploadedFile getEditImage() { return editImage; }
    public void setEditImage(UploadedFile editImage) { this.editImage = editImage; }
}