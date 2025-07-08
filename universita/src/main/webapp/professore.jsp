<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/professore.css">
<title>Area Docente</title>

</head>
<body>
 
	<div class="logo-container">
		<img src="immagini\logo2.png" alt="logo università" class="logo">
	</div>
 
	<%
String successo = (String) session.getAttribute("successo_login");
if (successo != null) {
%>
	<div class="success"><%= successo %></div>
	<%
  session.removeAttribute("successo_login");
}
%>
 
 
	<%
String nome = (String) session.getAttribute("nome");
String cognome = (String) session.getAttribute("cognome");
String materia = (String) session.getAttribute("materia");
ResultSet appelli = (ResultSet) request.getAttribute("appelli");
ResultSet elenco = (ResultSet) request.getAttribute("elenco_studenti");
String nomeMateria = (String) request.getAttribute("Materia");
String Data = (String) request.getAttribute("Data");
 
if (nome == null && cognome == null) {
    response.sendRedirect("index.jsp");
}
%>
 
	<div class="container">
		<h1>Area Docente</h1>
		<p>
			Bentornato <strong><%=nome%> <%=cognome%></strong>
		</p>
 
 
		<% if (appelli != null) { %>
		<h2>
			Appelli disponibili per la materia:
			<%=materia %></h2>
		<table>
			<thead>
				<tr>
					<th>ID Appello</th>
					<th>Data</th>
				</tr>
			</thead>
			<tbody>
				<% while (appelli.next()) { %>
				<tr>
					<td><%=appelli.getInt(1)%></td>
					<td><%=appelli.getDate("Data") %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
 
		<form action="StampaStudenti" method="post">
			<label for="ID_appello">Inserisci ID Appello:</label> <input
				type="number" name="ID_appello" required>
			<button type="submit" class="btn">Vai</button>
		</form>
		<% } %>
 
		<% if (elenco != null) { %>
		<h2>
			Studenti prenotati per
			<%=nomeMateria%>
			-
			<%=Data%></h2>
		<table>
			<thead>
				<tr>
					<th>Nome</th>
					<th>Cognome</th>
					<th>Matricola</th>
				</tr>
			</thead>
			<tbody>
				<% while (elenco.next()) { %>
				<tr>
					<td><%=elenco.getString("nome")%></td>
					<td><%=elenco.getString("cognome")%></td>
					<td><%=elenco.getString("Matricola")%></td>
				</tr>
				<% } %>
			</tbody>
		</table>
		<% } %>
		<a href="logout.jsp" class="btn">Logout</a>
	</div>
 
	<footer>
		<p>
			Università degli Studi di Napoli Federico II - Corso Umberto I 40 -
			80138 Napoli - Centralino +39 081 2531111 - <a
				href="https://www.contactcenter.unina.it" target="_blank">www.contactcenter.unina.it</a>
			- C.F. 00876220633 - <a href="mailto:ateneo@pec.unina.it">ateneo@pec.unina.it</a>
		</p>
	</footer>
 
</body>
</html>
 