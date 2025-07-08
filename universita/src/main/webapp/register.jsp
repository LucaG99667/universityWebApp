<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/register.css">
    <title>Registrazione Utente</title>
    <script>
        function toggleTipoUtente(tipo) {
            const tipoUtenteInput = document.getElementById("tipo_utente");
            const matricolaDiv = document.getElementById("matricolaField");
            const idProfessoreDiv = document.getElementById("idProfessoreField");

            if (tipo === 's') {
                tipoUtenteInput.value = 's';
                matricolaDiv.style.display = 'block';
                idProfessoreDiv.style.display = 'none';
            } else {
                tipoUtenteInput.value = 'p';
                matricolaDiv.style.display = 'none';
                idProfessoreDiv.style.display = 'block';
            }
        }

        window.onload = function () {
            toggleTipoUtente('s'); // default: studente
        };
    </script>
</head>
<body>

    <h2>Registrazione</h2>
    <a href="homepage.jsp">Torna alla homepage</a>

    <%
        String messaggio = (String) request.getAttribute("messaggio");
        if (messaggio != null) {
            boolean isSuccess = messaggio.toLowerCase().contains("successo");
            String colore = isSuccess ? "green" : "red";
    %>
        <p style="color:<%= colore %>"><%= messaggio %></p>
    <% } %>

    <form action="register" method="post">
        <!-- Toggle buttons -->
        <label>Seleziona tipo utente:</label><br>
        <input type="radio" name="scelta_tipo" onclick="toggleTipoUtente('s')" checked> Studente
        <input type="radio" name="scelta_tipo" onclick="toggleTipoUtente('p')"> Professore
        <br><br>

        <!-- Hidden input to hold s or p -->
        <input type="hidden" name="tipo_utente" id="tipo_utente" value="s">

        <label>Username:</label>
        <input type="text" name="username" required><br>

        <label>Password:</label>
        <input type="password" name="password" required><br>

        <div id="matricolaField">
            <label>Matricola:</label>
            <input type="text" name="matricola"><br>
        </div>

        <div id="idProfessoreField" style="display: none;">
            <label>ID Professore:</label>
            <input type="text" name="idProfessore"><br>
        </div>

        <label>Nome:</label>
        <input type="text" name="nome" required><br>

        <label>Cognome:</label>
        <input type="text" name="cognome" required><br>

        <input type="submit" value="Registrati">
    </form>
</body>
</html>