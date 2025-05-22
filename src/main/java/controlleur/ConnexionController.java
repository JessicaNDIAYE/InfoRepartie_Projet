package controlleur;
import dao.FanfaronDAO;
import jakarta.servlet.ServletException;
import model.Fanfaron;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Contrôleur pour gérer la connexion des fanfarons
 */
@WebServlet("/connexion")
public class ConnexionController extends HttpServlet {

    private FanfaronDAO fanfaronDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        fanfaronDAO = new FanfaronDAO();
    }

    /**
     * Méthode pour afficher le formulaire de connexion (GET)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ConnexionController: Affichage du formulaire de connexion (GET).");

        // Vérifier si l'utilisateur est déjà connecté
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("fanfaron") != null) {
            System.out.println("ConnexionController: Utilisateur déjà connecté. Redirection vers /accueil.");
            response.sendRedirect(request.getContextPath() + "/accueil");
            return;
        }

        // Afficher le formulaire de connexion
        request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
    }

    /**
     * Méthode pour traiter les données du formulaire de connexion (POST)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ConnexionController: Traitement de la connexion (POST).");

        // Récupérer les paramètres du formulaire
        String identifiant = request.getParameter("nomFanfaron"); // Peut être nomFanfaron ou email
        String mdp = request.getParameter("mdp");

        System.out.println("ConnexionController: Tentative de connexion avec identifiant: " + identifiant);
        System.out.println("ConnexionController: Mot de passe (masqué): " + (mdp != null ? "*".repeat(mdp.length()) : "null"));


        // Authentifier l'utilisateur via le DAO
        Fanfaron fanfaron = fanfaronDAO.authenticateFanfaron(identifiant, mdp);

        if (fanfaron != null) {
            System.out.println("ConnexionController: Authentification réussie pour " + fanfaron.getNomFanfaron());
            // Créer une session pour l'utilisateur connecté
            HttpSession session = request.getSession();
            session.setAttribute("fanfaron", fanfaron); // Stocke l'objet fanfaron en session

            // Rediriger vers la page d'accueil ou la dernière page visitée
            String targetUrl = (String) session.getAttribute("targetUrl");
            if (targetUrl != null) {
                session.removeAttribute("targetUrl");
                System.out.println("ConnexionController: Redirection vers URL cible: " + targetUrl);
                response.sendRedirect(targetUrl);
            } else {
                System.out.println("ConnexionController: Redirection par défaut vers /accueil.");
                response.sendRedirect(request.getContextPath() + "/accueil");
            }
        } else {
            // En cas d'échec d'authentification
            System.out.println("ConnexionController: Échec d'authentification. Identifiant ou mot de passe incorrect.");
            request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
            request.setAttribute("nomFanfaron", identifiant); // Conserver l'identifiant saisi
            request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
        }
    }
}