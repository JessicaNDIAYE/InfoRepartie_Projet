package com.example.fanfaron_project;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.*;


import java.io.IOException;

@WebServlet("/accueil")
public class AccueilServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vue/accueil.jsp").forward(request, response);
    }
}
