package Auberginn;

import java.util.LinkedList;
import java.util.List;

public class TupleChambre
{
    //region Fields
    private int idChambre;
    private String nomChambre;
    private String typeLit;
    private float prixBase;
    private List<TupleCommodite> commodites;
    //endregion

    //region Constructors
    public TupleChambre(int idChambre, String nomChambre, String typeLit, float prixBase) {
        this.idChambre = idChambre;
        this.nomChambre = nomChambre;
        this.typeLit = typeLit;
        this.prixBase = prixBase;
        this.commodites = new LinkedList<>();
    }
    //endregion

    //region Getters/Setters
    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public String getNomChambre() {
        return nomChambre;
    }

    public void setNomChambre(String nomChambre) {
        this.nomChambre = nomChambre;
    }

    public String getTypeLit() {
        return typeLit;
    }

    public void setTypeLit(String typeLit) {
        this.typeLit = typeLit;
    }

    public float getPrixBase() {
        return prixBase;
    }

    public void setPrixBase(float prixBase) {
        this.prixBase = prixBase;
    }

    public List<TupleCommodite> getCommodites() {
        return commodites;
    }

    public void setCommodites(List<TupleCommodite> commodites) {
        this.commodites = commodites;
    }

    public float getPrixTotal()
    {
        float total = prixBase;
        for (TupleCommodite com :
                commodites) {
            total += com.getSurplusPrix();
        }
        return total;
    }

    public void printInfo()
    {

        System.out.println("\n" + "Chambre : " + idChambre);
        System.out.println("Nom de la chambre : " + nomChambre);
        System.out.println("Type de lit : " + typeLit);
        System.out.println("Prix : " + getPrixTotal());
        if (!(commodites.size() > 0))
            System.out.println("Aucune commodité.");
        else
        {
            System.out.println("Commodités : ");
            printCommodites();
        }
    }

    private void printCommodites()
    {
        for (TupleCommodite com :
                commodites) {
            com.printInfo();
        }
    }

    public boolean possedeCommodite(int idCommodite)
    {
        for (TupleCommodite com : commodites) {
            if (com.getIdCommodite() == idCommodite)
                return true;
        }
        return false;
    }
    //endregion

}
