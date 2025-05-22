package controlleur;

import dao.DAOFactory;
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
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            fanfaronDAO = daoFactory.getFanfaronDAO();
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'initialisation de InscriptionServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nomFanfaron = trim(request.getParameter("nomFanfaron"));
        String email = trim(request.getParameter("email"));
        String emailConfirmation = trim(request.getParameter("emailConfirmation"));
        String mdp = trim(request.getParameter("mdp"));
        String mdpConfirmation = trim(request.getParameter("mdpConfirmation"));
        String nom = trim(request.getParameter("nom"));
        String prenom = trim(request.getParameter("prenom"));
        String genre = trim(request.getParameter("genre"));
        String contraintesAlimentaires = trim(request.getParameter("contraintesAlimentaires"));

        boolean hasError = false;

        if (fanfaronDAO.checkNomFanfaronExists(nomFanfaron)) {
            request.setAttribute("errorNomFanfaron", "Ce nom de fanfaron est déjà utilisé");
            hasError = true;
        }

        if (fanfaronDAO.checkEmailExists(email)) {
            request.setAttribute("errorEmail", "Cette adresse email est déjà utilisée");
            hasError = true;
        }

        if (!email.equals(emailConfirmation)) {
            request.setAttribute("errorEmailConfirmation", "Les adresses email ne correspondent pas");
            hasError = true;
        }

        if (!mdp.equals(mdpConfirmation)) {
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
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
            return;
        }

        Fanfaron fanfaron = new Fanfaron();
        fanfaron.setNomFanfaron(nomFanfaron);
        fanfaron.setEmail(email);
        fanfaron.setMdp(mdp); // À sécuriser par hashage
        fanfaron.setNom(nom);
        fanfaron.setPrenom(prenom);
        fanfaron.setGenre(genre);
        fanfaron.setContraintesAlimentaires(contraintesAlimentaires);

        int fanfaronId = fanfaronDAO.insertFanfaron(fanfaron);

        if (fanfaronId > 0) {
            fanfaron.setId(fanfaronId);
            HttpSession session = request.getSession();
            session.setAttribute("inscriptionSuccessMessage", "Votre compte a été créé avec succès ! Veuillez vous connecter.");
            response.sendRedirect(request.getContextPath() + "/connexion");
        } else {
            request.setAttribute("errorMessage", "Une erreur est survenue lors de l'inscription.");
            request.setAttribute("nomFanfaron", nomFanfaron);
            request.setAttribute("email", email);
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("genre", genre);
            request.setAttribute("contraintesAlimentaires", contraintesAlimentaires);
            request.getRequestDispatcher("/WEB-INF/vue/inscription.jsp").forward(request, response);
        }
    }

    private String trim(String param) {
        return param != null ? param.trim() : "";
    }
}
