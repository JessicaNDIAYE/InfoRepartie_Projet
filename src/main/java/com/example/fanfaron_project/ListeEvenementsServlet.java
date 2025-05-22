package com.example.fanfaron_project;

import dao.DAOFactory;
import dao.EvenementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Evenement;
import model.Fanfaron;

import java.io.IOException;
import java.util.List;

@WebServlet("/listevenementservlet")
public class ListeEvenementsServlet extends HttpServlet {
    private EvenementDAO evenementDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialisation du DAO via DAOFactory
        evenementDAO = DAOFactory.getInstance().getEvenementDAO();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Fanfaron fanfaron = (Fanfaron) req.getSession().getAttribute("fanfaron");
        if (fanfaron == null) {
            resp.sendRedirect("/WEB-INF/vue/connexion.jsp");
            return;
        }

        // Utilisation de la méthode findAll() au lieu de listerTous()
        List<Evenement> evenements = evenementDAO.findAll();
        req.setAttribute("evenements", evenements);

        req.getRequestDispatcher("/WEB-INF/vue/listeEvenements.jsp").forward(req, resp);
    }
}