package ru.job4j.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * The authorization filter
 *
 * @author Sergey Filippov (serdg1984@yandex.ru).
 * @version 1.0.
 * @since 26.11.18
 */
public class AuthFilter implements Filter {
    /**
     * Contains logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

    /**
     * Init filter
     *
     * @param filterConfig filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        LOG.info("Init Filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        req.setCharacterEncoding("UTF-8");
        if (session.getAttribute("login") != null
                || req.getRequestURI().matches(".*(/signing|/create|/info)")) {
            filterChain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        }
    }

    /**
     * Filter destroy
     *
     */
    @Override
    public void destroy() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOG.info("Deregistering JDBC driver {}", driver);
            } catch (SQLException e) {
                LOG.error("Error deregistering JDBC driver {}", driver, e, e.getMessage());
            }
        }
        LOG.info("Filter destroy");
    }
}