package auberginnServlet;

import Auberginn.AuberginnException;
import Auberginn.GestionAubergeInn;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Servlet qui g�re la connexion d'un utilisateur au syst�me de gestion de
 * biblioth�que
 * 
 * <pre>
 * Vincent Ducharme
 * Universit� de Sherbrooke
 * Version 1.0 - 11 novembre 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 * </pre>
 */

public class Gestion extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String randomVal;
    private static final String ajoutChambre = "AjoutChambre";
    private static final String ajoutCommodite = "AjoutCommodite";
    private static final String ajoutReservation = "AjoutReservation";
    private static final String SupprimerChambre = "SupprimerChambre";
    private static final String appelVersGestionChambre = "/GestionChambre";
    private static final String appelVersGestionCommodite = "/GestionCommodite";
    private static final String appelVersGestionReservation = "/GestionReservation";
    private List<String>transactions;
    private List<String> appelUrl;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        loadTransaction();

        String url = request.getRequestURL().toString();
        String lastPath = url.substring(url.lastIndexOf('/'));
        String transaction;

        String recupererAppel = verifierAuteurDeLappel(lastPath);


        //permet d'eviter de soumettre a nouveau l'action lorsque la page est reloadé
        if(Objects.equals(randomVal, request.getParameter("custId")))

        {
           for(String elm:appelUrl)
           {
               if(elm.equals(recupererAppel))redirectionVersAuteurAppel(request,response,elm);
               return;
           }
        }


        switch(recupererAppel)
        {
            case appelVersGestionChambre:
                transaction = determinerTransaction(request,response);

                try {
                    verifierChampChambre(request,response);
                } catch (AuberginnException e) {
                    e.printStackTrace();
                }
                executerTransactionChambre(transaction,request,response);
                break;

            case appelVersGestionReservation:
                transaction = determinerTransaction(request,response);

                try {
                    verifierChampReservation(request,response);

                } catch (AuberginnException e) {
                    e.printStackTrace();
                }
                executerTransactionReservation(transaction,request,response);

                break;

            case appelVersGestionCommodite:
                transaction = determinerTransaction(request,response);

                try {
                    verifierChampCommodite(request,response);
                } catch (AuberginnException e) {
                    e.printStackTrace();
                }
                executerTransactionCommodite(transaction,request,response);
                break;
        }
    }

    public void loadTransaction()
    {

        //transactions
        transactions = new ArrayList<>();
        transactions.add(ajoutChambre);
        transactions.add(ajoutCommodite);
        transactions.add(SupprimerChambre);
        transactions.add(ajoutReservation);

        //url
        appelUrl = new ArrayList<>();
        appelUrl.add(appelVersGestionChambre);
        appelUrl.add(appelVersGestionCommodite);
        appelUrl.add(appelVersGestionReservation);
    }

    public String determinerTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        for(String transaction:transactions)
        {
            if(request.getParameter(transaction)!=null)return transaction;
        }
        return null;
    }

    public String verifierAuteurDeLappel(String lastPath)
    {
        switch(lastPath)
        {
            case appelVersGestionChambre:
                return appelVersGestionChambre;

            case appelVersGestionCommodite:
                return appelVersGestionCommodite;

            case appelVersGestionReservation:
                return appelVersGestionReservation;

            default:
                return null;

        }
    }

    public void executerTransactionChambre(String transaction,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);

       switch(transaction)
       {
           case ajoutChambre:
               synchronized (aubergeUpdate)
               {
                   try {
                       aubergeUpdate.getGestionChambre().ajouterChambre(Integer.parseInt(request.getParameter("chambreId")),
                               request.getParameter("nomChambre"),request.getParameter("typeLit"),Float.parseFloat(request.getParameter("prixDeBase")),request,response);
                       randomVal = request.getParameter("custId");
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               redirectionVersAuteurAppel(request,response,appelVersGestionChambre);

               break;
       }


    }

    public void executerTransactionCommodite(String transaction,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);

        switch(transaction)
        {
            case ajoutCommodite:
                synchronized (aubergeUpdate)
                {
                    try {
                        aubergeUpdate.getGestionCommodite().ajouterCommodite(Integer.parseInt(request.getParameter("commoditeId")),
                                request.getParameter("description"), Float.parseFloat(request.getParameter("prixSurplus")),request,response);
                        randomVal = request.getParameter("custId");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                redirectionVersAuteurAppel(request,response,appelVersGestionCommodite);

                break;
        }
    }


    public void executerTransactionReservation(String transaction,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);

        switch(transaction)
        {
            case ajoutReservation:
                synchronized (aubergeUpdate)
                {
                    try {
                        aubergeUpdate.getGestionReservation().reserver(request.getParameter("utilisateur"),
                                Integer.parseInt(request.getParameter("noChambre")),new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dateDebutReservation")),
                                new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("dateFinReservation")),request,response);
                        randomVal = request.getParameter("custId");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                redirectionVersAuteurAppel(request,response,appelVersGestionReservation);

                break;
        }


    }


    public void redirectionVersAuteurAppel(HttpServletRequest request, HttpServletResponse response,String auteurAppel) throws ServletException, IOException {

        if(Objects.equals(auteurAppel, appelVersGestionChambre))
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererChambre.jsp");
            dispatcher.forward(request, response);
        }

        if(Objects.equals(auteurAppel, appelVersGestionReservation))
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
        }

        if(Objects.equals(auteurAppel, appelVersGestionCommodite))
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererCommodite.jsp");
            dispatcher.forward(request, response);
        }

    }

    public void verifierChampChambre(HttpServletRequest request,HttpServletResponse response) throws AuberginnException, ServletException, IOException {

        try {
            String chambreId = request.getParameter("chambreId");
            String nomChambre = request.getParameter("nomChambre");
            String typeLit = request.getParameter("typeLit");
            String prixDeBase = request.getParameter("prixDeBase");


            if (chambreId == null || chambreId.equals(""))
                throw new AuberginnException("Vous devez entrer un id pour la chambre");

            if (nomChambre == null || nomChambre.equals(""))
                throw new AuberginnException("Vous devez entrer un nom pour la chambre");

            if (typeLit == null || typeLit.equals(""))
                throw new AuberginnException("Vous devez entrer un type de lit pour la chambre");

            if (prixDeBase == null || prixDeBase.equals("")) {
                throw new AuberginnException("Vous devez entrer un prix de base");
            }
        }
          catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerChambre.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
        }




    }

    public void verifierChampCommodite(HttpServletRequest request,HttpServletResponse response) throws AuberginnException, ServletException, IOException {

        try {
            String commoditeId = request.getParameter("commoditeId");
            String desc = request.getParameter("description");
            String prix = request.getParameter("prixSurplus");


            if (commoditeId == null || commoditeId.equals(""))
                throw new AuberginnException("Vous devez entrer un id pour la commodité");

            if (desc == null || desc.equals(""))
                throw new AuberginnException("Vous devez entrer une description pour la commodité");

            if (prix == null || prix.equals("")) {
                throw new AuberginnException("Vous devez entrer un prix de surplus");
            }
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
        }
    }

    public void verifierChampReservation(HttpServletRequest request,HttpServletResponse response) throws AuberginnException, ServletException, IOException {

        try {
            String user= request.getParameter("utilisateur");
            String numChambre = request.getParameter("noChambre");
            String dateDebutReservation = request.getParameter("dateDebutReservation");
            String dateFinReservation = request.getParameter("dateFinReservation");


            if (user == null || user.equals(""))
                throw new AuberginnException("Vous devez entrer un client pour la chambre");

            if (numChambre == null || numChambre.equals(""))
                throw new AuberginnException("Vous devez entrer un numero pour la chambre");

            if (dateDebutReservation == null || dateDebutReservation.equals(""))
                throw new AuberginnException("Vous devez entrer une dateDebut pour la chambre");

            if (dateFinReservation == null || dateFinReservation.equals("")) {
                throw new AuberginnException("Vous devez entrer une dateFin pour la chambre");
            }
        }
        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerReservation.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();;
        }




    }


    // Dans les formulaires, on utilise la m�thode POST
    // donc, si le servlet est appel� avec la m�thode GET
    // s'est qu'on a �crit l'adresse directement dans la barre d'adresse.
    // On proc�de si on est connect� correctement, sinon, on retourne au login
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Inscription : GET");
        // Si on a d�j� entr� les informations de connexion valide

        if (AuberginnHelper.peutProceder(getServletContext(), request, response))
        {
            System.out.println("Servlet Inscription : GET dispatch vers creerCompte.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerCompte.jsp");
            dispatcher.forward(request, response);
        }
    }

} // class
