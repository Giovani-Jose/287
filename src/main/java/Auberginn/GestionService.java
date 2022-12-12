package Auberginn;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class GestionService
{
    private final TableCommodites commodite;
    private final TableChambres chambre;
    private final TableServices service;
    private final Connexion cx;

    public GestionService(TableChambres chambre, TableCommodites commodites, TableServices services)
            throws AuberginnException
    {
        this.cx = commodites.getConnexion();
        if (commodites.getConnexion() != services.getConnexion())
            throw new AuberginnException("Les instances de chambre et de reservation n'utilisent pas la même connexion au serveur");
        this.commodite = commodites;
        this.chambre = chambre;
        this.service = services;
    }


    public void inclureCommodite(int idChambre, int idCommodite, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            // Vérifier que la chambre existe
            TupleChambre tupleChambre = chambre.getChambre(idChambre);
            if (tupleChambre == null)
                throw new AuberginnException("Chambre inexistante: " + idChambre);

            // Vérifier que la commodite existe
            TupleCommodite tupleCommodite = commodite.getCommodite(idCommodite);
            if (tupleCommodite == null)
                throw new AuberginnException("Commodité inexistante: " + idCommodite);

            // Vérifier que la chambre ne possède pas déjà la commodité
            TupleServices tupleServices = service.getService(idChambre, idCommodite);
            if (tupleServices != null)
                throw new AuberginnException("La chambre " + idChambre + " possède déjà la commodité " + idCommodite);

            // Creation du service
            tupleChambre.getCommodites().add(tupleCommodite);
            service.inclureCommodite(idChambre, idCommodite);

            // Commit
            cx.commit();
        }

        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("idCommodite", idCommodite);
            request.setAttribute("typeAction", "inclure");
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/inclureCommodite.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
            cx.rollback();
            throw e;
        }
    }

    public void enleverCommodite(int idChambre, int idCommodite, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            // Validation
            // Vérifier que la chambre existe
            TupleChambre tupleChambre = chambre.getChambre(idChambre);
            if (tupleChambre == null)
                throw new AuberginnException("Chambre inexistante: " + idChambre);

            // Vérifier que la commodite existe
            TupleCommodite tupleCommodite = commodite.getCommodite(idCommodite);
            if (tupleCommodite == null)
                throw new AuberginnException("Commodité inexistante: " + idCommodite);

            // Vérifier que la chambre possède la commodité
            TupleServices tupleServices = service.getService(idChambre, idCommodite);
            if (tupleServices == null)
                throw new AuberginnException("La chambre " + idChambre + " ne possède pas la commodité " + idCommodite);

            // Suppression du service.
            int nb = service.enleverCommodite(idChambre, idCommodite);
            if (nb == 0)
                throw new AuberginnException("Chambre " + idChambre + " ou commodité " + idCommodite + " inexistantes.");

            tupleChambre.getCommodites().remove(tupleCommodite);
            // Commit
            cx.commit();
        }
        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("idCommodite", idCommodite);
            request.setAttribute("typeAction", "enlever");
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/inclureCommodite.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
            cx.rollback();
            throw e;
        }
    }
}
