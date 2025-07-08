package mypackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoUtente = request.getParameter("tipo_utente"); // "s" o "p"
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String matricola = request.getParameter("matricola"); // Solo per studenti

        if (tipoUtente == null || (!tipoUtente.equals("s") && !tipoUtente.equals("p")) ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty()) {

            request.setAttribute("messaggio", "Errore! Inserisci tutti i dati obbligatori.");
            forwardToJsp(request, response, "register.jsp");
            return;
        }

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = Connessione.getCon();
            conn.setAutoCommit(false);

            // Verifica se username già esiste in entrambe le tabelle
            String checkQuery = tipoUtente.equals("s") ?
                    "SELECT username FROM studente WHERE username = ?" :
                    "SELECT username FROM professore WHERE username = ?";

            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("messaggio", "Username già esistente");
                forwardToJsp(request, response, "register.jsp");
                return;
            }

            if (tipoUtente.equals("s")) {
                // Inserisci studente
                if (matricola == null || matricola.trim().isEmpty()) {
                    request.setAttribute("messaggio", "Inserire la matricola per lo studente.");
                    forwardToJsp(request, response, "register.jsp");
                    return;
                }

                insertStmt = conn.prepareStatement(
                    "INSERT INTO studente (matricola, nome, cognome, username, password, tipo_utente) VALUES (?, ?, ?, ?, ?, ?)");
                insertStmt.setString(1, matricola);
                insertStmt.setString(2, nome);
                insertStmt.setString(3, cognome);
                insertStmt.setString(4, username);
                insertStmt.setString(5, password);
                insertStmt.setString(6, "s");

            } else {
                // Inserisci professore
                insertStmt = conn.prepareStatement(
                    "INSERT INTO professore (nome, cognome, username, password, tipo_utente) VALUES (?, ?, ?, ?, ?)");
                insertStmt.setString(1, nome);
                insertStmt.setString(2, cognome);
                insertStmt.setString(3, username);
                insertStmt.setString(4, password);
                insertStmt.setString(5, "p");
            }

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                conn.commit();
                request.setAttribute("messaggio", "Registrazione completata con successo!");
            } else {
                conn.rollback();
                request.setAttribute("messaggio", "Errore durante la registrazione");
            }

            forwardToJsp(request, response, "register.jsp");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            request.setAttribute("messaggio", "Errore del database: " + e.getMessage());
            forwardToJsp(request, response, "register.jsp");
        } 
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPage)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(jspPage);
        rd.forward(request, response);
    }
}
