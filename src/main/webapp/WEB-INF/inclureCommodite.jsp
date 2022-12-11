<%@ page import="Auberginn.TupleClient" %>
<%@ page import="auberginnServlet.AuberginnHelper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Auberginn.AuberginnException" %>
<%@ page import="java.util.Random" %>
<%@ page import="Auberginn.TupleChambre" %>
<%@ page import="java.util.LinkedList" %><%--
  Created by IntelliJ IDEA.
  User: gtchi
  Date: 2022-12-07
  Time: 10:46 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<head>
    <title>IFT287 - AubergeInn</title>
    <meta name="author" content="Vincent Ducharme">
    <meta name="description"
          content="Page d'accueil de AubergeInn.">

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
          crossorigin="anonymous">

</head>
<body>
<div class="contain">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">

        <div class="navbar-collapse collapse justify-content-end">
            <ul class="nav navbar-nav navbar-right">
                <li><a class="nav-item nav-link" href="Logout">Déconnexion</a></li>
            </ul>
        </div>
    </nav>
    <h1 class="text-center">Auberginn</h1>
    <h3 class="text-center">Choisir une chambre</h3>
    <form action="GestionCommodite" method="POST">
        <div class="col-8 offset-2">

            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col"></th>
                    <th scope="col">Numéro de chambre</th>
                    <th scope="col">Nom</th>
                    <th scope="col">Type lit</th>
                    <th scope="col">Prix total</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<TupleChambre> chambres = null;
                    if (request.getAttribute("typeAction") == "inclure"){
                        try {
                            chambres = AuberginnHelper.getBiblioInterro(session).getGestionInterrogation()
                                    .listerTouteChambres();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (chambres.size() < 1) {
                            List<String> listeMessageErreur = new LinkedList<String>();
                            listeMessageErreur.add("Aucune chambre existante");
                            request.setAttribute("listeMessageErreur", listeMessageErreur);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererCommodite.jsp");
                            dispatcher.forward(request, response);
                        }
                    }
                    else if (request.getAttribute("typeAction") == "enlever"){
                        try {
                            chambres = AuberginnHelper.getBiblioInterro(session).getGestionInterrogation()
                                    .listerChambreAvecCommodite(Integer.parseInt((String)request.getAttribute("commoditeId")));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (chambres.size() < 1) {
                            List<String> listeMessageErreur = new LinkedList<String>();
                            listeMessageErreur.add("Aucune chambre ne possède cette commodité");
                            request.setAttribute("listeMessageErreur", listeMessageErreur);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/GererCommodite.jsp");
                            dispatcher.forward(request, response);
                        }
                    }
                    for (TupleChambre chambre : chambres)
                    {
                %>

                <tr>

                    <td>
                        <input type="radio" name="SelectChambre" value = "<%=chambre.getIdChambre()%>">


                    </td>
                    <td><%=chambre.getIdChambre()%></td>
                    <td><%=chambre.getNomChambre()%></td>
                    <td><%=chambre.getTypeLit()%></td>
                    <td><%=chambre.getPrixTotal()%></td>


                <tr>
                    <td></td>
                    <td colspan="2">
                        <%
                            } // end for
                        %>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="col-xs-4 text-center offset-3">
            <div class="row">

                <% if (request.getAttribute("typeAction") == "inclure"){ %>

                -<input class="btn btn-primary" type="SUBMIT" name="AjoutService" value="Inclure commodite">

                <% } %>
                <% if (request.getAttribute("typeAction") == "enlever"){ %>

                -<input class="btn btn-danger" type="SUBMIT" name="EnleverService" value="Retirer commodite">

                <% } %>

            <%
                Random rand = new Random();
                int n = rand.nextInt(90000) + 10000;
            %>

                <input type="hidden" id="custId" name="custId" value="<%=n%>">
                <input type="hidden" id="commoditeId" name="commoditeId" value="<%=request.getAttribute("commoditeId")%>">

            </div>
        </div>
    </form>
</div>

<br>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<br>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>

</body>
</html>
