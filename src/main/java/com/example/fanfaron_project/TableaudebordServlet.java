package com.example.fanfaron_project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/tableaudebord")
public class TableaudebordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier que l'utilisateur est connecté (session avec fanfaron)
        if (request.getSession(false) == null || request.getSession(false).getAttribute("fanfaron") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Forward vers la JSP protégée (dans WEB-INF pour empêcher l'accès direct)
        request.getRequestDispatcher("/WEB-INF/vue/tableau_de_bord.jsp").forward(request, response);
    }
}

