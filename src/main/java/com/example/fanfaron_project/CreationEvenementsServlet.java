package com.example.fanfaron_project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import dao.DAOFactory;
import dao.EvenementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Evenement;

@WebServlet("/creerevenement")
public class CreationEvenementsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id_fanfaron") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Vérification commission prestation
        Integer idFanfaron = (Integer) session.getAttribute("id_fanfaron");
        EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
        if (!evenementDAO.estDansCommissionPrestation(idFanfaron)) {
            System.out.println("L'utilisateur (ID: " + idFanfaron + ") n'est pas dans la commission prestation");

            response.sendRedirect(request.getContextPath() + "/tableaudebord");
            return;
        }
        System.out.println("L'utilisateur (ID: " + idFanfaron + ") n'est pas dans la commission prestation");

        request.getRequestDispatcher("/WEB-INF/vue/creerevenement.jsp").forward(request, response);
    }


    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            request.setCharacterEncoding("UTF-8"); // encodage des caractères

            HttpSession session = request.getSession(false);
            Integer idFanfaron = (Integer) session.getAttribute("id_fanfaron");

            if (idFanfaron == null) {
                response.sendRedirect("/WEB-INF/vue/connexion.jsp"); // Pas connecté
                return;
            }

            try {
                // 🔒 Vérification de l'appartenance à la Commission Prestation
                EvenementDAO evenementDAO = new EvenementDAO(null); // DAO temporaire sans connexion ici (adapter selon ton implémentation)
                if (!evenementDAO.estDansCommissionPrestation(idFanfaron)) {
                    request.setAttribute("erreur", "Vous n'êtes pas membre de la Commission Prestation. Création d'événement interdite.");
                    request.getRequestDispatcher("/WEB-INF/vue/creerevenement.jsp").forward(request, response);
                    return;
                }

                // 1. Récupération des données du formulaire
                String nom = request.getParameter("nom");
                String lieu = request.getParameter("lieu");
                String description = request.getParameter("description");

                String dateStr = request.getParameter("date"); // format: yyyy-MM-dd HH:mm
                String dureeStr = request.getParameter("duree"); // format: HH:mm

                // 2. Conversion des données
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Timestamp horodatage = new Timestamp(sdf.parse(dateStr).getTime());
                Time duree = Time.valueOf(dureeStr + ":00"); // HH:mm:ss

                // 3. Création de l'objet événement
                Evenement evenement = new Evenement();
                evenement.setNom(nom);
                evenement.setLieu(lieu);
                evenement.setDescription(description);
                evenement.setHorodatage(horodatage);
                evenement.setDuree(duree);
                evenement.setIdCreateur(idFanfaron);

                // 4. Insertion dans la base via DAO
                Connection conn = DAOFactory.getInstance().getConnection();
                EvenementDAO dao = new EvenementDAO(conn);
                boolean success = dao.insert(evenement);

                // 5. Redirection ou message
                if (success) {
                    response.sendRedirect("listeEvenements"); // éviter /WEB-INF ici
                } else {
                    request.setAttribute("error", "Échec de la création de l'événement.");
                    request.getRequestDispatcher("/WEB-INF/vue/creerevenement.jsp").forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Erreur lors de la création de l'événement.");
                request.getRequestDispatcher("/WEB-INF/vue/creerevenement.jsp").forward(request, response);
            }
        }
    }

