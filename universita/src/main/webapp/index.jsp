<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<title>Portale Università</title>
<link rel="stylesheet" type="text/css" href="css/index.css" />
</head>
<body>
 
	<div class="logo-container">
		<img src="immagini\logo.png" alt="logo università" class="logo">
	</div>
 
	<%
String errore = (String) request.getAttribute("messaggio");
if (errore != null) {
%>
	<div class="error"><%= errore %></div>
	<%
}
%>
 
	<%
  String messaggio = (String) request.getAttribute("errore");
  if (messaggio != null) {
%>
	<div class="error"><%= messaggio %></div>
	<%
  }
%>
 
	<div class="container">
		<h1>Benvenuto nel Portale Università</h1>
		<form method="post" action="login">
			<label for="username">Username</label> <input type="text"
				name="username" required> <label for="password">Password</label>
			<input type="password" name="password" required>
 
			<button type="submit">Accedi</button>
			<a href="logout.jsp" class="btn">Torna alla homepage</a>
		</form>
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