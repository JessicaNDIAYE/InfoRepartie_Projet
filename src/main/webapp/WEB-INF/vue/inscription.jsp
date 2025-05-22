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

        <%-- Message d'erreur général (si l'insertion DAO échoue) --%>
        <% if(request.getAttribute("errorMessage") != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= request.getAttribute("errorMessage") %>
        </div>
        <% } %>

        <form action="inscription" method="post" id="inscriptionForm">
            <div class="form-group">
                <label for="nomFanfaron">Nom de fanfaron (identifiant)</label>
                <input type="text" class="form-control <%= request.getAttribute("errorNomFanfaron") != null ? "is-invalid" : "" %>"
                       id="nomFanfaron" name="nomFanfaron" value="<%= request.getAttribute("nomFanfaron") != null ? request.getAttribute("nomFanfaron") : "" %>" required>
                <% if(request.getAttribute("errorNomFanfaron") != null) { %>
                <div class="invalid-feedback">
                    <%= request.getAttribute("errorNomFanfaron") %>
                </div>
                <% } %>
            </div>

            <div class="form-group">
                <label for="nom">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>" required>
            </div>

            <div class="form-group">
                <label for="prenom">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" value="<%= request.getAttribute("prenom") != null ? request.getAttribute("prenom") : "" %>" required>
            </div>

            <div class="form-group">
                <label for="genre">Genre</label>
                <select class="form-control" id="genre" name="genre" required>
                    <option value="">Sélectionnez</option>
                    <option value="homme" <%= "homme".equals(request.getAttribute("genre")) ? "selected" : "" %>>Homme</option>
                    <option value="femme" <%= "femme".equals(request.getAttribute("genre")) ? "selected" : "" %>>Femme</option>
                    <option value="autre" <%= "autre".equals(request.getAttribute("genre")) ? "selected" : "" %>>Autre</option>
                </select>
            </div>

            <div class="form-group">
                <label for="email">Adresse e-mail</label>
                <input type="email" class="form-control <%= request.getAttribute("errorEmail") != null ? "is-invalid" : "" %>"
                       id="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                <% if(request.getAttribute("errorEmail") != null) { %>
                <div class="invalid-feedback">
                    <%= request.getAttribute("errorEmail") %>
                </div>
                <% } %>
            </div>

            <div class="form-group">
                <label for="confirmEmail">Confirmation de l'adresse e-mail</label>
                <input type="email" class="form-control <%= request.getAttribute("errorEmailConfirmation") != null ? "is-invalid" : "" %>"
                       id="confirmEmail" name="emailConfirmation" required>
                <% if(request.getAttribute("errorEmailConfirmation") != null) { %>
                <div class="invalid-feedback">
                    <%= request.getAttribute("errorEmailConfirmation") %>
                </div>
                <% } %>
            </div>

            <div class="form-group">
                <label for="mdp">Mot de passe</label>
                <input type="password" class="form-control <%= request.getAttribute("errorMdpConfirmation") != null ? "is-invalid" : "" %>"
                       id="mdp" name="mdp" required>
                <small class="form-text text-muted">Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule et un chiffre.</small>
            </div>

            <div class="form-group">
                <label for="confirmMdp">Confirmation du mot de passe</label>
                <input type="password" class="form-control <%= request.getAttribute("errorMdpConfirmation") != null ? "is-invalid" : "" %>"
                       id="confirmMdp" name="mdpConfirmation" required>
                <% if(request.getAttribute("errorMdpConfirmation") != null) { %>
                <div class="invalid-feedback">
                    <%= request.getAttribute("errorMdpConfirmation") %>
                </div>
                <% } %>
            </div>

            <div class="form-group">
                <label for="contraintes">Contraintes alimentaires</label>
                <select class="form-control" id="contraintes" name="contraintesAlimentaires">
                    <option value="aucune" <%= "aucune".equals(request.getAttribute("contraintesAlimentaires")) ? "selected" : "" %>>Aucune</option>
                    <option value="végétarien" <%= "végétarien".equals(request.getAttribute("contraintesAlimentaires")) ? "selected" : "" %>>Végétarien</option>
                    <option value="vegan" <%= "vegan".equals(request.getAttribute("contraintesAlimentaires")) ? "selected" : "" %>>Vegan</option>
                    <option value="sans porc" <%= "sans porc".equals(request.getAttribute("contraintesAlimentaires")) ? "selected" : "" %>>Sans porc</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary w-100">S'inscrire</button>

            <div class="text-center mt-3">
                <p>Déjà inscrit ? <a href="<%= request.getContextPath() %>/connexion">Se connecter</a></p>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('inscriptionForm').addEventListener('submit', function(event) {
        const email = document.getElementById('email').value.trim();
        const confirmEmail = document.getElementById('confirmEmail').value.trim();
        const mdp = document.getElementById('mdp').value.trim();
        const confirmMdp = document.getElementById('confirmMdp').value.trim();

        let isValid = true;

        // Réinitialiser les messages d'erreur Bootstrap
        document.getElementById('email').classList.remove('is-invalid');
        document.getElementById('confirmEmail').classList.remove('is-invalid');
        document.getElementById('mdp').classList.remove('is-invalid');
        document.getElementById('confirmMdp').classList.remove('is-invalid');
        // Cache les messages d'erreur existants pour ne pas les accumuler
        const invalidFeedbacks = document.querySelectorAll('.invalid-feedback');
        invalidFeedbacks.forEach(feedback => feedback.remove());


        // Vérification des emails
        if (email !== confirmEmail) {
            alert('Les adresses e-mail ne correspondent pas !'); // Alerte JS temporaire
            document.getElementById('email').classList.add('is-invalid');
            document.getElementById('confirmEmail').classList.add('is-invalid');
            // Ajout d'un message d'erreur spécifique côté client pour Bootstrap
            document.getElementById('confirmEmail').insertAdjacentHTML('afterend', '<div class="invalid-feedback">Les adresses e-mail ne correspondent pas !</div>');
            isValid = false;
        }

        // Vérification des mots de passe
        if (mdp !== confirmMdp) {
            alert('Les mots de passe ne correspondent pas !'); // Alerte JS temporaire
            document.getElementById('mdp').classList.add('is-invalid');
            document.getElementById('confirmMdp').classList.add('is-invalid');
            document.getElementById('confirmMdp').insertAdjacentHTML('afterend', '<div class="invalid-feedback">Les mots de passe ne correspondent pas !</div>');
            isValid = false;
        }

        // Vérification de la complexité du mot de passe
        const mdpRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!mdpRegex.test(mdp)) {
            alert('Le mot de passe doit contenir au moins 8 caractères, dont une majuscule, une minuscule et un chiffre !'); // Alerte JS temporaire
            document.getElementById('mdp').classList.add('is-invalid');
            document.getElementById('mdp').insertAdjacentHTML('afterend', '<div class="invalid-feedback">Format du mot de passe incorrect.</div>');
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault(); // Empêche l'envoi du formulaire si validation côté client échoue
        }
    });
</script>
</body>
</html>