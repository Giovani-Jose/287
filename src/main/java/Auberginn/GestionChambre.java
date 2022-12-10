package Auberginn;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class GestionChambre {

    private final TableChambres chambre;
    private final TableReservations reservation;
    private final Connexion connex;

    public GestionChambre(TableChambres chambre, TableReservations reservation) throws AuberginnException
    {
        this.connex = chambre.getConnexion();
        if (chambre.getConnexion() != reservation.getConnexion())
            throw new AuberginnException("Les instances de chambre et de reservation n'utilisent pas la même connexion au serveur");
        this.chambre = chambre;
        this.reservation = reservation;
    }

    public void ajouterChambre(int idChambre, String nomChambre, String typeLit, float prix, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            // Vérifie si la chambre existe déja
            if (chambre.existe(idChambre))
                throw new AuberginnException("La chambre : " + idChambre + " existe déjà.");

            // Ajout de la chambre dans la table des chambres
            chambre.ajouterChambre(idChambre, nomChambre, typeLit, prix);

            // Commit
            connex.commit();
        }
        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerChambre.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
            connex.rollback();
            throw e;
        }
    }

    public void supprimerChambre(int idChambre) throws Exception
    {
        try
        {
           // Validation
            // Vérifier que la chambre existe
            TupleChambre tupleChambre = chambre.getChambre(idChambre);
            if (tupleChambre == null)
                throw new AuberginnException("Chambre inexistante: " + idChambre);

            // Vérifiée si la chambre a une réservation présente ou future.
            List<TupleReservation> tupleReservation = reservation.getReservationChambre(idChambre);
            if (tupleReservation.size() > 0)
            {
                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                for (TupleReservation res : tupleReservation)
                {
                    if (date.before(res.getDateFin()))
                        throw new AuberginnException("Chambre " + idChambre + " est réservée.");
                }
            }

            // Suppression de la chambre.
            int nb = chambre.supprimerChambre(idChambre);
            if (nb == 0)
                throw new AuberginnException("Chambre " + idChambre + " inexistante");

            // Commit
            connex.commit();
        }
        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

    public List<TupleChambre> afficherChambresLibres() throws Exception
    {
        try
        {
            List<TupleChambre> listeChambres = chambre.getFreeRooms();
            if (!(listeChambres.size() > 0))
                System.out.println("Aucune chambre libre");

            return listeChambres;
           /* for (TupleChambre ch :
                    listeChambres) {
                System.out.println("Chambre : " + ch.getIdChambre() + ", " + ch.getPrixTotal() + "$");
            }*/
        }

        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }

    public TupleChambre afficherChambre(int idChambre) throws Exception
    {
        try
        {
            // Validation
            // Vérifier que la chambre existe
            TupleChambre tupleChambre = chambre.getChambre(idChambre);
            // Affichage
            if (tupleChambre == null)
                throw new AuberginnException("Chambre inexistante: " + idChambre);

            return tupleChambre;

        }

        catch (Exception e)
        {
            connex.rollback();
            throw e;
        }
    }
}
