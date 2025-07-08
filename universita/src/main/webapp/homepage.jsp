<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/homepage.css">
<title>Home Page</title>


</head>
<body>
	<div class="wrapper">
		<header>
			<div class="intestazione">
				<img src="immagini/logo.png" alt="Logo Federico II" class="logo">
				<h1>Università degli studi di Napoli FEDERICO II</h1>
			</div>
		</header>

		<nav>
			<a href="index.jsp">Accedi</a> 
			<a href="register.jsp">Registrati</a>
		</nav>

		<section class="galleria">
			<h2>La nostra Struttura</h2>
			<div class="immagini-facolta">
				<img src="immagini/facolta.jpg" alt="Facoltà"> 
				<img src="immagini/campus.jpg" alt="Campus"> 
				<img src="immagini/aula.jpg" alt="Aula"> 
				<img src="immagini/biblioteca.jpg" alt="Biblioteca">
			</div>
		</section>
	</div> <!-- chiude wrapper -->

	<footer>
		<p>
			Università degli Studi di Napoli Federico II - Corso Umberto I 40 - 80138 Napoli - 
			Centralino +39 081 2531111 - 
			<a href="https://www.contactcenter.unina.it" target="_blank">www.contactcenter.unina.it</a> - 
			C.F. 00876220633 - 
			<a href="mailto:ateneo@pec.unina.it">ateneo@pec.unina.it</a>
		</p>
	</footer>
</body>
</html>