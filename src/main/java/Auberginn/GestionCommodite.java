package Auberginn;


public class GestionCommodite {
    private final TableCommodites commodites;
    private final TableServices services;
    private final Connexion cx;

    public GestionCommodite(TableCommodites commodites, TableServices services) throws AuberginnException
    {
        this.cx = commodites.getConnexion();
        if (commodites.getConnexion() != services.getConnexion())
            throw new AuberginnException("Les instances de chambre et de reservation n'utilisent pas la même connexion au serveur");
        this.commodites = commodites;
        this.services = services;
    }

    public void ajouterCommodite(int idCommodite, String description, float surplusPrix)
            throws Exception
    {
        try
        {
            // Vérifie si la commodité existe déja
            if (commodites.existe(idCommodite))
                throw new AuberginnException("La commodité " + idCommodite + " existe déjà.");

            // Ajout de la chambre dans la table des chambres
            commodites.ajouterCommodite(idCommodite, description, surplusPrix);

            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
}
