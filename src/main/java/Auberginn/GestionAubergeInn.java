package Auberginn;



import auberginnServlet.GestionInterrogation;

import java.sql.SQLException;

public class GestionAubergeInn
{
    private final Connexion cx;
    private final TableClients clients;
    private final TableChambres chambres;
    private final TableReservations reservations;
    private  final TableServices services;
    private final TableCommodites commodites;
    private GestionClient gestionClient;
    private GestionChambre gestionChambre;
    private GestionReservation gestionReservation;
    private GestionService gestionService;
    private GestionCommodite gestionCommodite;
    private GestionInterrogation gestionInterrogation;



    public GestionAubergeInn(String serveur, String bd, String user, String password)
            throws AuberginnException, SQLException
    {
        cx = new Connexion(serveur, bd, user, password);
        clients = new TableClients(cx);
        chambres = new TableChambres(cx);
        reservations = new TableReservations(cx);
        services = new TableServices(cx);
        commodites = new TableCommodites(cx);
        setGestionClient(new GestionClient(clients, reservations));
        setGestionChambre(new GestionChambre(chambres, reservations));
        setGestionCommodite(new GestionCommodite(commodites, services));
        setGestionReservation(new GestionReservation(reservations, clients, chambres));
        setGestionService(new GestionService(chambres, commodites, services));
        setGestionInterrogation(new GestionInterrogation(getConnexion()));

    }

    public void fermer() throws SQLException
    {
        // Fermeture de la connexion
        cx.fermer();
    }

    public GestionClient getGestionClient() {
        return gestionClient;
    }

    public void setGestionClient(GestionClient gestionClient) {
        this.gestionClient = gestionClient;
    }

    public GestionChambre getGestionChambre() {
        return gestionChambre;
    }

    public void setGestionChambre(GestionChambre gestionChambre) {
        this.gestionChambre = gestionChambre;
    }

    public GestionReservation getGestionReservation() {
        return gestionReservation;
    }

    public void setGestionReservation(GestionReservation gestionReservation) {
        this.gestionReservation = gestionReservation;
    }

    public GestionService getGestionService() {
        return gestionService;
    }

    public void setGestionService(GestionService gestionService) {
        this.gestionService = gestionService;
    }

    public GestionCommodite getGestionCommodite() {
        return gestionCommodite;
    }

    public void setGestionCommodite(GestionCommodite gestionCommodite) {
        this.gestionCommodite = gestionCommodite;
    }

    public Connexion getConnexion() {
        return cx;
    }

    private void setGestionInterrogation(GestionInterrogation gestionInterrogation)
    {
        this.gestionInterrogation = gestionInterrogation;
    }

    public GestionInterrogation getGestionInterrogation()
    {
        return gestionInterrogation;
    }


}
