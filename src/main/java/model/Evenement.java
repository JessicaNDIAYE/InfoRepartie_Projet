package model;

import java.sql.Time;
import java.sql.Timestamp;

public class Evenement {
    private int id;
    private String nom;
    private Timestamp horodatage;
    private Time duree;
    private String lieu;
    private String description;
    private int idType;
    private int idCreateur;

    // Constructeurs
    public Evenement() {
    }

    public Evenement(String nom, Timestamp horodatage, Time duree, String lieu, String description, int idCreateur) {
        this.nom = nom;
        this.horodatage = horodatage;
        this.duree = duree;
        this.lieu = lieu;
        this.description = description;
        this.idCreateur = idCreateur;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Timestamp getHorodatage() {
        return horodatage;
    }

    public void setHorodatage(Timestamp horodatage) {
        this.horodatage = horodatage;
    }

    public Time getDuree() {
        return duree;
    }

    public void setDuree(Time duree) {
        this.duree = duree;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdCreateur() {
        return idCreateur;
    }

    public void setIdCreateur(int idCreateur) {
        this.idCreateur = idCreateur;
    }

    @Override
    public String toString() {
        return "Evenement [id=" + id + ", nom=" + nom + ", horodatage=" + horodatage + ", duree=" + duree
                + ", lieu=" + lieu + ", description=" + description + ", idCreateur=" + idCreateur + "]";
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getIdType() {
        return idType;
    }
}
