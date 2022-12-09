package Auberginn;

import java.sql.*;

/**
 * Gestionnaire d'une connexion avec une BD relationnelle via JDBC.
 * 
 * <pre>
 * Marc Frappier - 83 427 378
 * Université de Sherbrooke
 * version 2.0 - 13 novembre 2004
 * ift287 - exploitation de bases de données
 * 
 * Vincent Ducharme
 * Université de Sherbrooke
 * version 3.0 - 21 mai 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme ouvre une connexion avec une BD via JDBC.
 * La méthode serveursSupportes() indique les serveurs supportés.
 * 
 * Pré-condition
 *   le driver JDBC approprié doit être accessible.
 * 
 * Post-condition
 *   la connexion est ouverte en mode autocommit false et sérialisable, 
 *   (s'il est supporté par le serveur).
 * </pre>
 */
public class Connexion
{
    private Connection conn;

    /**
     * Ouverture d'une connexion en mode autocommit false et sérialisable (si
     * supporté)
     * 
     * @serveur serveur SQL de la BD
     * @bd nom de la base de données
     * @user userid sur le serveur SQL
     * @pass mot de passe sur le serveur SQL
     */
    public Connexion(String serveur, String bd, String user, String pass) throws AuberginnException, SQLException
    {
        Driver d;
        try
        {
            d = (Driver)Class.forName("org.postgresql.Driver").newInstance();
            DriverManager.registerDriver(d);
            
            if (serveur.equals("local"))
            {
                conn = DriverManager.getConnection("jdbc:postgresql:" + bd, user, pass);
            }
            else if (serveur.equals("dinf"))
            {
                conn = DriverManager.getConnection("jdbc:postgresql://bd-info2.dinf.usherbrooke.ca:5432/" + bd + "?ssl=true&sslmode=require", user, pass);
            }
            else
            {
                throw new AuberginnException("Serveur inconnu");
            }

            // mettre en mode de commit manuel
            conn.setAutoCommit(false);

            // mettre en mode sérialisable si possible
            // (plus haut niveau d'integrité l'accès concurrent aux données)
            DatabaseMetaData dbmd = conn.getMetaData();
            if (dbmd.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
            {
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                System.out.println("Ouverture de la connexion en mode sérialisable :\n" + "Estampille "
                        + System.currentTimeMillis() + " " + conn);
            }
            else
            {
                System.out.println("Ouverture de la connexion en mode read committed (default) :\n" + "Heure "
                        + System.currentTimeMillis() + " " + conn);
            }
        } // try
        catch (SQLException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw new SQLException("JDBC Driver non instancié");
        }
    }

    /**
     * fermeture d'une connexion
     */
    public void fermer() throws SQLException
    {
        conn.rollback();
        conn.close();
        System.out.println("Connexion fermée " + conn);
    }

    /**
     * commit
     */
    public void commit() throws SQLException
    {
        conn.commit();
    }

    public void setIsolationReadCommited() throws SQLException
    {
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    /**
     * rollback
     */
    public void rollback() throws SQLException
    {
        conn.rollback();
    }

    /**
     * retourne la Connection jdbc
     */
    public Connection getConnection()
    {
        return conn;
    }

    public void setAutoCommit(boolean m) throws SQLException
    {
        conn.setAutoCommit(false);
    }

    /**
     * Retourne la liste des serveurs supportés par ce gestionnaire de
     * connexions
     */
    public static String serveursSupportes()
    {
        return "local : PostgreSQL installé localement\n"
             + "dinf  : PostgreSQL installé sur les serveurs du département\n";
    }
}// Classe Connexion
