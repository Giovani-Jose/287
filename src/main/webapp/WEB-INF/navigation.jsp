<nav class="navbar navbar-expand-lg navbar-dark bg-dark">

	<div class="navbar-collapse collapse">
    	<ul class="nav navbar-nav">
	<%
	    if (session.getAttribute("admin") != null)
	    {
	%>
			<li><a class="nav-item nav-link" href="GererMembre" >G�rer les clients</a></li>
			<li><a class="nav-item nav-link" href="GererChambre">G�rer les chambres</a></li>
			<li><a class="nav-item nav-link" href="GererCommodite">G�rer les commodites</a></li>

			<%
	    }
	%>
		</ul>
		</div>
		<div class="navbar-collapse collapse justify-content-end">
		<ul class="nav navbar-nav navbar-right">
			<li><a class="nav-item nav-link" href="Logout">D�connexion</a></li>
		</ul>
	</div>
</nav>
