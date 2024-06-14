package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import Model.Bean.ProductBean;
import Model.Interface.ProductModel_intf;

public class ProductModel implements ProductModel_intf {

	private static DataSource ds;
	
    private Connection connection = null;
    private PreparedStatement pstmt = null;

    // private ProductBean beanData = new ProductBean();
    //private List<ImgBean> images = new ArrayList<ImgBean>();
    
	private static final String TABLE_PRODOTTO = "prodotto";
    private static final String TABLE_IMG = "immagine";
    private static final String TABLE_RECENSIONE = "recensione";

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
	public synchronized void doSave(ProductBean product) throws SQLException {
/*
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		//String insertSQL = "INSERT INTO " + ProductModelDS.TABLE_PRODOTTO
		//		+ " (NAME, DESCRIPTION, PRICE, QUANTITY) VALUES (?, ?, ?, ?)";

		try {
			connection = ds.getConnection();
			//da fare
			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}*/
	}

	@Override
	public synchronized ProductBean doRetrieveByKey(int id) throws SQLException {
		
		ProductBean beanData = new ProductBean();

		String selectSQL = "SELECT " + TABLE_PRODOTTO + ".* " +
				                " FROM " + TABLE_PRODOTTO + 
				                " WHERE " + TABLE_PRODOTTO + ".ID = ?";
        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                beanData.setID(rs.getInt("ID"));
                beanData.setNome(rs.getString("nome"));
                beanData.setDescrizione(rs.getString("descrizione"));
                beanData.setGrammatura(rs.getInt("grammatura"));
                beanData.setCategoria(rs.getString("categoria"));
                beanData.setQTA_magazzino(rs.getInt("QTAmagazzino"));
                beanData.setPrezzo(rs.getInt("prezzo"));
                beanData.setPercentualeSconto(rs.getInt("percentualeSconto"));
                beanData.setColorazione(rs.getString("colorazione"));

            }


            if (beanData.isEmpty()) {
                // bisognerebbe lanciare un eccezione e quindi avere una pagina per le eccezioni
                System.out.println("bean vuoto, la query non ha prodotto risultati");
            }

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return beanData;
	}

 	@Override
    public Collection<ProductBean> doRetrieveAll(String order) throws SQLException {

	 	//una volta list , una volta array list , una volta arrayList...
        Collection<ProductBean> products = new LinkedList<ProductBean>(); 

        //presumiamo che le immagini siano 1 per prodotto , servirebbe un campo per la principale
        String selectSQL = "SELECT " + TABLE_PRODOTTO + ".*" +
			                "FROM " + TABLE_PRODOTTO + " JOIN " + TABLE_IMG + " ON " + 
			                TABLE_PRODOTTO + ".ID = " + TABLE_IMG + ".PRODOTTO_ID "
			                + " WHERE "+ TABLE_IMG + ".RUOLO = 'principale'";
        // evitiamo di prendere prodotti se eroneamente non hanno immagini , forse è una cosa inutile
        
        String votazioneSQL = "SELECT PRODOTTO_ID, AVG(votazione) as MediaValutazione , COUNT(PRODOTTO_ID) AS numValut " +
				  "FROM " + TABLE_RECENSIONE + " GROUP BY PRODOTTO_ID;";
        
        if (order != null && !order.isEmpty()) {
            selectSQL += " ORDER BY " + order;
        }

        // eseguo la query per prendere i prodotti
        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(selectSQL);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
            	
                ProductBean bean = new ProductBean();

                bean.setID(rs.getInt("ID"));
                bean.setNome(rs.getString("nome"));
                bean.setDescrizione(rs.getString("descrizione"));
                bean.setPrezzo(rs.getDouble("prezzo"));
                bean.setGrammatura(rs.getDouble("grammatura"));
                bean.setCategoria(rs.getString("categoria"));
                bean.setQTA_magazzino(rs.getInt("QTAmagazzino"));
                bean.setPercentualeSconto(rs.getInt("percentualeSconto"));
                bean.setColorazione(rs.getString("colorazione"));
                bean.setDataInserimento(rs.getDate("DataInserimento"));
    
                products.add(bean);
            }

        } finally {
            try {
                if (pstmt != null)
                	pstmt.close();
            } finally {
				connection.close();
            }
        }
        
        try {
        	
        	connection = ds.getConnection();
            pstmt = connection.prepareStatement(votazioneSQL);

            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
            	
            	double media = rs.getDouble("MediaValutazione");
            	int ID = rs.getInt("PRODOTTO_ID");
            	int NumValut = rs.getInt("numValut");
            	
            	for(ProductBean p : products) {
            		
            		if(p.getID() == ID) {
            			p.setVotazione(media);
            			p.setNrecensioni(NumValut);
            		}
            	}
            }
        	
        } finally {
        	
        	try {
                if (pstmt != null)
                	pstmt.close();
            } finally {
				connection.close();
            }
        }
        
        return products;
    }
 	
 	 public StringBuilder doRetrieveByInput(String input) throws SQLException {
 		 
     	 StringBuilder prodotti = new StringBuilder("["); // Uso StringBuilder per efficienza
     	 
         //presumiamo che le immagini siano 1 per prodotto , servirebbe un campo per la principale //FATTOs
         String selectSQL = "SELECT " + TABLE_PRODOTTO + ".*" +
 			                "FROM " + TABLE_PRODOTTO + " JOIN " + TABLE_IMG + " ON " + 
 			                TABLE_PRODOTTO + ".ID = " + TABLE_IMG + ".PRODOTTO_ID "
 			                + " WHERE "+ TABLE_IMG + ".RUOLO = 'principale'"
 			                + " AND " + TABLE_PRODOTTO + ".nome LIKE ?";
         // evitiamo di prendere prodotti se eroneamente non hanno immagini , forse è una cosa inutile
         // eseguo la query per prendere i prodotti
         try {
             connection = ds.getConnection();
             pstmt = connection.prepareStatement(selectSQL);
             pstmt.setString(1, "%" + input + "%");
             System.out.println(pstmt.toString());

             ResultSet rs = pstmt.executeQuery();
             while (rs.next()) {                             
            	 prodotti.append("{")
                 .append("\"Id\": ").append(rs.getInt("ID")).append(", ")
                 .append("\"nome\": \"").append(rs.getString("nome")).append("\"")
                 .append("}, ");
             }
             
             //rimuovo l'ultima virgola 
             if (prodotti.length() > 1) {
                 prodotti.setLength(prodotti.length() - 2);
             }
             
             prodotti.append("]");

         } finally {
             try {
                 if (pstmt != null)
                 	pstmt.close();
             } finally {
 				connection.close();
             }
         }
         
         return prodotti;
     }
	 

	@Override
	public synchronized boolean doDelete(int code) throws SQLException {/*
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + ProductModelDS.TABLE_PRODOTTO + " WHERE CODE = ?";

		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);

			result = preparedStatement.executeUpdate();

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return (result != 0);*/
		return true;
	}

	
}