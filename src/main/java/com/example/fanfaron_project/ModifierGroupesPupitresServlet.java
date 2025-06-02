package com.example.fanfaron_project;

import dao.DAOFactory;
import dao.FanfaronGroupeDAO;
import dao.FanfaronPupitreDAO;
import dao.GroupeDAO;
import dao.PupitreDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Fanfaron;
import model.FanfaronGroupe;
import model.FanfaronPupitre;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/modifiergroupespupitres")
public class ModifierGroupesPupitresServlet extends HttpServlet {
    private GroupeDAO groupeDAO;
    private PupitreDAO pupitreDAO;
    private FanfaronPupitreDAO fanfaronPupitreDAO;
    private FanfaronGroupeDAO fanfaronGroupeDAO;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Fanfaron fanfaron = (Fanfaron) req.getSession().getAttribute("fanfaron");
        if (fanfaron == null) {
            resp.sendRedirect("connexion");
            return;
        }

        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            PupitreDAO pupitreDAO = daoFactory.getPupitreDAO();
            GroupeDAO groupeDAO = daoFactory.getGroupeDAO();
            FanfaronPupitreDAO fanfaronPupitreDAO = daoFactory.getFanfaronPupitreDAO();
            FanfaronGroupeDAO fanfaronGroupeDAO = daoFactory.getFanfaronGroupeDAO();

            req.setAttribute("pupitres", pupitreDAO.findAll());
            req.setAttribute("groupes", groupeDAO.findAll());

            List<FanfaronPupitre> fanfaronPupitres = fanfaronPupitreDAO.findByFanfaronId(fanfaron.getId());
            List<FanfaronGroupe> fanfaronGroupes = fanfaronGroupeDAO.findByFanfaronId(fanfaron.getId());

            List<Integer> pupitreIds = new ArrayList<>();
            for (FanfaronPupitre fp : fanfaronPupitres) {
                pupitreIds.add(fp.getIdPupitre());
            }

            List<Integer> groupeIds = new ArrayList<>();
            for (FanfaronGroupe fg : fanfaronGroupes) {
                groupeIds.add(fg.getIdGroupe());
            }

            req.setAttribute("fanfaronPupitres", pupitreIds);
            req.setAttribute("fanfaronGroupes", groupeIds);

            req.getRequestDispatcher("/WEB-INF/vue/modifierGroupesPupitres.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors de la récupération des données");
            req.getRequestDispatcher("/WEB-INF/vue/erreur.jsp").forward(req, resp);
        }
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Fanfaron fanfaron = (Fanfaron) req.getSession().getAttribute("fanfaron");
        if (fanfaron == null) {
            resp.sendRedirect("connexion");
            return;
        }

        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            FanfaronPupitreDAO fanfaronPupitreDAO = daoFactory.getFanfaronPupitreDAO();
            FanfaronGroupeDAO fanfaronGroupeDAO = daoFactory.getFanfaronGroupeDAO();

            int fanfaronId = fanfaron.getId();

            fanfaronPupitreDAO.deleteByFanfaronId(fanfaronId);
            fanfaronGroupeDAO.deleteByFanfaronId(fanfaronId);

            String[] pupitresSelected = req.getParameterValues("pupitres");
            if (pupitresSelected != null) {
                for (String pupitreIdStr : pupitresSelected) {
                    int pupitreId = Integer.parseInt(pupitreIdStr);
                    FanfaronPupitre fp = new FanfaronPupitre();
                    fp.setIdFanfaron(fanfaronId);
                    fp.setIdPupitre(pupitreId);
                    fanfaronPupitreDAO.insert(fp);
                }
            }

            String[] groupesSelected = req.getParameterValues("groupes");
            if (groupesSelected != null) {
                for (String groupeIdStr : groupesSelected) {
                    int groupeId = Integer.parseInt(groupeIdStr);
                    FanfaronGroupe fg = new FanfaronGroupe();
                    fg.setIdFanfaron(fanfaronId);
                    fg.setIdGroupe(groupeId);
                    fanfaronGroupeDAO.insert(fg);
                }
            }

            resp.sendRedirect("tableaudebord");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("erreur", "Erreur lors de la mise à jour des associations");
            req.getRequestDispatcher("/WEB-INF/vue/erreur.jsp").forward(req, resp);
        }
    }

}