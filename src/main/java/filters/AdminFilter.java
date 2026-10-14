package filters;

import beans.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Restricts the admin page(s) to logged-in users whose role is "Administrator".
 * A guest is redirected to the login page; a logged-in non-admin is redirected
 * to the products page.
 */
@WebFilter(urlPatterns = {"/admin", "/admin/*", "/admin.xhtml"})
public class AdminFilter implements Filter {

    private static final String ADMIN_ROLE = "Administrator";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /* Cast the generic servlet request/response objects to HTTP-specific types */
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        /* Retrieve the current session without creating a new one */
        HttpSession session = httpRequest.getSession(false);

        /* Get the logged-in user from the session, if a session exists */
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        /* If no user is found, redirect to the login page */
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        /* Check whether the user has the ADMIN role
           Non-admin users are redirected to the products page */
        if (!ADMIN_ROLE.equals(user.getRole())) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/products.xhtml");
            return;
        }

        /* User is authenticated and authorized, continue processing the request */
        chain.doFilter(request, response);
    }
}