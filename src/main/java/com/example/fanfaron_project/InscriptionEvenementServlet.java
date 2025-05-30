package com.example.fanfaron_project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

@WebServlet("/inscriptionevenement")
public class InscriptionEvenementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
        if (fanfaron == null) {
            response.sendRedirect("connexion");
            return;
        }

        int idEvent = Integer.parseInt(request.getParameter("id"));
        int idFanfaron = fanfaron.getId();

        try {
            FanfaronEvenementParticipationDAO participationDAO = DAOFactory.getInstance().getFanfaronEvenementParticipationDAO();
            PupitreDAO pupitreDAO = DAOFactory.getInstance().getPupitreDAO();
            StatutParticipationDAO statutDAO = DAOFactory.getInstance().getStatutParticipationDAO();
            EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();

            FanfaronEvenementParticipation participation = participationDAO.find(idFanfaron, idEvent);
            List<Pupitre> pupitres = pupitreDAO.findAll();
            List<StatutParticipation> statuts = statutDAO.findAll();
            Evenement evenement = evenementDAO.findById(idEvent);

            request.setAttribute("participation", participation);
            request.setAttribute("pupitres", pupitres);
            request.setAttribute("statuts", statuts);
            request.setAttribute("evenement", evenement);

            request.getRequestDispatcher("/WEB-INF/vue/inscriptionEvenement.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement de la participation.");
            request.getRequestDispatcher("/WEB-INF/vue/erreur.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
        if (fanfaron == null) {
            response.sendRedirect("connexion");
            return;
        }

        int idFanfaron = fanfaron.getId();
        int idEvent = Integer.parseInt(request.getParameter("id_event"));
        int idPupitre = Integer.parseInt(request.getParameter("id_pupitre"));
        int idStatut = Integer.parseInt(request.getParameter("id_statut"));

        try {
            FanfaronEvenementParticipationDAO dao = DAOFactory.getInstance().getFanfaronEvenementParticipationDAO();
            FanfaronEvenementParticipation participation = new FanfaronEvenementParticipation();
            participation.setIdFanfaron(idFanfaron);
            participation.setIdEvent(idEvent);
            participation.setIdPupitre(idPupitre);
            participation.setIdStatutParticipation(idStatut);


            try {
                dao.saveOrUpdate(participation);
            } catch (SQLException e) {
                throw new ServletException("Erreur SQL dans saveOrUpdate", e);
            }

            response.sendRedirect("listevenementservlet");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la mise à jour de votre participation.");
            request.getRequestDispatcher("/WEB-INF/vue/erreur.jsp").forward(request, response);
        }

    }
}
