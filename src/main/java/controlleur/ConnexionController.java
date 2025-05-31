package controlleur;

import dao.FanfaronDAO;
import dao.DAOFactory;
import jakarta.servlet.ServletException;
import model.Fanfaron;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/connexion")
public class ConnexionController extends HttpServlet {

    private DAOFactory daoFactory;
    private static final String DEFAULT_REDIRECT = "/tableaudebord";

    @Override
    public void init() throws ServletException {
        super.init();
        daoFactory = DAOFactory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si déjà connecté, invalide la session = déconnexion
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Affiche la page de connexion
        request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifiant = request.getParameter("nomFanfaron");
        String mdp = request.getParameter("mdp");

        FanfaronDAO fanfaronDAO = daoFactory.getFanfaronDAO();
        Fanfaron fanfaron = fanfaronDAO.authenticateFanfaron(identifiant, mdp);

        if (fanfaron != null) {
            HttpSession session = request.getSession();
            session.setAttribute("fanfaron", fanfaron);

            if (fanfaron.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/accueil");
                return;
            }

            String targetUrl = (String) session.getAttribute("targetUrl");
            if (targetUrl != null) {
                session.removeAttribute("targetUrl");
                response.sendRedirect(targetUrl);
            } else {
                response.sendRedirect(request.getContextPath() + DEFAULT_REDIRECT);
            }
        } else {
            request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
            request.setAttribute("nomFanfaron", identifiant);
            request.getRequestDispatcher("/WEB-INF/vue/connexion.jsp").forward(request, response);
        }
    }
}
