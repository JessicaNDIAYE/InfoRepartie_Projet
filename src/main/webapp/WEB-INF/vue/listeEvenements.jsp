<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 22/05/2025
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Evenement" %>
<%@ page import="model.Fanfaron" %>
<%
    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    if (fanfaron == null) {
        response.sendRedirect("connexion" );
        return;
    }
    List<Evenement> evenements = (List<Evenement>) request.getAttribute("evenements");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des événements</title>
    <style>
        :root {
            --primary-color: #4a6fa5;
            --secondary-color: #6b8cae;
            --accent-color: #ff7e5f;
            --light-color: #f8f9fa;
            --dark-color: #343a40;
            --success-color: #28a745;
            --table-header: #4a6fa5;
            --table-row-odd: #ffffff;
            --table-row-even: #f8f9fa;
            --table-hover: #e9f0f7;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #f5f7fa;
            color: var(--dark-color);
            line-height: 1.6;
            padding: 40px 20px;
        }

        h2 {
            color: var(--primary-color);
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--secondary-color);
            font-size: 2rem;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            font-size: 0.9em;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
            border-radius: 8px;
            overflow: hidden;
        }

        table th {
            background-color: var(--table-header);
            color: white;
            text-align: left;
            padding: 15px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        table td {
            padding: 15px;
            border-bottom: 1px solid #dddddd;
        }

        table tr:nth-child(even) {
            background-color: var(--table-row-even);
        }

        table tr:nth-child(odd) {
            background-color: var(--table-row-odd);
        }

        table tr:last-of-type {
            border-bottom: 2px solid var(--table-header);
        }

        table tr:hover {
            background-color: var(--table-hover);
            transform: scale(1.005);
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }

        a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s;
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
        }

        a.action-link {
            background-color: var(--accent-color);
            color: white;
            padding: 8px 15px;
            border-radius: 4px;
            text-align: center;
        }

        a.action-link:hover {
            background-color: #e56a4f;
            transform: translateY(-2px);
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        a.back-link {
            display: inline-block;
            margin-top: 25px;
            padding: 10px 20px;
            background-color: var(--primary-color);
            color: white;
            border-radius: 4px;
            transition: all 0.3s;
        }

        a.back-link:hover {
            background-color: var(--secondary-color);
            transform: translateY(-2px);
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        table tr {
            animation: fadeIn 0.4s ease forwards;
        }

        table tr:nth-child(1) { animation-delay: 0.1s; }
        table tr:nth-child(2) { animation-delay: 0.2s; }
        table tr:nth-child(3) { animation-delay: 0.3s; }
        table tr:nth-child(4) { animation-delay: 0.4s; }
        table tr:nth-child(5) { animation-delay: 0.5s; }

        /* Responsive design */
        @media (max-width: 768px) {
            body {
                padding: 20px 10px;
            }

            .container {
                padding: 15px;
            }

            table {
                display: block;
                overflow-x: auto;
            }

            table th, table td {
                padding: 10px 8px;
                font-size: 0.8em;
            }

            a.action-link {
                padding: 6px 10px;
                font-size: 0.8em;
            }
        }

        /* Status indicators */
        .status {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.75em;
            font-weight: 600;
            text-transform: uppercase;
        }

        .status-upcoming {
            background-color: #d4edda;
            color: #155724;
        }

        .status-past {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>

<form method="get" action="<%= request.getContextPath() + "/listevenementservlet" %>">
    <input type="text" name="search" placeholder="Rechercher un événement ou type..."
           value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>">
    <button type="submit">🔍 Rechercher</button>
</form>


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
            <%= e.getTypeEvenement().getLibelle() %>
        </td>


        <td>
            <a class="action-link" href="inscriptionevenement?id=<%= e.getId() %>">Gérer ma participation</a>
        </td>
    </tr>
    <% } %>
</table>
<a href="<%= request.getContextPath() %>/tableaudebord">Retour au tableau de bord</a>
</body>
</html>

