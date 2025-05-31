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

        // Récupérer le paramètre search depuis l'URL
        String search = req.getParameter("search");

        // Appeler la méthode findAll avec filtre si tu modifies findAll(), sinon appelle getAll(search)
        List<Evenement> evenements = evenementDAO.getAll(search);

        req.setAttribute("evenements", evenements);
        req.setAttribute("search", search); // pour garder la valeur dans la JSP

        req.getRequestDispatcher("/WEB-INF/vue/listeEvenements.jsp").forward(req, resp);
    }

}