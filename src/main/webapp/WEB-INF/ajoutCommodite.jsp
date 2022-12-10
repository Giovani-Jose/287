<%@ page import="java.util.*,java.text.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>IFT287 - Système de gestion de bibliothèque</title>
  <meta name="author" content="Vincent Ducharme">
  <meta name="description" content="Page de création de compte du système de gestion de la bilbiothèque.">

  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

</head>
<body>
<div class="container">
  <h1 class="text-center">Ajouter une commodité</h1>
  <div class="col-md-4 offset-md-4">
    <form action="GestionCommodite" method="POST">
      <div class="form-group">
        <label for="commoditeId">Id commodité</label>
        <input class="form-control" type="TEXT" name="commoditeId" value="<%= request.getAttribute("commoditeId") != null ? (String)request.getAttribute("commoditeId") : "" %>">
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input class="form-control" type="Text" name="description" value="<%= request.getAttribute("description") != null ? (String)request.getAttribute("description") : "" %>">
      </div>
      <div class="form-group">
        <label for="prixSurplus">Prix surplus</label>
        <input class="form-control" type="number" name="prixSurplus" value="<%= request.getAttribute("prixSurplus") != null ? (String)request.getAttribute("prixSurplus") : "" %>">
      </div>

      <%
        Random rand = new Random();
        int n = rand.nextInt(90000) + 10000;
      %>

      <input class="btn btn-primary" type="SUBMIT" name="AjoutCommodite" value="Ajouter la commodite">
      <input type="hidden" id="custId" name="custId" value="<%=n%>">
    </form>
  </div>
</div>
<br>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<br>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
