package model;

public class Participation {
    private int idFanfaron;
    private int idEvenement;
    private int idPupitre;
    private int idStatutParticipation;
    private boolean estCreateur;

    // Constructeur
    public Participation() {
    }

    public Participation(int idFanfaron, int idEvenement, int idPupitre, int idStatutParticipation, boolean estCreateur) {
        this.idFanfaron = idFanfaron;
        this.idEvenement = idEvenement;
        this.idPupitre = idPupitre;
        this.idStatutParticipation = idStatutParticipation;
        this.estCreateur = estCreateur;
    }

    // Getters et setters
    public int getIdFanfaron() {
        return idFanfaron;
    }

    public void setIdFanfaron(int idFanfaron) {
        this.idFanfaron = idFanfaron;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public int getIdPupitre() {
        return idPupitre;
    }

    public void setIdPupitre(int idPupitre) {
        this.idPupitre = idPupitre;
    }

    public int getIdStatutParticipation() {
        return idStatutParticipation;
    }

    public void setIdStatutParticipation(int idStatutParticipation) {
        this.idStatutParticipation = idStatutParticipation;
    }

    public boolean isEstCreateur() {
        return estCreateur;
    }

    public void setEstCreateur(boolean estCreateur) {
        this.estCreateur = estCreateur;
    }

    @Override
    public String toString() {
        return "Participation [idFanfaron=" + idFanfaron + ", idEvenement=" + idEvenement + ", idPupitre=" + idPupitre
                + ", idStatutParticipation=" + idStatutParticipation + ", estCreateur=" + estCreateur + "]";
    }
}