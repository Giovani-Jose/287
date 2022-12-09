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
import java.util.Arrays;
import java.util.LinkedList;
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

public class Inscription extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String randomVal;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        //permet d'eviter de soumettre a nouveau l'action lorsque la page est reloadé

        if(Objects.equals(randomVal, request.getParameter("custId")))

        {

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererMembre.jsp");
            dispatcher.forward(request, response);
            return;
        }

        request.setAttribute("listeMessageErreur",null);

        if(request.getParameter("AfficherClient")!=null)
        {

            try {
                if(request.getParameter("Selection")!=null)
                {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/AfficherClient.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                else
                {
                    throw new AuberginnException("Vous devez selectionner le client a afficher");

                }

            }
            catch (Exception e)
            {
                List<String> listeMessageErreur = new LinkedList<String>();
                listeMessageErreur.add(e.getMessage());
                request.setAttribute("listeMessageErreur", listeMessageErreur);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererMembre.jsp");
                dispatcher.forward(request, response);

                // pour déboggage seulement : afficher tout le contenu de l'exception
                e.printStackTrace();;
            }


        }


        if(request.getParameter("Supprimer")!=null)
        {
            try {
                if (request.getParameter("Selection") != null) {
                    String[] res = request.getParameterValues("Selection");
                    HttpSession session = request.getSession();

                    for (String user : res) {
                        GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);
                        synchronized (aubergeUpdate) {
                            try {
                                aubergeUpdate.getGestionClient().desinscrire(user);
                                randomVal = request.getParameter("custId");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererMembre.jsp");
                    dispatcher.forward(request, response);
                    return;
                } else {
                    throw new AuberginnException("Vous devez selectionner le client a supprimer");

                }
            }
             catch (Exception e)
            {
                List<String> listeMessageErreur = new LinkedList<String>();
                listeMessageErreur.add(e.getMessage());
                request.setAttribute("listeMessageErreur", listeMessageErreur);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererMembre.jsp");
                dispatcher.forward(request, response);

                // pour déboggage seulement : afficher tout le contenu de l'exception
                e.printStackTrace();;
            }

        }
        HttpSession session = request.getSession();
        if(session.getAttribute("admin")==null)
        {
            if (!AuberginnHelper.peutProcederLogin(getServletContext(), request, response))
            {
                // Le dispatch vers le login se fait dans AuberginnHelper.peutProceder
                return;
            }
        }

        System.out.println("Servlet Inscription : POST dispatch vers creerCompte.jsp");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerCompte.jsp");

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
