-- Table Fanfaron
CREATE TABLE Fanfaron (
    id_fanfaron INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom_fanfaron VARCHAR(50) NOT NULL UNIQUE, -- Identifiant unique
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    genre ENUM('homme', 'femme', 'autre') NOT NULL, -- Valeurs exactes mentionnées dans l'énoncé
    email VARCHAR(100) NOT NULL UNIQUE,
    mdp VARCHAR(255) NOT NULL, -- Pour stocker le mot de passe haché
    contraintes_alimentaires ENUM('aucune', 'végétarien', 'vegan', 'sans porc') DEFAULT 'aucune', -- Valeurs exactes mentionnées
    date_creation DATE NOT NULL,
    derniere_connexion DATETIME,
    admin BOOLEAN DEFAULT FALSE
);

-- Table Pupitre
CREATE TABLE Pupitre (
    id_pupitre INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- Table Groupe (pour les commissions)
CREATE TABLE Groupe (
    id_groupe INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- Table Type_Evenement
CREATE TABLE Type_Evenement (
    id_type INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- Table Evenement
CREATE TABLE Evenement (
    id_event INTEGER PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    horodatage DATETIME NOT NULL, -- Date ET heure
    duree TIME NOT NULL,
    lieu VARCHAR(100) NOT NULL,
    description TEXT,
    id_type INTEGER NOT NULL,
    id_createur INTEGER NOT NULL,
    FOREIGN KEY (id_type) REFERENCES Type_Evenement(id_type),
    FOREIGN KEY (id_createur) REFERENCES Fanfaron(id_fanfaron)
);

-- Table Statut_Participation (pour indiquer le statut de participation à un événement)
CREATE TABLE Statut_Participation (
    id_statut INTEGER PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(50) NOT NULL,
    couleur VARCHAR(20) NOT NULL
);

-- Table d'association Fanfaron_Pupitre
CREATE TABLE Fanfaron_Pupitre (
    id_fanfaron INTEGER,
    id_pupitre INTEGER,
    PRIMARY KEY (id_fanfaron, id_pupitre),
    FOREIGN KEY (id_fanfaron) REFERENCES Fanfaron(id_fanfaron),
    FOREIGN KEY (id_pupitre) REFERENCES Pupitre(id_pupitre)
);

-- Table d'association Fanfaron_Groupe
CREATE TABLE Fanfaron_Groupe (
    id_fanfaron INTEGER,
    id_groupe INTEGER,
    PRIMARY KEY (id_fanfaron, id_groupe),
    FOREIGN KEY (id_fanfaron) REFERENCES Fanfaron(id_fanfaron),
    FOREIGN KEY (id_groupe) REFERENCES Groupe(id_groupe)
);

-- Table d'association Fanfaron_Evenement_Participation
CREATE TABLE Fanfaron_Evenement_Participation (
    id_fanfaron INTEGER,
    id_event INTEGER,
    id_pupitre INTEGER NOT NULL, -- Instrument choisi pour l'événement
    id_statut_participation INTEGER NOT NULL, -- présent, absent ou incertain
    PRIMARY KEY (id_fanfaron, id_event),
    FOREIGN KEY (id_fanfaron) REFERENCES Fanfaron(id_fanfaron),
    FOREIGN KEY (id_event) REFERENCES Evenement(id_event),
    FOREIGN KEY (id_pupitre) REFERENCES Pupitre(id_pupitre),
    FOREIGN KEY (id_statut_participation) REFERENCES Statut_Participation(id_statut)
);

-- Insertion des données de base

-- Insertion des pupitres mentionnés dans l'énoncé
INSERT INTO Pupitre (libelle) VALUES 
('clarinette'), ('saxophone alto'), ('euphonium'), ('percussion'),
('basse'), ('trompette'), ('saxophone baryton'), ('trombone');

-- Insertion des commissions mentionnées dans l'énoncé
INSERT INTO Groupe (libelle) VALUES 
('commission prestation'), ('commission artistique'), 
('commission logistique'), ('commission communication interne');

-- Insertion des types d'événements mentionnés
INSERT INTO Type_Evenement (libelle) VALUES 
('atelier'), ('répétition'), ('prestation');

-- Insertion des statuts de participation avec leurs couleurs
INSERT INTO Statut_Participation (libelle, couleur) VALUES 
('présent', 'vert'), ('absent', 'rouge'), ('incertain', 'orange');
