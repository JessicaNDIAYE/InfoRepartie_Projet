package controlleur;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
/**
 * Contrôleur pour gérer la déconnexion des fanfarons
 */
@WebServlet("/deconnexion")
public class DeconnexionController extends HttpServlet {

    /**
     * Méthode pour traiter la demande de déconnexion
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupérer la session actuelle si elle existe
        HttpSession session = request.getSession(false);

        if (session != null) {
            // Invalider la session pour déconnecter l'utilisateur
            session.invalidate();
        }

        // Rediriger vers la page de connexion avec un message de confirmation
        response.sendRedirect(request.getContextPath() + "/connexion?deconnexion=true");
    }
}