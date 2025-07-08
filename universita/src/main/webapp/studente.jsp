<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Area Studenti</title>
<link rel="stylesheet" type="text/css" href="css/studente.css" />
</head>
<body>
 
	<div class="logo-container">
		<img src="immagini\logo.png" alt="logo università" class="logo">
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
String matricola = (String) session.getAttribute("matricola");
ResultSet res = (ResultSet) request.getAttribute("tabella_corso");
ResultSet res1 = (ResultSet) request.getAttribute("elenco_appelli");
String materia = (String) request.getAttribute("materia");
String messaggio = (String) request.getAttribute("messaggio");
String data = (String) request.getAttribute("data");
String materia2 = (String) request.getAttribute("materia2");
if (matricola == null) {
    response.sendRedirect("index.jsp");
}
%>
 
	<div class="container">
		<h1>Area Studenti</h1>
		<p>
			Benvenuto studente: <strong><%=matricola %></strong>
		</p>
 
 
		<% if (res != null) { %>
		<h2>Corsi Disponibili</h2>
		<table>
			<thead>
				<tr>
					<th>ID Corso</th>
					<th>Materia</th>
					<th>Nome Docente</th>
					<th>Cognome Docente</th>
				</tr>
			</thead>
			<tbody>
				<% while (res.next()) { %>
				<tr>
					<td><%=res.getInt("idcorso") %></td>
					<td><%=res.getString("materia") %></td>
					<td><%=res.getString("nome") %></td>
					<td><%=res.getString("cognome") %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
 
		<form action="Prenotazione" method="post">
			<label for="materia">ID Corso da prenotare:</label> <input
				type="number" name="materia" required>
			<button type="submit" class="btn">Prenota Corso</button>
		</form>
		<% } %>
 
		<% if (res1 != null) { %>
		<h2>
			Appelli disponibili per
			<%=materia%></h2>
		<table>
			<thead>
				<tr>
					<th>ID Appello</th>
					<th>Data</th>
				</tr>
			</thead>
			<tbody>
				<% while (res1.next()) { %>
				<tr>
					<td><%=res1.getInt(1)%></td>
					<td><%=res1.getDate("Data") %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
 
		<form action="Prenota" method="post">
			<label for="appello">ID Appello da prenotare:</label> <input
				type="number" name="appello" required>
			<button type="submit" class="btn">Prenota Appello</button>
		</form>
	
		<% } %>
 
		<% if (messaggio != null) { %>
		<p class="success"><%=messaggio %></p>
		<% } %>
 
		<% if (materia2 != null && data != null) { %>
		<p class="success">
			Prenotazione effettuata con successo in data
			<%=data%>
			per il corso
			<%=materia2%></p>
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