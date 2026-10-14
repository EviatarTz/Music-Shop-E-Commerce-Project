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
 * Restricts a set of pages that require a logged-in user (cart, checkout,
 * orders, profile, order confirmation). A guest hitting any of these is
 * redirected to the login page.
 */
@WebFilter(urlPatterns = {
        "/cart", "/order",
        "/cart.xhtml", "/checkout.xhtml", "/orders.xhtml",
        "/profile.xhtml", "/orderConfirmation.xhtml"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        /* Cast the generic servlet request/response objects to HTTP-specific types */
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        /* Retrieve the current session without creating a new one */
        HttpSession session = httpRequest.getSession(false);

        /* Get the logged-in user from the session, if a session exists */
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        /* If no user is found, redirect to the login page */
        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        /* User is authenticated and authorized, continue processing the request */
        chain.doFilter(request, response);
    }
}