package com.pahanaedu.controller;

import com.pahanaedu.model.Category;
import com.pahanaedu.model.Item;
import com.pahanaedu.service.ItemService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ItemServlet", value = "/items")
public class ItemServlet extends HttpServlet {
    private ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            case "list":
            default:
                listItems(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addItem(request, response);
        } else if ("update".equals(action)) {
            updateItem(request, response);
        } else {
            listItems(request, response);
        }
    }

    private void listItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Item> items = itemService.getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/item/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = itemService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/item/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemId = request.getParameter("itemId");
        Item item = itemService.getItemById(itemId);
        List<Category> categories = itemService.getAllCategories();

        request.setAttribute("item", item);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/item/form.jsp").forward(request, response);
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Item item = new Item();
        item.setItemId(request.getParameter("itemId"));
        item.setName(request.getParameter("name"));
        item.setDescription(request.getParameter("description"));

        Category category = new Category();
        category.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
        item.setCategory(category);

        item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
        item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        item.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

        if (itemService.addItem(item)) {
            request.setAttribute("message", "Item added successfully");
        } else {
            request.setAttribute("error", "Failed to add item");
        }

        listItems(request, response);
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemId = request.getParameter("itemId");
        Item item = itemService.getItemById(itemId);

        if (item != null) {
            item.setName(request.getParameter("name"));
            item.setDescription(request.getParameter("description"));

            Category category = new Category();
            category.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            item.setCategory(category);

            item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
            item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
            item.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

            if (itemService.updateItem(item)) {
                request.setAttribute("message", "Item updated successfully");
            } else {
                request.setAttribute("error", "Failed to update item");
            }
        } else {
            request.setAttribute("error", "Item not found");
        }

        listItems(request, response);
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemId = request.getParameter("itemId");

        if (itemService.deleteItem(itemId)) {
            request.setAttribute("message", "Item deleted successfully");
        } else {
            request.setAttribute("error", "Failed to delete item");
        }

        listItems(request, response);
    }
}