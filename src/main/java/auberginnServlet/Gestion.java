package auberginnServlet;

import Auberginn.GestionAubergeInn;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private static final String SupprimerChambre = "SupprimerChambre";
    private List<String>transactions;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        loadTransaction();

        String url = request.getRequestURL().toString();
        String lastPath = url.substring(url.lastIndexOf('/'));
        String transaction;

        switch(lastPath)
        {
            case "/GestionChambre":
                transaction = verifierTransaction(request,response);
                executerTransactionChambre(transaction,request,response);
                break;

        }





    }

    public void loadTransaction()
    {
        transactions = new ArrayList<>();
        transactions.add(ajoutChambre);
        transactions.add(SupprimerChambre);
    }

    public String verifierTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        for(String transaction:transactions)
        {
            if(request.getParameter(transaction)!=null)return transaction;
        }
        return null;
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
                               request.getParameter("nomChambre"),request.getParameter("typeLit"),Float.parseFloat(request.getParameter("prixDeBase")));
                       randomVal = request.getParameter("custId");
                       redirectionVersGestionChambre(request,response);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               break;
       }
    }

    public void redirectionVersGestionChambre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererChambre.jsp");
        dispatcher.forward(request, response);
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
