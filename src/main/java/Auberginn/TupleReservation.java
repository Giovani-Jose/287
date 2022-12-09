package Auberginn;

import java.sql.Date;

public class TupleReservation
{
    private String utilisateur;
    private int idChambre;
    private float prixTotal;
    private Date dateDebut;
    private Date dateFin;

    public TupleReservation() {
    }

    public TupleReservation(String utilisateur, int idChambre, float prixTotal, Date dateDebut, Date dateFin) {
        this.utilisateur = utilisateur;
        this.idChambre = idChambre;
        this.prixTotal = prixTotal;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public void printInfo()
    {
        System.out.println("Chambre : " + idChambre);
        System.out.println("Date de début : " + dateDebut.toString());
        System.out.println("Date de fin : " + dateFin.toString());
        System.out.println("Prix total : " + prixTotal);

    }
}
