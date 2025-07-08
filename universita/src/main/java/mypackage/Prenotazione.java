package mypackage;
//import java.sql.Statement;
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
//import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class Prenotazione
 */
@WebServlet("/Prenotazione")
public class Prenotazione extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Prenotazione() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String materia = request.getParameter("materia");
	    
	    // Controllo iniziale se la materia è null o vuota
	    if (materia == null || materia.trim().isEmpty()) {
	        request.setAttribute("messaggio", "Errore! Seleziona un corso valido");
	        RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	        rd.forward(request, response);
	        return;
	    }
	    
	    Connection conn = Connessione.getCon();
	    try {
	        // Verifica se la materia esiste nel database
	        PreparedStatement smt1 = conn.prepareStatement("SELECT materia FROM corso WHERE idcorso = CAST(? AS UNSIGNED INTEGER)");
	        smt1.setString(1, materia);
	        ResultSet rs1 = smt1.executeQuery();
	        
	        if (!rs1.next()) { // Se non ci sono risultati
	            request.setAttribute("messaggio", "Errore! Il corso selezionato non esiste");
	            RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	            rd.forward(request, response);
	            return;
	        }
	        
	        String nomeMateria = rs1.getString(1);
	        
	        // Recupera gli appelli per la materia
	        PreparedStatement smt = conn.prepareStatement("SELECT idAppello, Data FROM appello WHERE materia = CAST(? AS UNSIGNED INTEGER)");
	        smt.setString(1, materia);
	        ResultSet rs = smt.executeQuery();
	        
	        RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	        request.setAttribute("materia", nomeMateria);
	        request.setAttribute("elenco_appelli", rs);
	        rd.forward(request, response);
	        
	    } catch (SQLException e) {
	        // Gestione degli errori SQL
	        request.setAttribute("messaggio", "Errore del database: " + e.getMessage());
	        RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	        rd.forward(request, response);
	    } catch (NumberFormatException e) {
	        // Gestione del caso in cui l'ID corso non sia un numero
	        request.setAttribute("messaggio", "Errore! ID corso non valido");
	        RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	        rd.forward(request, response);
	    } 
	}
	
	
	
	
	}
	


