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
        :root {
            --primary-blue: #4a90e2;
            --secondary-blue: #6ba3e8;
            --light-blue: #e6f0fa;
            --dark-blue: #2c5282;
            --accent-yellow: #ffd166;
            --paper-color: #f5f5f5;
        }

        body {
            background-image:
                    linear-gradient(rgba(230, 240, 250, 0.9), rgba(230, 240, 250, 0.9)),
                    url('https://cdn.pixabay.com/photo/2017/01/31/23/42/decorative-2028039_1280.png');
            background-size: 30%, cover;
            background-repeat: no-repeat;
            background-position: center;
            font-family: 'Comic Sans MS', 'Marker Felt', 'Segoe Print', cursive;
            color: #333;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
            position: relative;
        }

        /* Effet cahier */
        body::before {
            content: "";
            position: absolute;
            top: 0;
            left: 50px;
            width: calc(100% - 100px);
            height: calc(100% - 40px);
            background: repeating-linear-gradient(
                    var(--paper-color),
                    var(--paper-color) 30px,
                    #d9e8ff 31px,
                    #d9e8ff 32px
            );
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            z-index: -1;
        }

        .welcome-box {
            background-color: white;
            padding: 40px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            border: 2px solid var(--primary-blue);
            position: relative;
            max-width: 600px;
            width: 100%;
        }

        .welcome-box::before {
            content: "";
            position: absolute;
            top: -15px;
            left: -15px;
            right: -15px;
            bottom: -15px;
            border: 2px dashed var(--secondary-blue);
            border-radius: 15px;
            z-index: -1;
            opacity: 0.6;
        }

        h1 {
            font-size: 2.5rem;
            margin-bottom: 1.5rem;
            color: var(--dark-blue);
            position: relative;
            display: inline-block;
        }

        h1::after {
            content: "";
            position: absolute;
            bottom: -5px;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, var(--primary-blue), var(--accent-yellow));
            border-radius: 3px;
        }

        .lead {
            font-size: 1.2rem;
            margin-bottom: 2rem;
            color: #555;
        }

        .btn-custom {
            background-color: var(--accent-yellow);
            border: none;
            font-weight: bold;
            color: #333;
            padding: 12px 30px;
            border-radius: 50px;
            transition: all 0.3s ease;
            margin: 10px;
            box-shadow: 0 3px 5px rgba(0, 0, 0, 0.1);
            position: relative;
            overflow: hidden;
        }

        .btn-custom:hover {
            background-color: #ffc233;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        .btn-custom::before {
            content: "";
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0) 70%);
            transform: scale(0);
            transition: transform 0.5s;
        }

        .btn-custom:hover::before {
            transform: scale(1);
        }

        .btn-admin {
            background-color: var(--primary-blue);
            color: white;
            border: none;
            font-weight: bold;
            padding: 12px 30px;
            border-radius: 50px;
            transition: all 0.3s ease;
            margin: 10px;
            box-shadow: 0 3px 5px rgba(0, 0, 0, 0.1);
        }

        .btn-admin:hover {
            background-color: var(--dark-blue);
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            color: white;
        }

        /* Notes de musique animées */
        .music-note {
            position: absolute;
            font-size: 24px;
            opacity: 0.6;
            animation: floatNote 5s infinite linear;
        }

        @keyframes floatNote {
            0% {
                transform: translateY(0) rotate(0deg);
                opacity: 0;
            }
            10% {
                opacity: 0.6;
            }
            90% {
                opacity: 0.6;
            }
            100% {
                transform: translateY(-100px) rotate(360deg);
                opacity: 0;
            }
        }

        /* Positionnement des notes */
        .note1 { left: 10%; top: 20%; animation-delay: 0s; }
        .note2 { right: 15%; top: 30%; animation-delay: 1s; }
        .note3 { left: 20%; bottom: 25%; animation-delay: 2s; }
        .note4 { right: 25%; bottom: 15%; animation-delay: 3s; }

        /* Stylo effet */
        .pen-icon {
            position: absolute;
            right: 20px;
            top: 20px;
            font-size: 24px;
            color: var(--dark-blue);
            transform: rotate(-30deg);
            opacity: 0.7;
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
        <a href="<%= request.getContextPath() %>/tableaudebord" class="btn btn-admin btn-lg">
            <i class="fas fa-music"></i> Page de la Fanfare
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
<!-- Ajoutez ces éléments avant la fermeture du body -->
<div class="music-note note1">♪</div>
<div class="music-note note2">♫</div>
<div class="music-note note3">♩</div>
<div class="music-note note4">♬</div>
<div class="pen-icon">✏️</div>
</body>
</html>