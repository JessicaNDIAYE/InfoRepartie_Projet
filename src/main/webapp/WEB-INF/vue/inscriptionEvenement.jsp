<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.*" %>
<%@ page import="java.util.List" %>

<%
    Evenement evenement = (Evenement) request.getAttribute("evenement");
    FanfaronEvenementParticipation participation = (FanfaronEvenementParticipation) request.getAttribute("participation");
    List<Pupitre> pupitres = (List<Pupitre>) request.getAttribute("pupitres");
    List<StatutParticipation> statuts = (List<StatutParticipation>) request.getAttribute("statuts");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier ma participation</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7f9;
            margin: 0;
            padding: 40px;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .card {
            background: white;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 500px;
        }

        h2 {
            color: #333;
            margin-bottom: 25px;
            font-size: 1.5em;
            text-align: center;
        }

        label {
            display: block;
            margin: 15px 0 5px;
            font-weight: bold;
            color: #555;
        }

        select {
            width: 100%;
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
            background-color: #f9f9f9;
            font-size: 1em;
        }

        button {
            margin-top: 25px;
            width: 100%;
            padding: 12px;
            background-color: #4a6fa5;
            color: white;
            font-size: 1em;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        button:hover {
            background-color: #3e5f8c;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
            color: #4a6fa5;
            text-decoration: none;
            font-size: 0.95em;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="card">
    <h2>Modifier ma participation à : <%= evenement.getNom() %></h2>
    <form method="post" action="inscriptionevenement">
        <input type="hidden" name="id_event" value="<%= evenement.getId() %>">

        <label for="id_pupitre">Pupitre :</label>
        <select name="id_pupitre" id="id_pupitre" required>
            <% for (Pupitre p : pupitres) { %>
            <option value="<%= p.getIdPupitre() %>" <%= (participation != null && participation.getIdPupitre() == p.getIdPupitre()) ? "selected" : "" %>>
                <%= p.getLibelle() %>
            </option>
            <% } %>
        </select>

        <label for="id_statut">Statut :</label>
        <select name="id_statut" id="id_statut" required>
            <% for (StatutParticipation s : statuts) { %>
            <option value="<%= s.getIdStatut() %>" <%= (participation != null && participation.getIdStatutParticipation() == s.getIdStatut()) ? "selected" : "" %>>
                <%= s.getLibelle() %>
            </option>
            <% } %>
        </select>

        <button type="submit">Valider</button>
    </form>
    <a class="back-link" href="listevenementservlet">← Retour à la liste des événements</a>
</div>
</body>
</html>
