<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 26/05/2025
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Fanfaron" %>
<%@ page import="model.TypeEvenement" %>
<%@ page import="dao.EvenementDAO" %>
<%@ page import="dao.TypeEvenementDAO" %>
<%@ page import="dao.DAOFactory" %>
<%@ page import="java.util.List" %>
<%
    // Déclarer la variable error au début du scriptlet
    String error = (String) request.getAttribute("error");

    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    System.out.println("Fanfaron dans session: " + fanfaron);
    if (fanfaron == null) {
        System.out.println("Redirection vers connexion.jsp - Fanfaron non trouvé en session");
        response.sendRedirect("connexion.jsp");
        return;
    } else {
        System.out.println("Fanfaron trouvé en session, ID: " + fanfaron.getId());
    }

    Boolean dansCommission = (Boolean) session.getAttribute("dansCommissionPrestation");
    System.out.println("dansCommissionPrestation dans session: " + dansCommission);

    if (dansCommission == null) {
        System.out.println("Calcul de dansCommissionPrestation via DAO");
        EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
        dansCommission = evenementDAO.estDansCommissionPrestation(fanfaron.getId());
        System.out.println("Résultat DAO pour estDansCommissionPrestation: " + dansCommission);
        session.setAttribute("dansCommissionPrestation", dansCommission);
    } else {
        System.out.println("Utilisation de la valeur en cache pour dansCommissionPrestation");
    }
    System.out.println("Valeur finale de dansCommissionPrestation: " + dansCommission);

    // Récupérer la liste des types d'événements
    TypeEvenementDAO typeDAO = DAOFactory.getInstance().getTypeEvenementDAO();
    List<TypeEvenement> typesEvenements = typeDAO.findAll();

    System.out.println("Tentative d'accès à creerunevenement.jsp");
    System.out.println("Chemin demandé: " + request.getRequestURI());
    System.out.println("Session ID: " + session.getId());
%>

<!DOCTYPE html>
<html>
<head>
    <title>Créer un Événement</title>
    <style>
        /* Réutilisation des variables CSS */
        :root {
            --primary-color: #4a6fa5;
            --secondary-color: #6b8cae;
            --accent-color: #ff7e5f;
            --light-color: #f8f9fa;
            --dark-color: #343a40;
            --success-color: #28a745;
        }

        body {
            background-color: #f5f7fa;
            color: var(--dark-color);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 40px 20px;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        }

        h2 {
            text-align: center;
            color: var(--primary-color);
            margin-bottom: 25px;
            font-size: 1.8rem;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            margin-top: 15px;
        }

        input[type="text"],
        input[type="datetime-local"],
        input[type="time"],
        select,
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 1em;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
        }

        select {
            background-color: white;
            cursor: pointer;
        }

        select:focus,
        input:focus,
        textarea:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 5px rgba(74, 111, 165, 0.3);
        }

        .btn {
            margin-top: 25px;
            background-color: var(--primary-color);
            color: white;
            padding: 12px 20px;
            font-size: 1em;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 100%;
        }

        .btn:hover {
            background-color: var(--secondary-color);
        }

        .error {
            color: red;
            margin-top: 10px;
            text-align: center;
            font-weight: bold;
        }

        a.back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
            color: var(--primary-color);
            text-decoration: none;
        }

        a.back-link:hover {
            text-decoration: underline;
        }

        .required {
            color: red;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Créer un nouvel événement</h2>

    <% if (error != null) { %>
    <div class="error"><%= error %></div>
    <% } %>

    <form method="post" action="creerevenement">
        <label for="nom">Nom de l'événement <span class="required">*</span> :</label>
        <input type="text" id="nom" name="nom" required>

        <label for="typeEvenement">Type d'événement <span class="required">*</span> :</label>
        <select id="typeEvenement" name="typeEvenement" required>
            <option value="">-- Sélectionnez un type --</option>
            <%
                if (typesEvenements != null) {
                    for (TypeEvenement type : typesEvenements) {
            %>
            <option value="<%= type.getIdType() %>"><%= type.getLibelle() %></option>
            <%
                }
            } else {
            %>
            <option value="">Aucun type disponible</option>
            <% } %>
        </select>

        <label for="date">Date et heure <span class="required">*</span> :</label>
        <input type="datetime-local" id="date" name="date" required>

        <label for="duree">Durée (hh:mm) <span class="required">*</span> :</label>
        <input type="time" id="duree" name="duree" required>

        <label for="lieu">Lieu <span class="required">*</span> :</label>
        <input type="text" id="lieu" name="lieu" required>

        <label for="description">Description :</label>
        <textarea id="description" name="description" rows="4" placeholder="Description optionnelle de l'événement..."></textarea>

        <button type="submit" class="btn">Créer l'événement</button>
    </form>

    <a href="tableaudebord" class="back-link">⬅ Retour au tableau de bord</a>
</div>

</body>
</html>