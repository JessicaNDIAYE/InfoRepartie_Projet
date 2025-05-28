<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 18/05/2025
  Time: 03:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Fanfaron" %>
<%@ page import="dao.EvenementDAO" %>
<%@ page import="dao.DAOFactory" %>
<%@ page session="true" %>

<%
    Fanfaron fanfaron = (Fanfaron) session.getAttribute("fanfaron");
    if (fanfaron == null) {
        response.sendRedirect("connexion.jsp");
        return;
    }

    Boolean dansCommission = (Boolean) session.getAttribute("dansCommissionPrestation");
    if (dansCommission == null) {
        EvenementDAO evenementDAO = DAOFactory.getInstance().getEvenementDAO();
        dansCommission = evenementDAO.estDansCommissionPrestation(fanfaron.getId());
        session.setAttribute("dansCommissionPrestation", dansCommission);
    }
    request.setAttribute("dansCommissionPrestation", dansCommission);


%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <title>Tableau de bord - Fanfare de l'école</title>
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

        header {
            background-color: var(--primary-blue);
            color: white;
            padding: 20px 40px;
            border-radius: 12px;
            margin: 20px auto;
            max-width: 900px;
            text-align: center;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border: 2px solid var(--dark-blue);
            position: relative;
        }

        header::after {
            content: "";
            position: absolute;
            top: -10px;
            left: -10px;
            right: -10px;
            bottom: -10px;
            border: 2px dashed var(--secondary-blue);
            border-radius: 15px;
            z-index: -1;
            opacity: 0.6;
        }

        header h1 {
            margin: 0;
            font-weight: 700;
            font-size: 2rem;
            text-shadow: 1px 1px 3px rgba(0,0,0,0.2);
        }

        header p {
            margin: 5px 0 0;
            font-size: 1.1rem;
            font-weight: 500;
            color: white;
        }

        main {
            margin: 30px auto;
            max-width: 900px;
            position: relative;
        }

        section {
            background: white;
            padding: 25px 30px;
            margin-bottom: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.08);
            border: 1px solid var(--light-blue);
            position: relative;
        }

        section::before {
            content: "";
            position: absolute;
            top: 5px;
            left: 5px;
            right: 5px;
            bottom: 5px;
            border: 1px dashed var(--secondary-blue);
            border-radius: 5px;
            pointer-events: none;
            opacity: 0.3;
        }

        section h3 {
            margin-top: 0;
            border-bottom: 2px solid var(--accent-yellow);
            padding-bottom: 8px;
            color: var(--dark-blue);
            font-size: 1.3rem;
        }

        ul {
            list-style: none;
            padding-left: 0;
        }

        ul li {
            padding: 10px 0;
            font-size: 1rem;
            border-bottom: 1px dashed #eee;
            position: relative;
            padding-left: 25px;
        }

        ul li::before {
            content: "♪";
            position: absolute;
            left: 0;
            color: var(--primary-blue);
        }

        ul li:last-child {
            border-bottom: none;
        }

        a.btn {
            display: inline-block;
            padding: 12px 24px;
            margin-top: 15px;
            border-radius: 50px;
            text-decoration: none;
            font-weight: 600;
            color: white;
            transition: all 0.3s ease;
            box-shadow: 0 3px 5px rgba(0,0,0,0.1);
            position: relative;
            overflow: hidden;
        }

        a.btn::before {
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

        a.btn:hover::before {
            transform: scale(1);
        }

        a.btn-primary {
            background-color: var(--primary-blue);
        }

        a.btn-primary:hover {
            background-color: var(--dark-blue);
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        a.btn-success {
            background-color: #27ae60;
        }

        a.btn-success:hover {
            background-color: #1e8449;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        form {
            text-align: center;
            margin: 40px 0 30px;
        }

        button {
            background-color: #e74c3c;
            color: white;
            border: none;
            padding: 12px 24px;
            font-size: 1rem;
            border-radius: 50px;
            cursor: pointer;
            font-weight: 700;
            transition: all 0.3s ease;
            box-shadow: 0 3px 5px rgba(0,0,0,0.1);
        }

        button:hover {
            background-color: #c0392b;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }

        /* Notes de musique animées */
        .music-note {
            position: absolute;
            font-size: 24px;
            opacity: 0.6;
            animation: floatNote 5s infinite linear;
            color: var(--primary-blue);
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
        .note1 { left: 5%; top: 10%; animation-delay: 0s; }
        .note2 { right: 10%; top: 20%; animation-delay: 1s; }
        .note3 { left: 15%; bottom: 15%; animation-delay: 2s; }
        .note4 { right: 20%; bottom: 5%; animation-delay: 3s; }

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

        /* Responsive */
        @media (max-width: 768px) {
            body {
                padding: 10px;
            }

            body::before {
                left: 10px;
                width: calc(100% - 20px);
            }

            header {
                padding: 15px 20px;
            }

            section {
                padding: 20px;
            }
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
        <a href="<%= request.getContextPath() %>/modifiergroupespupitres" class="btn btn-primary">Modifier mes choix</a>
    </section>

    <section>
        <h3>Événements</h3>
        <p>Consultez et gérez votre participation aux événements.</p>

        <!-- Bouton pour voir les événements (toujours visible) -->
        <a href="<%= request.getContextPath() %>/listevenementservlet" class="btn btn-success">Voir les événements</a>

        <%
            Boolean commission = (Boolean) request.getAttribute("dansCommissionPrestation");
            if (commission != null && commission) {
        %>
        <!-- Bouton visible uniquement pour la commission Prestation -->
        <a href="<%= request.getContextPath() %>/creerevenement" class="btn btn-primary">Créer un événement</a>
        <%
            }
        %>
    </section>


    <form action="deconnexion" method="post">
        <button type="submit">Se déconnecter</button>
    </form>

</main>
<!-- Ajoutez ces éléments avant la fermeture du body -->
<div class="music-note note1">♪</div>
<div class="music-note note2">♫</div>
<div class="music-note note3">♩</div>
<div class="music-note note4">♬</div>
<div class="pen-icon">✏️</div>
</body>
</html>
