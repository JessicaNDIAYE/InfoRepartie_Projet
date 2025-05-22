<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>FanfareHub - Inscription</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
    <style>
        :root {
            --primary-blue: #4a90e2;
            --secondary-blue: #6ba3e8;
            --light-blue: #e6f0fa;
            --dark-blue: #2c5282;
            --paper-color: #f5f5f5;
            --line-color: #d9e8ff;
        }

        body {
            background-color: var(--light-blue);
            font-family: 'Comic Sans MS', 'Marker Felt', 'Segoe Print', cursive;
            color: #333;
            min-height: 100vh;
            padding: 50px 20px;
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

        .container {
            position: relative;
        }

        .inscription-container {
            max-width: 600px;
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

        h1 {
            text-align: center;
            margin-bottom: 30px;
            color: var(--dark-blue);
            font-size: 2rem;
            position: relative;
        }

        h1::after {
            content: "";
            position: absolute;
            bottom: -10px;
            left: 25%;
            width: 50%;
            height: 3px;
            background: linear-gradient(90deg, var(--primary-blue), var(--secondary-blue));
            border-radius: 3px;
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-group label {
            font-weight: bold;
            color: var(--dark-blue);
            margin-bottom: 8px;
            display: block;
        }

        .form-control {
            border: 1px solid var(--secondary-blue);
            border-radius: 4px;
            padding: 10px 15px;
            font-size: 1rem;
            transition: all 0.3s;
        }

        .form-control:focus {
            border-color: var(--primary-blue);
            box-shadow: 0 0 0 0.25rem rgba(74, 144, 226, 0.25);
        }

        .btn-primary {
            background-color: var(--primary-blue);
            border: none;
            padding: 12px;
            font-size: 1.1rem;
            font-weight: bold;
            transition: all 0.3s;
        }

        .btn-primary:hover {
            background-color: var(--dark-blue);
            transform: translateY(-2px);
        }

        .text-center a {
            color: var(--primary-blue);
            font-weight: bold;
            text-decoration: none;
            transition: all 0.3s;
        }

        .text-center a:hover {
            color: var(--dark-blue);
            text-decoration: underline;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border-color: #f5c6cb;
        }

        .invalid-feedback {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 5px;
        }

        .is-invalid {
            border-color: #dc3545;
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

            .inscription-container {
                padding: 30px 20px;
            }
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