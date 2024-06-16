package control;

import Model.Bean.UserBean;
import Model.UserModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


//@WebServlet(name = "User", urlPatterns = "/User")
public class UserControl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static UserModel model = new UserModel();
	
	private boolean Validate(UserBean user) {
		
		// controllo se l'oggetto ha campi vuoti
        
        if (user.getNome() == null || user.getNome().equals("") || user.getCognome() == null || user.getCognome().equals("") || 
        	user.getUserName() == null || user.getUserName().equals("") || user.getDataNascita() == null || 
        	user.getDataNascita().toString().equals("") || user.getEmail() == null || user.getEmail().equals("") || 
        	user.getPassword() == null || user.getPassword().equals("")) {
        	
            return false;
        }
        
        return true;
	}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action != null) {

            if (action.equals("register")) {

                UserBean user = new UserBean();

                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String username = request.getParameter("new_username");
                String dataNascita = request.getParameter("dataNascita");
                String email = request.getParameter("email");
                
                boolean valid = false;

                // già qua credo che la passwoed vada crittografata con una funzione di hash
                String password = request.getParameter("new_password");

                // la data di nascita viene passata come stringa e
                // va convertita in formato Date della libreria sql

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parsed = null;

                try {
                    parsed = format.parse(dataNascita);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                java.sql.Date dataNascitaConvertita = new java.sql.Date(parsed.getTime());

                ////////////////////
                user.setNome(nome);
                user.setCognome(cognome);
                user.setUserName(username);
                user.setDataNascita(dataNascitaConvertita);
                user.setEmail(email);
                user.setPassword(password);
                ////////////////////////////
                
                // controlla se l'utente è valido
                valid = Validate(user);
                
                if(valid) {
                	// se valido lo registro nel db
                	try {
                		model.AddUser(user);
                		
                	} catch (SQLException e) {
                		// TODO Auto-generated catch block
                		e.printStackTrace();
                	}
                	// una volta registrato l'utente in qualche modo
                	// bisogna dirgli che deve effettuare il login
                	
                } else {
                	
                	System.out.println("utente inserito non valido");
                	
                	response.setContentType("application/json");
                	response.setCharacterEncoding("UTF-8");
                	
                	// Se i dati non sono validi, invia un messaggio di errore come risposta alla richiesta AJAX
                    response.getWriter().write("{\"error\": \"Compila i campi obbligatori!\"}");
                    return;
                }

            }

            else if (action.equals("login")) {
            	//per sicurezza , così se per situazioni losche qualcuno provi a forzare
            	//il login senza prima eseguire il logout
            	if(!(request.getSession().getAttribute("userID") == null))
            		request.getSession().invalidate();

                // Applica i dovuti controlli per verificare se l'utente è già loggato

                // Crea un pulsante per sloggarsi che è mostrato solo quando si è loggati
            		//aggiornalmento : lo gestisco nella jsp
            	
                // ricordati di impostare
                // (username) UNIQUE
                // (email) UNIQUEs

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                
                UserBean user;
				
                try {
					user = model.getUserTramiteCredenziali(username,password);

	                // ricorda che va implementata la funzione di hashing per la password
	                if (user != null && password.equals(user.getPassword())) {
	
	                    // L'utente esiste e la password è corretta
	                    // quindi setto i valori di sessione utili
	                    session.setAttribute("username", user.getUserName());
	                    session.setAttribute("userID", user.getID());
	                    session.setAttribute("cartID", user.getCartID());
	                    
	                    if(user.getPermessiAdmin()) 
	        				request.getSession().setAttribute("adminRoles", true);
	                    else 
	        				request.getSession().setAttribute("adminRoles", false);
	                    
	                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home.jsp");
	                    dispatcher.forward(request, response);
	                } else {
	                    // L'utente non esiste o la password non è corretta
	                    // mostra un messaggio di errore
	                	
	                	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/LoginRegistrazione.jsp");
	                    dispatcher.forward(request, response);
	                }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }
            else if (action.equals("DatiUtente")) {
                UserBean user;
            	Integer ID;
            	
            	ID = (Integer) request.getSession().getAttribute("userID");
            	System.out.println("Id recuperato (int,Integer): " + ID.intValue()+ " " + ID);
            	
            	//contollo che effettivamente sia loggato
            	if(!(ID == null)) {
            		try {
						user = model.getUserTramiteID(ID.intValue());
						
						request.setAttribute("datiUtente", user);
	                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AreaUtente.jsp");
	                    dispatcher.forward(request, response);
	                    
					} catch (SQLException e) {
						e.printStackTrace();
					}
            	}
            	
            }
            else if (action.equals("logout")) {

                // situazione in cui l'utente vuole sloggarsi
                // credo vada solo cancellata la sessione
            	
            	request.getSession().invalidate();
            	System.out.println("Sessione invalidata");
            	
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/LoginRegistrazione.jsp");
                dispatcher.forward(request, response);
            }
	        
	        
            
        }
        else{
        	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home.jsp");
	        dispatcher.forward(request, response);
	        // non so se bisogna fare qualcosa se l'action è vuoto
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    	// forse non va fatto nulla?
    }
}
