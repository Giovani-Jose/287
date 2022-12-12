package auberginnServlet;

import Auberginn.Connexion;
import Auberginn.TupleChambre;
import Auberginn.TupleCommodite;
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
    private PreparedStatement stmtListeTousChambre;
    private PreparedStatement stmtListeTousReservation;
    private PreparedStatement stmtListeReservationUnClient;
    private PreparedStatement stmtListeTousCommodite;
    private PreparedStatement stmtListeTousChambreAvecCommodite;
    private PreparedStatement stmtListeTousCommoditeAvecChambre;
    private Connexion cx;

    /**
     * Creation d'une instance
     */
    public GestionInterrogation(Connexion cx) throws SQLException
    {
        this.cx = cx;
        
        stmtListeTousReservation = cx.getConnection().prepareStatement(
                "SELECT * FROM reservation");

        stmtListeReservationUnClient = cx.getConnection().prepareStatement(
                "SELECT * FROM reservation where utilisateur = ?");

        stmtListeTousChambre = cx.getConnection().prepareStatement(
                "SELECT * FROM chambre");

        stmtListeTousCommodite = cx.getConnection().prepareStatement(
                "SELECT * FROM commodite");

        stmtListeTousChambreAvecCommodite = cx.getConnection().prepareStatement(
            "SELECT c.* FROM chambre c WHERE c.idChambre in (SELECT s.idChambre FROM service s WHERE s.idCommodite = ?)");

        stmtListeTousCommoditeAvecChambre = cx.getConnection().prepareStatement(
            "SELECT c.* FROM commodite c WHERE c.idCommodite in (SELECT s.idCommodite FROM service s WHERE s.idChambre = ?)");
    }
    
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

    public List<TupleCommodite> listerToutesCommodites() throws SQLException
    {
        List<TupleCommodite> cs = new LinkedList<TupleCommodite>();

        ResultSet rset = stmtListeTousCommodite.executeQuery();
        while (rset.next())
        {
            TupleCommodite c = new TupleCommodite(rset.getInt("idCommodite"), rset.getString("description"),
                    rset.getFloat("surplusPrix"));
            cs.add(c);
        }
        rset.close();
        cx.commit();
        return cs;
    }

    public List<TupleChambre> listerChambreAvecCommodite(int idCommodite) throws SQLException
    {
        List<TupleChambre> chambres = new LinkedList<TupleChambre>();

        stmtListeTousChambreAvecCommodite.setInt(1, idCommodite);
        ResultSet rset = stmtListeTousChambreAvecCommodite.executeQuery();
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

    public List<TupleCommodite> listerCommoditeAvecChambre(int idChambre) throws SQLException
    {
        List<TupleCommodite> cs = new LinkedList<TupleCommodite>();

        stmtListeTousCommoditeAvecChambre.setInt(1, idChambre);
        ResultSet rset = stmtListeTousCommoditeAvecChambre.executeQuery();
        while (rset.next())
        {
            TupleCommodite c = new TupleCommodite(rset.getInt("idCommodite"), rset.getString("description"),
                    rset.getFloat("surplusPrix"));
            cs.add(c);
        }
        rset.close();
        cx.commit();
        return cs;
    }

}
