package Auberginn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TableServices
{
    private final PreparedStatement stmtExists;
    private final PreparedStatement stmtListeChambres;
    private final PreparedStatement stmtInsert;
    private final PreparedStatement stmtDelete;
    private final Connexion cx;

    public TableServices(Connexion cx) throws SQLException
    {
        this.cx = cx;
        stmtExists = cx.getConnection().prepareStatement(
                "select * from service where idChambre = ? and idCommodite = ?");
        stmtInsert = cx.getConnection()
                .prepareStatement("insert into service (idChambre, idCommodite) "
                        + "values (?,?)");
        stmtDelete = cx.getConnection().prepareStatement("delete from service " +
                "where idChambre = ? and idCommodite = ?");
        stmtListeChambres = cx.getConnection().prepareStatement(
                "select * from service where idCommodite = ?");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Lecture d'un service.
     */
    public TupleServices getService(int idChambre, int idCommodite) throws SQLException
    {
        stmtExists.setInt(1, idChambre);
        stmtExists.setInt(2, idCommodite);
        ResultSet rset = stmtExists.executeQuery();
        if (rset.next())
        {
            return new TupleServices(
                    rset.getInt(1),
                    rset.getInt(2));
        }
        else
        {
            return null;
        }
    }

    /**
     * Ajout d'un service dans la base de données.
     */
    public void inclureCommodite(int idChambre, int idCommodite) throws SQLException
    {
        /* Ajout du service */
        stmtInsert.setInt(1, idChambre);
        stmtInsert.setInt(2, idCommodite);
        stmtInsert.executeUpdate();
    }

    /**
     * Retrait d'un service dans la base de données.
     */
    public int enleverCommodite(int idChambre, int idCommodite) throws SQLException
    {
        /* Ajout du service */
        stmtDelete.setInt(1, idChambre);
        stmtDelete.setInt(2, idCommodite);
        return stmtDelete.executeUpdate();
    }

    public List<TupleServices> listeChambre(int idCommodite) throws SQLException
    {
        stmtListeChambres.setInt(1, idCommodite);
        ResultSet rset = stmtListeChambres.executeQuery();
        List<TupleServices> cs = new LinkedList<>();
        while (rset.next())
        {
            TupleServices ts = new TupleServices(rset.getInt(1),
                    rset.getInt(2));
            cs.add(ts);
        }
        rset.close();
        return cs;
    }
}
