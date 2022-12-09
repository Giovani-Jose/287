package Auberginn;



import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TableReservations
{
    private PreparedStatement stmtExists;
    private final PreparedStatement stmtInsert;
    private final PreparedStatement stmtChambreExists;
    private final PreparedStatement stmtClientExists;
    private final Connexion connex;

    public TableReservations (Connexion cx) throws SQLException
    {

        this.connex = cx;
        stmtExists = connex.getConnection().prepareStatement(
                "select * from reservation" +
                        " where utilisateur = ? and idChambre = ?");
        stmtInsert = connex.getConnection()
                .prepareStatement("insert into reservation (idClient, idChambre,utilisateur, prixTotal, dateDebut, dateFin) "
                        + "values (?,?,?,?,?,?)");
        stmtChambreExists = connex.getConnection()
                .prepareStatement("select * from reservation" +
                        " where idChambre = ? " + "order by dateDebut");
        stmtClientExists = connex.getConnection().prepareStatement(
                "select * from reservation" +
                        " where utilisateur = ? " + "order by dateDebut");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return connex;
    }

    /**
     * Lecture d'une reservation.
     */
    public TupleReservation getReservation(int idClient, int idChambre) throws SQLException
    {
        stmtExists.setInt(1, idClient);
        stmtExists.setInt(2, idChambre);
        ResultSet rset = stmtExists.executeQuery();
        if (rset.next())
        {
            TupleReservation tupleReservation = new TupleReservation();
            tupleReservation.setUtilisateur(rset.getString(1));
            tupleReservation.setIdChambre(rset.getInt(2));
            tupleReservation.setPrixTotal(rset.getFloat(3));
            tupleReservation.setDateDebut(rset.getDate(4));
            tupleReservation.setDateFin(rset.getDate(5));

            return tupleReservation;
        }
        else
        {
            return null;
        }
    }

    /**
     * Vérifie si une reservation existe.
     */
    public boolean existe(int idClient, int idChambre) throws SQLException
    {
        stmtExists.setInt(1, idClient);
        stmtExists.setInt(2, idChambre);
        ResultSet rset = stmtExists.executeQuery();
        boolean reservationExists = rset.next();
        rset.close();
        return reservationExists;
    }

    /**
     * Ajout d'une nouvelle reservation dans la base de données.
     */
    public void reserver(int idClient, int idChambre,String utilisateur, float prixTotal, Date dateDebut, Date dateFin) throws SQLException
    {
        /* Ajout de la reservation */
        stmtInsert.setInt(1, idClient);
        stmtInsert.setInt(2, idChambre);
        stmtInsert.setString(3,utilisateur);
        stmtInsert.setFloat(4,prixTotal);
        stmtInsert.setDate(5, dateDebut);
        stmtInsert.setDate(6, dateFin);
        stmtInsert.executeUpdate();
    }

    /**
     * Lecture des reservations d'une chambre.
     */
    public List<TupleReservation> getReservationClient(String utilisateur) throws SQLException
    {
        stmtClientExists.setString(1, utilisateur);
        ResultSet rset = stmtClientExists.executeQuery();
        List<TupleReservation> listReserv = new LinkedList<>();
        while (rset.next())
        {
            TupleReservation tupleReservation = new TupleReservation();
            tupleReservation.setUtilisateur(rset.getString(1));
            tupleReservation.setIdChambre(rset.getInt(2));
            tupleReservation.setPrixTotal(rset.getFloat(3));
            tupleReservation.setDateDebut(rset.getDate(4));
            tupleReservation.setDateFin(rset.getDate(5));

            listReserv.add(tupleReservation);
        }
        rset.close();
        return listReserv;
    }

    /**
     * Lecture de la première reservation d'un livre.
     */
    public List<TupleReservation> getReservationChambre(int idChambre) throws SQLException
    {
        stmtChambreExists.setInt(1, idChambre);
        ResultSet rset = stmtChambreExists.executeQuery();
        List<TupleReservation> listReserv = new LinkedList<>();
        while (rset.next())
        {
            TupleReservation tupleReservation = new TupleReservation(
            rset.getString(1),
            rset.getInt(2),
            rset.getFloat(3),
            rset.getDate(4),
            rset.getDate(5));

            listReserv.add(tupleReservation);
        }
        rset.close();
        return listReserv;
    }
}
