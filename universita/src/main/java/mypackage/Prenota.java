package mypackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//import jakarta.servlet.http.HttpSession;
/**
 * Servlet implementation class Prenota
 */
@WebServlet("/Prenota")
public class Prenota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Prenota() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appello = request.getParameter("appello");
        HttpSession session = request.getSession(false);
        String matricola = (String) session.getAttribute("matricola");
        
     
        
        Connection conn = null;
        PreparedStatement smt2 = null;
        PreparedStatement recuperoData = null;
        PreparedStatement recuperoMateria = null;
        ResultSet data = null;
        ResultSet materia = null;
        
        try {
            conn = Connessione.getCon();
            
            // Prima verifica se l'appello esiste
            recuperoData = conn.prepareStatement("SELECT data FROM appello WHERE idAppello = CAST(? AS UNSIGNED INTEGER)");
            recuperoData.setString(1, appello);
            data = recuperoData.executeQuery();
            
            if (!data.next()) {
                request.setAttribute("messaggio", "Errore! L'appello selezionato non esiste");
                RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
                rd.forward(request, response);
                return;
            }
            
            String dataScelta = data.getString(1);
            
            // Recupera la materia associata all'appello
            recuperoMateria = conn.prepareStatement("SELECT c.materia FROM corso c JOIN appello a ON c.idcorso = a.materia WHERE a.idAppello = CAST(? AS UNSIGNED INTEGER)");
            recuperoMateria.setString(1, appello);
            materia = recuperoMateria.executeQuery();
            
            if (!materia.next()) {
                request.setAttribute("messaggio", "Errore! Materia non trovata per l'appello selezionato");
                RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
                rd.forward(request, response);
                return;
            }
            
            String nomeMateria = materia.getString(1);
            
            // Effettua la prenotazione
            smt2 = conn.prepareStatement("INSERT INTO prenotazione (stud_prenotato, app_prenotato) VALUES (CAST(? AS UNSIGNED INTEGER), CAST(? AS UNSIGNED INTEGER))");
            PreparedStatement checkDuplicate = conn.prepareStatement(
            	    "SELECT * FROM prenotazione WHERE stud_prenotato = CAST(? AS UNSIGNED INTEGER) AND app_prenotato = CAST(? AS UNSIGNED INTEGER)"
            	);
            	checkDuplicate.setString(1, matricola);
            	checkDuplicate.setString(2, appello);

            	ResultSet duplicateResult = checkDuplicate.executeQuery();

            	if (duplicateResult.next()) {
            	    request.setAttribute("messaggio", "Sei già prenotato a questo appello.");
            	    RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
            	    rd.forward(request, response);
            	    return;
            	}
            smt2.setString(1, matricola);
            smt2.setString(2, appello);
            int rowsAffected = smt2.executeUpdate();
            
            if (rowsAffected == 0) {
                request.setAttribute("messaggio", "Errore durante la prenotazione");
            } else {
                request.setAttribute("messaggio", "Prenotazione effettuata con successo!");
                request.setAttribute("data", dataScelta);
                request.setAttribute("materia2", nomeMateria);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggio", "Errore del database: " + e.getMessage());
        } catch (NumberFormatException e) {
            request.setAttribute("messaggio", "Errore! ID appello o matricola non validi");
        }
        RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
        rd.forward(request, response);

    }
}
