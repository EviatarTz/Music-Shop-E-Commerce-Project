package managedbeans;

import beans.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import service.ProductService;

import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Backing bean for the search box and the search-results page
 * (searchResults.xhtml).
 */
@Named("searchBean")
@RequestScoped
public class SearchBean implements Serializable {

    private final ProductService productService = new ProductService();

    private String keyword;
    private List<Product> results = Collections.emptyList();

    /* Builds the redirect URL (with the URL-encoded keyword) to the search-results page */
    public String search() {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        return "/searchResults.xhtml?faces-redirect=true&keyword=" + encoded;
    }

    /* Runs the search for the current keyword, populating 'results' */
    public void loadResults() {
        if (keyword != null && !keyword.isBlank()) {
            results = productService.searchProducts(keyword);
        }
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public List<Product> getResults() {
        return results != null ? results : Collections.emptyList();
    }
}
