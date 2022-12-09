package Auberginn;

public class TupleCommodite
{
    //region Fields
    private int idCommodite;
    private String description;
    private float surplusPrix;
    //endregion

    //region Constructors
    public TupleCommodite() {
    }

    public TupleCommodite(int idCommodite, String description, float surplusPrix) {
        this.idCommodite = idCommodite;
        this.description = description;
        this.surplusPrix = surplusPrix;
    }
    //endregion

    //region Getters/Setters
    public int getIdCommodite() {
        return idCommodite;
    }

    public void setIdCommodite(int idCommodite) {
        this.idCommodite = idCommodite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getSurplusPrix() {
        return surplusPrix;
    }

    public void setSurplusPrix(float surplusPrix) {
        this.surplusPrix = surplusPrix;
    }

    public void printInfo()
    {
        System.out.println("\n" + "Commodité numéro : " + idCommodite);
        System.out.println("Description : " + description);
        System.out.println("Prix en surplus : " + surplusPrix);
    }
    //endregion
}
