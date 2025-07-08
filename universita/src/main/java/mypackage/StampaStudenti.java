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
//import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class StampaStudenti
 */
@WebServlet("/StampaStudenti")
public class StampaStudenti extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StampaStudenti() {
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

	/*
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idAppello = request.getParameter("ID_appello");
        
        // Controllo iniziale del parametro
        if (idAppello == null || idAppello.trim().isEmpty()) {
            sendError(request, response, "ID appello non fornito");
            return;
        }
        
        Connection conn = null;
        PreparedStatement smt = null, smt2 = null, smt1 = null;
        ResultSet rs = null, rs2 = null, rs1 = null;
        
        try {
            conn = Connessione.getCon();
            
            // 1. Verifica esistenza appello e recupero dati
            smt = conn.prepareStatement("SELECT Materia, Data FROM appello WHERE idAppello = CAST(? AS UNSIGNED INTEGER)");
            smt.setString(1, idAppello);
            rs = smt.executeQuery();
            
            if (!rs.next()) {
                sendError(request, response, "L'appello con ID " + idAppello + " non esiste nel database");
                return;
            }
            
            String materiaId = rs.getString("Materia");
            String data = rs.getString("Data");
            
            // 2. Recupera nome materia
            smt2 = conn.prepareStatement("SELECT Materia FROM corso WHERE idcorso = CAST(? AS UNSIGNED INTEGER)");
            smt2.setString(1, materiaId);
            rs2 = smt2.executeQuery();
            
            if (!rs2.next()) {
                sendError(request, response, "Materia non trovata per questo appello");
                return;
            }
            
            String nomeMateria = rs2.getString("Materia");
            
            // 3. Recupera studenti prenotati
            smt1 = conn.prepareStatement(
                "SELECT s.nome, s.cognome, s.Matricola " +
                "FROM studente s " +
                "JOIN prenotazione p ON s.Matricola = p.stud_prenotato " +
                "WHERE p.app_prenotato = CAST(? AS UNSIGNED INTEGER)");
            smt1.setString(1, idAppello);
            rs1 = smt1.executeQuery();
            
            // Verifica se ci sono studenti prenotati
            if (!rs1.isBeforeFirst()) {
                request.setAttribute("messaggio", "Nessuno studente prenotato per questo appello");
            }
            
            request.setAttribute("Materia", nomeMateria);
            request.setAttribute("Data", data);
            request.setAttribute("elenco_studenti", rs1);
            
            RequestDispatcher rd = request.getRequestDispatcher("professore.jsp");
            rd.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            sendError(request, response, "Errore del database: " + e.getMessage());
        } catch (NumberFormatException e) {
            sendError(request, response, "ID appello non valido (deve essere un numero)");
        } 
    }
    
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message) 
            throws ServletException, IOException {
        request.setAttribute("messaggio", message);
        RequestDispatcher rd = request.getRequestDispatcher("professore.jsp");
        rd.forward(request, response);
    }
    
}

