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
    private TypeEvenement typeEvenement; // Changement ici
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

    // Constructeur avec TypeEvenement
    public Evenement(String nom, Timestamp horodatage, Time duree, String lieu, String description,
                     TypeEvenement typeEvenement, int idCreateur) {
        this.nom = nom;
        this.horodatage = horodatage;
        this.duree = duree;
        this.lieu = lieu;
        this.description = description;
        this.typeEvenement = typeEvenement;
        this.idCreateur = idCreateur;
    }

    // Getters et setters existants
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

    // Nouveaux getters/setters pour TypeEvenement
    public TypeEvenement getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(TypeEvenement typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    // Méthodes de compatibilité pour l'ID du type
    public int getIdType() {
        return typeEvenement != null ? typeEvenement.getIdType() : 0;
    }

    public void setIdType(int idType) {
        // Cette méthode peut créer un TypeEvenement temporaire ou être utilisée
        // en combinaison avec une méthode DAO pour récupérer le TypeEvenement complet
        if (this.typeEvenement == null) {
            this.typeEvenement = new TypeEvenement(idType, ""); // Libellé vide temporaire
        } else {
            this.typeEvenement.setIdType(idType);
        }
    }

    @Override
    public String toString() {
        return "Evenement [id=" + id + ", nom=" + nom + ", horodatage=" + horodatage + ", duree=" + duree
                + ", lieu=" + lieu + ", description=" + description + ", typeEvenement=" + typeEvenement
                + ", idCreateur=" + idCreateur + "]";
    }
}