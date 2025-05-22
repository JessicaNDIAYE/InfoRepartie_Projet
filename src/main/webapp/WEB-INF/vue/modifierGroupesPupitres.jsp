<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 22/05/2025
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Pupitre" %>
<%@ page import="model.Groupe" %>
<%@ page import="model.Fanfaron" %>
<%
    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    if (fanfaron == null) {
        response.sendRedirect("connexion.jsp");
        return;
    }
    List<Pupitre> pupitres = (List<Pupitre>) request.getAttribute("pupitres");
    List<Groupe> groupes = (List<Groupe>) request.getAttribute("groupes");
    List<Integer> fanfaronPupitres = (List<Integer>) request.getAttribute("fanfaronPupitres");
    List<Integer> fanfaronGroupes = (List<Integer>) request.getAttribute("fanfaronGroupes");
%>

<!DOCTYPE html>
<html>
<head><title>Modifier Groupes et Pupitres</title></head>
<body>
<h2>Modifier vos groupes et pupitres</h2>
<form action="ModifierGroupesPupitresServlet" method="post">

    <h3>Pupitres</h3>
    <% for (Pupitre p : pupitres) { %>
    <input type="checkbox" name="pupitres" value="<%=p.getIdPupitre()%>"
            <%= fanfaronPupitres.contains(p.getIdPupitre()) ? "checked" : "" %> />
    <%= p.getLibelle() %><br/>
    <% } %>

    <h3>Groupes</h3>
    <% for (Groupe g : groupes) { %>
    <input type="checkbox" name="groupes" value="<%=g.getIdGroupe()%>"
            <%= fanfaronGroupes.contains(g.getIdGroupe()) ? "checked" : "" %> />
    <%= g.getLibelle() %><br/>
    <% } %>

    <button type="submit">Enregistrer</button>
</form>
<a href="tableau_de_bord.jsp">Retour au tableau de bord</a>
</body>
</html>
