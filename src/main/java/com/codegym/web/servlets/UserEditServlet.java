package com.codegym.web.servlets;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserEditServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;

    public UserEditServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.valueOf(req.getParameter("id"));
            User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
            req.setAttribute("mode", "edit");
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/jsp/form.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = bindUser(req);
        if (userRepository.update(user)) {
            resp.sendRedirect(req.getContextPath() + "/users");
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update user. User ID not found.");
        }
    }

    private User bindUser(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("age"));
        Long id = Long.valueOf(idStr);
        return new User(id, firstName, lastName, email, age);
    }
}