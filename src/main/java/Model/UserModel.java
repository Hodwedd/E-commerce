package Model;

import Model.Bean.UserBean;
import Model.Interface.UserModel_intf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class UserModel implements UserModel_intf {

	private static DataSource ds;

    private Connection connection = null;
    private PreparedStatement pstmt = null;
    
    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            ds = (DataSource) envCtx.lookup("jdbc/ecommerce");

        } catch (NamingException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    @Override
    public void AddUser(UserBean user) throws SQLException  {

        String sql = "INSERT INTO utente (NOME, COGNOME, USER_NAME, DATA_NASCITA, email, password) VALUES (?, ?, ?, ?, ?, ?)";
        
        String sql2 = "SELECT id FROM utente WHERE user_name = ? AND password = ?";

        String sql3 = "INSERT INTO ordine (utente_id) VALUES (?)";
        int idUtente = -1;
        
        try {
            connection = ds.getConnection();

            if (connection != null) {
                pstmt = connection.prepareStatement(sql);

                pstmt.setString(1, user.getNome());
                pstmt.setString(2, user.getCognome());
                pstmt.setString(3, user.getUserName());
                pstmt.setString(4, user.getDataNascita().toString());
                pstmt.setString(5, user.getEmail());
                pstmt.setString(6, user.getPassword());

                //controllo sul numero di righe inserite
                int rowsInserted = pstmt.executeUpdate();
                
                //connection.commit();

                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }

            } else {
                System.out.println("problema con la connessione al db");
            }

        } finally {
            if (pstmt != null) 
                    pstmt.close();            
            if (connection != null) 
                    connection.close();
        }
        
        try {
            connection = ds.getConnection();

            if (connection != null) {
                pstmt = connection.prepareStatement(sql2);

                pstmt.setString(1, user.getUserName());
                pstmt.setString(2, user.getPassword());

                //controllo sul numero di righe inserite
                ResultSet rs = pstmt.executeQuery();
                
                if(rs.next()) {
                	idUtente = rs.getInt("id");
                }
                
            } else {
                System.out.println("problema con la connessione al db");
            }

        } finally {
            if (pstmt != null) 
                    pstmt.close();            
            if (connection != null) 
                    connection.close();
        }
        
        try {
            connection = ds.getConnection();

            if (connection != null) {
                pstmt = connection.prepareStatement(sql3);

                pstmt.setInt(1, idUtente);
                
                //controllo sul numero di righe inserite
                int rowsInserted = pstmt.executeUpdate();
                
                //connection.commit();

                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }

            } else {
                System.out.println("problema con la connessione al db");
            }

        } finally {
            if (pstmt != null) 
                    pstmt.close();            
            if (connection != null) 
                    connection.close();
        }
    }

    public UserBean getUserTramiteCredenziali(String username,String pwd) throws SQLException  {

        UserBean user = new UserBean();

        String sql = "SELECT utente.* , Ordine.ID as cartID FROM utente"
        		+ " JOIN ordine ON ordine.utente_id = utente.id"
        		+ " WHERE USER_NAME = ? AND password = ? AND ordine.stato = 'in corso';";

        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, pwd);

            ResultSet rs = pstmt.executeQuery();

            //connection.commit();

            if (rs.next()) {

                user.setID(rs.getInt("ID"));
                user.setUserName(rs.getString("USER_NAME"));
                user.setPassword(rs.getString("password"));
                user.setCartID(rs.getInt("cartID"));
                user.setIndirizzoBase(rs.getString("indirizzoBase"));
                user.setNumeroDiTelefono(rs.getString("telefono"));
                user.setPermessiAdmin(rs.getBoolean("permessi_admin"));

            } else {

                System.out.println("l'utente "+ username +" non è stato trovato");
            }

        }finally {
            if (pstmt != null) 
                    pstmt.close();
            if (connection != null) 
                    connection.close();   
        }

        return user;
        
    }

    public UserBean getUserTramiteID(int ID) throws SQLException  {

        UserBean user = new UserBean();

        String sql = "SELECT utente.* FROM utente"
        		+ " WHERE utente.ID = ?";

        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, ID);

            ResultSet rs = pstmt.executeQuery();

            //connection.commit();

            if (rs.next()) {
            	user.setNome(rs.getString("nome"));
            	user.setCognome(rs.getString("cognome"));
                user.setID(rs.getInt("ID"));
                user.setUserName(rs.getString("USER_NAME"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCartID(-1);
                user.setIndirizzoBase(rs.getString("indirizzoBase"));
                user.setNumeroDiTelefono(rs.getString("telefono"));
                user.setPermessiAdmin(rs.getBoolean("permessi_admin"));

            } else {
                System.out.println("accesso negato, l'utente non è stato trovato");
            }

        }finally {
            if (pstmt != null) 
                    pstmt.close();
            if (connection != null) 
                    connection.close();   
        }

        return user;
        
    }
    
	public Collection<UserBean> getUtenti() throws SQLException{

        UserBean user = new UserBean();
        Collection<UserBean> utenti = new ArrayList<UserBean>();
        String sql = "SELECT utente.* FROM utente";

        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
	            while(rs.next()) {
	            	user.setNome(rs.getString("nome"));
	            	user.setCognome(rs.getString("cognome"));
	                user.setID(rs.getInt("ID"));
	                user.setUserName(rs.getString("USER_NAME"));
	                user.setPassword(rs.getString("password"));
	                user.setEmail(rs.getString("email"));
	                user.setCartID(-1);
	                user.setIndirizzoBase(rs.getString("indirizzoBase"));
	                user.setNumeroDiTelefono(rs.getString("telefono"));
	                user.setPermessiAdmin(rs.getBoolean("permessi_admin"));
	                
	                utenti.add(user);
	            }   
            } else {
                System.out.println("accesso negato, l'utente non è stato trovato");
            }

        }finally {
            if (pstmt != null) 
                    pstmt.close();
            if (connection != null) 
                    connection.close();   
        }

        return utenti;
        	
	}

}
