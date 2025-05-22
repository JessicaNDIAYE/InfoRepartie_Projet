<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 22/05/2025
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="model.Evenement" %>
<%@ page import="model.Fanfaron" %>
<%
    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    if (fanfaron == null) {
        response.sendRedirect("connexion.jsp");
        return;
    }
    List<Evenement> evenements = (List<Evenement>) request.getAttribute("evenements");
%>

<!DOCTYPE html>
<html>
<head><title>Liste des événements</title></head>
<body>
<h2>Événements disponibles</h2>
<table border="1">
    <tr>
        <th>Nom</th><th>Date et heure</th><th>Durée</th><th>Lieu</th><th>Type</th><th>Actions</th>
    </tr>
    <% for (Evenement e : evenements) { %>
    <tr>
        <td><%= e.getNom() %></td>
        <td><%= e.getHorodatage() %></td>
        <td><%= e.getDuree() %></td>
        <td><%= e.getLieu() %></td>
        <td>
            <a href="ModifierParticipationServlet?id_event=<%= e.getId() %>">Gérer ma participation</a>
        </td>
    </tr>
    <% } %>
</table>
<a href="tableau_de_bord.jsp">Retour au tableau de bord</a>
</body>
</html>

