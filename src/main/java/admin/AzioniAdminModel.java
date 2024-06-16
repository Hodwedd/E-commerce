package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import Model.Bean.ProductBean;

public class AzioniAdminModel implements AzioniAdminModel_intf{

	private static DataSource ds;
    private Connection connection = null;
    private PreparedStatement pstmt = null;
    
	private static final String TABLE_ORDINE = "ordine";
	private static final String TABLE_PRODOTTO = "prodotto";
    private static final String TABLE_IMMAGINE = "immagine";
    private static final String TABLE_UTENTE = "utente";
    private static final String RELAZIONE = "contiene";



	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/ecommerce");

		} catch (NamingException e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
	
	public AzioniAdminModel() {
		
	}
	
	public boolean inserisciProdotto(ProductBean prodotto)  throws SQLException {
        connection = ds.getConnection();

		// inserisco i prodotti nel database
            String insertSQL = "INSERT INTO prodotto (id, nome, descrizione, prezzo, grammatura, categoria, QTAmagazzino, percentualeSconto, colorazione, dataInserimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            	preparedStatement.setInt(1, prodotto.getID());
                preparedStatement.setString(2, prodotto.getNome());
                preparedStatement.setString(3, prodotto.getDescrizione());
                preparedStatement.setDouble(4, prodotto.getPrezzo());
                preparedStatement.setDouble(5, prodotto.getGrammatura());
                preparedStatement.setString(6, prodotto.getCategoria());
                preparedStatement.setInt(7, prodotto.getQTA_magazzino());
                preparedStatement.setDouble(8, prodotto.getPercentualeSconto());
                preparedStatement.setString(9, prodotto.getColorazione());
                preparedStatement.setDate(10, prodotto.getDataInserimento());

                if(preparedStatement.executeUpdate() > 0) 
                	return true;
                //else
                return false;
	}
	
	
}
