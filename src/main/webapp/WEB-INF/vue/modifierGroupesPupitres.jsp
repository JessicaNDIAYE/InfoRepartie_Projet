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
<head>
    <meta charset="UTF-8" />
    <title>Modifier Groupes et Pupitres</title>
    <style>
        :root {
            --primary-blue: #4a90e2;
            --secondary-blue: #6ba3e8;
            --light-blue: #e6f0fa;
            --dark-blue: #2c5282;
            --accent-yellow: #ffd166;
            --paper-color: #f5f5f5;
            --line-color: #d9e8ff;
        }

        body {
            background-color: var(--light-blue);
            font-family: 'Comic Sans MS', 'Marker Felt', 'Segoe Print', cursive;
            color: #333;
            min-height: 100vh;
            padding: 40px 20px;
            position: relative;
            line-height: 1.6;
        }

        /* Effet cahier principal */
        body::before {
            content: "";
            position: absolute;
            top: 20px;
            left: 50px;
            right: 50px;
            bottom: 20px;
            background:
                /* Lignes horizontales */
                    repeating-linear-gradient(
                            var(--paper-color),
                            var(--paper-color) 29px,
                            var(--line-color) 30px,
                            var(--line-color) 31px
                    ),
                        /* Bordure bleue à gauche */
                    linear-gradient(
                            to right,
                            var(--primary-blue) 0px,
                            var(--primary-blue) 50px,
                            transparent 50px
                    );
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            z-index: -1;
        }

        /* Trous de cahier */
        body::after {
            content: "";
            position: absolute;
            left: 25px;
            top: 50%;
            transform: translateY(-50%);
            height: 60%;
            width: 20px;
            background:
                    radial-gradient(circle at 10px 50px, var(--primary-blue) 5px, transparent 6px),
                    radial-gradient(circle at 10px 150px, var(--primary-blue) 5px, transparent 6px),
                    radial-gradient(circle at 10px 250px, var(--primary-blue) 5px, transparent 6px);
            z-index: -1;
        }

        h2 {
            color: var(--dark-blue);
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 10px;
            font-size: 2rem;
            position: relative;
            display: inline-block;
            left: 50%;
            transform: translateX(-50%);
        }

        h2::after {
            content: "";
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, var(--primary-blue), var(--accent-yellow));
            border-radius: 3px;
        }

        form {
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
            border: 1px solid var(--light-blue);
            position: relative;
            background: linear-gradient(
                    to bottom,
                    white 0%,
                    white 98%,
                    var(--line-color) 98%,
                    var(--line-color) 100%
            );
            background-size: 100% 30px;
        }

        h3 {
            color: var(--dark-blue);
            margin: 20px 0 15px;
            padding-left: 15px;
            border-left: 4px solid var(--accent-yellow);
            font-size: 1.3rem;
        }

        input[type="checkbox"] {
            margin-right: 10px;
            width: 18px;
            height: 18px;
            accent-color: var(--primary-blue);
            position: relative;
            top: 2px;
        }

        label {
            display: inline-block;
            margin-bottom: 10px;
            padding-left: 5px;
            position: relative;
        }

        label::before {
            content: "→";
            color: var(--primary-blue);
            margin-right: 8px;
        }

        button[type="submit"] {
            display: block;
            width: 100%;
            padding: 12px;
            background-color: var(--primary-blue);
            color: white;
            border: none;
            border-radius: 50px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 30px;
            font-weight: bold;
            box-shadow: 0 3px 5px rgba(0, 0, 0, 0.1);
        }

        button[type="submit"]:hover {
            background-color: var(--dark-blue);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        a {
            display: inline-block;
            margin-top: 20px;
            color: var(--primary-blue);
            text-decoration: none;
            padding: 8px 16px;
            border: 2px solid var(--primary-blue);
            border-radius: 50px;
            transition: all 0.3s;
            font-weight: bold;
            position: relative;
            left: 50%;
            transform: translateX(-50%);
        }

        a:hover {
            background-color: var(--primary-blue);
            color: white;
        }

        /* Effet de stylo */
        .pen-icon {
            position: absolute;
            right: 30px;
            top: 30px;
            font-size: 24px;
            color: var(--dark-blue);
            transform: rotate(-30deg);
            opacity: 0.7;
        }

        /* Responsive */
        @media (max-width: 768px) {
            body::before {
                left: 20px;
                right: 20px;
            }

            body::after {
                left: 10px;
            }

            form {
                padding: 30px 20px;
            }
        }
    </style>


</head>
<body>
<h2>Modifier vos groupes et pupitres</h2>
<form action="modifiergroupespupitres" method="post">

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
<a href="<%= request.getContextPath() %>/tableaudebord">Retour au tableau de bord</a>
<!-- Ajoutez ceci avant la fermeture du body -->
<div class="pen-icon">✏️</div>
</body>
</html>
