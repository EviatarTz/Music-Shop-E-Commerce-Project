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

    /* Service used to search and retrieve products */
    private final ProductService productService = new ProductService();

    /* Search keyword entered by the user */
    private String keyword;

    /* Products matching the search keyword */
    private List<Product> results = Collections.emptyList();

    /* Builds the redirect URL (with the URL-encoded keyword) to the search-results page */
    public String search() {

        /* Ignore empty searches */
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        /* Encode the keyword for use in the URL */
        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        /* Redirect to the search results page */
        return "/searchResults.xhtml?faces-redirect=true&keyword=" + encoded;
    }

    /* Runs the search for the current keyword, populating 'results' */
    public void loadResults() {

        /* Run the search if a keyword was provided */
        if (keyword != null && !keyword.isBlank()) {
            results = productService.searchProducts(keyword);
        }
    }

    /* Returns the search keyword */
    public String getKeyword() {
        return keyword;
    }

    /* Updates the search keyword */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /* Returns the search results */
    public List<Product> getResults() {

        /* Return an empty list if no results are available */
        return results != null ? results : Collections.emptyList();
    }
}
