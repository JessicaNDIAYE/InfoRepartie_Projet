package controlleur;
import dao.FanfaronDAO;
import jakarta.servlet.ServletException;
import model.Fanfaron;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
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
     * Méthode pour afficher le formulaire de connexion
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Vérifier si l'utilisateur est déjà connecté
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("fanfaron") != null) {
            // Rediriger vers la page d'accueil si déjà connecté
            response.sendRedirect(request.getContextPath() + "/accueil");
            return;
        }

        // Afficher le formulaire de connexion
        request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
    }

    /**
     * Méthode pour traiter les données du formulaire de connexion
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer les paramètres du formulaire
        String nomFanfaron = request.getParameter("nomFanfaron");
        String mdp = request.getParameter("mdp");

        // Authentifier l'utilisateur
        Fanfaron fanfaron = fanfaronDAO.authenticateFanfaron(nomFanfaron, mdp);

        if (fanfaron != null) {
            // Créer une session pour l'utilisateur connecté
            HttpSession session = request.getSession();
            session.setAttribute("fanfaron", fanfaron);

            // Rediriger vers la page d'accueil ou la dernière page visitée
            String targetUrl = (String) session.getAttribute("targetUrl");
            if (targetUrl != null) {
                session.removeAttribute("targetUrl");
                response.sendRedirect(targetUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/accueil");
            }
        } else {
            // En cas d'échec d'authentification
            request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
            request.setAttribute("nomFanfaron", nomFanfaron); // Conserver le nom d'utilisateur saisi
            request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
        }
    }
}