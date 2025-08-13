package com.pahanaedu.controller;

import com.pahanaedu.model.Item;
import com.pahanaedu.service.ItemService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/bill-item")
public class BillItemServlet extends HttpServlet {
    private ItemService itemService = new ItemService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Debug logging
        System.out.println("Request received for item: " + request.getQueryString());

        String itemId = request.getParameter("itemId");
        System.out.println("Extracted itemId: " + itemId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if (itemId == null || itemId.trim().isEmpty()) {
                out.print("{\"error\":\"Item ID parameter is missing\"}");
                return;
            }

            Item item = itemService.getItemForBilling(itemId);
            if (item != null) {
                String json = String.format(
                        "{\"itemId\":\"%s\",\"name\":\"%s\",\"unitPrice\":%.2f,\"stock\":%d}",
                        item.getItemId(), item.getName(), item.getUnitPrice(), item.getStockQuantity()
                );
                out.print(json);
            } else {
                out.print("{\"error\":\"Item not available or out of stock\"}");
            }
        }
    }
}
