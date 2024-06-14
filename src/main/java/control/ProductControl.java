package control;

import java.io.IOException; 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.ProductModel;
import Model.Bean.ProductBean;
import Model.Interface.ProductModel_intf;


//@WebServlet(name = "Home", urlPatterns = "/")
public class ProductControl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	static ProductModel_intf model = new ProductModel();
	
	public ProductControl() {
		super();
	}

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        try {

            if (action != null) {

                // visualizzare un singolo prodotto
                if (action.equalsIgnoreCase("read")) {

                    int id = Integer.parseInt(req.getParameter("ProductID"));
                    req.removeAttribute("product");
                    req.setAttribute("product", model.doRetrieveByKey(id));

                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/productView.jsp");
                    dispatcher.forward(req, resp);
                }
                else if (action.equalsIgnoreCase("dettagliBarraRicercaJSON")) {
                     
                	String input = req.getParameter("input");
                	StringBuilder prodotti = model.doRetrieveByInput(input);

                    System.out.println(prodotti);
                     
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                     
                    // Invia la risposta JSON
                    resp.getWriter().write(prodotti.toString());
                 }            

            }                 else {
             	req.removeAttribute("products");
                try {
        			req.setAttribute("products", model.doRetrieveAll("ID"));
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home.jsp");
                dispatcher.forward(req, resp);	
             }

                
                
                // mostra tutti i prodotti nel db
                
            
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
            throw new RuntimeException(e);
        }

//        String sort = req.getParameter("sort");
//
//        try {
//            req.removeAttribute("products");
//            req.setAttribute("products", model.doRetrieveAll(sort));
//        } catch (SQLException e) {
//            System.out.println("Error:" + e.getMessage());
//        }
        

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
