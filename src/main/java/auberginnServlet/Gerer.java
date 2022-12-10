package auberginnServlet;

import Auberginn.GestionAubergeInn;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;


public class Gerer extends HttpServlet {

    private String message;
    private String randomVal;


    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {


        String url = request.getRequestURL().toString();
        String lastPath = url.substring(url.lastIndexOf('/'));
        RequestDispatcher dispatcher;

        switch (lastPath)
        {
            case "/GererMembre":
                dispatcher = request.getRequestDispatcher("/WEB-INF/GererMembre.jsp");
                dispatcher.forward(request, response);
                break;
            case "/GererChambre":
                dispatcher = request.getRequestDispatcher("/WEB-INF/GererChambre.jsp");
                dispatcher.forward(request, response);
                break;
            case "/GererCommodite":
                dispatcher = request.getRequestDispatcher("/WEB-INF/GererCommodite.jsp");
                dispatcher.forward(request, response);
                break;
        }

    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {


        String url = request.getRequestURL().toString();
        String lastPath = url.substring(url.lastIndexOf('/'));
        RequestDispatcher dispatcher;

        if(Objects.equals(randomVal, request.getParameter("custId")))

        {
            dispatcher = request.getRequestDispatcher("/WEB-INF/GererChambre.jsp");
            dispatcher.forward(request, response);
            return;
        }


        switch (lastPath)
        {
            case "/ActionChambre":

                if(request.getParameter("AjoutChambre")!=null)
                {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/creerChambre.jsp");
                    dispatcher.forward(request, response);
                }else if(request.getParameter("SupprimerChambre")!=null && request.getParameter("SelectionChambre")!=null)
                {
                    actionSuppChambre(request);
                    dispatcher = request.getRequestDispatcher("/WEB-INF/GererChambre.jsp");
                    dispatcher.forward(request, response);
                }
                else if(request.getParameter("AfficherChambreLibres")!=null)
                {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/AfficherChambresLibres.jsp");
                    dispatcher.forward(request, response);
                }

                break;

            case "/ActionCommodite":

                if(request.getParameter("AjoutCommodite")!=null)
                {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/ajoutCommodite.jsp");
                    dispatcher.forward(request, response);
                }
                else if(request.getParameter("InclureCommodite")!=null && request.getParameter("SelectionCommodite")!=null)
                {
                    request.setAttribute("commoditeId", request.getParameter("SelectionCommodite"));
                    dispatcher = request.getRequestDispatcher("/WEB-INF/inclureCommodite.jsp");
                    dispatcher.forward(request, response);
                }
                else if(request.getParameter("EnleverCommodite")!=null)
                {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/AfficherChambresLibres.jsp");
                    dispatcher.forward(request, response);
                }

                break;



        }

    }
    public void actionSuppChambre(HttpServletRequest request)
    {
        String[] res = request.getParameterValues("SelectionChambre");
        HttpSession session = request.getSession();

        for(String id:res)
        {
            GestionAubergeInn aubergeUpdate = AuberginnHelper.getBiblioUpdate(session);
            synchronized (aubergeUpdate)
            {
                try {
                    aubergeUpdate.getGestionChambre().supprimerChambre(Integer.parseInt(id));
                    randomVal = request.getParameter("custId");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }




    public void destroy() {
        // do nothing.
    }

}