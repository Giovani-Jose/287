package Auberginn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableClients
{
    private final PreparedStatement stmtExists;
    private final PreparedStatement stmtInsert;
    private final PreparedStatement stmtDelete;
    private PreparedStatement stmtListeUtilisateur;

    private final Connexion connex;

    public TableClients(Connexion cx) throws SQLException
    {
        this.connex = cx;

        stmtExists = connex.getConnection().prepareStatement(
                "select * from client where utilisateur = ?");
        stmtListeUtilisateur = cx.getConnection().prepareStatement(
                "SELECT *  FROM client WHERE acces = ?");
        stmtInsert = connex.getConnection()
                .prepareStatement("insert into client (utilisateur,motDePasse,acces, nom, prenom, age) "
                        + "values (?,?,?,?,?,?)");
        stmtDelete = connex.getConnection().prepareStatement("delete from client where utilisateur= ?");

    }

    /**
     * Retourner la connexion associée.
     */
    public Connexion getConnexion()
    {
        return connex;
    }

    /**
     * Vérifie si un client existe.
     */
    public boolean existe(String utilisateur) throws SQLException
    {


        stmtExists.setString(1, utilisateur);
        ResultSet rset = stmtExists.executeQuery();
        boolean clientExists = rset.next();
        rset.close();
        return clientExists;
    }

    /**
     * Ajout d'un nouveau client dans la base de données.
     */
    public void ajouterClient(int idClient, String nom, String prenom, int age) throws SQLException
    {
        /* Ajout du client. */
        stmtInsert.setInt(1, idClient);
        stmtInsert.setString(2, nom);
        stmtInsert.setString(3, prenom);
        stmtInsert.setInt(4, age);
        stmtInsert.executeUpdate();
    }

    public void inscrire(String utilisateur, String motDePasse, int acces, String nom, String prenom, int age)
            throws SQLException
    {
        stmtInsert.setString(1, utilisateur);
        stmtInsert.setString(2, motDePasse);
        stmtInsert.setInt(3, acces);
        stmtInsert.setString(4, nom);
        stmtInsert.setString(5, prenom);
        stmtInsert.setInt(6, age);
        stmtInsert.executeUpdate();
    }

    public int desinscrire(String utilisateur) throws SQLException
    {
        stmtDelete.setString(1, utilisateur);
        return stmtDelete.executeUpdate();
    }

    /**
     * Lecture d'un client.
     */
    public TupleClient getClient(String utilisateur) throws SQLException
    {
        stmtExists.setString(1, utilisateur);
        ResultSet rset = stmtExists.executeQuery();
        if (rset.next())
        {
            return new TupleClient(
                    (rset.getString(1)),
                    (rset.getString(2)),
                    (rset.getInt(3)),
                    (rset.getString(4)),
                    (rset.getString(5)),
                    (rset.getInt(6))
                    );
        }
        else
            return null;
    }

    public List<TupleClient> getListeClients(boolean avecAdmin) throws SQLException
    {
        List<TupleClient> clients = new ArrayList<TupleClient>();
        stmtListeUtilisateur.setInt(1, 1);
        ResultSet rset = stmtListeUtilisateur.executeQuery();
        while (rset.next())
        {
            String motDePasse = rset.getString(2);
            int acces = rset.getInt(3);
            String nom = rset.getString(4);
            String prenom = rset.getString(5);
            int age = rset.getInt(6);

            TupleClient tupleClient = new TupleClient(rset.getString(1), motDePasse, acces, nom, prenom,age);
            clients.add(tupleClient);
        }
        if (avecAdmin)
        {
            stmtListeUtilisateur.setInt(1, 0);
            rset = stmtListeUtilisateur.executeQuery();
            while (rset.next())
            {
                String motDePasse = rset.getString(2);
                int acces = rset.getInt(3);
                String nom = rset.getString(4);
                String prenom = rset.getString(5);
                int age = rset.getInt(6);

                TupleClient tupleMembre = new TupleClient(rset.getString(1), motDePasse, acces, nom, prenom,age);
                clients.add(tupleMembre);
            }
        }
        rset.close();
        return clients;
    }
}
