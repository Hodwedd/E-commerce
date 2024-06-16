package admin;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import GestioneIMG.IMGmodel;
import Model.ProductModel;
import Model.Bean.ProductBean;
import Model.Interface.ProductModel_intf;

@MultipartConfig
public class AdminControl extends HttpServlet {
	
	static String SAVE_DIR ="/uploadTemp";
	private static final long serialVersionUID = 1L;
	
	static AzioniAdminModel_intf model = new AzioniAdminModel();
	
	public AdminControl() {
		super();
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("action vale : " + action);
        
        if (action != null) {
			
		    if (action.equalsIgnoreCase("inserisciProdotto")) {     
		    	
		    	boolean esito = false;
		        String id = request.getParameter("ID");
		        String nome = request.getParameter("nome");
		        String descrizione = request.getParameter("descrizione");
		        String prezzo = request.getParameter("prezzo");
		        String grammatura = request.getParameter("grammatura");
		        String categoria = request.getParameter("categoria");
		        String QTAmagazzino = request.getParameter("QTAmagazzino");
		        String percentualeSconto = request.getParameter("percentualeSconto");
		        String colorazione = request.getParameter("colorazione");

		        ProductBean p = new ProductBean();
		        p.setID(Integer.parseInt(id));
		        p.setNome(nome);
		        p.setDescrizione(descrizione);
		        p.setPrezzo(Double.parseDouble(prezzo));
		        p.setGrammatura(Double.parseDouble(grammatura));
		        p.setCategoria(categoria);
		        p.setQTA_magazzino(Integer.parseInt(QTAmagazzino));
		        p.setPercentualeSconto(Integer.parseInt(percentualeSconto));
		        p.setColorazione(colorazione);
		        p.setDataInserimento(Date.valueOf(LocalDate.now()));
		        
		        try {
					esito = model.inserisciProdotto(p);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		        if(esito) {
		        	//model.inserisciImmaginiProdotto(p);
		        	System.out.println("Inesrimenti : " + esito);
		        }
	        	System.out.println("INIZIAMO CON LE PARTI");
	        	ArrayList<Part> parti = (ArrayList<Part>) request.getParts();
	        	 //ogni campo del form rappresenta una parte
	        	 //l'ultima parte è il campo nascosto action
	        	 //utilizzo questo metodo perchè implemento funzioni forniteci dalla prof 
	        	 //che lavorano questa struttura dati
		        insImmagine(request.getServletContext().getRealPath(""),Integer.parseInt(id),"principale",parti.get(parti.size()- 3));
		        System.out.println("primo : " + parti.get(parti.size()- 3).getName());
		        insImmagine(request.getServletContext().getRealPath(""),Integer.parseInt(id),"secondaria",parti.get(parti.size()- 2));
		        System.out.println("secondo : " + parti.get(parti.size()- 2).getName());

		        
		        if(esito) { //il path non deve partire da /admin/
		        	response.sendRedirect(request.getContextPath() +"/?action=read&ProductID=" + id);
		        }else {
		        	response.sendRedirect(request.getContextPath() + "/admin/InserisciProdotto.jsp");
		        }
		    }                 
		    else if (action.equalsIgnoreCase("mostraUtenti")){
		     	request.removeAttribute("Utenti");

		        
		        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Home.jsp");
		        dispatcher.forward(request, response);	
		     }
		     else {
		    	//action ha un valore non accettato .. porta ad una pagina di errore..
		     }
		}
        else
        {
        	//se action è null , porterà ad una pagina di errore..
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
    
    protected void insImmagine(String pathContext,int id,String ruolo,Part part) throws ServletException, IOException {

	    String savePath = pathContext + File.separator + SAVE_DIR;
	         
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

			String fileName = extractFileName(part);
			if (fileName != null && !fileName.equals("")) {
				part.write(savePath + File.separator + fileName);
				try {
					
					IMGmodel.updatePhoto(id,ruolo, savePath + File.separator + fileName);
					
				} catch (SQLException sqlException) {
					System.out.println(sqlException);
				}
			}
	}
	
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }		


}
