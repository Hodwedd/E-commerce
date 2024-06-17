<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" 
import="java.util.*,Model.Bean.UserBean"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="../style/HomeST.css" rel="stylesheet" type="text/css">
	<link href="../style/menu-style.css" rel="stylesheet" type="text/css">
	<!-- Footer -->
	<link rel="stylesheet" type="text/css" href="./style/footer.css">
	
	<title>Beauty Blind</title>
	
</head>
<body>
        <%@ include file="../html/menu.html" %>
        <%
                Collection<?> utenti = (Collection<?>) request.getAttribute("listaUtenti");
				int i = 0;
				
	            if(utenti == null) {
	        		System.out.println("NIENTE PRODOTTI");
	        		
	        		// si potrebbe aggiungere error page
	        		response.sendRedirect("./");	
	        		return;
	        	}

                if (utenti != null && !utenti.isEmpty()) {

                    Iterator<?> it = utenti.iterator();
                    
                    while (it.hasNext()) {
                        UserBean bean = (UserBean) it.next();
                        System.out.println(bean.getNome());
                    }
                }
            %>
        
		<%@ include file="../html/footer.html" %>     
</body>
</html>