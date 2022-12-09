package Auberginn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableCommodites
{
    private final PreparedStatement stmtExists;
    private final PreparedStatement stmtInsert;
    private final Connexion cx;

    public TableCommodites(Connexion cx) throws SQLException
    {
        this.cx = cx;

        stmtExists = cx.getConnection().prepareStatement(
                "select * from commodite where idCommodite = ?");
        stmtInsert = cx.getConnection()
                .prepareStatement("insert into commodite (idCommodite, description, surplusPrix) "
                        + "values (?,?,?)");
    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * Vérifie si une commodite existe.
     */
    public boolean existe(int idCommodite) throws SQLException
    {
        stmtExists.setInt(1, idCommodite);
        ResultSet rset = stmtExists.executeQuery();
        boolean commoditeExists = rset.next();
        rset.close();
        return commoditeExists;
    }

    /**
     * Ajout d'une nouvelle commodite dans la base de données.
     */
    public void ajouterCommodite(int idCommodite, String description, float surplusPrix) throws SQLException
    {
        /* Ajout de la chambre. */
        stmtInsert.setInt(1, idCommodite);
        stmtInsert.setString(2, description);
        stmtInsert.setFloat(3, surplusPrix);
        stmtInsert.executeUpdate();
    }

    /**
     * Lecture d'une commodité.
     */
    public TupleCommodite getCommodite(int idCommodite) throws SQLException
    {
        stmtExists.setInt(1, idCommodite);
        ResultSet rset = stmtExists.executeQuery();
        if (rset.next())
        {
            return new TupleCommodite(
                    idCommodite,
                    rset.getString(2),
                    rset.getFloat(3));
        }
        else
            return null;
    }
}
