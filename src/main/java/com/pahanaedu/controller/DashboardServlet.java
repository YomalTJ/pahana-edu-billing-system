package com.pahanaedu.controller;

import com.pahanaedu.dao.BillDAO;
import com.pahanaedu.dao.CustomerDAO;
import com.pahanaedu.dao.ItemDAO;
import com.pahanaedu.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Initialize with default values
            int customerCount = 0;
            int itemCount = 0;
            int lowStockCount = 0;
            double todaySales = 0.0;

            // Get counts for dashboard with proper resource handling
            try (Connection conn = DBUtil.getConnection()) {
                CustomerDAO customerDao = new CustomerDAO();
                ItemDAO itemDao = new ItemDAO();
                BillDAO billDao = new BillDAO();

                customerCount = customerDao.getCustomerCount();
                itemCount = itemDao.getItemCount();
                lowStockCount = itemDao.getLowStockItemCount();
                todaySales = billDao.getTodaySalesTotal();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database connection error", e);
                request.setAttribute("error", "Database connection error. Please try again later.");
            }

            request.setAttribute("customerCount", customerCount);
            request.setAttribute("itemCount", itemCount);
            request.setAttribute("lowStockCount", lowStockCount);
            request.setAttribute("todaySales", todaySales);

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in DashboardServlet", e);
            request.setAttribute("error", "A system error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error500.jsp").forward(request, response);
        }
    }
}