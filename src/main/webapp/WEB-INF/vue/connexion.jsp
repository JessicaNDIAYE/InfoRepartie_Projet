<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 17/05/2025
  Time: 19:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>FanfareHub - Connexion</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
  <style>
    body {
      background-color: #f8f9fa;
      padding-top: 100px;
    }
    .connexion-container {
      max-width: 400px;
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
  <div class="connexion-container">
    <h1>Connexion à FanfareHub</h1>

    <% if(request.getAttribute("errorMessage") != null) { %>
    <div class="alert alert-danger" role="alert">
      <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <form action="connexion" method="post">
      <div class="form-group">
        <label for="nomFanfaron">Nom de fanfaron ou email</label>
        <input type="text" class="form-control" id="nomFanfaron" name="nomFanfaron" required>
      </div>

      <div class="form-group">
        <label for="mdp">Mot de passe</label>
        <input type="password" class="form-control" id="mdp" name="mdp" required>
      </div>

      <button type="submit" class="btn btn-primary w-100">Se connecter</button>

      <div class="text-center mt-3">
        <p>Pas encore inscrit ? <a href="inscription">S'inscrire</a></p>
      </div>
    </form>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>