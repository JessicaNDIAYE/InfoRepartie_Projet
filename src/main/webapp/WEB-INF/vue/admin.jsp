<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Fanfaron" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Administration - FanfareHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .admin-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .user-card {
            transition: transform 0.2s;
        }
        .user-card:hover {
            transform: translateY(-2px);
        }
        .admin-badge {
            background-color: #28a745;
        }
        .btn-admin {
            background-color: #6f42c1;
            border-color: #6f42c1;
        }
        .btn-admin:hover {
            background-color: #5a359a;
            border-color: #5a359a;
        }
        .edit-form {
            background-color: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 8px;
            padding: 1.5rem;
            margin-top: 4rem;
        }
    </style>
</head>
<body>
<div class="admin-header">
    <div class="container">
        <div class="row align-items-center">
            <div class="col">
                <h1><i class="fas fa-users-cog"></i> Administration FanfareHub</h1>
                <p class="mb-0">Gestion des utilisateurs de la fanfare</p>
            </div>
            <div class="col-auto">
                <a href="<%= request.getContextPath() %>/accueil" class="btn btn-light">
                    <i class="fas fa-home"></i> Retour à l'accueil
                </a>
                <a href="<%= request.getContextPath() %>/deconnexion" class="btn btn-outline-light ms-2">
                    <i class="fas fa-sign-out-alt"></i> Déconnexion
                </a>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <% if(request.getAttribute("successMessage") != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="fas fa-check-circle"></i> <%= request.getAttribute("successMessage") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <% if(request.getAttribute("errorMessage") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("errorMessage") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <% List<Fanfaron> fanfarons = (List<Fanfaron>) request.getAttribute("fanfarons");
        int totalUsers = fanfarons != null ? fanfarons.size() : 0;
        long adminCount = fanfarons != null ? fanfarons.stream().filter(Fanfaron::isAdmin).count() : 0;
    %>

    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card border-primary">
                <div class="card-body text-center">
                    <i class="fas fa-users fa-3x text-primary mb-3"></i>
                    <h3><%= totalUsers %></h3>
                    <p class="card-text">Utilisateurs total</p>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card border-success">
                <div class="card-body text-center">
                    <i class="fas fa-user-shield fa-3x text-success mb-3"></i>
                    <h3><%= adminCount %></h3>
                    <p class="card-text">Administrateurs</p>
                </div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h3><i class="fas fa-list"></i> Liste des utilisateurs</h3>
        </div>
        <div class="card-body">
            <% if(fanfarons != null && !fanfarons.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nom de fanfaron</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Genre</th>
                        <th>Contraintes</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for(Fanfaron fanfaron : fanfarons) { %>
                    <tr>
                        <td><%= fanfaron.getId() %></td>
                        <td><strong><%= fanfaron.getNomFanfaron() %></strong></td>
                        <td><%= fanfaron.getNom() %></td>
                        <td><%= fanfaron.getPrenom() %></td>
                        <td><%= fanfaron.getEmail() %></td>
                        <td><%= fanfaron.getGenre() %></td>
                        <td><%= fanfaron.getContraintesAlimentaires() %></td>
                        <td>
                            <% if(fanfaron.isAdmin()) { %>
                            <span class="badge admin-badge">
                                <i class="fas fa-crown"></i> Admin
                            </span>
                            <% } else { %>
                            <span class="badge bg-secondary">Utilisateur</span>
                            <% } %>
                        </td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="admin?action=edit&id=<%= fanfaron.getId() %>" class="btn btn-sm btn-outline-primary" title="Modifier">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <form action="admin" method="post" style="display:inline-block;"
                                      onsubmit="return confirm('Êtes-vous sûr de vouloir basculer le statut administrateur ?')">
                                    <input type="hidden" name="action" value="toggleAdmin">
                                    <input type="hidden" name="id" value="<%= fanfaron.getId() %>">
                                    <button type="submit" class="btn btn-sm btn-admin" title="Basculer admin">
                                        <i class="fas fa-user-shield"></i>
                                    </button>
                                </form>
                                <a href="admin?action=delete&id=<%= fanfaron.getId() %>" class="btn btn-sm btn-outline-danger" title="Supprimer"
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="text-center py-4">
                <i class="fas fa-users fa-3x text-muted mb-3"></i>
                <h4>Aucun utilisateur trouvé</h4>
                <p class="text-muted">La base de données ne contient aucun utilisateur.</p>
            </div>
            <% } %>
        </div>
    </div>

    <div class="edit-form">
        <h3><i class="fas fa-user-plus"></i> Ajouter un utilisateur</h3>
        <form action="admin" method="post">
            <input type="hidden" name="action" value="add">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Nom de fanfaron</label>
                    <input type="text" name="nomFanfaron" class="form-control" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Nom</label>
                    <input type="text" name="nom" class="form-control" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Prénom</label>
                    <input type="text" name="prenom" class="form-control" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Genre</label>
                    <select name="genre" class="form-control" required>
                        <option value="homme">Homme</option>
                        <option value="femme">Femme</option>
                        <option value="autre">Autre</option>
                    </select>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Contraintes alimentaires</label>
                    <select name="contraintesAlimentaires" class="form-control">
                        <option value="aucune">Aucune</option>
                        <option value="végétarien">Végétarien</option>
                        <option value="vegan">Vegan</option>
                        <option value="sans porc">Sans porc</option>
                    </select>
                </div>
                <div class="col-md-12 mb-3">
                    <label class="form-label">Mot de passe</label>
                    <input type="password" name="motDePasse" class="form-control" required>
                </div>
                <div class="form-check mb-3">
                    <input type="checkbox" class="form-check-input" id="adminCheckbox" name="isAdmin">
                    <label class="form-check-label" for="adminCheckbox">Administrateur</label>
                </div>
            </div>
            <button type="submit" class="btn btn-success">
                <i class="fas fa-plus-circle"></i> Ajouter l'utilisateur
            </button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
