package controlleur;

import dao.FanfaronDAO;
import dao.DAOFactory;
import model.Fanfaron;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * Contrôleur pour la gestion des utilisateurs par l'administrateur
 */
@WebServlet("/admin")
public class AdminController extends HttpServlet {

    private DAOFactory daoFactory;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialiser la factory DAO qui gère les connexions
        daoFactory = DAOFactory.getInstance();
    }

    /**
     * Afficher la page d'administration avec la liste des utilisateurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier si l'utilisateur est connecté et est administrateur
        if (!isUserAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String action = request.getParameter("action");
        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        if ("edit".equals(action)) {
            // Afficher le formulaire de modification d'un utilisateur
            int userId = Integer.parseInt(request.getParameter("id"));
            Fanfaron fanfaron = fanfaronDAO.getFanfaronById(userId);
            if (fanfaron != null) {
                request.setAttribute("fanfaronAModifier", fanfaron);
            }
        } else if ("delete".equals(action)) {
            // Supprimer un utilisateur
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean success = fanfaronDAO.deleteFanfaron(userId);
            if (success) {
                request.setAttribute("successMessage", "Utilisateur supprimé avec succès");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la suppression");
            }
        }

        // Récupérer la liste de tous les fanfarons
        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    /**
     * Traiter les modifications d'utilisateur
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier si l'utilisateur est connecté et est administrateur
        if (!isUserAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            // Modifier un utilisateur existant
            updateFanfaron(request, response);
        } else if ("toggleAdmin".equals(action)) {
            // Basculer le statut administrateur
            toggleAdminStatus(request, response);
        }
    }

    /**
     * Modifier les informations d'un fanfaron
     */
    private void updateFanfaron(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nomFanfaron = request.getParameter("nomFanfaron").trim();
            String nom = request.getParameter("nom").trim();
            String prenom = request.getParameter("prenom").trim();
            String email = request.getParameter("email").trim();
            String genre = request.getParameter("genre");
            String contraintesAlimentaires = request.getParameter("contraintesAlimentaires");

            // Vérifier l'unicité du nom de fanfaron et de l'email (sauf pour l'utilisateur actuel)
            if (fanfaronDAO.checkNomFanfaronExistsExcept(nomFanfaron, id)) {
                request.setAttribute("errorMessage", "Ce nom de fanfaron est déjà utilisé par un autre utilisateur");
                request.setAttribute("fanfaronAModifier", fanfaronDAO.getFanfaronById(id));
            } else if (fanfaronDAO.checkEmailExistsExcept(email, id)) {
                request.setAttribute("errorMessage", "Cette adresse email est déjà utilisée par un autre utilisateur");
                request.setAttribute("fanfaronAModifier", fanfaronDAO.getFanfaronById(id));
            } else {
                // Mettre à jour l'utilisateur
                Fanfaron fanfaron = new Fanfaron();
                fanfaron.setId(id);
                fanfaron.setNomFanfaron(nomFanfaron);
                fanfaron.setNom(nom);
                fanfaron.setPrenom(prenom);
                fanfaron.setEmail(email);
                fanfaron.setGenre(genre);
                fanfaron.setContraintesAlimentaires(contraintesAlimentaires);

                boolean success = fanfaronDAO.updateFanfaron(fanfaron);
                if (success) {
                    request.setAttribute("successMessage", "Utilisateur modifié avec succès");
                } else {
                    request.setAttribute("errorMessage", "Erreur lors de la modification");
                }
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID utilisateur invalide");
        }

        // Récupérer la liste mise à jour des fanfarons
        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    /**
     * Basculer le statut administrateur d'un utilisateur
     */
    private void toggleAdminStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean success = fanfaronDAO.toggleAdminStatus(userId);

            if (success) {
                request.setAttribute("successMessage", "Statut administrateur modifié avec succès");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la modification du statut");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID utilisateur invalide");
        }

        // Récupérer la liste mise à jour des fanfarons
        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    /**
     * Vérifier si l'utilisateur connecté est administrateur
     */
    private boolean isUserAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
        return fanfaron != null && fanfaron.isAdmin();
    }
}