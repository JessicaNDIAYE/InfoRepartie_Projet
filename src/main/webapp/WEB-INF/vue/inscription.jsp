<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 17/05/2025
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FanfareHub - Inscription</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 50px;
        }
        .inscription-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .form-group {
            margin-bottom: 20px;
        }
        h1 {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="inscription-container">
        <h1>Inscription à FanfareHub</h1>

        <% if(request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= request.getAttribute("errorMessage") %>
        </div>
        <% } %>

        <form action="inscription" method="post" id="inscriptionForm">
            <div class="form-group">
                <label for="nomFanfaron">Nom de fanfaron (identifiant)</label>
                <input type="text" class="form-control" id="nomFanfaron" name="nomFanfaron" required>
            </div>

            <div class="form-group">
                <label for="nom">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" required>
            </div>

            <div class="form-group">
                <label for="prenom">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" required>
            </div>

            <div class="form-group">
                <label for="genre">Genre</label>
                <select class="form-control" id="genre" name="genre" required>
                    <option value="">Sélectionnez</option>
                    <option value="homme">Homme</option>
                    <option value="femme">Femme</option>
                    <option value="autre">Autre</option>
                </select>
            </div>

            <div class="form-group">
                <label for="email">Adresse e-mail</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="confirmEmail">Confirmation de l'adresse e-mail</label>
                <input type="email" class="form-control" id="confirmEmail" name="emailConfirmation" required>
            </div>

            <div class="form-group">
                <label for="mdp">Mot de passe</label>
                <input type="password" class="form-control" id="mdp" name="mdp" required>
                <small class="form-text text-muted">Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule et un chiffre.</small>
            </div>

            <div class="form-group">
                <label for="confirmMdp">Confirmation du mot de passe</label>
                <input type="password" class="form-control" id="confirmMdp" name="mdpConfirmation" required>

            <div class="form-group">
                <label for="contraintes">Contraintes alimentaires</label>
                <select class="form-control" id="contraintes" name="contraintesAlimentaires">
                <option value="aucune">Aucune</option>
                    <option value="végétarien">Végétarien</option>
                    <option value="vegan">Vegan</option>
                    <option value="sans porc">Sans porc</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary w-100">S'inscrire</button>

            <div class="text-center mt-3">
                <p>Déjà inscrit ? <a href="connexion.jsp">Se connecter</a></p>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('inscriptionForm').addEventListener('submit', function(event) {
        const email = document.getElementById('email').value;
        const confirmEmail = document.getElementById('confirmEmail').value;
        const mdp = document.getElementById('mdp').value;
        const confirmMdp = document.getElementById('confirmMdp').value;

        let isValid = true;

        // Vérification des emails
        if (email !== confirmEmail) {
            alert('Les adresses e-mail ne correspondent pas !');
            isValid = false;
        }

        // Vérification des mots de passe
        if (mdp !== confirmMdp) {
            alert('Les mots de passe ne correspondent pas !');
            isValid = false;
        }

        // Vérification de la complexité du mot de passe
        const mdpRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!mdpRegex.test(mdp)) {
            alert('Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule et un chiffre !');
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
</script>
</body>
</html>