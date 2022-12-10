package auberginnServlet;

import Auberginn.Connexion;
import Auberginn.TupleChambre;
import Auberginn.TupleReservation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestion des transactions d'interrogation dans une biblioth�que.
 * 
 * <pre>
 * 
 *  
 *   Marc Frappier - 83 427 378
 *   Universit� de Sherbrooke
 *   version 2.0 - 13 novembre 2004
 *   ift287 - exploitation de bases de donn�es
 *  
 *   Ce programme permet de faire diverses interrogations
 *   sur l'�tat de la biblioth�que.
 *  
 *   Pr�-condition
 *     la base de donn�es de la biblioth�que doit exister
 *  
 *   Post-condition
 *     le programme effectue les maj associ�es � chaque
 *     transaction
 * 
 * 
 * </pre>
 */

public class GestionInterrogation
{
    private PreparedStatement stmtLivresRetard;
    private PreparedStatement stmtLivresTitreMot;
    private PreparedStatement stmtListeTousLivres;
    private PreparedStatement stmtListeTousChambre;
    private PreparedStatement stmtListeTousReservation;
    private PreparedStatement stmtListeReservationUnClient;
    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public GestionInterrogation(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtLivresRetard = cx.getConnection().prepareStatement("SELECT idlivre, titre, auteur, datePret, "
                + "utilisateur, to_date(?,'YYYY-MM-DD') - datePret - 14 as retard FROM livre "
                + "WHERE utilisateur = ? AND to_date(?,'YYYY-MM-DD') - datePret > 14 "
                + "ORDER BY retard");

        stmtLivresTitreMot = cx.getConnection()
                .prepareStatement("select t1.idLivre, t1.titre, t1.auteur, t1.utilisateur, t1.datePret + 14 "
                        + "from livre t1 " + "where lower(titre) like ?");

        stmtListeTousLivres = cx.getConnection().prepareStatement(
                "select t1.idLivre, t1.titre, t1.auteur, t1.utilisateur, t1.datePret " + "from livre t1");
        
        stmtListeTousReservation = cx.getConnection().prepareStatement(
                "SELECT * FROM reservation");

        stmtListeReservationUnClient = cx.getConnection().prepareStatement(
                "SELECT * FROM reservation where utilisateur = ?");

        stmtListeTousChambre = cx.getConnection().prepareStatement(
                "SELECT * FROM chambre");
    }

    /**
     * Affiche les livres pr�t�s depuis plus de 14 jours.
     */
    /*public List<TupleLivre> listerLivresRetard(String userID, String date) throws SQLException
    {
        List<TupleLivre> livres = new LinkedList<TupleLivre>();
        
        stmtLivresRetard.setString(1, date);
        stmtLivresRetard.setString(2, userID);
        stmtLivresRetard.setString(3, date);
        ResultSet rset = stmtLivresRetard.executeQuery();

        while (rset.next())
        {
            TupleLivre livre = new TupleLivre(rset.getInt("idLivre"), rset.getString("titre"), rset.getString("auteur"), rset.getDate("dateAcquisition"),  rset.getString("utilisateur"),  rset.getDate("datePret"));
            livres.add(livre);
        }
        rset.close();
        cx.commit();
        return livres;
    }*/

    /**
     * Affiche les livres contenu un mot dans le titre
     */
    public void listerLivresTitre(String mot) throws SQLException
    {
        stmtLivresTitreMot.setString(1, "%" + mot + "%");
        ResultSet rset = stmtLivresTitreMot.executeQuery();

        int idMembre;
        System.out.println("idLivre titre auteur idMembre dateRetour");
        while (rset.next())
        {
            System.out.print(rset.getInt(1) + " " + rset.getString(2) + " " + rset.getString(3));
            idMembre = rset.getInt(4);
            if (!rset.wasNull())
            {
                System.out.print(" " + idMembre + " " + rset.getDate(5));
            }
            System.out.println();
        }
        cx.commit();
    }

    /**
     * Affiche tous les livres de la BD
     */
  /*  public List<TupleLivre> listerLivres() throws SQLException
    {
        List<TupleLivre> livres = new LinkedList<TupleLivre>();
        
        ResultSet rset = stmtListeTousLivres.executeQuery();
        while (rset.next())
        {
            TupleLivre livre = new TupleLivre(rset.getInt("idLivre"), rset.getString("titre"), rset.getString("auteur"), rset.getDate("dateAcquisition"),  rset.getString("utilisateur"),  rset.getDate("datePret"));
            livres.add(livre);
        }
        rset.close();
        cx.commit();
        return livres;
    }*/
    
    /**
     * Affiche tous les livres de la BD pour un membre
     */
    public List<TupleReservation> listerTouteReservations() throws SQLException
    {
        List<TupleReservation> reservations = new LinkedList<TupleReservation>();
        
        ResultSet rset = stmtListeTousReservation.executeQuery();
        while (rset.next())
        {
            TupleReservation reservation = new TupleReservation(rset.getString("utilisateur"), rset.getInt("idChambre"), rset.getFloat("prixTotal"), rset.getDate("dateDebut"),  rset.getDate("dateFin"));
            reservations.add(reservation);
        }
        rset.close();
        cx.commit();
        return reservations;
    }

    public List<TupleReservation> listerReservationsUnClient(String utilisateur) throws SQLException
    {
        List<TupleReservation> reservations = new LinkedList<TupleReservation>();

        stmtListeReservationUnClient.setString(1,utilisateur);
        ResultSet rset = stmtListeReservationUnClient.executeQuery();
        while (rset.next())
        {
            TupleReservation reservation = new TupleReservation(rset.getString("utilisateur"), rset.getInt("idChambre"), rset.getFloat("prixTotal"), rset.getDate("dateDebut"),  rset.getDate("dateFin"));
            reservations.add(reservation);
        }
        rset.close();
        cx.commit();
        return reservations;
    }

    public List<TupleChambre> listerTouteChambres() throws SQLException
    {
        List<TupleChambre> chambres = new LinkedList<TupleChambre>();

        ResultSet rset = stmtListeTousChambre.executeQuery();
        while (rset.next())
        {
            TupleChambre chambre= new TupleChambre(rset.getInt("idChambre"), rset.getString("nomChambre"),
                    rset.getString("typeLit"),rset.getFloat("prixBase"));
            chambres.add(chambre);
        }
        rset.close();
        cx.commit();
        return chambres;
    }

}
