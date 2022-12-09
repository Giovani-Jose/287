package auberginnServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Gerer extends HttpServlet {

    private String message;

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

        }

    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {


        String url = request.getRequestURL().toString();
        String lastPath = url.substring(url.lastIndexOf('/'));
        RequestDispatcher dispatcher;

        System.out.println(lastPath);

        switch (lastPath)
        {
            case "/ajoutChambre":
                dispatcher = request.getRequestDispatcher("/WEB-INF/creerChambre.jsp");
                dispatcher.forward(request, response);
                break;

        }

    }

    public void destroy() {
        // do nothing.
    }

}