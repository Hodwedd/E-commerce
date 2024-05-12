package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import Model.Bean.ImgBean;
import Model.Bean.ProductBean;
import Model.Interface.ProductModel_intf;

public class ProductModel implements ProductModel_intf {

	private static DataSource ds;
	
    private Connection connection = null;
    private PreparedStatement pstmt = null;
    
	private static final String TABLE_PRODOTTO = "prodotto";
    private static final String TABLE_IMG = "immagine";


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

		String selectSQL = "SELECT " + TABLE_PRODOTTO + ".*, " + TABLE_IMG + ".immagine " +
                "FROM " + TABLE_PRODOTTO + " " +
                "JOIN " + TABLE_IMG + " ON " + TABLE_PRODOTTO + ".ID = " + TABLE_IMG + ".PRODOTTO_ID " +
                "WHERE " + TABLE_PRODOTTO + ".ID = ?";

        try {
            connection = ds.getConnection();
            pstmt = connection.prepareStatement(selectSQL);
            pstmt.setInt(1, id);
            ArrayList<ImgBean> images = new ArrayList<ImgBean>();

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

                ImgBean img = new ImgBean();

                img.setImg(rs.getBlob("immagine"));
                images.add(img);
            }
            beanData.setImages(images);


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

	        

	        Collection<ProductBean> products = new LinkedList<ProductBean>(); 
	        //una volta list , una volta array list , una volta arrayList...

	        String selectSQL = "SELECT " + TABLE_PRODOTTO + ".*, " + TABLE_IMG + ".immagine " +
	                "FROM " + TABLE_PRODOTTO + " " +
	                "JOIN " + TABLE_IMG + " ON " + TABLE_PRODOTTO + ".ID = " + TABLE_IMG + ".PRODOTTO_ID ";
	        
	        //presumiamo che le immagini siano 1 per prodotto , servirebbe un campo per la principale

	        if (order != null && !order.isEmpty()) {
	            selectSQL += " ORDER BY " + order;
	        }

	        try {
	            connection = ds.getConnection();
	            pstmt = connection.prepareStatement(selectSQL);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	            	
	                ProductBean bean = new ProductBean();
	                ImgBean img = new ImgBean();
	                ArrayList<ImgBean> FirstImg = new ArrayList<ImgBean>();

	                bean.setID(rs.getInt("ID"));
	                bean.setNome(rs.getString("nome"));
	                bean.setDescrizione(rs.getString("descrizione"));
	                bean.setPrezzo(rs.getDouble("prezzo"));
	                bean.setGrammatura(rs.getDouble("grammatura"));
	                bean.setCategoria(rs.getString("categoria"));
	                bean.setQTA_magazzino(rs.getInt("QTAmagazzino"));
	                bean.setPercentualeSconto(rs.getInt("percentualeSconto"));
	                bean.setColorazione(rs.getString("colorazione"));
	                	                
	                img.setImg(rs.getBlob("immagine"));
	                
	                FirstImg.add(img);
	                
	                bean.setImages(FirstImg);

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
	        return products;
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