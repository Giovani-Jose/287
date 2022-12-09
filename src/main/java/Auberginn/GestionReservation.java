package Auberginn;

import java.sql.Date;
import java.util.List;

public class GestionReservation
{
    private final TableReservations reservation;
    private final TableClients client;
    private final TableChambres chambre;
    private final Connexion cx;

    /**
     * Creation d'une instance. La connection de l'instance de client et de
     * chambre doit être la même que cx, afin d'assurer l'intégrité des
     * transactions.
     */
    public GestionReservation(TableReservations reservation, TableClients client, TableChambres chambre) throws AuberginnException
    {
        if (client.getConnexion() != chambre.getConnexion() || reservation.getConnexion() != chambre.getConnexion())
            throw new AuberginnException(
                    "Les instances de client, de chambre et de reservation n'utilisent pas la même connexion au serveur");
        this.cx = client.getConnexion();
        this.client = client;
        this.chambre = chambre;
        this.reservation = reservation;
    }

    /**
     * Réservation d'une chambre par un client.
     */
    public void reserver(int idClient, int idChambre,String utilisateur, Date dateDebut, Date dateFin)
            throws Exception
    {
        try
        {
            // Vérifier que le client existe
            TupleClient tupleClient = client.getClient(utilisateur);
            if (tupleClient == null)
                throw new AuberginnException("Client inexistant: " + idClient);

            // Vérifier que la chambre existe
            TupleChambre tupleChambre = chambre.getChambre(idChambre);
            if (tupleChambre == null)
                throw new AuberginnException("Chambre inexistante: " + idChambre);

            // Vérifier que la chambre n'est pas déjà réservée dans la période donnée
            List<TupleReservation> listReservationChambre = reservation.getReservationChambre(idChambre);
            if (listReservationChambre.size() > 0)
            {
                for (TupleReservation res : listReservationChambre)
                {
                    if (dateDebut.before(res.getDateFin()) && res.getDateDebut().before(dateFin))
                        throw new AuberginnException ("Chambre " + idChambre + " déjà réservée pour cette période.");

                }
            }

            // Vérifier que le client n'a pas déjà une réservation dans la période donnée
            List<TupleReservation> listReservationClient = reservation.getReservationClient(utilisateur);
            if (listReservationClient.size() > 0)
            {
                for (TupleReservation res : listReservationClient)
                {
                    if (dateDebut.before(res.getDateFin()) && res.getDateDebut().before(dateFin))
                        throw new AuberginnException ("Le client " + idChambre + " à déjà une réservation pour cette période.");

                }
            }

            // Calcul du prix total
            float prixTotal = tupleChambre.getPrixBase();
            for (TupleCommodite com : tupleChambre.getCommodites())
            {
                prixTotal += com.getSurplusPrix();
            }

            // Creation de la reservation
            reservation.reserver(idClient, idChambre,utilisateur, prixTotal, dateDebut, dateFin);

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
