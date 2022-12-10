package Auberginn;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class GestionClient
{
    private final TableClients client;
    private final TableReservations reservation;
    private final Connexion connex;

    public GestionClient(TableClients client, TableReservations reservation) throws AuberginnException
    {
        this.connex = client.getConnexion();
        if (client.getConnexion() != reservation.getConnexion())
            throw new AuberginnException("Les instances de client et de reservation n'utilisent pas la même connexion au serveur");
        this.client = client;
        this.reservation = reservation;
    }

   /* public void ajouterClient(int idClient, String nom, String prenom, int age)
            throws Exception
    {
        try
        {
            // Vérifie si la chambre existe déja
            if (client.existe(idClient))
                throw new AuberginnException("Le client : " + idClient + " existe déjà.");

            // Ajout de la chambre dans la table des chambres
            client.ajouterClient(idClient, nom, prenom, age);

            // Commit
            connex.commit();
        }
        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }*/


    public void inscrire(String utilisateur, String motDePasse, int acces, String nom, String prenom, int age)
            throws SQLException, AuberginnException, Exception
    {
        try
        {
            // Vérifie si le membre existe déja
            if (client.existe(utilisateur))
                throw new AuberginnException("L'utilisateur " + utilisateur + " existe déjà.");


            if(acces != 0 && acces != 1)
                throw new AuberginnException("Le niveau d'accès doit être 0 (administrateur) ou 1 (membre).");

            // Ajout du membre.
            client.inscrire(utilisateur, motDePasse, acces, nom, prenom,age);

            // Commit
            connex.commit();
        }
        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

    public void desinscrire(String utilisateur) throws Exception
    {
        try
        {
            // Validations
            // Client existe
            TupleClient tupleClient = client.getClient(utilisateur);
            if (tupleClient == null)
                throw new AuberginnException("Client inexistant: " + utilisateur);

            // Client à une réservation présente ou future
            List<TupleReservation> tupleReservation = reservation.getReservationClient(utilisateur);
            if (tupleReservation.size() > 0)
            {
                Date date = new Date(Calendar.getInstance().getTime().getTime());
                for (TupleReservation res : tupleReservation)
                {
                    if (date.before(res.getDateFin()))
                        throw new AuberginnException("Client " + utilisateur + " à une réservation en cours.");
                }
            }

            // Suppression du client.
            int nb = client.desinscrire(utilisateur);
            if (nb == 0)
                throw new AuberginnException("Client " + utilisateur + " inexistant.");

            // Commit
            connex.commit();
        }
        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }





    public TupleClient afficherClient(String user) throws Exception
    {
        try
        {
            // Validations
            // Client existe
            TupleClient tupleClient = client.getClient(user);
            // Affichage :
            if (tupleClient == null)
                throw new AuberginnException("Client inexistant: " + user);
            return tupleClient;
        }

        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }

    }

    public List<TupleReservation> afficherReservation(String user) throws SQLException {
        // Chercher toutes les réservations du client

        return reservation.getReservationClient(user);
    }

    public boolean informationsConnexionValide(String userId, String motDePasse)
            throws SQLException, AuberginnException
    {
        try
        {
            // Vérifie si le membre existe déja
            if (!client.existe(userId))
                throw new AuberginnException("Aucun utilisateur n'existe avec ce nom.");


            TupleClient user = client.getClient(userId);
            if(!user.getMotDePasse().equals(motDePasse))
                throw new AuberginnException("Mauvais mot de passe.");

            // Commit
            connex.commit();

            return true;
        }
        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

    public boolean utilisateurEstAdministrateur(String utilisateur)
            throws SQLException, AuberginnException
    {
        try
        {
            TupleClient m = client.getClient(utilisateur);
            if(m == null)
                throw new AuberginnException("L'utilisateur n'existe pas");

            connex.commit();
            return m.getNiveauAcces() == 0;
        }
        catch(Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

    public List<TupleClient> getListClients(boolean avecAdmin) throws SQLException, AuberginnException
    {
        try
        {
            List<TupleClient> clients = client.getListeClients(avecAdmin);
            connex.commit();
            return clients;
        }
        catch(Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

}
