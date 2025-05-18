package model;

import java.sql.Date;

public class Fanfaron {
    private int id;
    private String nomFanfaron;
    private String nom;
    private String prenom;
    private String genre;
    private String email;
    private String mdp;
    private String contraintesAlimentaires;
    private Date dateCreation;
    private Date derniereConnexion;
    private boolean admin;

    // Constructeurs
    public Fanfaron() {
    }

    public Fanfaron(String nomFanfaron, String nom, String prenom, String genre, String email,
                    String mdp, String contraintesAlimentaires) {
        this.nomFanfaron = nomFanfaron;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.email = email;
        this.mdp = mdp;
        this.contraintesAlimentaires = contraintesAlimentaires;
        this.dateCreation = new Date(System.currentTimeMillis());
        this.admin = false;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomFanfaron() {
        return nomFanfaron;
    }

    public void setNomFanfaron(String nomFanfaron) {
        this.nomFanfaron = nomFanfaron;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getContraintesAlimentaires() {
        return contraintesAlimentaires;
    }

    public void setContraintesAlimentaires(String contraintesAlimentaires) {
        this.contraintesAlimentaires = contraintesAlimentaires;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(Date derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Fanfaron [id=" + id + ", nomFanfaron=" + nomFanfaron + ", nom=" + nom + ", prenom=" + prenom
                + ", genre=" + genre + ", email=" + email + ", contraintesAlimentaires=" + contraintesAlimentaires
                + ", dateCreation=" + dateCreation + ", derniereConnexion=" + derniereConnexion + ", admin=" + admin + "]";
    }
}