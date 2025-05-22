<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 18/05/2025
  Time: 03:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Fanfaron" %>
<%@ page session="true" %>

<%
    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    if (fanfaron == null) {
        response.sendRedirect("connexion.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <title>Tableau de bord - Fanfare de l'école</title>
    <style>
        /* Reset */
        * {
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f4f8;
            margin: 0;
            padding: 0 20px;
            color: #333;
        }

        header {
            background-color: #2c3e50;
            color: white;
            padding: 20px 40px;
            border-radius: 6px;
            margin-top: 20px;
            text-align: center;
            box-shadow: 0 3px 8px rgba(0,0,0,0.15);
        }

        header h1 {
            margin: 0;
            font-weight: 700;
            font-size: 2rem;
        }

        header p {
            margin: 5px 0 0;
            font-size: 1.1rem;
            font-weight: 500;
            color: #ecf0f1;
        }

        main {
            margin: 30px auto;
            max-width: 900px;
        }

        section {
            background: white;
            padding: 20px 30px;
            margin-bottom: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        section h3 {
            margin-top: 0;
            border-bottom: 2px solid #3498db;
            padding-bottom: 8px;
            color: #2980b9;
        }

        ul {
            list-style: none;
            padding-left: 0;
        }

        ul li {
            padding: 8px 0;
            font-size: 1rem;
            border-bottom: 1px solid #eee;
        }

        ul li:last-child {
            border-bottom: none;
        }

        a.btn {
            display: inline-block;
            padding: 10px 18px;
            margin-top: 12px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
            color: white;
            transition: background-color 0.3s ease;
        }

        a.btn-primary {
            background-color: #3498db;
        }

        a.btn-primary:hover {
            background-color: #2980b9;
        }

        a.btn-success {
            background-color: #27ae60;
        }

        a.btn-success:hover {
            background-color: #1e8449;
        }

        form {
            text-align: center;
            margin: 40px 0 30px;
        }

        button {
            background-color: #c0392b;
            color: white;
            border: none;
            padding: 12px 24px;
            font-size: 1rem;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 700;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #e74c3c;
        }
    </style>
</head>
<body>

<header>
    <h1>Bienvenue à la fanfare de l'école</h1>
    <p>Bonjour, <strong><%= fanfaron.getPrenom() %> <%= fanfaron.getNom() %></strong> !</p>
</header>

<main>

    <section>
        <h3>Vos informations</h3>
        <ul>
            <li><strong>Nom de fanfaron :</strong> <%= fanfaron.getNomFanfaron() %></li>
            <li><strong>Email :</strong> <%= fanfaron.getEmail() %></li>
            <li><strong>Genre :</strong> <%= fanfaron.getGenre() %></li>
            <li><strong>Contraintes alimentaires :</strong> <%= fanfaron.getContraintesAlimentaires() %></li>
        </ul>
    </section>

    <section>
        <h3>Groupes et pupitres</h3>
        <p>Gérez vos groupes et pupitres.</p>
        <a href="modifierGroupesPupitres.jsp" class="btn btn-primary">Modifier mes choix</a>
    </section>

    <section>
        <h3>Événements</h3>
        <p>Consultez et gérez votre participation aux événements.</p>
        <a href="listeEvenements.jsp" class="btn btn-success">Voir les événements</a>
    </section>

    <form action="DeconnexionServlet" method="post">
        <button type="submit">Se déconnecter</button>
    </form>

</main>

</body>
</html>
