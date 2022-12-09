package auberginnServlet;

import Auberginn.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class AuberginnHelper
{
    public static boolean infoBDValide(ServletContext c)
    {
        return c.getAttribute("serveur") != null;
    }
    
    public static boolean peutProceder(ServletContext c, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(infoBDValide(c))
        {
            HttpSession session = request.getSession(false);
            if (AuberginnHelper.estConnecte(session))
            {
                return true;
            }
            DispatchToLogin(request, response);
            return false;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }
    
    public static boolean peutProcederLogin(ServletContext c, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(infoBDValide(c))
        {
            HttpSession session = request.getSession(false);
            if (session != null)
            {
                session.invalidate();
            }
            return true;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }
    
    public static void DispatchToLoginServlet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AuberginnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Login");
        dispatcher.forward(request, response);
    }
    
    public static void DispatchToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AuberginnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }
    
    public static void DispatchToBDConnect(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (AuberginnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
    
    public static boolean estConnecte(HttpSession session)
    {
        if(session == null)
            return false;
        return session.getAttribute("etat") != null;
    }
    
    public static boolean gestionnairesCrees(HttpSession session)
    {
        if(session == null)
            return false;
        return session.getAttribute("biblioInterrogation") != null;
    }
    
    public static void creerGestionnaire(ServletContext c, HttpSession s) throws SQLException, AuberginnException
    {
        String serveur = (String) c.getAttribute("serveur");
        String bd = (String) c.getAttribute("bd");
        String userIdBD = (String) c.getAttribute("user");
        String pass = (String) c.getAttribute("pass");

        GestionAubergeInn auberginnInterrogation = new GestionAubergeInn(serveur, bd, userIdBD, pass);
        auberginnInterrogation.getConnexion().setIsolationReadCommited();
        s.setAttribute("auberginnInterrogation", auberginnInterrogation);
        GestionAubergeInn auberginnUpdate = new GestionAubergeInn(serveur, bd, userIdBD, pass);
        s.setAttribute("auberginnUpdate", auberginnUpdate);
    }
    
    public static GestionAubergeInn getBiblioInterro(HttpSession session)
    {
        return (GestionAubergeInn)session.getAttribute("auberginnInterrogation");
    }
    
    public static GestionAubergeInn getBiblioUpdate(HttpSession session)
    {
        return (GestionAubergeInn)session.getAttribute("auberginnUpdate");
    }
    
    
    public static int ConvertirInt(String v, String nom) throws AuberginnException
    {
        try
        {
            return Integer.parseInt(v);
        }
        catch(Exception e)
        {
            throw new AuberginnException(nom + " ne doit �tre compos� que de chiffre.");
        }
    }
    
    public static long ConvertirLong(String v, String nom) throws AuberginnException
    {
        try
        {
            return Long.parseLong(v);
        }
        catch(Exception e)
        {
            throw new AuberginnException(nom + " ne doit �tre compos� que de chiffre.");
        }
    }
}
