package controlleur;

import dao.FanfaronDAO;
import model.Fanfaron;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

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
        System.out.println("InscriptionServlet: Affichage du formulaire d'inscription (GET)");
        request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("InscriptionServlet: Traitement de l'inscription (POST)");

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

        // Logging des paramètres reçus
        System.out.println("Paramètres reçus:");
        System.out.println("  nomFanfaron: " + nomFanfaron);
        System.out.println("  email: " + email);
        System.out.println("  emailConfirmation: " + emailConfirmation);
        System.out.println("  mdp: " + (mdp != null ? "*".repeat(mdp.length()) : "null")); // Masque le MDP pour le log
        System.out.println("  mdpConfirmation: " + (mdpConfirmation != null ? "*".repeat(mdpConfirmation.length()) : "null")); // Masque le MDP
        System.out.println("  nom: " + nom);
        System.out.println("  prenom: " + prenom);
        System.out.println("  genre: " + genre);
        System.out.println("  contraintesAlimentaires: " + contraintesAlimentaires);


        boolean hasError = false;

        // Validations
        if (fanfaronDAO.checkNomFanfaronExists(nomFanfaron)) {
            System.out.println("Validation Erreur: Nom fanfaron '" + nomFanfaron + "' est déjà utilisé.");
            request.setAttribute("errorNomFanfaron", "Ce nom de fanfaron est déjà utilisé");
            hasError = true;
        }

        if (fanfaronDAO.checkEmailExists(email)) {
            System.out.println("Validation Erreur: Email '" + email + "' est déjà utilisé.");
            request.setAttribute("errorEmail", "Cette adresse email est déjà utilisée");
            hasError = true;
        }

        if (!email.equals(emailConfirmation)) { // .trim() déjà fait
            System.out.println("Validation Erreur: Les emails ne correspondent pas.");
            request.setAttribute("errorEmailConfirmation", "Les adresses email ne correspondent pas");
            hasError = true;
        }

        if (!mdp.equals(mdpConfirmation)) { // .trim() déjà fait
            System.out.println("Validation Erreur: Les mots de passe ne correspondent pas.");
            request.setAttribute("errorMdpConfirmation", "Les mots de passe ne correspondent pas");
            hasError = true;
        }

        // Si des erreurs de validation sont détectées, on revient au formulaire
        if (hasError) {
            System.out.println("InscriptionServlet: Erreurs de validation détectées. Retour au formulaire.");
            // Réattribuer les valeurs saisies pour qu'elles restent dans le formulaire
            request.setAttribute("nomFanfaron", nomFanfaron);
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("genre", genre);
            request.setAttribute("contraintesAlimentaires", contraintesAlimentaires);
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
            return; // Arrêter l'exécution ici
        }

        // Création de l'objet Fanfaron
        Fanfaron fanfaron = new Fanfaron();
        fanfaron.setNomFanfaron(nomFanfaron);
        fanfaron.setEmail(email);
        fanfaron.setMdp(mdp); // ATTENTION: Stocké en clair pour l'instant. HASHING RECOMMANDÉ !
        fanfaron.setNom(nom);
        fanfaron.setPrenom(prenom);
        fanfaron.setGenre(genre);
        fanfaron.setContraintesAlimentaires(contraintesAlimentaires);
        // Date de création et dernière connexion sont gérées par la DB ou par le DAO lors de l'insertion

        System.out.println("InscriptionServlet: Tentative d'insertion du nouvel utilisateur dans la BD.");
        int fanfaronId = fanfaronDAO.insertFanfaron(fanfaron); // Appelle la méthode DAO

        System.out.println("InscriptionServlet: Résultat de insertFanfaron : " + (fanfaronId > 0 ? fanfaronId : "Échec (-1)"));

        if (fanfaronId > 0) { // L'insertion a réussi
            fanfaron.setId(fanfaronId);
            HttpSession session = request.getSession();
            System.out.println("InscriptionServlet: Inscription réussie. Redirection vers /connexion.");
            response.sendRedirect(request.getContextPath() + "/connexion"); // Redirige vers la page connexion

            session.setAttribute("inscriptionSuccessMessage", "Votre compte a été créé avec succès ! Veuillez vous connecter.");
        } else { // L'insertion a échoué pour une raison inconnue (probablement une SQLException)
            System.out.println("InscriptionServlet: Échec de l'insertion en base de données.");
            request.setAttribute("errorMessage", "Une erreur inattendue est survenue lors de l'inscription. Veuillez réessayer.");
            // Réattribuer les valeurs saisies pour qu'elles restent dans le formulaire
            request.setAttribute("nomFanfaron", nomFanfaron);
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("genre", genre);
            request.setAttribute("contraintesAlimentaires", contraintesAlimentaires);
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
        }
    }

    // Méthode utilitaire pour trim un paramètre (évite les NPE)
    private String trim(String param) {
        return param != null ? param.trim() : "";
    }
}