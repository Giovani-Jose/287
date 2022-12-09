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
import java.util.LinkedList;
import java.util.List;

/**
 * Servlet qui gère la connexion d'un utilisateur au système de gestion de
 * bibliothèque
 * 
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 11 novembre 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 * </pre>
 */

public class Accueil extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Accueil : POST");
        try
        {
            HttpSession oldSession = request.getSession();
            HttpSession session = request.getSession();

            if(oldSession.getAttribute("admin")==null)
            {

                if (!AuberginnHelper.peutProcederLogin(getServletContext(), request, response))
                {
                    System.out.println("Servlet Accueil : POST ne peut pas procéder.");
                    // Le dispatch sera fait par AuberginnHelper.peutProceder
                    return;
                }

                 session = request.getSession();

                // Si c'est la première fois qu'on essaie de se logguer, ou
                // d'inscrire quelqu'un
                if (!AuberginnHelper.gestionnairesCrees(session))
                {
                    System.out.println("Servlet Accueil : POST Création des gestionnaires");
                    AuberginnHelper.creerGestionnaire(getServletContext(), session);
                }
            }



            if (request.getParameter("connecter") != null)
            {
                System.out.println("Servlet Accueil : POST - Connecter");
                try
                {
                    // lecture des paramètres du formulaire login.jsp
                    String userId = request.getParameter("userID");
                    String motDePasse = request.getParameter("motDePasse");

                    request.setAttribute("userID", userId);
                    request.setAttribute("motDePasse", motDePasse);
                                        
                    if (userId == null || userId.equals(""))
                        throw new AuberginnException("Le nom d'utilisateur ne peut pas être nul!");
                    if (motDePasse == null || motDePasse.equals(""))
                        throw new AuberginnException("Le mot de passe ne peut pas être nul!");

                    if (AuberginnHelper.getBiblioInterro(session).getGestionClient().informationsConnexionValide(userId,
                            motDePasse))
                    {
                        session.setAttribute("userID", userId);
                        if(AuberginnHelper.getBiblioInterro(session).getGestionClient().utilisateurEstAdministrateur(userId))
                            session.setAttribute("admin", true);
                        session.setAttribute("etat", new Integer(AubergeInnConstantes.CONNECTE));


                        System.out.println("Servlet Accueil : POST dispatch vers accueil.jsp");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
                        dispatcher.forward(request, response);
                    }
                    else
                    {
                        throw new AuberginnException("Les informations de connexion sont erronées.");
                    }
                }
                catch (Exception e)
                {
                    List<String> listeMessageErreur = new LinkedList<String>();
                    listeMessageErreur.add(e.getMessage());
                    request.setAttribute("listeMessageErreur", listeMessageErreur);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
                    dispatcher.forward(request, response);
                    // pour déboggage seulement : afficher tout le contenu de l'exception
                    e.printStackTrace();
                }
            }
            else if (request.getParameter("inscrire") != null)
            {
                System.out.println("Servlet Accueil : POST - Inscrire");
                try
                {
                    // lecture des paramètres du formulaire de creerCompte.jsp
                    String userId = request.getParameter("userID");
                    String motDePasse = request.getParameter("motDePasse");
                    String nom = request.getParameter("nom");
                    String prenom = request.getParameter("prenom");
                    String age = request.getParameter("age");

                    request.setAttribute("userID", userId);
                    request.setAttribute("motDePasse", motDePasse);
                    request.setAttribute("nom", nom);
                    request.setAttribute("prenom", prenom);
                    request.setAttribute("age",age);
                    
                    if (userId == null || userId.equals(""))
                        throw new AuberginnException("Vous devez entrer un nom d'utilisateur!");
                    if (motDePasse == null || motDePasse.equals(""))
                        throw new AuberginnException("Vous devez entrer un mot de passe!");

                    if (nom == null || nom.equals(""))
                        throw new AuberginnException("Vous devez entrer un nom!");

                    if (prenom == null || prenom.equals(""))
                        throw new AuberginnException("Vous devez entrer un prenom!");

                    if (age == null ||age.equals(""))
                        throw new AuberginnException("Vous devez entrer un age!");

                    String accesS = request.getParameter("acces");
                    int acces = 1;
                    if (accesS != null)
                        acces = AuberginnHelper.ConvertirInt(accesS, "Le niveau d'accès");


                    GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);
                    synchronized (aubergeUpdate)
                    {
                        aubergeUpdate.getGestionClient().inscrire(userId, motDePasse, acces, nom, prenom, Integer.parseInt(age));
                    }

                    // S'il y a déjà un userID dans la session, c'est parce
                    // qu'on est admin et qu'on inscrit un nouveau membre
                    System.out.println(session.getAttribute("userID"));
                    if (session.getAttribute("userID") == null)
                    {
                        session.setAttribute("userID", userId);
                        if(acces == 0)
                            session.setAttribute("admin", acces == 0);
                        session.setAttribute("etat", new Integer(AubergeInnConstantes.CONNECTE));

                        System.out.println("Servlet Accueil : POST dispatch vers accueil.jsp");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
                        dispatcher.forward(request, response);
                    }
                    else
                    {
                        // Vers gestionMembre?
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
                        dispatcher.forward(request, response);
                    }
                }
                catch (Exception e)
                {
                    List<String> listeMessageErreur = new LinkedList<String>();
                    listeMessageErreur.add(e.getMessage());
                    request.setAttribute("listeMessageErreur", listeMessageErreur);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/creerCompte.jsp");
                    dispatcher.forward(request, response);
                    // pour déboggage seulement : afficher tout le contenu de l'exception
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            List<String> listeMessageErreur = new LinkedList<String>();
            listeMessageErreur.add(e.getMessage());
            request.setAttribute("listeMessageErreur", listeMessageErreur);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
            dispatcher.forward(request, response);
            // pour déboggage seulement : afficher tout le contenu de l'exception
            e.printStackTrace();
        }
    }

    // Dans les formulaires, on utilise la méthode POST
    // donc, si le servlet est appelé avec la méthode GET
    // c'est que l'adresse a été demandé par l'utilisateur.
    // On procède si la connexion est actives seulement, sinon
    // on retourne au login
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("Servlet Accueil : GET");
        if (AuberginnHelper.peutProceder(getServletContext(), request, response))
        {
            System.out.println("Servlet Accueil : GET dispatch vers accueil.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
        }
    }

} // class
