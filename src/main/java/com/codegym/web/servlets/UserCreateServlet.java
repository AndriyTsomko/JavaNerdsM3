package com.codegym.web.servlets;

import com.codegym.web.model.User;
import com.codegym.web.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserCreateServlet extends HttpServlet {
    private final InMemoryUserRepository userRepository;

    public UserCreateServlet(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("mode", "create");
        req.getRequestDispatcher("/WEB-INF/jsp/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = bindUser(req);
        userRepository.save(user);
        req.setAttribute("users", userRepository.findAll());
        resp.sendRedirect(req.getContextPath() + "/users");
    }

    private User bindUser(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("age"));

        if (idStr == null) {
            return new User(firstName, lastName, email, age);
        } else {
            Long id = Long.valueOf(idStr);
            return new User(id, firstName, lastName, email, age);
        }
    }
}
