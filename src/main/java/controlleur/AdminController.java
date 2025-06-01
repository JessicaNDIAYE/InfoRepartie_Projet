package controlleur;

import dao.FanfaronDAO;
import dao.DAOFactory;
import model.Fanfaron;
import utils.HashUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {

    private DAOFactory daoFactory;

    @Override
    public void init() throws ServletException {
        super.init();
        daoFactory = DAOFactory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isUserAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String action = request.getParameter("action");
        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        if ("edit".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            Fanfaron fanfaron = fanfaronDAO.getFanfaronById(userId);
            if (fanfaron != null) {
                request.setAttribute("fanfaronAModifier", fanfaron);
            }
        } else if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean success = fanfaronDAO.deleteFanfaron(userId);
            if (success) {
                request.setAttribute("successMessage", "Utilisateur supprimé avec succès");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la suppression");
            }
        }

        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isUserAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateFanfaron(request, response);
        } else if ("toggleAdmin".equals(action)) {
            toggleAdminStatus(request, response);
        } else if ("add".equals(action)) {
            addFanfaron(request, response);
        }
    }

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

            if (fanfaronDAO.checkNomFanfaronExistsExcept(nomFanfaron, id)) {
                request.setAttribute("errorMessage", "Ce nom de fanfaron est déjà utilisé");
                request.setAttribute("fanfaronAModifier", fanfaronDAO.getFanfaronById(id));
            } else if (fanfaronDAO.checkEmailExistsExcept(email, id)) {
                request.setAttribute("errorMessage", "Cet email est déjà utilisé");
                request.setAttribute("fanfaronAModifier", fanfaronDAO.getFanfaronById(id));
            } else {
                Fanfaron fanfaron = new Fanfaron();
                fanfaron.setId(id);
                fanfaron.setNomFanfaron(nomFanfaron);
                fanfaron.setNom(nom);
                fanfaron.setPrenom(prenom);
                fanfaron.setEmail(email);
                fanfaron.setGenre(genre);
                fanfaron.setContraintesAlimentaires(contraintesAlimentaires);

                boolean success = fanfaronDAO.updateFanfaron(fanfaron);
                request.setAttribute(success ? "successMessage" : "errorMessage",
                        success ? "Utilisateur modifié avec succès" : "Erreur lors de la modification");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID utilisateur invalide");
        }

        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    private void toggleAdminStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean success = fanfaronDAO.toggleAdminStatus(userId);

            request.setAttribute(success ? "successMessage" : "errorMessage",
                    success ? "Statut administrateur modifié" : "Erreur lors de la modification du statut");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID utilisateur invalide");
        }

        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);

        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    private void addFanfaron(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();

        String nomFanfaron = request.getParameter("nomFanfaron");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String mdp = request.getParameter("motDePasse");
        String genre = request.getParameter("genre");
        String contraintes = request.getParameter("contraintesAlimentaires");
        boolean isAdmin = request.getParameter("isAdmin") != null;

        Fanfaron f = new Fanfaron();
        f.setNomFanfaron(nomFanfaron);
        f.setNom(nom);
        f.setPrenom(prenom);
        f.setEmail(email);
        f.setMdp(HashUtil.hashPassword(mdp));
        f.setGenre(genre);
        f.setContraintesAlimentaires(contraintes);
        f.setAdmin(isAdmin);

        int id = fanfaronDAO.insertFanfaron(f);
        if (id > 0) {
            request.setAttribute("successMessage", "Utilisateur ajouté avec succès.");
        } else {
            request.setAttribute("errorMessage", "Erreur lors de l'ajout de l'utilisateur.");
        }

        List<Fanfaron> fanfarons = fanfaronDAO.getAllFanfarons();
        request.setAttribute("fanfarons", fanfarons);
        request.getRequestDispatcher("/WEB-INF/vue/admin.jsp").forward(request, response);
    }

    private boolean isUserAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Fanfaron f = (Fanfaron) session.getAttribute("fanfaron");
        return f != null && f.isAdmin();
    }
}
