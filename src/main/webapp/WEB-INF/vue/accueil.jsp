<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 18/05/2025
  Time: 01:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Bienvenue à la Fanfare de Polytech</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

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
            background-color: rgba(0, 0, 0, 0.6);
            padding: 40px;
            border-radius: 12px;
            text-align: center;
        }

        h1 {
            font-size: 3rem;
        }

        .btn-custom {
            background-color: #ffc107;
            border: none;
            font-weight: bold;
        }

        .btn-custom:hover {
            background-color: #e0a800;
        }
    </style>
</head>
<body>
<div class="welcome-box">
    <h1>🎺 Bienvenue à la fanfare de Polytech 🎶</h1>
    <p class="lead mt-3">Rejoignez la communauté musicale la plus festive du campus !</p>
    <a href="<%= request.getContextPath() %>/connexion" class="btn btn-custom btn-lg mt-4">Se connecter</a>
</div>
</body>
</html>

