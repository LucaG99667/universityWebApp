package mypackage;
 
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
/**
* Servlet implementation class login
*/
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public login() {
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
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
 
	    Connection conn = Connessione.getCon();
	    HttpSession session = request.getSession(true);
 
	    boolean loginRiuscito = false; // ✅ flag per controllo
 
	    try {
	        // 1. LOGIN STUDENTE
	        Statement smt = conn.createStatement();
	        ResultSet rs = smt.executeQuery("SELECT username, password FROM studente");
 
	        while (rs.next()) {
	            if (rs.getString("username").equalsIgnoreCase(username) &&
	                rs.getString("password").equalsIgnoreCase(password)) {
 
	                PreparedStatement smt1 = conn.prepareStatement("SELECT matricola FROM studente WHERE username = ?");
	                smt1.setString(1, username);
	                ResultSet rs1 = smt1.executeQuery();
	                rs1.next();
	                String matricola = rs1.getString("matricola");
 
	                Statement smt2 = conn.createStatement();
	                ResultSet rs2 = smt2.executeQuery("SELECT idcorso, materia, nome, cognome FROM corso JOIN professore ON cattedra = idprofessore");
 
	                session.setAttribute("matricola", matricola);
	                session.setAttribute("successo_login", "Accesso effettuato con successo!");
 
	                request.setAttribute("tabella_corso", rs2);
	                RequestDispatcher rd = request.getRequestDispatcher("studente.jsp");
	                rd.forward(request, response);
	                loginRiuscito = true;
	                return;
	            }
	        }
 
	        // 2. LOGIN PROFESSORE
	        Statement smt3 = conn.createStatement();
	        ResultSet rs3 = smt3.executeQuery("SELECT username, password FROM professore");
 
	        while (rs3.next()) {
	            if (rs3.getString("username").equalsIgnoreCase(username) &&
	                rs3.getString("password").equalsIgnoreCase(password)) {
 
	                PreparedStatement smt4 = conn.prepareStatement("SELECT nome, cognome, idProfessore FROM professore WHERE username = ?");
	                smt4.setString(1, username);
	                ResultSet rs4 = smt4.executeQuery();
	                rs4.next();
 
	                String nome = rs4.getString("nome");
	                String cognome = rs4.getString("cognome");
	                int idProfessore = rs4.getInt("idProfessore");
 
	                PreparedStatement smt5 = conn.prepareStatement("SELECT idcorso, materia FROM corso WHERE cattedra = ?");
	                smt5.setInt(1, idProfessore);
	                ResultSet rs5 = smt5.executeQuery();
	                rs5.next();
 
	                int idcorso = rs5.getInt("idcorso");
	                String materia = rs5.getString("materia");
 
	                PreparedStatement smt6 = conn.prepareStatement("SELECT idAppello, Data FROM appello WHERE Materia = ?");
	                smt6.setInt(1, idcorso);
	                ResultSet appelli = smt6.executeQuery();
 
	                session.setAttribute("nome", nome);
	                session.setAttribute("cognome", cognome);
	                session.setAttribute("materia", materia);
	                session.setAttribute("successo_login", "Accesso effettuato con successo!");
 
	                request.setAttribute("appelli", appelli);
	                RequestDispatcher rd4 = request.getRequestDispatcher("professore.jsp");
	                rd4.forward(request, response);
	                loginRiuscito = true;
	                return;
	            }
	        }
 
	        // 3. SE LOGIN FALLISCE
	        if (!loginRiuscito) {
	            request.setAttribute("messaggio", "⚠️ Username o password errati. Riprova.");
	            RequestDispatcher rd3 = request.getRequestDispatcher("index.jsp");
	            rd3.forward(request, response);
	        }
 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        request.setAttribute("messaggio", "Errore interno. Contatta l'amministratore.");
	        RequestDispatcher rd3 = request.getRequestDispatcher("index.jsp");
	        rd3.forward(request, response);
	    }
	}
}
