<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 18/05/2025
  Time: 01:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Fanfaron" %> <%-- Importation de la classe Fanfaron --%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Bienvenue à la Fanfare de Polytech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">


    <style>
        body {
            background-image: url('https://cdn.pixabay.com/photo/2014/04/02/10/55/music-306124_960_720.png');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            height: 100vh;
            margin: 0;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            text-shadow: 1px 1px 3px black;
        }

        .welcome-box {
            background-color: rgba(0, 0, 0, 0.7); /* Légèrement plus opaque pour mieux voir le texte */
            padding: 40px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5); /* Ombre pour un meilleur effet */
        }

        h1 {
            font-size: 3rem;
            margin-bottom: 1.5rem;
        }

        .btn-custom {
            background-color: #ffc107;
            border: none;
            font-weight: bold;
            color: #333; /* Texte plus foncé pour un meilleur contraste */
            padding: 12px 30px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .btn-custom:hover {
            background-color: #e0a800;
            transform: translateY(-2px); /* Petit effet au survol */
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
        }

        .btn-admin {
            background-color: #6f42c1; /* Couleur violette */
            border: none;
            font-weight: bold;
            color: white;
            padding: 12px 30px;
            border-radius: 8px;
            transition: all 0.3s ease;
            margin-top: 15px; /* Espace au-dessus du bouton admin */
        }

        .btn-admin:hover {
            background-color: #5a359a;
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
        }
    </style>
</head>
<body>
<div class="welcome-box">
    <h1>🎺 Bienvenue à la fanfare de Polytech 🎶</h1>
    <p class="lead mt-3">Rejoignez la communauté musicale la plus festive du campus !</p>

    <%
        // Récupérer l'objet Fanfaron de la session
        Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");

        // Vérifier si un utilisateur est connecté
        if (fanfaron != null) {
    %>
    <p class="mt-4">Bonjour, **<%= fanfaron.getPrenom() %>** !</p>
    <div class="d-grid gap-2 col-8 mx-auto">
        <a href="<%= request.getContextPath() %>/deconnexion" class="btn btn-outline-light btn-lg">
            <i class="fas fa-sign-out-alt"></i> Se déconnecter
        </a>
        <%
            // Vérifier si l'utilisateur est administrateur
            if (fanfaron.isAdmin()) {
        %>
        <a href="<%= request.getContextPath() %>/admin" class="btn btn-admin btn-lg">
            <i class="fas fa-user-shield"></i> Administration
        </a>
        <%
            }
        %>
    </div>
    <%
    } else {
        // Si personne n'est connecté, afficher le bouton de connexion
    %>
    <a href="<%= request.getContextPath() %>/connexion" class="btn btn-custom btn-lg mt-4">
        <i class="fas fa-sign-in-alt"></i> Se connecter
    </a>
    <%
        }
    %>
</div>
</body>
</html>