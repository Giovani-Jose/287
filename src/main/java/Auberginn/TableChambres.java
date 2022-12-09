package Auberginn;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class TableChambres {

    private final PreparedStatement stmtExists;
    private final PreparedStatement stmtInsert;
    private final PreparedStatement stmtSelectFreeRooms;
    private final PreparedStatement stmtDelete;
    private final Connexion connex;

    public TableChambres(Connexion cx) throws SQLException
    {
        this.connex = cx;

        stmtExists = connex.getConnection().prepareStatement(
                "select * from chambre where idChambre = ?");
        stmtInsert = connex.getConnection()
                .prepareStatement("insert into chambre (idChambre, nomChambre, typeLit, prixBase) "
                        + "values (?,?,?,?)");
        stmtDelete = connex.getConnection().prepareStatement("delete from chambre where idChambre = ?");
        java.sql.Date dateNow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        stmtSelectFreeRooms = connex.getConnection().prepareStatement("select *" +
                " from chambre where idChambre not in (select idChambre from reservation where CURRENT_DATE > dateFin)");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return connex;
    }

    /**
     * Vérifie si une chambre existe.
     */
    public boolean existe(int idChambre) throws SQLException
    {
        stmtExists.setInt(1, idChambre);
        ResultSet rset = stmtExists.executeQuery();
        boolean chambreExist = rset.next();
        rset.close();
        return chambreExist;
    }

    /**
     * Ajout d'une nouvelle chambre dans la base de données.
     */
    public void ajouterChambre(int idChambre, String nomChambre, String typeLit, float prix) throws SQLException
    {
        /* Ajout de la chambre. */
        stmtInsert.setInt(1, idChambre);
        stmtInsert.setString(2, nomChambre);
        stmtInsert.setString(3, typeLit);
        stmtInsert.setFloat(4, prix);
        stmtInsert.executeUpdate();
    }

    /**
     * Supprimer une chambre de la base de données
     */
    public int supprimerChambre(int idChambre) throws SQLException
    {
        /* Suppression du livre. */
        stmtDelete.setInt(1, idChambre);
        return stmtDelete.executeUpdate();
    }

    /**
     * Lecture d'une chambre.
     */
    public TupleChambre getChambre(int idChambre) throws SQLException
    {
        stmtExists.setInt(1, idChambre);
        ResultSet rset = stmtExists.executeQuery();
        if (rset.next())
        {
            return new TupleChambre(
                    idChambre,
                    rset.getString(2),
                    rset.getString(3),
                    rset.getFloat(4));
        }
        else
            return null;
    }

    public List<TupleChambre> getFreeRooms() throws SQLException
    {
            ResultSet rset = stmtSelectFreeRooms.executeQuery();
            List<TupleChambre> listeChambres = new LinkedList<>();
            while (rset.next())
            {
                TupleChambre tupleChambre = new TupleChambre(
                        rset.getInt(1),
                        rset.getString(2),
                        rset.getString(3),
                        rset.getFloat(4));

                listeChambres.add(tupleChambre);
            }
            rset.close();
            return listeChambres;
    }
}
