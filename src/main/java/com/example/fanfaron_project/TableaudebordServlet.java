package com.example.fanfaron_project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.EvenementDAO;
import dao.DAOFactory;
import jakarta.servlet.http.HttpSession;
import model.Fanfaron;
import java.io.IOException;

@WebServlet("/tableaudebord")
public class TableaudebordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier que l'utilisateur est connecté
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("fanfaron") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Récupérer le fanfaron connecté
        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");

        // Vérifier s'il fait partie de la commission Prestation
        EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
        boolean dansCommissionPrestation = evenementDAO.estDansCommissionPrestation(fanfaron.getId());

        // Ajouter l'information à la requête
        request.setAttribute("dansCommissionPrestation", dansCommissionPrestation);

        // Forward vers la JSP
        request.getRequestDispatcher("/WEB-INF/vue/tableau_de_bord.jsp").forward(request, response);
    }
}