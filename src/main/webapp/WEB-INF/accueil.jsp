<%@ page import="java.util.*,java.text.*,auberginnServlet.*,Auberginn.*"
		 contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
	<title>IFT287 - Système de gestion de bibliothèque</title>
	<meta name="author" content="Vincent Ducharme">
	<meta name="description"
		  content="Page d'accueil du système de gestion de la bilbiothèque.">

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
<div class="container">
	<jsp:include page="/WEB-INF/navigation.jsp" />
	<h1 class="text-center">Auberginn</h1>

	<%
		if (session.getAttribute("admin") != null)
		{
	%>
	<h3 class="text-center">Liste des reservations de tout les clients</h3>
	<div class="col-8 offset-2">
		<table class="table">
			<thead class="thead-dark">
			<tr>
				<th scope="col">Utilisateur</th>
				<th scope="col">no de chambre</th>
				<th scope="col">prix total</th>
				<th scope="col">date debut de reservation</th>
				<th scope="col">date fin de reservation</th>


			</tr>
			</thead>
			<tbody>
			<%
				List<TupleReservation> reservations = null;
				try {
					reservations = AuberginnHelper.getBiblioInterro(session).getGestionInterrogation()
							.listerTouteReservations((String)session.getAttribute("userID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				for (TupleReservation m : reservations)
				{
			%>
			<tr>
				<td><%=m.getUtilisateur()%></td>
				<td><%=m.getIdChambre()%></td>
				<td><%=m.getPrixTotal()%></td>
				<td><%=m.getDateDebut()%></td>
				<td><%=m.getDateFin()%></td>


			<tr>
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
	<%
	} // end if admin
	else
	{
		GestionAubergeInn b = AuberginnHelper.getBiblioInterro(session);
		List<TupleReservation> reservations = null;
		try {
			reservations = b.getGestionInterrogation().listerTouteReservations((String) session.getAttribute("userID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	%>
	<h3 class="text-center">Mes Reservations</h3>
	<div class="col-8 offset-2">
		<table class="table">
			<thead class="thead-dark">
			<tr>
				<th scope="col">#</th>
				<th scope="col">numero de chambre</th>
				<th scope="col">Prix total</th>
				<th scope="col">Date de debut</th>
				<th scope="col">Date de fin</th>
			</tr>
			</thead>
			<tbody>
			<%
				for(TupleReservation r : reservations)
				{
			%>
			<tr>
				<td><%= r.getIdChambre() %></td>
				<td><%= r.getPrixTotal()%></td>
				<td><%= r.getDateDebut() %></td>
				<td><%= r.getDateFin() %></td>
			</tr>
			<%
				}
			%>
			</tbody>
		</table>
	</div>
	<%

		}
	%>

	<br>
	<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
	<jsp:include page="/WEB-INF/messageErreur.jsp" />
	<br>
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
