package controlleur;

import dao.FanfaronDAO;
import model.Fanfaron;
import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    private FanfaronDAO fanfaronDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        fanfaronDAO = new FanfaronDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupération et nettoyage des paramètres
        String nomFanfaron = trim(request.getParameter("nomFanfaron"));
        String email = trim(request.getParameter("email"));
        String emailConfirmation = trim(request.getParameter("emailConfirmation"));
        String mdp = trim(request.getParameter("mdp"));
        String mdpConfirmation = trim(request.getParameter("mdpConfirmation"));
        String nom = trim(request.getParameter("nom"));
        String prenom = trim(request.getParameter("prenom"));
        String genre = trim(request.getParameter("genre"));
        String contraintesAlimentaires = trim(request.getParameter("contraintesAlimentaires"));

        System.out.println("Début doPost InscriptionServlet");
        System.out.println("Vérification du nom de fanfaron : " + nomFanfaron);
        System.out.println("Vérification de l'email : " + email);

        boolean hasError = false;

        if (fanfaronDAO.checkNomFanfaronExists(nomFanfaron)) {
            System.out.println("Nom fanfaron déjà utilisé.");
            request.setAttribute("errorNomFanfaron", "Ce nom de fanfaron est déjà utilisé");
            hasError = true;
        }

        if (fanfaronDAO.checkEmailExists(email)) {
            System.out.println("Email déjà utilisé.");
            request.setAttribute("errorEmail", "Cette adresse email est déjà utilisée");
            hasError = true;
        }

        if (!email.trim().equals(emailConfirmation.trim())) {
            System.out.println("Emails ne correspondent pas.");
            request.setAttribute("errorEmailConfirmation", "Les adresses email ne correspondent pas");
            hasError = true;
        }

        if (!mdp.trim().equals(mdpConfirmation.trim())) {
            System.out.println("Mots de passe ne correspondent pas.");
            request.setAttribute("errorMdpConfirmation", "Les mots de passe ne correspondent pas");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("nomFanfaron", nomFanfaron);
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("genre", genre);
            request.setAttribute("contraintesAlimentaires", contraintesAlimentaires);
            System.out.println("Erreurs détectées, retour formulaire.");
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
            return;
        }

        // Création de l'objet fanfaron inscription
        Fanfaron fanfaron = new Fanfaron();
        fanfaron.setNomFanfaron(nomFanfaron);
        fanfaron.setEmail(email);
        fanfaron.setMdp(mdp); // À sécuriser avec un hash plus tard
        fanfaron.setNom(nom);
        fanfaron.setPrenom(prenom);
        fanfaron.setGenre(genre);
        fanfaron.setContraintesAlimentaires(contraintesAlimentaires);

        System.out.println("Appel de insertFanfaron avec :");
        System.out.println("nomFanfaron = " + nomFanfaron);
        System.out.println("email = " + email);
        System.out.println("mdp = " + mdp);
        System.out.println("nom = " + nom);
        System.out.println("prenom = " + prenom);
        System.out.println("genre = " + genre);
        System.out.println("contraintesAlimentaires = " + contraintesAlimentaires);

        int fanfaronId = fanfaronDAO.insertFanfaron(fanfaron);
        System.out.println("Résultat insertFanfaron : " + fanfaronId);

        if (fanfaronId > 0) {
            fanfaron.setId(fanfaronId);
            HttpSession session = request.getSession();
            session.setAttribute("fanfaron", fanfaron);
            System.out.println("Inscription réussie, redirection vers /accueil");
            response.sendRedirect(request.getContextPath() + "/accueil");
        } else {
            System.out.println("Erreur lors de l'insertion en base.");
            request.setAttribute("errorMessage", "Une erreur est survenue lors de l'inscription. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
        }
    }

    // Méthode utilitaire pour trim un paramètre (évite les NPE)
    private String trim(String param) {
        return param != null ? param.trim() : "";
    }
}
