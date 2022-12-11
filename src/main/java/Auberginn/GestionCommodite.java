package Auberginn;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class GestionCommodite {
    private final TableCommodites commodites;
    private final TableServices services;
    private final Connexion cx;

    public GestionCommodite(TableCommodites commodites, TableServices services) throws AuberginnException
    {
        this.cx = commodites.getConnexion();
        if (commodites.getConnexion() != services.getConnexion())
            throw new AuberginnException("Les instances de chambre et de reservation n'utilisent pas la même connexion au serveur");
        this.commodites = commodites;
        this.services = services;
    }

    public void ajouterCommodite(int idCommodite, String description, float surplusPrix, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            // Vérifie si la commodité existe déja
            if (commodites.existe(idCommodite))
                throw new AuberginnException("La commodité " + idCommodite + " existe déjà.");

            // Ajout de la chambre dans la table des chambres
            commodites.ajouterCommodite(idCommodite, description, surplusPrix);

            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/ajoutCommodite.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
            cx.rollback();
            throw e;
        }
    }
}
