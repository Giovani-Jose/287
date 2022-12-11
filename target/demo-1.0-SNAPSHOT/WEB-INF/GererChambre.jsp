<%@ page import="Auberginn.TupleClient" %>
<%@ page import="auberginnServlet.AuberginnHelper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Auberginn.AuberginnException" %>
<%@ page import="java.util.Random" %>
<%@ page import="Auberginn.TupleChambre" %><%--
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

    <jsp:include page="/WEB-INF/navigation.jsp" />
    <h1 class="text-center">Auberginn</h1>
    <h3 class="text-center">Liste des chambres</h3>
    <form action="ActionChambre" method="POST">

    <div class="col-8 offset-2">

        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col"></th>
                <th scope="col">no de chambre</th>
                <th scope="col">nom de chambre</th>
                <th scope="col">type de lit</th>
                <th scope="col">prix de base</th>

            </tr>
            </thead>
            <tbody>
            <%
                List<TupleChambre> chambres = null;
                try {
                    chambres = AuberginnHelper.getBiblioInterro(session).getGestionInterrogation()
                            .listerTouteChambres();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (TupleChambre chambre : chambres)
                {
            %>

            <tr>

                <td>
                <input type="checkbox" name="SelectionChambre" value = "<%=chambre.getIdChambre()%>">


            </td>
                <td><%=chambre.getIdChambre()%></td>
                <td><%=chambre.getNomChambre()%></td>
                <td><%=chambre.getTypeLit()%></td>
                <td><%=chambre.getPrixBase()%></td>


            <tr>
                <td></td>
                <td colspan="2">
                    <%

                            // end else livre en retard
                        } // end for all members
                    %>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

        <div class="col-xs-4 text-center offset-3">


        <div class="row">
            <div class="col-md-2">
                <input class="btn btn-outline-primary" type="SUBMIT" name="AjoutChambre" value="Ajouter une chambre">
            </div>
            <div class="col-md-2">
                <input class="btn btn-dark" type="SUBMIT" name="AfficherChambre" value="Afficher une chambre">
            </div>
            <div class="col-md-2">
                <input class="btn btn-outline-danger" type="SUBMIT" name="SupprimerChambre" value="Supprimer Chambre">
            </div>
            <div class="col-md-2">
                <input class="btn btn-outline-success" type="SUBMIT" name="AfficherChambreLibres" value="Afficher Chambre libres">
            </div>
        </div>
            <%
                Random rand = new Random();
                int n = rand.nextInt(90000) + 10000;
            %>
            <input type="hidden" id="custId" name="custId" value="<%=n%>">


        </div>
    </form>


</div>



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
