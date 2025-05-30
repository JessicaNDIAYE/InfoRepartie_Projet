package com.example.fanfaron_project;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import dao.DAOFactory;
import dao.EvenementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Evenement;
import model.Fanfaron;

@WebServlet("/creerevenement")

public class CreationEvenementsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron"); // Utilisez le même attribut partout

        if (fanfaron == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Vérification commission prestation
        EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
        boolean estDansCommission = evenementDAO.estDansCommissionPrestation(fanfaron.getId());

        if (!estDansCommission) {
            request.setAttribute("error", "Accès réservé à la commission prestation");
            response.sendRedirect(request.getContextPath() + "/tableaudebord");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/vue/creerevenement.jsp").forward(request, response);
    }

    @Override
    // Dans votre servlet creerevenement, méthode doPost
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String nom = request.getParameter("nom");
            String dateStr = request.getParameter("date");
            String dureeStr = request.getParameter("duree");
            String lieu = request.getParameter("lieu");
            String description = request.getParameter("description");
            String typeEvenementStr = request.getParameter("typeEvenement"); // Nouveau paramètre

            // Validation
            if (nom == null || nom.trim().isEmpty() ||
                    dateStr == null || dateStr.trim().isEmpty() ||
                    dureeStr == null || dureeStr.trim().isEmpty() ||
                    lieu == null || lieu.trim().isEmpty() ||
                    typeEvenementStr == null || typeEvenementStr.trim().isEmpty()) {

                request.setAttribute("error", "Tous les champs obligatoires doivent être remplis");
                request.getRequestDispatcher("creerunevenement.jsp").forward(request, response);
                return;
            }

            // Conversion des données
            Timestamp horodatage = Timestamp.valueOf(dateStr.replace("T", " ") + ":00");
            Time duree = Time.valueOf(dureeStr + ":00");
            int idType = Integer.parseInt(typeEvenementStr);

            // Récupération de l'utilisateur connecté
            Fanfaron fanfaron = (Fanfaron) request.getSession().getAttribute("fanfaron");
            if (fanfaron == null) {
                response.sendRedirect("connexion.jsp");
                return;
            }

            // Création de l'événement
            Evenement evenement = new Evenement(nom, horodatage, duree, lieu, description, fanfaron.getId());
            evenement.setIdType(idType); // Définir le type

            // Sauvegarde via DAO
            EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
            boolean success = evenementDAO.insert(evenement);

            if (success) {
                response.sendRedirect("tableaudebord?success=evenement_cree");
            } else {
                request.setAttribute("error", "Erreur lors de la création de l'événement");
                request.getRequestDispatcher("creerunevenement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la création de l'événement : " + e.getMessage());
            request.getRequestDispatcher("creerunevenement.jsp").forward(request, response);
        }
    }
}